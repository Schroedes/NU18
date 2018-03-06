package org.usfirst.frc.team125.robot.commands.Groups.Autos.PalmettoAutos.TwoScaleAutos;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.usfirst.frc.team125.robot.commands.CubeLift.RunToPositionMotionMagicCmd;
import org.usfirst.frc.team125.robot.commands.Drivetrain.DrivePathCmd;
import org.usfirst.frc.team125.robot.commands.Drivetrain.DrivePathReverseCmd;
import org.usfirst.frc.team125.robot.commands.Groups.AutoLiftCmdGrp;
import org.usfirst.frc.team125.robot.commands.Groups.ScoreCmdGrp;
import org.usfirst.frc.team125.robot.commands.Groups.SecureCubeCmdGrp;
import org.usfirst.frc.team125.robot.commands.Intake.AutonomousIntakeCmd;
import org.usfirst.frc.team125.robot.commands.Intake.IntakeDownCmd;
import org.usfirst.frc.team125.robot.subsystems.CubeLift;
import org.usfirst.frc.team125.robot.util.Paths.PalmettoPaths.ScaleToSwitchPaths.LeftSideCloseScaleCloseSwitchPaths;
import org.usfirst.frc.team125.robot.util.Paths.PalmettoPaths.TwoScale.LeftSideCloseTwoScalePaths;

public class LeftSideCloseTwoScaleAuto extends CommandGroup {
    Command intakeDown = new IntakeDownCmd();
    Command secureCube = new SecureCubeCmdGrp();
    Command liftElevatorToScale = new AutoLiftCmdGrp(0.5, CubeLift.Positions.ScoreScale);
    Command driveToScale = new DrivePathCmd(LeftSideCloseTwoScalePaths.toScale);
    Command scoreCube = new ScoreCmdGrp();
    Command bringElevatorToIntake = new RunToPositionMotionMagicCmd(CubeLift.Positions.Intake);
    Command driveToSwitchA = new DrivePathReverseCmd(LeftSideCloseTwoScalePaths.reverse_kTurnToSwitch1A);
    Command intakeCube = new AutonomousIntakeCmd();
    Command driveToSwitchB = new DrivePathCmd(LeftSideCloseTwoScalePaths.kTurnToSwitch1B);
    Command secureCubeAgain = new SecureCubeCmdGrp();
    Command liftElevatorToScaleAgain = new RunToPositionMotionMagicCmd(CubeLift.Positions.ScoreScale);
    Command driveToScaleA = new DrivePathReverseCmd(LeftSideCloseTwoScalePaths.reverse_kTurnToScaleA);
    Command driveToScaleB = new DrivePathCmd(LeftSideCloseTwoScalePaths.kTurnToScaleB);
    Command scoreCubeAgain = new ScoreCmdGrp();

    public LeftSideCloseTwoScaleAuto() {
        /*addSequential(intakeDown);
        addSequential(new WaitCommand(0.25));
        addSequential(secureCube);
        */
        addParallel(liftElevatorToScale);
        addSequential(driveToScale);
        addSequential(scoreCube);
        addSequential(new WaitCommand(0.4));
        addParallel(bringElevatorToIntake);
        addSequential(driveToSwitchA);
        addParallel(intakeCube, 3);
        addSequential(driveToSwitchB);
        addSequential(secureCubeAgain);
        addParallel(liftElevatorToScaleAgain, 3);
        addSequential(driveToScaleA);
        addSequential(driveToScaleB);
        addSequential(scoreCubeAgain);
    }

}
