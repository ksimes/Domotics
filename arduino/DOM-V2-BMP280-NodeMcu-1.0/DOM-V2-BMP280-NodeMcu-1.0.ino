/*
    Environment station final version. Sends moisture values
    to central server using JSON for recording.
*/

#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ESP8266WiFiMulti.h>
#include <Wire.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_BME280.h>
#include "access.h"

#include <ArduinoJson.h>

// Version of this software
const String VERSION = "2.0";
/**
**  1  Study "Upstairs box room - Study"
**  2  "Main Bedroom"  "Upstairs Main bedroom - Cats room"
**  3  Bedroom "Upstairs Second bedroom - Human bedroom"
**  4  "Upstairs cupboard"
**  5  "Upstairs hall 1"
**  6  Bathroom
**  7  "Downstairs Hall 1"
**  8  "Downstairs Hall 2"
**  9  Kitchen
** 10  "Music Room"
** 11  "Living Room"
** 12  "Downstairs cupboard"
** 13  "Downstairs toilet"
** 14  "Front porch"
** 15  "Utility Room"
** 17  "Loft 1"
** 18  "Loft 2"
**/
// Cluster or what used to be called Station is the Chip supplying the data. There can be a number of sensors attached
// to one Chip so this is why it is now called a cluster. The Cluster can have any description.
const String cluster = "01";

const String measureType = "MOISTURE";   // What is the MeasureType
const String sensorType = "BME280";   // What is the SensorType

#define SEALEVELPRESSURE_HPA (1013.25)

Adafruit_BME280 bme; // using I2C

const String api = "/domotic/2.0/api/reading/";   // url measurement being sent to

// Serial comms speed to speak to the serial port
const long serialSpeed = 115200;

ESP8266WiFiMulti wifiMulti;   // Talk to the first access point found in the list.

#define RED_LED LED_BUILTIN
#define BLUE_LED 2

// Time to sleep between taking samples (in seconds):
const int sleepTimeS = 10;
const String processor = "ESP8266";

struct TransportLayer {
  const String protocol = "wifi";
  String accessPoint;
  String host;
  String localIp;
  String rssi;
  String api;
} transportLayer;

static long sleepTime() {
  return sleepTimeS * 1000;
}

struct Measurement {
  boolean isSet;
  String temperature;
  String humidity;
  String pressure;
  String altitude;
};

Measurement measure[8];    // Up to 8 possible measurements on each ESP8266/TCA9548A combo

static boolean connect() {
  int status = 0;

  WiFi.hostname("Cluster " + cluster);
  /* Explicitly set the ESP8266 to be a WiFi-client, otherwise, it by default,
     would try to act as both a client and an access-point and could cause
     network-issues with your other WiFi-devices on your WiFi-network.
  */
  WiFi.mode(WIFI_STA);
  for (int i = 0; i < accessPointArraySize; i++) {
    wifiMulti.addAP(accessPoint[i].ssid, accessPoint[i].password);
  }

  while (wifiMulti.run() != WL_CONNECTED) {
    delay(500);
  }

  transportLayer.accessPoint = WiFi.SSID().c_str();
  transportLayer.host = host;
  transportLayer.localIp = WiFi.localIP().toString();
  transportLayer.rssi = WiFi.RSSI();
  transportLayer.api = api + ":" + String(port);
}

static boolean disconnect() {
  WiFi.disconnect(true);
}

static void turnOn(int LEDPin) {
  digitalWrite(LEDPin, LOW);  // Turn the LED on (Note that LOW is the voltage level
  // but actually the LED is on; this is because it is active low on the ESP-13)
}

static void turnOff(int LEDPin) {
  digitalWrite(LEDPin, HIGH);  // Turn the LED off
}

static void setPayload(size_t index, float temperature, float humidity, float pressure, float altitude) {
  measure[index].isSet = true;
  measure[index].temperature = String(temperature);
  measure[index].humidity = String(humidity);
  measure[index].pressure = String(pressure);
  measure[index].altitude = String(altitude);
}

static boolean checkPayload(size_t index) {
  return measure[index].isSet;
}

static String buildDocument() {
  // Allocate the JSON documents
  //
  // Inside the brackets, () is the RAM allocated to this document.
  // Change this value to match your requirement.
  // Use arduinojson.org/v6/assistant to compute the capacity.

  DynamicJsonDocument doc(1024);
  JsonObject transport;
  JsonArray readings;
  // Add fixed struture and data to the document
  //
  doc["software"] = VERSION;
  doc["cluster"] = cluster;
  doc["processor"] = processor;

  transport = doc.createNestedObject("transport");
  transport["protocol"] = transportLayer.protocol;
  transport["accessPoint"] = transportLayer.accessPoint;
  transport["host"] = transportLayer.host;
  transport["localIP"] = transportLayer.localIp;
  transport["api"] = transportLayer.api;
  transport["RSSI"] = transportLayer.rssi;

  readings = doc.createNestedArray("readings");

  // We will add these readings as we get then if they are not 0.
  for (size_t index = 0; index < 8; index++) {
    if (checkPayload(index)) {
      DynamicJsonDocument measurement(250);
      measurement["address"] = index;
      measurement["measureType"] = measureType;
      measurement["sensorType"] = sensorType;
      measurement["sampleRate"] = sleepTime();
      JsonObject payload = measurement.createNestedObject("payload");
      payload["temperature"] = measure[index].temperature;
      payload["humidity"] = measure[index].humidity;
      payload["pressure"] = measure[index].pressure;
      payload["altitude"] = measure[index].altitude;

      readings.add(measurement);
    }
  }

  String json;

  json.reserve(850);
  // Generate the minified JSON and store it in a String.
  serializeJson(doc, json);

  transport.clear();
  doc.clear();

  return json;
}

static void sendData() {
  yield();

  String json = buildDocument();

  Serial.print("POSTing to ");
  Serial.println(String(host) + ":" + port + api);
  Serial.println(json);

  yield();

  HTTPClient http;
  http.begin(host, port, api, false, "");
  http.addHeader("Content-Type", "application/json");
  http.addHeader("Content-Length", String(json.length()));
  int result = http.POST(json);
  http.end();
  yield();

  Serial.print("POST result: ");
  Serial.println(String(result) + " - " + http.errorToString(result));
  yield();
}

void setup() {
  Serial.begin(serialSpeed);
  while (!Serial);   // time to get serial running

  Serial.println(F("Starting"));
  rst_info *rinfo;
  rinfo = ESP.getResetInfoPtr();
  Serial.println(String("ResetInfo.reason = ") + (*rinfo).reason);
  yield();

  // if we woke from deep sleep, do a full reset
  // this is needed because http gets from deep sleep are not properly returned.
  // test to see if this is fixed in future esp8266 libraries

  if ((*rinfo).reason == REASON_DEEP_SLEEP_AWAKE) {
    Serial.println(F("Woke from deep sleep, performing full reset")) ;
    ESP.restart() ;
  } // - See more at: http://www.esp8266.com/viewtopic.php?f=32&t=4819&start=5#sthash.a9HioWG6.dpuf

  pinMode(BLUE_LED, OUTPUT);     // Initialize the LED_BUILTIN pin as an output
  turnOff(BLUE_LED);
  pinMode(RED_LED, OUTPUT);      // Initialize the LED_BUILTIN pin as an output
  turnOff(RED_LED);

  Serial.print(F("This is Cluster "));
  Serial.println(cluster);
  Serial.print(F("Sensor Type "));
  Serial.println(sensorType);

  Serial.print(F("Will connect to: "));
  Serial.println(host);

  unsigned status;

  // default settings
  //  status = bme.begin();
  // You can also pass in a Wire library object like &Wire2
  // status = bme.begin(0x76, &Wire2)
  status = bme.begin(0x76);
  if (!status) {
    Serial.println("Could not find a valid BME280 sensor, check wiring, address, sensor ID!");
    Serial.print("SensorID was: 0x"); Serial.println(bme.sensorID(), 16);
    Serial.print("        ID of 0xFF probably means a bad address, a BMP 180 or BMP 085\n");
    Serial.print("   ID of 0x56-0x58 represents a BMP 280,\n");
    Serial.print("        ID of 0x60 represents a BME 280.\n");
    Serial.print("        ID of 0x61 represents a BME 680.\n");
    while (true) delay(10);
  }
}


void loop() {
  yield();
  delay(200);

  size_t index = 0;

  setPayload(index, bme.readTemperature(), bme.readHumidity(), bme.readPressure() / 100.0F, bme.readAltitude(SEALEVELPRESSURE_HPA));

  // We start by connecting to a WiFi network
  connect();
  turnOn(BLUE_LED);
  yield();
  sendData();
  disconnect();
  yield();
  turnOff(BLUE_LED);
  turnOff(RED_LED);
  yield();
  delay(sleepTime()); // once every x minutes (eventually)

  // deepSleep time is defined in microseconds. Multiply seconds by 1e6
  //  ESP.deepSleep(sleepTime() * 1000, WAKE_RF_DEFAULT);
}
