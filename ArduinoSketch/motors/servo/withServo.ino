#include <Smartcar.h>
Car car;
const int fSpeed =30; //70% of the full speed forward
const int bSpeed = -30; //70% of the full speed backward
const int lDegrees = -75; //degrees to turn left
const int rDegrees = 75; //degrees to turn right
SR04 ultrasonicSensor;
Gyroscope gyro;
Servo servo;
int angle = 0;
const int TRIGGER_PIN = 6; //D6
const int ECHO_PIN = 5; //D5


void setup() {
  Serial3.begin(9600);
  ultrasonicSensor.attach(TRIGGER_PIN, ECHO_PIN);
  gyro.attach();
  gyro.begin();
  car.begin(gyro); //initialize the car using the encoders and the gyro
  servo.attach(9);
}

void loop() {
  handleInput();
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
        servoRotation();
        car.setSpeed(fSpeed);
        car.setAngle(0);
        break;
      case 's': //go back
        servoRotation();
        car.setSpeed(bSpeed);
        car.setAngle(0);
        break;
      case 'o': //autonomous drive
        atonomousDrive();
        break;
        
      default: //if you receive something that you don't know, just stop
        car.setSpeed(0);
        car.setAngle(0);
        servoRotation();
    }
  }
}


void atonomousDrive(){
  
  while(true){
    int dist = ultrasonicSensor.getDistance();
  car.setSpeed(fSpeed);
  // We set the distance to 35 instead of 15, so the car has time to stop before it reaches 15
  if (dist < 35 && dist > 2) {
  
  car.rotate(45);
  delay(200);  
      }
    }
  }

  void servoRotation(){
    while(true){
  // scan from 0 to 180 degrees
  for(angle = 0; angle < 180; angle++){                                  
    servo.write(angle);               
    delay(15);                   
  }
  // now scan back from 180 to 0 degrees
  for(angle = 180; angle > 0; angle--){                                
    servo.write(angle);           
    delay(15);       
  }
  }
}
