package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by hallmw on 9/12/15.
 */
public abstract class RobotTask {

    public RobotTask(Robot robot)
    {
        this.robot = robot;
    }

    public abstract void start();
    public abstract void stop();

    public void handleEvent(RobotEvent e)
    {
        robot.handleEvent(e);
    }

    /*
     * Perform work for this task.
     *
     * The task should return false if there is more work to
     * do, true otherwise.
     */
    public abstract boolean timeslice();

    protected Robot robot;
}
