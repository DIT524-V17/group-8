#include <Smartcar.h>
Car car;
const int fSpeed = 27; //70% of the full speed forward
const int bSpeed = -27; //70% of the full speed backward
const int lDegrees = -32; //degrees to turn left
const int rDegrees = 32; //degrees to turn right
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
long interval = 450;
long autoInterval = 200;
int rotatingDist = 0;
int servoAngle = 0;
int staticDist = 0;

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
  servoAngle = myservo.read();
  
  unsigned long currentMillis = millis();
  
  if(currentMillis - previousMillis > interval) {
    previousMillis = currentMillis;
    if (servoAngle == 15){
      myservo.write(170);      
    }     
    else{
      myservo.write(15);      
    }
  }
  if(currentMillis - autoPreviousMillis > autoInterval) {
    autoPreviousMillis = currentMillis;
    if (servoAngle == 15) {
      Serial3.print("DISTANCE FROM RIGHT UNDER  ");
      Serial3.println(rotatingUltraSonic.getDistance());
      autonomousLeft();      
    }
    else {
      Serial3.print("LEFT   ");
      Serial3.println(rotatingUltraSonic.getDistance());
      autonomousRight();
      
    }
    
  }
  //myservo.write(15);
  //Serial3.println(myservo.read());
  //atonomousDrive2();
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
        //atonomousDrive();
        break;
        
      default: //if you receive something that you don't know, just stop
        car.setSpeed(0);
        car.setAngle(0);
    }
  }
}
/*
void atonomousDrive1(){ 
    int dist = ultraSonic.getDistance();
            
    if (dist < 35 && dist > 2) {
      //rotatingAngle = myservo.read();
      rotatingDist = rotatingUltraSonic.getDistance();
           
      if (rotatingDist < 25 && rotatingDist > 2 && rotatingAngle == 15) {
        car.rotate(-45);
      }
      else {
        rotatingDist = rotatingUltraSonic.getDistance();
        
        if (rotatingDist < 25 && rotatingDist > 2) {
          car.rotate(45);
        }
     }    
      delay(200);
    }
}
*/
void atonomousDrive2(){ 
  staticDist = ultraSonic.getDistance();
  servoAngle = myservo.read();
  if (staticDist < 35 && staticDist > 2) {
    rotatingDist = rotatingUltraSonic.getDistance();
    if (rotatingDist < 25 && rotatingDist > 2) {
      switch (servoAngle){
        case 15:      
          car.rotate(-45);
          delay(200);          
          break;
  
        case 170:          
          car.rotate(45);
          delay(200);
          break;
      } 
    }
  }
}
                                                 
void autonomousLeft() {
  staticDist = ultraSonic.getDistance();
  if (staticDist < 35 && staticDist > 2) {
    car.setSpeed(0);
    rotatingDist = rotatingUltraSonic.getDistance();
    if (rotatingDist < 30  && rotatingDist > 2) {
      car.rotate(-45);
    }
  }  
}
void autonomousRight() {
  staticDist = ultraSonic.getDistance();
  if (staticDist < 35 && staticDist > 2) {
    car.setSpeed(0);
    rotatingDist = rotatingUltraSonic.getDistance();
    if (rotatingDist < 30 && rotatingDist > 2) {
      car.rotate(45);
    }
  }  
}
 
