#include <Arduino.h>
#include <WiFi.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>
#include <SimpleDHT.h>

int pinDHT22 =  23;
SimpleDHT22 dht22(pinDHT22);

#define USE_SERIAL Serial

#define WIFI_SSID ""
#define WIFI_PASSWORD ""

void initWiFi() {
  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  USE_SERIAL.println("Connecting to WiFi ..");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print('.');
    delay(1000);
  }
  USE_SERIAL.print("IP: ");
  USE_SERIAL.println(WiFi.localIP());
}

DynamicJsonDocument doc(1024);
JsonObject object = doc.to<JsonObject>();
String url = "http://<hostname>:<port>/sensor";
int sensor_read_success = 0;

void sampleSensor() { // DHT22 sampling rate is 0.5HZ.
  USE_SERIAL.println("================================="); 
  USE_SERIAL.println("Sample DHT22...");
  
  float temperature = 0;
  float humidity = 0;
  int err = SimpleDHTErrSuccess;
  
  if((err=dht22.read2(&temperature, &humidity, NULL)) != SimpleDHTErrSuccess){
    USE_SERIAL.print("Read DHT22 failed, err="); 
    USE_SERIAL.println(err);
    delay(2000);
    return;
  }
  object["temperature"] = temperature;
  object["humidity"] = humidity;
  USE_SERIAL.print("Sample OK: "); 
  USE_SERIAL.print((float)temperature); 
  USE_SERIAL.print(" *C, "); 
  USE_SERIAL.print((float)humidity); 
  USE_SERIAL.println(" RH%");
  sensor_read_success = 1;
}

void sendData() {
  sampleSensor();
  if (sensor_read_success) {
    HTTPClient http;

    USE_SERIAL.print("[HTTP] begin...\n");
    http.begin(url); //HTTP
    http.addHeader("Content-Type", "application/json");
    USE_SERIAL.print("[HTTP] POST...\n");
    String body;
    serializeJson(doc, body);
    USE_SERIAL.printf(body.c_str());
    int httpCode = http.POST(body);
    if(httpCode > 0) {
      USE_SERIAL.printf("[HTTP] GET... code: %d\n", httpCode);
      if(httpCode == HTTP_CODE_OK) {
        String payload = http.getString();
        USE_SERIAL.println(payload);
      }
    } else {
      USE_SERIAL.printf("[HTTP] GET... failed, error: %s\n", http.errorToString(httpCode).c_str());
    }

    http.end();
    sensor_read_success = 0;
  }
}

void setup() {

    USE_SERIAL.begin(115200);

    USE_SERIAL.println();
    USE_SERIAL.println();
    USE_SERIAL.println();

    for(uint8_t t = 4; t > 0; t--) {
        USE_SERIAL.printf("[SETUP] WAIT %d...\n", t);
        USE_SERIAL.flush();
        delay(1000);
    }
    initWiFi();
    //wifiMulti.addAP(WIFI_SSID, WIFI_SSID);
    object["token"] = WiFi.macAddress();
    USE_SERIAL.printf(WiFi.macAddress().c_str());
    sampleSensor();
}

unsigned long last_time = millis(); 
unsigned long update_time_millis = 60000;

void loop() {
    if(WiFi.status() == WL_CONNECTED) {
      if (millis() > last_time + update_time_millis) {
         sendData();
         last_time = millis();
      }
      
    } else {
      USE_SERIAL.println("No WiFi");
      WiFi.disconnect();
      WiFi.reconnect();
    }
    
    delay(50);
}
