package org.usfirst.frc.team125.robot.commands.Groups.Autos.PalmettoAutos.CenterAutos;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.usfirst.frc.team125.robot.commands.Drivetrain.DrivePathCmd;
import org.usfirst.frc.team125.robot.commands.Groups.AutoLiftCmdGrp;
import org.usfirst.frc.team125.robot.commands.Groups.ScoreCmdGrp;
import org.usfirst.frc.team125.robot.commands.Groups.SecureCubeCmdGrp;
import org.usfirst.frc.team125.robot.commands.Intake.IntakeDownCmd;
import org.usfirst.frc.team125.robot.subsystems.CubeLift;
import org.usfirst.frc.team125.robot.util.Paths.PalmettoPaths.CenterPaths.CenterLeftPath;

public class CenterLeftAuto extends CommandGroup {
    Command intakeDown = new IntakeDownCmd();
    Command secureCube = new SecureCubeCmdGrp();
    Command liftElevator = new AutoLiftCmdGrp(0.1, CubeLift.Positions.ScoreSwitch);
    Command driveToScale = new DrivePathCmd(CenterLeftPath.toSwitch);
    Command scoreCube = new ScoreCmdGrp();

    public CenterLeftAuto() {
        addSequential(intakeDown);
        addSequential(new WaitCommand(0.25));
        addSequential(secureCube);
        addParallel(liftElevator);
        addSequential(driveToScale);
        addSequential(scoreCube);
    }

}