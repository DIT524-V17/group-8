#include <Smartcar.h>

Car car;

SR04 leftSonic;
const int TRIGGER_PIN_LEFT = 32;
const int ECHO_PIN_LEFT = 33;

SR04 rightSonic;
const int TRIGGER_PIN_RIGHT = 30;
const int ECHO_PIN_RIGHT = 31;

SR04 centerSonic;
const int TRIGGER_PIN_CENTER = 7;
const int ECHO_PIN_CENTER = 4;

SR04 servoSonic;
const int TRIGGER_PIN_SERVO = 5;
const int ECHO_PIN_SERVO = 6;

Servo myServo;
const int SERVO_PIN = 40;

int endOfString = 4;
int angleInt;
int speedInt;

const int numReadingsLeft = 20;
int leftReadings[numReadingsLeft];
int readIndexLeft = 0;
int totalLeft = 0;
int averageLeft = 0;

const int numReadingsRight = 20;
int rightReadings[numReadingsRight];
int readIndexRight = 0;
int totalRight = 0;
int averageRight = 0;

const int numReadingsCenter = 20;
int centerReadings[numReadingsCenter];
int readIndexCenter = 0;
int totalCenter = 0;
int averageCenter = 0;

const int numReadingsServo = 20;
int servoReadings[numReadingsServo];
int readIndexServo = 0;
int totalServo = 0;
int averageServo = 0;

String selectStr;
String data;

bool isManual = false;
bool isAuto = false;

char terminator = ':';

void setup() {
  Serial.begin(9600);
  Serial3.begin(9600);
  Serial3.setTimeout(3);

  servoSonic.attach(TRIGGER_PIN_SERVO, ECHO_PIN_SERVO);
  centerSonic.attach(TRIGGER_PIN_CENTER, ECHO_PIN_CENTER);
  leftSonic.attach(TRIGGER_PIN_LEFT, ECHO_PIN_LEFT);
  rightSonic.attach(TRIGGER_PIN_RIGHT, ECHO_PIN_RIGHT);

  myServo.attach(SERVO_PIN);
  myServo.write(90);

  for (int thisReadingRight = 0; thisReadingRight < numReadingsRight; thisReadingRight++) {
    rightReadings[thisReadingRight] = 0;
  }
  for (int thisReadingLeft = 0; thisReadingLeft < numReadingsLeft; thisReadingLeft++) {
    leftReadings[thisReadingLeft] = 0;
  }
  for (int thisReadingCenter = 0; thisReadingCenter < numReadingsCenter; thisReadingCenter++) {
    centerReadings[thisReadingCenter] = 0;
  }
  for (int thisReadingServo = 0; thisReadingServo < numReadingsServo; thisReadingServo++) {
    servoReadings[thisReadingServo] = 0;
  }

  car.begin();
}

void loop() {
  if (Serial3.available() && isManual == false) {
    selectStr = Serial3.readString();
    if (selectStr.equals("m")) {
      isManual = true;
    }
  }
Serial.println("Hello I am in the loop");
  if (isManual) {
    Serial.println("INSIDE IF MAUNALSD JIASNDISASUAIJD");
    manualDrive();
  }
}

void manualDrive() {
  while (isManual) {
    if (Serial3.available()) {
      data = Serial3.readString();

      Serial.println(data);

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
}

void autoDrive() {
  while (Serial3.available()) {
    Serial.println("IN AUTIDRIVE");
  }
}

int getLeftReading() {
  totalLeft = totalLeft - leftReadings[readIndexLeft];
  leftReadings[readIndexLeft] = leftSonic.getDistance();

  if (leftReadings[readIndexLeft] == 0) {
    leftReadings[readIndexLeft] = 30;
  }

  totalLeft = totalLeft + leftReadings[readIndexLeft];
  readIndexLeft = readIndexLeft + 1;

  if (readIndexLeft >= numReadingsLeft) {
    readIndexLeft = 0;
  }

  averageLeft = totalLeft / numReadingsLeft;
  return averageLeft;
}

int getRightReading() {
  totalRight = totalRight - rightReadings[readIndexRight];
  rightReadings[readIndexRight] = rightSonic.getDistance();

  if (rightReadings[readIndexRight] == 0) {
    rightReadings[readIndexRight] = 30;
  }

  totalRight = totalRight + rightReadings[readIndexRight];
  readIndexRight = readIndexRight + 1;

  if (readIndexRight >= numReadingsRight) {
    readIndexRight = 0;
  }

  averageRight = totalRight / numReadingsRight;
  return averageRight;
}

int getCenterReading() {
  totalCenter = totalCenter - centerReadings[readIndexCenter];
  centerReadings[readIndexCenter] = centerSonic.getDistance();

  if (centerReadings[readIndexCenter] == 0) {
    centerReadings[readIndexCenter] = 30;
  }

  totalCenter = totalCenter + centerReadings[readIndexCenter];
  readIndexCenter = readIndexCenter + 1;

  if (readIndexCenter >= numReadingsCenter) {
    readIndexCenter = 0;
  }

  averageCenter = totalCenter / numReadingsCenter;
  return averageCenter;
}

int getServoReading() {
  totalServo = totalServo - servoReadings[readIndexServo];
  servoReadings[readIndexServo] = servoSonic.getDistance();

  if (servoReadings[readIndexServo] == 0) {
    servoReadings[readIndexServo] = 30;
  }

  totalServo = totalServo + servoReadings[readIndexServo];
  readIndexServo = readIndexServo + 1;

  if (readIndexServo >= numReadingsServo) {
    readIndexServo = 0;
  }

  averageServo = totalServo / numReadingsServo;
  return averageServo;
}
