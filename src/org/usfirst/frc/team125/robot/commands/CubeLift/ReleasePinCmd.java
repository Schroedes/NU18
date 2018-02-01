package org.usfirst.frc.team125.robot.commands.CubeLift;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team125.robot.Robot;

public class ReleasePinCmd extends Command {

    public ReleasePinCmd() {
        requires(Robot.cubeLift);
    }

    protected void initialize() {

    }

    protected void execute() {
        Robot.cubeLift.releasePin();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    protected void end() {

    }

    protected void interrupted() {

    }

}