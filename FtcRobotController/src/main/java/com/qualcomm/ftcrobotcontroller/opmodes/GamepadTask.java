package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by hallmw on 9/23/15.
 */
import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadTask extends RobotTask {

    public enum EventKind {
        BUTTON_A_DOWN,
        BUTTON_A_UP,
        BUTTON_B_DOWN,
        BUTTON_B_UP,
        BUTTON_X_DOWN,
        BUTTON_X_UP,
        BUTTON_Y_DOWN,
        BUTTON_Y_UP,
        LEFT_STICK_Y,
        RIGHT_STICK_Y
    }

    public class GamepadEvent extends RobotEvent {

        EventKind kind;

        public GamepadEvent(RobotTask task, EventKind k)
        {
            super(task);
            kind = k;
        }

        @Override
        public String toString()
        {
            return (super.toString() + "Gamepad Event " + kind);
        }
    }

    protected class ButtonState {
        public boolean a_pressed;
        public boolean b_pressed;
        public boolean x_pressed;
        public boolean y_pressed;
    }

    protected class JoystickState{
        public boolean Left_Stick_Y;
        public boolean Right_Stick_Y;
    }

    protected ButtonState buttonState;
    protected JoystickState joystickState;

    public GamepadTask(Robot robot)//, Gamepad gamepad)
    {
        super(robot);

        this.buttonState = new ButtonState();
        this.buttonState.a_pressed = false;
        this.buttonState.b_pressed = false;
        this.buttonState.x_pressed = false;
        this.buttonState.y_pressed = false;

        this.joystickState = new JoystickState();
        this.joystickState.Left_Stick_Y = false;
        this.joystickState.Right_Stick_Y = false;
    }

    @Override
    public void start()
    {
        // TODO: ??
    }

    @Override
    public void stop()
    {
        // TODO: ??
    }

    /*
     * Process gamepad actions and send them to the robot as events.
     *
     * Note that these are not state changes, but is designed to send a
     * continual stream of events as long as the button is pressed (hmmm,
     * this may not be a good idea if software can't keep up).
     */
    @Override
    public boolean timeslice()
    {
        Gamepad gamepad;

        /*
         * I thought Java passed objects by reference, but oddly enough if you cache
         * the gamepad in the task's contstructor, it will never update.  Hence this.
         *
         * TODO: Choose the right gamepad (pass an enumerated value into the constructor).
         */
        gamepad = robot.gamepad1;

        if (gamepad.a) robot.queueEvent(new GamepadEvent(this, EventKind.BUTTON_A_DOWN));
 /*       if ((gamepad.a) && (buttonState.a_pressed == false)) {
            robot.queueEvent(new GamepadEvent(this, EventKind.BUTTON_A_DOWN));
            buttonState.a_pressed = true;
        } else if ((!gamepad.a) && (buttonState.a_pressed == true)) {
            robot.queueEvent(new GamepadEvent(this, EventKind.BUTTON_A_UP));
            buttonState.a_pressed = false;
        }
*/
        if(gamepad.b)
        {
            robot.queueEvent(new GamepadEvent(this, EventKind.BUTTON_B_DOWN));
        }
 /*       if ((gamepad.b) && (buttonState.b_pressed == false)) {
            robot.queueEvent(new GamepadEvent(this, EventKind.BUTTON_B_DOWN));
            buttonState.b_pressed = true;
        } else if ((!gamepad.b) && (buttonState.b_pressed == true)) {
            robot.queueEvent(new GamepadEvent(this, EventKind.BUTTON_B_UP));
            buttonState.b_pressed = false;
        }*/

        if ((gamepad.x) && (buttonState.x_pressed == false)) {
            robot.queueEvent(new GamepadEvent(this, EventKind.BUTTON_X_DOWN));
            buttonState.x_pressed = true;
        } else if ((!gamepad.x) && (buttonState.x_pressed == true)) {
            robot.queueEvent(new GamepadEvent(this, EventKind.BUTTON_X_UP));
            buttonState.x_pressed = false;
        }

        if ((gamepad.y) && (buttonState.y_pressed == false)) {
            robot.queueEvent(new GamepadEvent(this, EventKind.BUTTON_Y_DOWN));
            buttonState.y_pressed = true;
        } else if ((!gamepad.y) && (buttonState.y_pressed == true)) {
            robot.queueEvent(new GamepadEvent(this, EventKind.BUTTON_Y_UP));
            buttonState.y_pressed = false;
        }

        if(gamepad.left_stick_y > 0.0 || gamepad.left_stick_y < 0.0)
        {
            robot.queueEvent(new GamepadEvent(this,EventKind.LEFT_STICK_Y));
        }

        if(gamepad.right_stick_y > 0.0 || gamepad.right_stick_y < 0.0)
        {
            robot.queueEvent(new GamepadEvent(this,EventKind.RIGHT_STICK_Y));
        }

        /*
         * This task lives forever.
         */
        return false;
    }
}


