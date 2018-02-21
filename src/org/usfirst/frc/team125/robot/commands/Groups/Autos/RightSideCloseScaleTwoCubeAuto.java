package org.usfirst.frc.team125.robot.commands.Groups.Autos;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.usfirst.frc.team125.robot.commands.CubeLift.OpenGrabbersCmd;
import org.usfirst.frc.team125.robot.commands.CubeLift.RunToPositionMotionMagicCmd;
import org.usfirst.frc.team125.robot.commands.Drivetrain.DrivePathCmd;
import org.usfirst.frc.team125.robot.commands.Drivetrain.DrivePathReverseCmd;
import org.usfirst.frc.team125.robot.commands.Groups.AutoLiftCmdGrp;
import org.usfirst.frc.team125.robot.commands.Groups.ScoreCmdGrp;
import org.usfirst.frc.team125.robot.commands.Groups.SecureCubeCmdGrp;
import org.usfirst.frc.team125.robot.commands.Intake.AutonomousIntakeCmd;
import org.usfirst.frc.team125.robot.commands.Intake.IntakeDownCmd;
import org.usfirst.frc.team125.robot.subsystems.CubeLift;
import org.usfirst.frc.team125.robot.util.Paths.RightSideCloseScalePaths;
import org.usfirst.frc.team125.robot.util.Paths.RightSideCloseScaleTwoCubePaths;

public class RightSideCloseScaleTwoCubeAuto extends CommandGroup {
    Command intakeDown = new IntakeDownCmd();
    Command secureCube = new SecureCubeCmdGrp();
    Command liftElevatorA = new AutoLiftCmdGrp(0.5, CubeLift.Positions.ScoreScale);
    Command driveToScale = new DrivePathCmd(RightSideCloseScaleTwoCubePaths.toScale);
    Command scoreCube = new ScoreCmdGrp();
    Command bringElevatorToIntakePosition = new RunToPositionMotionMagicCmd(CubeLift.Positions.Intake);
    Command driveKTurnToSwitch1A = new DrivePathReverseCmd(RightSideCloseScaleTwoCubePaths.kTurnToSwitch1A);
    Command runIntakeToGrabSwitchCube = new AutonomousIntakeCmd();
    Command driveKTurnToSwitch1B = new DrivePathCmd(RightSideCloseScaleTwoCubePaths.kTurnToSwitch1B);
    Command secureSwitchCube = new SecureCubeCmdGrp();
    Command openGrabbers1 = new OpenGrabbersCmd();
    Command liftElevatorB = new RunToPositionMotionMagicCmd(CubeLift.Positions.ScoreScale);
    Command driveKTurnToScale1A = new DrivePathReverseCmd(RightSideCloseScaleTwoCubePaths.kTurnToScale1A);
    Command driveKTurnToScale1B = new DrivePathCmd(RightSideCloseScaleTwoCubePaths.kTurnToScale1B);
    Command scoreCube2 = new ScoreCmdGrp();


    public RightSideCloseScaleTwoCubeAuto() {
        addSequential(intakeDown);
        addSequential(new WaitCommand(0.25));
        addSequential(secureCube);
        addParallel(liftElevatorA);
        addSequential(driveToScale);
        addSequential(scoreCube);
        addSequential(new WaitCommand(0.15));
        addParallel(bringElevatorToIntakePosition);
        addSequential(driveKTurnToSwitch1A);
        addParallel(runIntakeToGrabSwitchCube, 3);
        addSequential(driveKTurnToSwitch1B);
        addSequential(secureSwitchCube);
        addSequential(openGrabbers1);
        addSequential(new WaitCommand(0.5));
        addParallel(liftElevatorB);
        addSequential(driveKTurnToScale1A);
        addSequential(driveKTurnToScale1B);
        addSequential(scoreCube2);
    }

}
