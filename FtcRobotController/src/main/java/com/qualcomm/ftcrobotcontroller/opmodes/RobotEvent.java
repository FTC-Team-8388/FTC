package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by hallmw on 9/12/15.
 */
public class RobotEvent {

    /*
     * The task this event is associated with
     */
    protected RobotTask task;

    public RobotEvent(RobotTask task)
    {
        this.task = task;
    }

    public String toString()
    {
        return "RobotEvent: ";
    }
}
