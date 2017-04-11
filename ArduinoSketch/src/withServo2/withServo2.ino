#include <Smartcar.h>
Car car;
const int fSpeed = 33; //70% of the full speed forward
const int bSpeed = -33; //70% of the full speed backward
//const int lDegrees = 0; //degrees to turn left
//const int rDegrees = 0; //degrees to turn right
Gyroscope gyro;
SR04 rotatingUltraSonic;
const int TRIGGER_PIN = 5; //D5
const int ECHO_PIN = 6; //D6
SR04 ultraSonic;
const int TR_PIN = 7; //D11
const int EC_PIN = 4; //D10
Servo myservo;
const int SERVO_PIN = 40;
long previousMillis = 0;
long autoPreviousMillis = 0;
long interval = 550;
long autoInterval = 50;
int rotatingDist = 0;
int servoAngle = 0;
int staticDist = 0;
int distanceTravelled;
long previousDistanceMillis = 0;
long distanceInterval = 1000;
int previousDistanceTravelled = -1;
long previousReading;


const int numReadingsRight = 15;
int readingsRight[numReadingsRight];      // the readings from the analog input
int readIndexRight = 0;                   // the index of the current reading
int totalRight = 0;                       // the running total
int averageRight = 0;                     // the average

const int numReadingsLeft = 15;
int readingsLeft[numReadingsLeft];
int readIndexLeft = 0;
int totalLeft = 0;
int averageLeft = 0;

boolean isAutonomous = false;
boolean isSpeed = false;
boolean firstTime = true;

void setup() {
  Serial3.begin(9600);
  Serial.begin(9600);
  rotatingUltraSonic.attach(TRIGGER_PIN, ECHO_PIN);
  ultraSonic.attach(TR_PIN, EC_PIN);
  gyro.attach();
  gyro.begin();

  car.begin(gyro); //initialize the car using the encoders and the gyro
  //set array all to 0
  for (int thisReadingRight = 0; thisReadingRight < numReadingsRight; thisReadingRight++) {
    readingsRight[thisReadingRight] = 0;
  }
  for (int thisReadingLeft = 0; thisReadingLeft < numReadingsLeft; thisReadingLeft++) {
    readingsLeft[thisReadingLeft] = 0;
  }

  myservo.attach(SERVO_PIN);
  myservo.write(90);

}

//TEST loops for bluetooth connection

void loop() {
  if (firstTime) {
    Serial.println("In FirstTime");
    if (Serial3.available()) {
      char maReading = Serial3.read();
      if (maReading == 'a') {
        isAutonomous = true;
        car.setSpeed(fSpeed);
        firstTime = false;
      } else if (maReading == 'm') {
        firstTime = false;
      }
    }
  }

  //if it is not autonomous, manual starts
  if (!isAutonomous  && firstTime == false) {
    if (Serial3.available()) {
      int readings = Serial3.read();
      if  (readings == 1) {
        car.setSpeed(0);
      }
      if (readings != 0) {
        if (isSpeed) {
          isSpeed = false;
          Serial.print("Setting speed: ");
          Serial.println(readings);
          car.setSpeed(readings);
        }
        else  {
          isSpeed = true;
          if (readings < 100 && readings >  80) {
            car.setAngle(0);
          }
          else if (readings < 80 && readings > 1)
            car.setAngle(90);
          else if (readings < 180 && readings > 100)
            car.setAngle(-90);
          else
            car.setSpeed(0);

          Serial.print("Angle:  ");
          Serial.println(readings);
        }
      }
    }
  }
  else if (isAutonomous) {
    servoAngle = myservo.read();

    if (servoAngle == 15) {
      totalRight = totalRight - readingsRight[readIndexRight];
      readingsRight[readIndexRight] = rotatingUltraSonic.getDistance();
      if (readingsRight[readIndexRight] == 0) {
        readingsRight[readIndexRight] = 30;
      }
      totalRight = totalRight + readingsRight[readIndexRight];
      readIndexRight = readIndexRight + 1;
      if (readIndexRight >= numReadingsRight) {
        readIndexRight = 0;
      }

      averageRight = totalRight / numReadingsRight;
      delay(1);
    }
    else {
      totalLeft = totalLeft - readingsLeft[readIndexLeft];
      readingsLeft[readIndexLeft] = rotatingUltraSonic.getDistance();
      if (readingsLeft[readIndexLeft] == 0) {
        readingsLeft[readIndexLeft] = 30;
      }
      totalLeft = totalLeft + readingsLeft[readIndexLeft];
      readIndexLeft = readIndexLeft + 1;
      if (readIndexLeft >= numReadingsLeft) {
        readIndexLeft = 0;
      }

      averageLeft = totalLeft / numReadingsLeft;
      delay(1);
    }

    unsigned long currentMillis = millis();

    if (currentMillis - previousMillis > interval) {
      previousMillis = currentMillis;
      if (servoAngle == 15) {
        myservo.write(170);
      }
      else {
        myservo.write(15);
      }
    }
    if (currentMillis - autoPreviousMillis > autoInterval) {
      autoPreviousMillis = currentMillis;

      staticDist = ultraSonic.getDistance();
      if (staticDist < 40 && staticDist > 3) {
        if (averageLeft < 25 || averageRight < 25) {
          if (averageLeft < averageRight) {
            car.rotate(45);
            delay(100);
          }
          else {
            car.rotate(-45);
            delay(100);
          }
        }
        else if (staticDist < 20 && staticDist > 3) {
          car.rotate(45);
          delay(100);
        }
      }
    }
  }
}
