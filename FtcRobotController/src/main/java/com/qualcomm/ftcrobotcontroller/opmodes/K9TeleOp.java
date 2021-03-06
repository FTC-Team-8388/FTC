/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.ftcrobotcontroller.opmodes.GamepadTask;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class K9TeleOp extends Robot {

	public ConcurrentLinkedQueue<GamepadTask> tasks;
	public ConcurrentLinkedQueue<GamepadTask.GamepadEvent> events;




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
	double servoDelta=0.0025;
	double boxPower=0.0;



	//Servo motors;
	Servo lowerRightArm;
	Servo upperRightArm;
	Servo lowerLeftArm;
	Servo upperLeftArm;
	Servo peopleArm;
	Servo boxServo;

	/**
	 * Constructor
	 */
	public K9TeleOp() {


	}

	@Override
	protected void addTask(RobotTask task) {

		tasks.add((GamepadTask) task);
		task.start();
	}

	@Override
	public void queueEvent(RobotEvent event) {

		events.add((GamepadTask.GamepadEvent) event);
	}

	/*
         * Code to run when the op mode is initialized goes here
         *
         * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
         */
	@Override
	public void init() {


		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */
		


		// Create our tasks an events lists
		tasks = new ConcurrentLinkedQueue<GamepadTask>();
		events = new ConcurrentLinkedQueue<GamepadTask.GamepadEvent>();

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

		// set initial servo values to the closed position
		// prevents random value assigned on start.

		boxServo.setPosition(0.05);
		lowerLeftArm.setPosition(0.0);	// retracted
		lowerRightArm.setPosition(0.95);   // retracted
		upperLeftArm.setPosition(0.97);		// retracted
		upperRightArm.setPosition(0.0);   // retracted
		peopleArm.setPosition(0.96);

		//Revers Direction on left motor
		motorLeft.setDirection(DcMotor.Direction.REVERSE);

		//Create our task and add to tasks list for gamepad 1.
		GamepadTask task = new GamepadTask(this, GamepadTask.GamepadNumber.GAMEPAD1);
		addTask(task);

		//Create our task and add to tasks list for gamepad 2.
		GamepadTask task2 = new GamepadTask(this, GamepadTask.GamepadNumber.GAMEPAD2);
		addTask(task2);


		// assign the starting position of the wrist and claw
		//armPosition = 0.2;
		//clawPosition = 0.2;
		//telemetry.addData("End Init", "*** Robot Data***");
	}

	//This function takes action based on the EventKind
	@Override
	public void handleEvent(RobotEvent e) {
		GamepadTask.GamepadEvent event = (GamepadTask.GamepadEvent) e;
		// Driver controller
		// drives robot - left stick left motor, right stick right motor
		// controls -
		// button down means button pressed
		if(((GamepadTask)event.task).gamepadNumber == GamepadTask.GamepadNumber.GAMEPAD1) {

			switch (event.kind) {
				case BUTTON_A_DOWN:
					motorSweeper.setPower(0.0);
					motorSweeper.setPower(-1.0);// TURN sweeper on reversed full power
					break;
				case BUTTON_B_DOWN:
					motorSweeper.setPower(0.0);// TURN sweeper off
					break;
				case BUTTON_X_DOWN:
					motorSweeper.setPower(0.0);// TURN sweeper off
					break;
				case BUTTON_Y_DOWN:
					motorSweeper.setPower(0.0);
					motorSweeper.setPower(1.0);// TURN sweeper on full power
					break;
				case BUMPER_LEFT_DOWN:     // open box for load
					if(boxPower < 0.4)
					{
						boxPower += servoDelta;
						if (boxPower > 0.4)
							boxPower=0.4;
					}
					else
					{
						boxPower -= servoDelta;
						if (boxPower < 0.4)
							boxPower=0.4;
					}
					boxServo.setPosition(boxPower);
					//boxServo.setPosition(0.4);
					break;
				case TRIGGER_LEFT_DOWN:   // open box discharge
					boxPower += servoDelta;
					if (boxPower > 0.9)
						boxPower=0.9;
					boxServo.setPosition(boxPower);
					//boxServo.setPosition(0.85);
					break;
				case BUMPER_RIGHT_DOWN:
					boxPower -= servoDelta;
					if (boxPower <0.05)
						boxPower=0.05;
					boxServo.setPosition(boxPower);
					//boxServo.setPosition(0.07);
					break;

				case TRIGGER_RIGHT_DOWN:

					break;
				case LEFT_STICK_Y:
					leftMotorPower = gamepad1.left_stick_y;

					// clip the right/left values so that the values never exceed +/- 1
					leftMotorPower = Range.clip(leftMotorPower, -1, 1);

					// scale the joystick value to make it easier to control
					// the robot more precisely at slower speeds
					leftMotorPower = (float) scaleInput(leftMotorPower);

					// write the values to the motors
					motorLeft.setPower(leftMotorPower);
					break;
				case RIGHT_STICK_Y:
					rightMotorPower = gamepad1.right_stick_y;

					// clip the right/left values so that the values never exceed +/- 1
					rightMotorPower = Range.clip(rightMotorPower, -1, 1);

					// scale the joystick value to make it easier to control
					// the robot more precisely at slower speeds.
					rightMotorPower = (float) scaleInput(rightMotorPower);
					//left =  (float)scaleInput(left);

					// write the values to the motors
					motorRight.setPower(rightMotorPower);
					break;
			}
		}
		else if(((GamepadTask)event.task).gamepadNumber == GamepadTask.GamepadNumber.GAMEPAD2) {
			switch (event.kind) {
				case BUTTON_X_DOWN:
					lowerLeftArm.setPosition(.75);		//extended

					/*
					lowerLeftArm.setPosition(0.0);	// retracted
					lowerRightArm.setPosition(0.98);   // retracted
					upperLeftArm.setPosition(0.97);		// retracted
					upperRightArm.setPosition(0.0);   // retracted
					*/
					//wait(500);

					break;
				case BUTTON_Y_DOWN:
					upperLeftArm.setPosition(0.2);
/*
					lowerLeftArm.setPosition(.75);		//extended
					lowerRightArm.setPosition(0.3);  // extended
					upperLeftArm.setPosition(0.2);		//extended
					upperRightArm.setPosition(0.75);	// extended
					*/
					break;

				case BUTTON_A_DOWN:
					lowerRightArm.setPosition(0.3);  //Extended

					break;

				case BUTTON_B_DOWN:
					upperRightArm.setPosition(0.75);  //Extended

					break;
				case BUMPER_LEFT_DOWN:
					armPower += servoDelta;
					if(armPower>0.96)
					armPower = 0.96;
					//armPower = 0.95;  // retracted position
					peopleArm.setPosition(armPower);
					telemetry.addData("Text", "*** Robot Data***");
					telemetry.addData("arm", "arm: " + String.format("%.2f", armPower));
					break;
				case TRIGGER_LEFT_DOWN:
					armPower -= servoDelta;
					if(armPower<0.35)
						armPower = 0.35;
					//armPower = 0.35;		//launched position
					peopleArm.setPosition(armPower);
					telemetry.addData("Text", "*** Robot Data***");
					telemetry.addData("arm", "arm: " + String.format("%.2f", armPower));
					//peopleArm.setPosition(0);
					break;
				case BUMPER_RIGHT_DOWN:
					lowerLeftArm.setPosition(0.0);	// retracted
					lowerRightArm.setPosition(0.95);   // retracted
					upperLeftArm.setPosition(0.97);		// retracted
					upperRightArm.setPosition(0.0);   // retracted
					break;
				case TRIGGER_RIGHT_DOWN:
					boxServo.setPosition(0.0);
					break;
				case LEFT_STICK_Y:
					leftExtensionPower = gamepad2.left_stick_y;

					// clip the right/left values so that the values never exceed +/- 1
					leftExtensionPower = Range.clip(leftExtensionPower, -1, 1);

					// scale the joystick value to make it easier to control
					// the arm more precisely at slower speeds.
					leftExtensionPower = (float) scaleInput(leftExtensionPower);

					// write the values to the motors
					motorLeftExtension.setPower(leftExtensionPower);
					break;
				case RIGHT_STICK_Y:
					rightExtensionPower = gamepad2.right_stick_y;

					// clip the right/left values so that the values never exceed +/- 1
					rightExtensionPower = Range.clip(rightExtensionPower, -1, 1);

					// scale the joystick value to make it easier to control
					// the arm more precisely at slower speeds.
					rightExtensionPower = (float) scaleInput(rightExtensionPower);

					// write the values to the motors
					motorRightExtension.setPower(rightExtensionPower);

				break;

			}
		}
	}

	/*
         * This method will be called repeatedly in a loop
         *
         * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
         */
	@Override
	public void loop() {

		GamepadTask.GamepadEvent e;
		//RobotEvent e;
		//motorLeft.setPower(.1);
        /*
         * This is a straight FIFO queue.  Pull an event off the queue, process it,
         * move on to the next one.
         */
		e = events.poll();
		while (e != null) {
			handleEvent(e);
			e = events.poll();
		}

        /*
         * A list of tasks to give timeslices to.  A task remains in the list
         * until it tells the Robot that it is finished (true: I'm done, false: I have
         * more work to do), at which point it is stopped.
         */
		for (GamepadTask t : tasks) {
			//if(gamepad1!=null) motorLeft.setPower(.1);
			if (t.timeslice()) {
				t.stop();
			}
		}
		

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        //telemetry.addData("Text", "*** Robot Data***");
		//controller.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);
        //telemetry.addData("Motor_Right", "Power:  " + String.format("%.2f", motorRight.getPower()));
		//controller.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
        //telemetry.addData("claw", "claw:  " + String.format("%.2f", clawPosition));
        //telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
        //telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));

	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

	}
	
	/*
	 * This method scales the joystick input so for low joystick values, the 
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
	double scaleInput(double dVal)  {
		double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
				0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };
		
		// get the corresponding index for the scaleInput array.
		int index = (int) (dVal * 16.0);
		if (index < 0) {
			index = -index;
		} else if (index > 16) {
			index = 16;
		}
		
		double dScale;
		if (dVal < 0) {
			dScale = -scaleArray[index];
		} else {
			dScale = scaleArray[index];
		}
		
		return dScale;
	}

}
