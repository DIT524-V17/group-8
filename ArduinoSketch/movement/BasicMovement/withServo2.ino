#include <Smartcar.h>
Car car;
const int fSpeed = 30; //70% of the full speed forward
const int bSpeed = -30; //70% of the full speed backward
const int lDegrees = -35; //degrees to turn left
const int rDegrees = 35; //degrees to turn right
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
long interval = 500;
int rotatingDist = 0;
int rotatingAngle = 0;
void setup() {
  Serial3.begin(9600);
  rotatingUltraSonic.attach(TRIGGER_PIN, ECHO_PIN);
  ultraSonic.attach(TR_PIN, EC_PIN);
  gyro.attach();
  gyro.begin();
  car.begin(gyro); //initialize the car using the encoders and the gyro
  
  myservo.attach(SERVO_PIN);
  myservo.write(90);
  car.setSpeed(fSpeed); //TEST
}
void loop() {
  //handleInput();
  /*
  myservo.write(45);  
  delay(500); 
  myservo.write(135);
  delay(500);
  */
  
  unsigned long currentMillis = millis();
  if(currentMillis - previousMillis > interval) {
    previousMillis = currentMillis;
    if (myservo.read() == 35)
      myservo.write(145);
    else
      myservo.write(35);
  }
  
  atonomousDrive();
  //Serial3.println(rotatingUltraSonic.getDistance());
  //Serial3.println(ultraSonic.getDistance());
}
void handleInput() { //handle serial input if there is any
  if (Serial3.available()) {
    char input = Serial3.read(); //read everything that has been received so far and log down the last entry
    switch (input) {
      case 'a': //rotate counter-clockwise going forward
        car.setSpeed(fSpeed);
        car.setAngle(lDegrees);
        break;
      case 'd': //turn clock-wise
        car.setSpeed(fSpeed);
        car.setAngle(rDegrees);
        break;
      case 'w': //go ahead
        car.setSpeed(fSpeed);
        car.setAngle(0);
        break;
      case 's': //go back
        car.setSpeed(bSpeed);
        car.setAngle(0);
        break;
      case 'o': //autonomous drive
        atonomousDrive();
        break;
        
      default: //if you receive something that you don't know, just stop
        car.setSpeed(0);
        car.setAngle(0);
    }
  }
}
void atonomousDrive(){ 
    int dist = ultraSonic.getDistance();
            
    if (dist < 35 && dist > 2) {
      rotatingAngle = myservo.read();
      rotatingDist = rotatingUltraSonic.getDistance();
      
      if (rotatingDist < 35 && rotatingDist > 2 && rotatingAngle == 35) {
        car.rotate(-45);
      }
      else {
        rotatingDist = rotatingUltraSonic.getDistance();
        
        if (rotatingDist < 35 && rotatingDist > 2 && rotatingAngle == 145){
          car.rotate(45);
        }
        else {
          car.setSpeed(0);
        }
     }
      delay(200);
    }
}
 
