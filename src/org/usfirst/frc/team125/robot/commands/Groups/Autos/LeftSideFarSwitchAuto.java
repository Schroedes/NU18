package org.usfirst.frc.team125.robot.commands.Groups.Autos;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team125.robot.commands.CubeLift.RunToPositionMotionMagicCmd;
import org.usfirst.frc.team125.robot.commands.Drivetrain.DrivePathCmd;
import org.usfirst.frc.team125.robot.commands.Groups.SecureCubeCmdGrp;
import org.usfirst.frc.team125.robot.commands.Groups.ScoreCmdGrp;
import org.usfirst.frc.team125.robot.subsystems.CubeLift;
import org.usfirst.frc.team125.robot.util.Paths.LeftSideFarSwitchPaths;

public class LeftSideFarSwitchAuto extends CommandGroup {

    Command driveToFarSwitch = new DrivePathCmd(LeftSideFarSwitchPaths.toSwitch);
    Command secureCube = new SecureCubeCmdGrp();
    Command liftElevatorToSwitchPos = new RunToPositionMotionMagicCmd(CubeLift.Positions.ScoreSwitch);
    Command scoreCube = new ScoreCmdGrp();

    public LeftSideFarSwitchAuto(){
        addSequential(secureCube);
        addSequential(driveToFarSwitch);
        addSequential(liftElevatorToSwitchPos, 3);
        addSequential(scoreCube);
    }

}
