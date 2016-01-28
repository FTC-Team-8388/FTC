package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;


public class AutoRed_AutoRed extends LinearOpMode {
    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor motorSweeper;
    DcMotor motorRightExtension;
    DcMotor motorLeftExtension;

    protected DcMotorController controller;

    float leftMotorPower;
    float rightMotorPower;
    //float motorSweeperPower;
    float rightExtensionPower;
    float leftExtensionPower;
    double armPower=0.96;
    //double servoDelta=0.0025;
    double servoDelta=0.01;
    double boxPower=0.0;



    //Servo motors;
    Servo lowerRightArm;
    Servo upperRightArm;
    Servo lowerLeftArm;
    Servo upperLeftArm;
    Servo peopleArm;
    Servo boxServo;

    @Override
    public void runOpMode() throws InterruptedException {


		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */
        //Get our motors from the hardware map
        motorRight = hardwareMap.dcMotor.get("RightMotor");
        motorLeft = hardwareMap.dcMotor.get("LeftMotor");
        motorSweeper = hardwareMap.dcMotor.get("SweeperMotor");
        motorRightExtension = hardwareMap.dcMotor.get("RightExtensionMotor");
        motorLeftExtension = hardwareMap.dcMotor.get("LeftExtensionMotor");

        //Get our servo motors
        lowerLeftArm = hardwareMap.servo.get("LowerLeftServo");
        upperLeftArm = hardwareMap.servo.get("UpperLeftServo");
        lowerRightArm = hardwareMap.servo.get("LowerRightServo");
        upperRightArm = hardwareMap.servo.get("UpperRightServo");
        peopleArm = hardwareMap.servo.get("PeopleHurlerArm");
        boxServo = hardwareMap.servo.get("BoxServo");

        waitForStart();

        // set initial servo values to the closed position
        // prevents random value assigned on start.

        boxServo.setPosition(0.00);
        lowerLeftArm.setPosition(0.0);	// retracted
        // lowerRightArm.setPosition(0.95);   // retracted
        lowerRightArm.setPosition(0.97);   // retracted
        upperLeftArm.setPosition(0.97);		// retracted
        upperRightArm.setPosition(0.0);   // retracted
        peopleArm.setPosition(0.96);

        //Revers Direction on left motor
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        sleep(2000);

        // drop people
        while (armPower > 0.35) {
            armPower -= servoDelta;
            if (armPower <= 0.35)
                armPower = 0.35;
            //armPower = 0.35;		//launched position
            peopleArm.setPosition(armPower);
            sleep(20);  // delay 25 ms to slow rate
        }

        sleep(2000); // wait 2 seconds
        // move people arm back to start
        while(armPower< 0.96) {
            armPower += servoDelta;
            if (armPower >= 0.96)
                armPower = 0.96;
            //armPower = 0.95;  // retracted position
            peopleArm.setPosition(armPower);
            sleep(20);
        }

        sleep(2000); // wait 2 seconds

        lowerLeftArm.setPosition(.25);
        upperLeftArm.setPosition(0.2);
        lowerRightArm.setPosition(0.7);
        upperRightArm.setPosition(0.75);
        sleep(2000); // wait 2 seconds

        upperLeftArm.setPosition(0.97);		// retracted
        upperRightArm.setPosition(0.0);   // retracted
        lowerLeftArm.setPosition(0.0);
        lowerRightArm.setPosition(0.97);
        sleep(2000);

        // drop people
        while (armPower > 0.35) {
            armPower -= servoDelta;
            if (armPower <= 0.35)
                armPower = 0.35;
            //armPower = 0.35;		//launched position
            peopleArm.setPosition(armPower);
            sleep(20);  // delay 25 ms to slow rate
        }

        sleep(2000); // wait 2 seconds
        // move people arm back to start
        while(armPower< 0.96) {
            armPower += servoDelta;
            if (armPower >= 0.96)
                armPower = 0.96;
            //armPower = 0.95;  // retracted position
            peopleArm.setPosition(armPower);
            sleep(20);
        }
        //waitOneFullHardwareCycle();


    }//end run op mode


}// end of class
