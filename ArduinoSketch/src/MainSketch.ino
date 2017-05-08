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

Odometer odometer;
const int ODOMETER_PIN = 2;

int endOfString = 4;
int angleInt;
int speedInt;

// LEFT ultrasonic smoothed readings
const int numReadingsLeft = 20; // size of array
int leftReadings[numReadingsLeft];
int readIndexLeft = 0;
int totalLeft = 0;
int averageLeft = 0;

// RIGHT ultrasonic smoothed readings
const int numReadingsRight = 20;
int rightReadings[numReadingsRight];
int readIndexRight = 0;
int totalRight = 0;
int averageRight = 0;

// CENTER ultrasonic smoothed readings
const int numReadingsCenter = 20;
int centerReadings[numReadingsCenter];
int readIndexCenter = 0;
int totalCenter = 0;
int averageCenter = 0;

// SERVO ultrasonic smoothed readings
const int numReadingsServo = 20;
int servoReadings[numReadingsServo];
int readIndexServo = 0;
int totalServo = 0;
int averageServo = 0;

String data; // data from app to car, used in handleInput

// Servo millis and position
unsigned long currentMillis = 0;
unsigned long previousMillis = 0;
long interval = 30;
int currentPos = 90;

// mode booleans, car is on manual and servo is moving from 90 to 45 (righgt direction) at the start
bool isAuto = false;
bool isManual = true;
bool goingRight = true;
bool safety = false;
bool wasRotating = false;
bool isConnected = false;

char terminator = ':';

//ultrasonic distance calculation variables
float distanceServo = 0;

void setup() {
  Serial.begin(9600);
  Serial3.begin(9600);
  Serial3.setTimeout(25);

  servoSonic.attach(TRIGGER_PIN_SERVO, ECHO_PIN_SERVO);
  centerSonic.attach(TRIGGER_PIN_CENTER, ECHO_PIN_CENTER);
  leftSonic.attach(TRIGGER_PIN_LEFT, ECHO_PIN_LEFT);
  rightSonic.attach(TRIGGER_PIN_RIGHT, ECHO_PIN_RIGHT);

  odometer.attach(ODOMETER_PIN);
  myServo.attach(SERVO_PIN);
  myServo.write(90); // servo starts at 90 degrees, facing forward
  gyro.attach();

  // Fill smoothing arrays with 0 at the start
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
  car.begin(odometer);
}

void loop() {



  currentMillis = millis(); // checks current time
  if(isConnected){
    servoMovement();
  }
  handleInput();
  if (isAuto) {
    autoDrive();
  }

  getServoReading();
  sendData();
  Serial.println(averageServo);
}

/*
If the interval has passed, the servo will move 5 degrees to the left/right
until it reaches 135/45 and then switches to the opposite direction.
Only one addition/subtraction of 5 degrees happens per call.
Follows a sweeping movement.
*/
void servoMovement() {
  if (currentMillis - previousMillis > interval) {
    previousMillis = currentMillis; // updates previousMillis

    if (goingRight) { // if servo should be going right, 5 degrees is subtracted until we reach 45
      currentPos = currentPos - 5;
      if (currentPos == 45) { // Switch to moving left
        goingRight = false;
      }
    }
    else if (!goingRight) { // if servo should be going left, 5 degrees is added until we reach 135
      currentPos = currentPos + 5;
      if (currentPos == 135) { // Switch to moving right
        goingRight = true;
      }
    }
    myServo.write(currentPos);
  }
}

/*
Handles all input received from the app
*/
void handleInput() {
  if (Serial3.available()) {
    data = Serial3.readString(); // Receives the input from the app and stores it in data
    Serial.println(data);
    if (data.equals("a")) { // If 'a' is received, switch to autonomous
      car.setAngle(0); // fixes car swirling bug once toggled to autonomous
      car.setSpeed(35); // higher speed than the autoDrive() method to give a movement boost for the car at first
      isAuto = true;
      isManual = false;
      delay(100);
    }
    else if(data.equals("x")) {
      toggleSafety();
    }
    else if (data.equals("m")) { // Switches to manual, autonomous shuts off
      isManual = true;
      isAuto = false;
      car.setSpeed(0);
    }
    else if(data.equals("c")){
      isConnected = true;
    }
    else if (data.substring(data.length() - endOfString).equals("STOP")) {
      Serial.println("Car STOPPED");
      car.stop();
    } else { // else the car is receiving movement data for manual
      // split the data string to the angle and speed, store in angleStr and speedStr
      int beginIndex = data.indexOf(terminator);
      String angleStr = data.substring(0, beginIndex);

      int endIndex = data.lastIndexOf(terminator);
      String speedStr = data.substring(beginIndex + 1, endIndex);

      // converts angleStr and speedStr to integers if they are not empty
      if (angleStr != "") {
        angleInt = angleStr.toInt();
      }

      if (speedStr != "") {
        speedInt = speedStr.toInt();
      }
      if((safety==true) && (centerSonic.getDistance() < 20 && centerSonic.getDistance() != 0) ){
        speedInt=0;
        angleInt=0;
      }
      car.setSpeed(speedInt);
      car.setAngle(angleInt);
    }
  }
}

/*
The obstacle detection and avoidance method.
*/
void autoDrive() {
  if (wasRotating) {
    car.setSpeed(35); // if car rotated
    wasRotating = false;
  }
  else
    car.setSpeed(25); // car moves at this speed throughout autonomous

  getLeftReading();
  getRightReading();
  getServoReading();
  int staticDist = centerSonic.getDistance();
  Serial3.println(staticDist);
  if (staticDist < 45 && staticDist != 0) { // if there is an imminent obstacle detected from the front
    if (averageLeft < 35 || averageRight < 35)  { // checks left and right to determine the more appropriate rotation direction
      if (averageLeft < averageRight) {
        car.rotate(40);
        delay(100);
      }
      else if (averageLeft > averageRight) {
        car.rotate(-40);
        delay(100);
      }
    }
    else if (staticDist < 10 && staticDist != 0) { // if no obstacles are found from left and right and an obstacle is very close
      // the car goes backwards a bit then rotates 180 degrees
      car.setSpeed(-30);
      delay(500);
      car.stop();
      delay(200);
      rotateOnSpot(90);
      delay(200);
    }
  }
}

void sendData() {
  //int sonicDistance = servoSonic.getDistance();
  int odometerDistance = odometer.getDistance();
  int servoAngle = myServo.read();
  int speed = car.getSpeed();

  String strSonic = String(averageServo);
  String strOdometer = String(odometerDistance);
  String strAngle = String(servoAngle);
  String strSpeed = String(speed);

  String dataStr = "s" + strSpeed + ":" + "d" + strOdometer + ":" + "a" + strAngle + ":" + "u" + strSonic + ":";
  //Serial.println(dataStr);

  int strLength = dataStr.length() + 1;
  char charArray[strLength];
  dataStr.toCharArray(charArray, strLength);
  Serial3.write(charArray, strLength);
}

/*
Smoothing methods.
All get___Reading() methods are to refresh the average of each ultrasonic reading
by removing the oldest value in the array and adding a new one.
*/
void getLeftReading() {
  totalLeft = totalLeft - leftReadings[readIndexLeft]; // subtracts oldest reading from the total
  leftReadings[readIndexLeft] = leftSonic.getDistance(); // gets a new reading

  // replacing 0's with another value so that the average is not weighed down
  if (leftReadings[readIndexLeft] == 0) {
    leftReadings[readIndexLeft] = 35;
  }

  totalLeft = totalLeft + leftReadings[readIndexLeft]; // adds newest reading to the array
  readIndexLeft = readIndexLeft + 1;

  if (readIndexLeft >= numReadingsLeft) { // if current index is bigger than size of the array
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

  totalServo = totalServo + servoReadings[readIndexServo];
  readIndexServo = readIndexServo + 1;

  if (readIndexServo >= numReadingsServo) {
    readIndexServo = 0;
  }

  averageServo = (totalServo / numReadingsServo) - 3; // subtracting 3 to not count the space in the car
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
  wasRotating = true;
}

void toggleSafety(){
  if(safety){
    safety = false;
  }
  else{
    safety = true;
  }
}
