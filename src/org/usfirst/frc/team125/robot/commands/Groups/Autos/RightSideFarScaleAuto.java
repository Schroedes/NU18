package org.usfirst.frc.team125.robot.commands.Groups.Autos;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team125.robot.commands.Drivetrain.DrivePathCmd;
import org.usfirst.frc.team125.robot.commands.Groups.AutoLiftCmdGrp;
import org.usfirst.frc.team125.robot.commands.Groups.ScoreCmdGrp;
import org.usfirst.frc.team125.robot.commands.Groups.SecureCubeCmdGrp;
import org.usfirst.frc.team125.robot.subsystems.CubeLift;
import org.usfirst.frc.team125.robot.util.Paths.RightSideFarScalePaths;

public class RightSideFarScaleAuto extends CommandGroup {

    Command driveToBeforeScaleTurn = new DrivePathCmd(RightSideFarScalePaths.toBeforeScaleTurn);
    Command driveToScale = new DrivePathCmd(RightSideFarScalePaths.turnToScale);
    Command secureCube = new SecureCubeCmdGrp();
    Command liftElevatorA = new AutoLiftCmdGrp(0.5, CubeLift.Positions.ScoreScale);
    Command scoreCube = new ScoreCmdGrp();


    public RightSideFarScaleAuto() {
        addSequential(secureCube);
        addParallel(liftElevatorA);
        addSequential(driveToBeforeScaleTurn);
        addSequential(driveToScale);
        addSequential(scoreCube);
    }

}
