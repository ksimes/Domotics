/*
 *  Simple HTTP get webclient test version ecapsulating all of the connecting
 *  and sending of data in the loop rather than connecting in the setup
 */

#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include "DHT.h"

#define DHTPIN 5     // what digital pin we're connected to

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
// Note that older versions of this library took an optional third parameter to
// tweak the timings for faster processors.  This parameter is no longer needed
// as the current DHT reading algorithm adjusts itself to work on faster procs.
DHT dht(DHTPIN, DHTTYPE);

const char* ssid     = "TALKTALK-66430C";
const char* password = "WHBQTGAQ";

#define HOST "192.168.1.8"
#define STATION "1"
#define RED_LED LED_BUILTIN
#define BLUE_LED 2
#define DELAY 300000

const int port = 8080;
const String api = "/domotic/api/reading/";
const long serialSpeed = 115000;

const int station = 1;    // Which monitoring station is this.

// Time to sleep (in seconds):
const int sleepTimeS = 30;

boolean connect()
{
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  
  WiFi.begin(ssid, password);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");  
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
}

void setup() {
  Serial.begin(serialSpeed);
  
  // Initialise the temp sensor.
  dht.begin();
  
  pinMode(BLUE_LED, OUTPUT);     // Initialize the LED_BUILTIN pin as an output
  pinMode(RED_LED, OUTPUT);      // Initialize the LED_BUILTIN pin as an output
}

void loop() {
  digitalWrite(BLUE_LED, HIGH);  // Turn the LED off 
  digitalWrite(RED_LED, HIGH);   // Turn the LED off 
  Serial.print("connecting to ");
  Serial.println(HOST);
  
  // We start by connecting to a WiFi network
  connect();

  // Reading temperature or humidity takes about 250 milliseconds!
  // Sensor readings may also be up to 2 seconds 'old' (its a very slow sensor)
  float h = dht.readHumidity();
  // Read temperature as Celsius (the default)
  float t = dht.readTemperature();

  // Check if any reads failed and exit early (to try again).
  if (isnan(h) || isnan(t)) {
    digitalWrite(RED_LED, LOW);      // Turn the LED on (Note that LOW is the voltage level 
    Serial.println("Failed to read from DHT sensor!");
    delay(500);                      // Wait for a half second 
    digitalWrite(RED_LED, HIGH);     // Turn the LED off by making the voltage HIGH 
    delay(DELAY); // once every 5 minutes (eventually)
    WiFi.disconnect();
    return;
  }

  // Compute heat index in Celsius (isFahreheit = false)
  float hic = dht.computeHeatIndex(t, h, false);

  String postData = "{\"stationId\":" STATION ",\"temperatureValue\":";
  postData = postData + String(t, 3) + ",\"humidityValue\":";
  postData = postData + String(h, 3) + ",\"humitureValue\":";
  postData = postData + String(hic, 3) + "}";

  Serial.println(postData);

  digitalWrite(BLUE_LED, LOW);      // Turn the LED on (Note that LOW is the voltage level 
                                    // but actually the LED is on; this is because  
                                    // it is active low on the ESP-13) 
  Serial.print("POSTing to ");
  Serial.println(String(HOST) + ":" + port + api);
  
  HTTPClient http;
  http.begin(HOST, port, api);
  http.addHeader("Content-Type", "application/json");
  http.addHeader("Content-Length", String(postData.length()));
  int result = http.POST(postData);
  http.end();

  Serial.print("POST result: ");
  Serial.println(http.errorToString(result));

  delay(100);
  digitalWrite(BLUE_LED, HIGH);    // Turn the LED off by making the voltage HIGH 

  Serial.println();
  Serial.println("closing connection");
  WiFi.disconnect();

  delay(DELAY); // once every 5 minutes (eventually)

  // deepSleep time is defined in microseconds. Multiply seconds by 1e6 
//  ESP.deepSleep(sleepTimeS * 1000000);
}
