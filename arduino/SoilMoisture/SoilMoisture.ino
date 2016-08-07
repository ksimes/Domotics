
// YL-39 + YL-69 humidity sensor
#define humidity_sensor_pin A0
#define humidity_sensor_vcc  14

const long serialSpeed = 115000;

void setup() {
  // Init the humidity sensor board
  pinMode(humidity_sensor_vcc, OUTPUT);
  digitalWrite(humidity_sensor_vcc, LOW);

  pinMode(humidity_sensor_pin, INPUT);

  Serial.begin(serialSpeed);
}

int read_humidity_sensor() {
  digitalWrite(humidity_sensor_vcc, HIGH);
  delay(500);
  int value = analogRead(humidity_sensor_pin);
  int val = map(1023 - value, 0.0, 1023.0, 0.0, 100.0);

  digitalWrite(humidity_sensor_vcc, LOW);
  return val;
}

void loop() {
  Serial.print("Humidity Level (0-1023): ");
  Serial.println(" value " + String(read_humidity_sensor()));
  delay(10000);
}

