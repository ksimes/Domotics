/*
 *  Simple HTTP get webclient test
 */

#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>

const char* ssid     = "TALKTALK-66430C";
const char* password = "WHBQTGAQ";

const char* host = "192.168.1.8";

const int station = 1;    // Which monitoring station is this.

void setup() {
  Serial.begin(115200);
  delay(100);

  // We start by connecting to a WiFi network

  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  
  WiFi.begin(ssid, password);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Bridge.begin();

  Serial.println("");
  Serial.println("WiFi connected");  
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

int value = 0;

void loop() {
  delay(5000);
  ++value;

  Serial.print("connecting to ");
  Serial.println(host);
  
  // Use WiFiClient class to create TCP connections
  WiFiClient client;
  const int httpPort = 8080;
  if (!client.connect(host, httpPort)) {
    Serial.println("connection failed");
    return;
  }
  
  // We now create a URI for the request
  String url = "/domotic/api/temperture/";
  Serial.print("Requesting URL: ");
  Serial.println(url);
  
  // This will send the request to the server
  client.print(String("GET ") + url + " HTTP/1.1\r\n" +
               "Host: " + host + "\r\n" + 
               "Connection: close\r\n\r\n");


  delay(500);
  
  Serial.println("Reply");
  // Read all the lines of the reply from server and print them to Serial
  while(client.available()){
    String line = client.readStringUntil('\r');
    Serial.print(line);
  }

  delay(5000);

  String PostData = "{\"stationId\":3,\"value\":17.21,\"timeStamp\":\"201607031750080310\"}";

  Serial.println("starting POST");
  HTTPClient http;
  http.begin("192.168.1.8", 8080, "/domotic/api/temperture/");
  http.addHeader("Content-Type", "application/json");
  http.addHeader("Content-Length", String(PostData.length()));
  http.POST(PostData);
  http.writeToStream(&Serial);
  http.end();
  
  Serial.println("Ending POST");

/**
  client.print(String("GET ") + url + " HTTP/1.1\r\n" +
               "Host: " + host + "\r\n");
  client.println("Cache-Control: no-cache");
/*  client.println("Content-Type: application/x-www-form-urlencoded");
  client.print("Content-Length: ");
  client.println(PostData.length());
  client.println();
  client.println(PostData);
**/
  delay(500);
  
  // Read all the lines of the reply from server and print them to Serial
  while(client.available()){
    String line = client.readStringUntil('\r');
    Serial.print(line);
  }

  Serial.println();
  Serial.println("closing connection");
}
