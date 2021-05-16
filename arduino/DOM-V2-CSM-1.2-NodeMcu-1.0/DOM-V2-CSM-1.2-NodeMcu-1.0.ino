/*
    Environment station final version. Sends moisture values
    to central server using JSON for recording.
*/

#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <MCP3XXX.h>
#include "access.h"

#include <ArduinoJson.h>

// Version of this software
#define VERSION "1.0"
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
const String cluster = "1";

const String measureType = "MOISTURE";   // What is the MeasureType
const String sensorType = "CSM1.2";   // What is the SensorType

const String api = "/domotic/v2.0/api/reading/";

// Serial comms speed to speak to the serial port
const long serialSpeed = 115200;

// This is using a MCP3008 analogue to digital convertor chip to pick up the values from the Capacitive Soil Moisture Sensors.
// There can be up to 8 possible sensors connected to the chip. The addresses are defined by the channel the CSM is connected
// to. The cluster and address defined the 'station' that is used by the DB to identify what is being measured.
// As this can read any kind of Analogue signal it need not be just a CSM that is attached so one of these processors can read
// any type of analogue sensor that returns a double value.
// Note: any channel that is not being used should be earthed with a plug so that there is no noise on the line which will
// generate random measurements for that address.

MCP3008 adc;

uint32_t measure[8];

#define RED_LED LED_BUILTIN
#define BLUE_LED 2

// Time to sleep between taking samples (in seconds):
const int sleepTimeS = 20;
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

static boolean connect() {
  int status = 0;

  WiFi.hostname("Cluster " + cluster);
  /* Explicitly set the ESP8266 to be a WiFi-client, otherwise, it by default,
     would try to act as both a client and an access-point and could cause
     network-issues with your other WiFi-devices on your WiFi-network.
  */
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  status = WiFi.waitForConnectResult();

  if (status == WL_CONNECTED) {
    transportLayer.accessPoint = ssid;
    transportLayer.host = host;
    transportLayer.localIp = WiFi.localIP().toString();
    transportLayer.rssi = WiFi.RSSI();
    transportLayer.api = api + ":" + String(port);
  }
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

static void setPayload(uint32_t value, size_t index) {
  measure[index] = value;
}

static boolean checkPayload(size_t index) {
  return (measure[index] > 0 );
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
      payload["moistureLevel"] = measure[index];

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
  Serial.println("Starting") ;
  rst_info *rinfo;
  rinfo = ESP.getResetInfoPtr();
  Serial.println(String("ResetInfo.reason = ") + (*rinfo).reason);
  yield();

  // if we woke from deep sleep, do a full reset
  // this is needed because http gets from deep sleep are not properly returned.
  // test to see if this is fixed in future esp8266 libraries

  //  if ((*rinfo).reason == REASON_DEEP_SLEEP_AWAKE) {
  //    Serial.println("Woke from deep sleep, performing full reset") ;
  //    ESP.restart() ;
  //  } // - See more at: http://www.esp8266.com/viewtopic.php?f=32&t=4819&start=5#sthash.a9HioWG6.dpuf

  pinMode(BLUE_LED, OUTPUT);     // Initialize the LED_BUILTIN pin as an output
  turnOff(BLUE_LED);
  pinMode(RED_LED, OUTPUT);      // Initialize the LED_BUILTIN pin as an output
  turnOff(RED_LED);

  Serial.print("This is Cluster ");
  Serial.println(cluster);
  Serial.print("Sensor Type ");
  Serial.println(sensorType);

  Serial.print("Will connect to: ");
  Serial.println(host);

  // Use the default SPI hardware interface.
  adc.begin();
}


void loop() {
  yield();
  delay(200);

  for (size_t i = 0; i < adc.numChannels(); ++i) {
    setPayload(adc.analogRead(i), i);
  }

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
