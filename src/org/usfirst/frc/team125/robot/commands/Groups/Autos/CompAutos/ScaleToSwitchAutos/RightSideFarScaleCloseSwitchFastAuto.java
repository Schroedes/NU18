package org.usfirst.frc.team125.robot.commands.Groups.Autos.CompAutos.ScaleToSwitchAutos;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.usfirst.frc.team125.robot.commands.CubeLift.RunToPositionMotionMagicCmd;
import org.usfirst.frc.team125.robot.commands.Drivetrain.DrivePathCmd;
import org.usfirst.frc.team125.robot.commands.Drivetrain.DrivePathReverseCmd;
import org.usfirst.frc.team125.robot.commands.Groups.AutoLiftCmdGrp;
import org.usfirst.frc.team125.robot.commands.Groups.IntakeCmdGrp;
import org.usfirst.frc.team125.robot.commands.Groups.ScoreCmdGrp;
import org.usfirst.frc.team125.robot.commands.Groups.SecureCubeCmdGrp;
import org.usfirst.frc.team125.robot.commands.Intake.IntakeDownCmd;
import org.usfirst.frc.team125.robot.subsystems.CubeLift;
import org.usfirst.frc.team125.robot.util.Paths.CompPaths.ScaleToSwitchPaths.RightSideFarScaleCloseSwitchPaths;

public class RightSideFarScaleCloseSwitchFastAuto extends CommandGroup {
    Command intakeDown = new IntakeDownCmd();
    Command secureCube = new SecureCubeCmdGrp();
    Command liftElevatorToScale = new AutoLiftCmdGrp(0.5, CubeLift.Positions.ScoreScale);
    Command driveToScale = new DrivePathCmd(RightSideFarScaleCloseSwitchPaths.toScale, false);
    Command scoreCube = new ScoreCmdGrp();
    Command bringElevatorToIntake = new RunToPositionMotionMagicCmd(CubeLift.Positions.Intake);
    Command driveToSwitchA = new DrivePathReverseCmd(RightSideFarScaleCloseSwitchPaths.reverse_kTurnToSwitch1A, false);
    Command intakeCube = new IntakeCmdGrp();
    Command driveToSwitchB = new DrivePathCmd(RightSideFarScaleCloseSwitchPaths.kTurnToSwitch1B, false);
    Command secureCubeAgain = new SecureCubeCmdGrp();
    Command liftElevatorToSwitch = new RunToPositionMotionMagicCmd(CubeLift.Positions.ScoreSwitch);
    Command scoreCubeAgain = new ScoreCmdGrp();

    public RightSideFarScaleCloseSwitchFastAuto() {
        /*
        addSequential(intakeDown);
        addSequential(new WaitCommand(0.25));
        addSequential(secureCube);
        */
        addParallel(liftElevatorToScale);
        addSequential(driveToScale);
        addSequential(scoreCube);
        addSequential(new WaitCommand(0.4));
        addParallel(bringElevatorToIntake);
        addSequential(driveToSwitchA);
        addSequential(new WaitCommand(1));
        addParallel(intakeCube, 5);
        addSequential(driveToSwitchB);
        addSequential(secureCubeAgain);
        addSequential(liftElevatorToSwitch, 3);
        addSequential(scoreCubeAgain);
    }

}