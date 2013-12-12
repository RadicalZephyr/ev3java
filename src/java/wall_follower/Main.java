/* Program name: Main
     Description: A wall-following robot
     Interesting Features:

Hardware:
Port                  Sensor
1                      Touch
2                      Ultrasonic
4                      Touch

Port                Motor
B                    Left
C                    Right

Course Number: CSCI 372, Fall 2013
Student Name: Geoff Shannon
References: lejos
*/

package wall_follower;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;

import lejos.hardware.Button;
import lejos.hardware.LCD;
import lejos.hardware.Sound;

import lejos.robotics.SampleProvider;

import lejos.utility.Delay;

public class Main
{
    private EV3TouchSensor leftTouch;
    private EV3TouchSensor rightTouch;

    private NXTRegulatedMotor leftMotor;
    private NXTRegulatedMotor rightMotor;

    private EV3UltrasonicSensor distance;

    public static void main(String[] args) {
        Main current = new Main();
        boolean done = false;
        LCD.clear();
        current.promptForStartPush();
        current.followWall();
        LCD.clear();
        LCD.drawString("Done!", 1, 1);
        Delay.msDelay(1000);
    }

    public Main() {
        setupSensors();
        setupMotors();
    }

    void setupSensors() {
        leftTouch = new EV3TouchSensor(SensorPort.S4);
        rightTouch = new EV3TouchSensor(SensorPort.S3);

        distance = new EV3UltrasonicSensor(SensorPort.S1);
        distance.enable();
    }

    void setupMotors() {
        leftMotor = Motor.C;
        rightMotor = Motor.B;
        leftMotor.setAcceleration(3000);
        rightMotor.setAcceleration(3000);
        leftMotor.setSpeed(200);
        rightMotor.setSpeed(200);
    }

    void promptForStartPush() {
        Sound.twoBeeps();
        Sound.beep();
        LCD.clear();
        LCD.drawString("Please push any", 1, 1);
        LCD.drawString("button to begin.", 1, 3);
        Button.waitForAnyPress();
        LCD.clear();
        LCD.drawString("Running...", 1, 1);
    }

    void startMotors() {
        leftMotor.backward();
        rightMotor.backward();
    }

    void stopMotors() {
        leftMotor.stop();
        rightMotor.stop();
        leftMotor.flt();
        rightMotor.flt();
    }

    float checkDistance(SampleProvider distanceSampler) {
        float[] distance = new float[1];
        distanceSampler.fetchSample(distance, 0);
        return distance[0];
    }

    void followWall() {
        startMotors();
        SensorReading prevReading = readSensors();
        SensorReading curReading = readSensors();
        boolean done = false;

        while (!done) {
            prevReading = curReading;
            curReading = readSensors();
        }
    }

    SensorReading readSensors() {
        SensorReading r = new SensorReading();
        return r;
    }

    private class SensorReading {
        public float distance;
        public boolean leftTouching;
        public boolean rightTouching;
    }
}
