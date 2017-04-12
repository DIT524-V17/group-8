#include <Smartcar.h>

Car car;
int endOfString = 4;
int angleInt;
int speedInt;
char selectChar;
char terminator = ':';

void setup() {
  Serial.begin(9600);
  Serial3.begin(9600);
  Serial3.setTimeout(3);
  car.begin();
}

void loop() {
  /*if (Serial3.available()) {
    selectChar = (char) Serial3.read();
    Serial3.println(selectChar);
    switch (selectChar) {
      case 'm': manualDrive(); break;
      case 'a': autoDrive(); break;
    }
  }*/
  manualDrive();
}

void manualDrive() {
  while (Serial3.available()) {
    String data = Serial3.readString();
    if (data.substring(data.length() - endOfString).equals("STOP")) {
      car.stop();
    } else {
      int beginIndex = data.indexOf(terminator);
      String angleStr = data.substring(0, beginIndex);

      int endIndex = data.lastIndexOf(terminator);
      String speedStr = data.substring(beginIndex + 1, endIndex);

      if (angleStr != "") {
        angleInt = angleStr.toInt();
      }

      if (speedStr != "") {
        speedInt = speedStr.toInt();
      }

      car.setSpeed(speedInt);
      car.setAngle(angleInt);
    }
  }
}

void autoDrive() {
  while (Serial3.available()) {

  }
}
