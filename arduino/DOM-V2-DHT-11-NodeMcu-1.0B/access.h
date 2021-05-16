const int accessPointArraySize = 5;

struct AccessPoints {
  const char* ssid;
  const char* password;
} accessPoint[accessPointArraySize] = 
            {{"TALKTALKEE41F3", "YXQTQ8F4"},
             {"SUMER AP4", "[pz2d3$J]"},
             {"SUMER AP3", "[pz2d3$J]"},
             {"SUMER AP2", "[pz2d3$J]"},
             {"SUMER AP1", "[pz2d3$J]"}};

// Main data server ADAB
const String host = "192.168.1.162";
const int port = 31000;
