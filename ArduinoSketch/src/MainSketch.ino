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
int servoAngle = 0;

Gyroscope gyro;
int motorSpeed = 80;

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

unsigned long currentMillis = 0;
unsigned long previousMillis = 0;
long interval = 30;
int currentPos = 90;

bool isBeginning = true;
bool isAuto = false;
bool isManual = false;
bool goingRight = true;

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
  gyro.attach();

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

  car.begin(gyro);
}

void loop() {
  if (Serial3.available() && !isManual && !isAuto) {
    selectStr = Serial3.readString();
    if (selectStr.equals("m")) {
      isManual = true;
    }
    if (selectStr.equals("a")) {
      isAuto = true;
    }
  }

  if (isManual) {
    manualDrive();
  }
  else if (isAuto) {
    autoDrive();
  }

  currentMillis = millis();
  servoMovement();
}

void servoMovement() {
  if (currentMillis - previousMillis > interval) {
    previousMillis = currentMillis;
    Serial.println(currentPos);
    if (goingRight) {
      currentPos = currentPos - 5;
      if (currentPos == 45) {
        goingRight = false;
      }
    }

    else if (!goingRight) {
      currentPos = currentPos + 5;
      if (currentPos == 135) {
        goingRight = true;
      }
    }
    myServo.write(currentPos);
  }
}

void manualDrive() {
  if (Serial3.available()) {
    data = Serial3.readString();
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
  if (isBeginning) {
    car.setSpeed(35);
    isBeginning = false;
    delay(100);
  }
  car.setSpeed(25);
  getLeftReading();
  getRightReading();
  getServoReading();
  int staticDist = centerSonic.getDistance();
  Serial3.println(staticDist);
  if (staticDist < 45 && staticDist != 0) {
    if (averageLeft < 35 || averageRight < 35)  {
      if (averageLeft < averageRight) {
        car.rotate(40);
        delay(100);
      }
      else if (averageLeft > averageRight) {
        car.rotate(-40);
        delay(100);
      }
    }
    else if (staticDist < 10 && staticDist != 0) {
      car.setSpeed(-30);
      delay(500);
      car.stop();
      delay(200);
      rotateOnSpot(90);
      delay(200);
    }
  }
}

void getLeftReading() {
  totalLeft = totalLeft - leftReadings[readIndexLeft];
  leftReadings[readIndexLeft] = leftSonic.getDistance();

  if (leftReadings[readIndexLeft] == 0) {
    leftReadings[readIndexLeft] = 35;
  }

  totalLeft = totalLeft + leftReadings[readIndexLeft];
  readIndexLeft = readIndexLeft + 1;

  if (readIndexLeft >= numReadingsLeft) {
    readIndexLeft = 0;
  }

  averageLeft = totalLeft / numReadingsLeft;
  delay(1);
}

void getRightReading() {
  totalRight = totalRight - rightReadings[readIndexRight];
  rightReadings[readIndexRight] = rightSonic.getDistance();

  if (rightReadings[readIndexRight] == 0) {
    rightReadings[readIndexRight] = 35;
  }

  totalRight = totalRight + rightReadings[readIndexRight];
  readIndexRight = readIndexRight + 1;

  if (readIndexRight >= numReadingsRight) {
    readIndexRight = 0;
  }

  averageRight = totalRight / numReadingsRight;
  delay(1);
}

void getCenterReading() {
  totalCenter = totalCenter - centerReadings[readIndexCenter];
  centerReadings[readIndexCenter] = centerSonic.getDistance();

  if (centerReadings[readIndexCenter] == 0) {
    centerReadings[readIndexCenter] = 40;
  }

  totalCenter = totalCenter + centerReadings[readIndexCenter];
  readIndexCenter = readIndexCenter + 1;

  if (readIndexCenter >= numReadingsCenter) {
    readIndexCenter = 0;
  }

  averageCenter = totalCenter / numReadingsCenter;
  delay(1);
}

void getServoReading() {
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
  delay(1);
}

/*
This method was taken from the examples presented by Dimitris Platis
*/
void rotateOnSpot(int targetDegrees) {
  targetDegrees %= 360; //put it on a (-360,360) scale
  if (!targetDegrees) return; //if the target degrees is 0, don't bother doing anything
  /* Let's set opposite speed on each side of the car, so it rotates on spot */
  if (targetDegrees > 0) { //positive value means we should rotate clockwise
    car.setMotorSpeed(motorSpeed, -motorSpeed); // left motors spin forward, right motors spin backward
  } else { //rotate counter clockwise
    car.setMotorSpeed(-motorSpeed, motorSpeed); // left motors spin backward, right motors spin forward
  }
  unsigned int initialHeading = gyro.getAngularDisplacement(); //the initial heading we'll use as offset to calculate the absolute displacement
  int degreesTurnedSoFar = 0; //this variable will hold the absolute displacement from the beginning of the rotation
  while (abs(degreesTurnedSoFar) < abs(targetDegrees)) { //while absolute displacement hasn't reached the (absolute) target, keep turning
    gyro.update(); //update to integrate the latest heading sensor readings
    int currentHeading = gyro.getAngularDisplacement(); //in the scale of 0 to 360
    if ((targetDegrees < 0) && (currentHeading > initialHeading)) { //if we are turning left and the current heading is larger than the
      //initial one (e.g. started at 10 degrees and now we are at 350), we need to substract 360, so to eventually get a signed
      currentHeading -= 360; //displacement from the initial heading (-20)
    } else if ((targetDegrees > 0) && (currentHeading < initialHeading)) { //if we are turning right and the heading is smaller than the
      //initial one (e.g. started at 350 degrees and now we are at 20), so to get a signed displacement (+30)
      currentHeading += 360;
    }
    degreesTurnedSoFar = initialHeading - currentHeading; //degrees turned so far is initial heading minus current (initial heading
    //is at least 0 and at most 360. To handle the "edge" cases we substracted or added 360 to currentHeading)
  }
  car.stop(); //we have reached the target, so stop the car
}
