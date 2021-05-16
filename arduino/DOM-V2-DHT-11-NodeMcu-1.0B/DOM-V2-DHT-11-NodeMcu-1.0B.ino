/*
    Environment station final version. Sends moisture values to central server using JSON for recording.
*/

#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ESP8266WiFiMulti.h>
#include "DHT.h"
#include "access.h"

#include <ArduinoJson.h>

// Version of this software
#define VERSION "2.0"
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
const String cluster = "17";

const String measureType = "ENVIRONMENT";   // What is the MeasureType
const String sensorType = "DHT11";   // What is the SensorType
const String errorSignal = "ERROR";   // If an error has occurred

const String api = "/domotic/2.0/api/reading/";

// Serial comms speed to speak to the serial port
const long serialSpeed = 115200;

ESP8266WiFiMulti wifiMulti;

#define DHTPIN1 D5        // what digital pin we're reading the values from.
#define DHTPIN2 D6        // what digital pin we're reading the values from.
#define DHTPIN3 D7        // what digital pin we're reading the values from.
#define DHTPIN4 D8        // what digital pin we're reading the values from.

// Uncomment whatever type you're using!
// #define DHTTYPE DHT11   // DHT 11
#define DHTTYPE DHT22   // DHT 22  (AM2302), AM2321
// #define DHTTYPE DHT21   // DHT 21 (AM2301)

// Connect pin 1 (on the left) of the sensor to +5V
// NOTE: If using a board with 3.3V logic like an Arduino Due connect pin 1
// to 3.3V instead of 5V!
// Connect pin 2 of the sensor to whatever your DHTPIN is
// Connect pin 4 (on the right) of the sensor to GROUND
// Connect a 10K resistor from pin 2 (data) to pin 1 (power) of the sensor

// Initialize DHT sensor(s).
#define ARRAY_SIZE 1

// Note that the Ardunio Nano will not support more than 3 DHT22 sensors before running out of power
// Note: D6 is the normal pin the old single sensor boxs were soldered to.
DHT dht[ARRAY_SIZE] = { DHT(DHTPIN2, DHTTYPE) };

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


struct env {
  boolean status;
  String message;
  String details;

  double temperature;
  double humidity;
  double heatIndex;
  int resolution;
} environment[ARRAY_SIZE];

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

static String buildDocument() {
  // Allocate the JSON documents
  //
  // Inside the brackets, () is the RAM allocated to this document.
  // Change this value to match your requirement.
  // Use arduinojson.org/v6/assistant to compute the capacity.

  DynamicJsonDocument doc(2048);
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
  for (int i = 0; i < ARRAY_SIZE; i++) {
    DynamicJsonDocument measurement(250);
    measurement["address"] = i;
    measurement["measureType"] = measureType;
    measurement["sensorType"] = environment[i].status ? sensorType : errorSignal;
    measurement["sampleRate"] = sleepTime();

    JsonObject payload = measurement.createNestedObject("payload");

    if (environment[i].status) {
      //Results
      payload["resolution"] = environment[i].resolution;
      payload["celsius"] = environment[i].temperature;
      payload["humidity"] = environment[i].humidity;
      payload["humiture"] = environment[i].heatIndex;
    } else {
      payload["message"] = environment[i].message;
      payload["details"] = environment[i].details;
    }

    readings.add(measurement);
  }

  String json;

  json.reserve(1024);
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

static void readDHTSensor(DHT dht, struct env &environment) {
  
  environment.status = true;
  environment.resolution = 0;
  
  // Initialise the sensors.
  dht.begin();
  delay(500);

  // Reading temperature or humidity takes about 250 milliseconds!
  // Sensor readings may also be up to 2 seconds 'old' (its a very slow sensor)
  environment.humidity = dht.readHumidity();
  yield();
  // Read temperature as Celsius (the default)
  environment.temperature = dht.readTemperature();
  yield();

  // Check if any reads failed and exit early (to try again).
  if (isnan(environment.humidity) || isnan(environment.temperature)) {
    turnOn(RED_LED);
    environment.status = false;
    environment.message = "Failed to read from DHT sensor!";
    environment.details = environment.message;
    Serial.println(environment.message);
    delay(500);                      // Wait for a half second
    turnOff(RED_LED);
  } else {
    // Compute heat index in Celsius (isFahreheit = false)
    environment.heatIndex = dht.computeHeatIndex(environment.temperature, environment.humidity, false);
  }
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

  for (int i = 0; i < accessPointArraySize; i++) {
    Serial.println(accessPoint[i].ssid);
    Serial.println(accessPoint[i].password);
  }
}

void loop() {
  yield();
  delay(200);

  // Read all the sensors connected
  for (int i = 0; i < ARRAY_SIZE; i++) {
    readDHTSensor(dht[i], environment[i]);
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
