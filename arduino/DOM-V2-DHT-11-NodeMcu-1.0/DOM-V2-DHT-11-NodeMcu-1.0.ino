/*
    Environment station final version. Sends humdity and temperature values
    to central server using JSON for recording.
*/

#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
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
const String cluster = "2";

#define ADDRESS_ID 0

// Time to sleep between taking samples (in milliseconds)
const long sampleRate = 5000;
const String sensorType = "DHT11";   // What is the SensorType

const String api = "/domotic/v2.0/api/reading/";
// Serial comms speed to speak to the ESP8266
const long serialSpeed = 115200;

#define DHTPIN 12     // what digital pin we're reading the values from.
#define DHTPINVCC 4     // what pin is supplying the power

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

// Initialize DHT sensor.
DHT dht(DHTPIN, DHTTYPE);

// Allocate the JSON documents
//
// Inside the brackets, <> is the RAM allocated to this document.
// Change this value to match your requirement.
// Use arduinojson.org/v6/assistant to compute the capacity.

StaticJsonDocument<250> postData;
JsonObject temperature;

#define RED_LED LED_BUILTIN
#define BLUE_LED 2

// Time to sleep between taking samples (in seconds):
// 5 minute delay
const int sleepTimeS = 300;

static boolean connect()
{
  int status = 0;

  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.hostname("Cluster " + cluster);
  /* Explicitly set the ESP8266 to be a WiFi-client, otherwise, it by default,
     would try to act as both a client and an access-point and could cause
     network-issues with your other WiFi-devices on your WiFi-network.
  */
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  status = WiFi.waitForConnectResult();

  if (status == WL_CONNECTED) {
    Serial.println("WiFi connected");
    Serial.print("IP address: ");
    Serial.println(WiFi.localIP());
    Serial.printf("RSSI: %d dBm\n", WiFi.RSSI());
    Serial.println("");
  }
}

static boolean disconnect()
{
  Serial.println();
  Serial.println("closing connection");
  WiFi.disconnect(true);
}

static void turnOn(int LEDPin)
{
  digitalWrite(LEDPin, LOW);  // Turn the LED on (Note that LOW is the voltage level
  // but actually the LED is on; this is because
  // it is active low on the ESP-13)
}

static void turnOff(int LEDPin)
{
  digitalWrite(LEDPin, HIGH);  // Turn the LED off
}

static void sendData(float temp, float humidity, float heatIndex, int res)
{
  String json;

  //Results
  temperature["resolution"] = res;
  temperature["celsius"] = temp;
  temperature["humidity"] = humidity;
  temperature["humiture"] = heatIndex;

  json.reserve(250);
  // Generate the minified JSON and store it in a String.
  serializeJson(postData, json);

  yield();

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

  // Init the humidity sensor power pin
  pinMode(DHTPINVCC, OUTPUT);
  digitalWrite(DHTPINVCC, LOW);

  Serial.print("Will connect to: ");
  Serial.println(host);

  // Add fixed data to the document
  //
  postData["softwareVer"] = VERSION;
  postData["address"] = ADDRESS_ID;
  postData["clusterId"] = cluster;
  postData["sampleRate"] = sampleRate;
  postData["sensorType"] = sensorType;
  // For memory usage you need to create nested structures once in setup and then just populate the attributes as required
  // when you read the data.
  temperature = postData.createNestedObject("temperatureReading");
}

void loop() {
  yield();
  // Supply power to the DHT22
  digitalWrite(DHTPINVCC, HIGH);
  delay(200);
  // Initialise the sensor.
  dht.begin();
  delay(500);

  // Reading temperature or humidity takes about 250 milliseconds!
  // Sensor readings may also be up to 2 seconds 'old' (its a very slow sensor)
  float h = dht.readHumidity();
  yield();
  // Read temperature as Celsius (the default)
  float t = dht.readTemperature();
  yield();
  digitalWrite(DHTPINVCC, LOW);

  // Check if any reads failed and exit early (to try again).
  if (isnan(h) || isnan(t)) {
    turnOn(RED_LED);
    Serial.println("Failed to read from DHT sensor!");
    delay(500);                      // Wait for a half second
    turnOff(RED_LED);
    delay(sleepTimeS * 1000);
    return;
  }

  // Compute heat index in Celsius (isFahreheit = false)
  float hic = dht.computeHeatIndex(t, h, false);
  yield();

  // We start by connecting to a WiFi network
  connect();
  turnOn(BLUE_LED);
  sendData(t, h, hic, 0);
  disconnect();
  delay(100);
  turnOff(BLUE_LED);
  turnOff(RED_LED);

  //  delay(500); // pause before deepsleep

  delay(sleepTimeS * 1000); // once every 5 minutes (eventually)

  // deepSleep time is defined in microseconds. Multiply seconds by 1e6
  //  ESP.deepSleep(sleepTimeS * 1000000, WAKE_RF_DEFAULT);
}
