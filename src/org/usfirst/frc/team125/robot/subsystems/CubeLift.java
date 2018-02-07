package org.usfirst.frc.team125.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team125.robot.RobotMap;
import org.usfirst.frc.team125.robot.commands.CubeLift.ElevatorDriveCmd;
import org.usfirst.frc.team125.robot.util.DebouncedBoolean;

/**
 * DoubleLift's DoubleLift for DoubleLifting
 */
public class CubeLift extends Subsystem {

    public static enum LiftState {
        GoingUp,
        GoingDown,
        Stationary,
        HallEffect,
        ToppedOut,
    }

    public LiftState getState() {
        return state;
    }

    private void setState(LiftState newState) {
        this.state = newState;
    }

    private volatile LiftState state = LiftState.Stationary;

    private volatile TalonSRX rightElevatorLeader = new TalonSRX(RobotMap.RIGHT_ELEVATOR_MAIN);
    private VictorSPX rightElevatorSlave = new VictorSPX(RobotMap.RIGHT_ELEVATOR_SLAVE);
    private VictorSPX leftElevatorSlaveA = new VictorSPX(RobotMap.LEFT_ELEVATOR_SLAVE_A);
    private VictorSPX leftElevatorSlaveB = new VictorSPX(RobotMap.LEFT_ELEVATOR_SLAVE_B);

    private Solenoid grabbers = new Solenoid(RobotMap.GRABBERS);
    private Solenoid releasePin = new Solenoid(RobotMap.RELEASE_PIN);

    private double kP = 0.3; // .3
    private double kI = 0.0;
    private double kD = 4.0; // 4.0
    private double kF = 0.1165 * 2; // 0.1165 * 2

    //81.2 inches per/s
    //gear is 42:26 geared up
    //4096 ticks per rev
    //233 ticks per inch
    private static final int CRUISE_VELOCITY = 17600; // 1024
    private static final int CRUISE_ACCELERATION = 40000; // 1024
    private static final int CRUISE_VELOCITY_DOWN = (int)(17600 * 0.25); // 1024
    private static final int CRUISE_ACCELERATION_DOWN = (int)(40000 * 0.25); // 1024

    private DigitalInput hallEffectSensor = new DigitalInput(RobotMap.CUBELIFT_HALL_EFFECT_SENSOR);
    private static final double CALIBRATION_MIN_TIME = 0.;
    private DebouncedBoolean hallEffectDebouncer = new DebouncedBoolean(CALIBRATION_MIN_TIME);

    public static enum Positions {
        Intake(0),
        ScoreSwitch(20000),
        ScoreScale(50000),
        Climbing(70000),
        Top(75001);
        private int position;
        Positions(int encPos) {
            this.position = encPos;
        }
        public int getPosition() {
            return this.position;
        }
    }

    private volatile Positions position = Positions.Intake;

    public Positions getPosition() {
        return position;
    }

    private void setPosition(Positions newPos) {
        this.position = newPos;
    }

    private class ElevatorSafety implements Runnable {
        int i = 0;
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(20L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                checkIfBottomedOut();
                checkIfToppedOut();
                SmartDashboard.putBoolean("Is Safety alive", true);
                SmartDashboard.putNumber("i counter", i);
                i++;
            }
        }
    }

    private static final boolean GRAB_SET = true;
    private static final boolean UNGRAB_SET = false;
    private boolean grabberToggle = false;
    private static final boolean UNPIN_SET = true;
    private static final boolean PIN_SET = false;
    private boolean pinToggle = false;

    private final int TOLERANCE = 150;
    private static final double ELEVATOR_HI_POW = 1.0;
    private static final double ELEVATOR_LOW_POW = -ELEVATOR_HI_POW;

    public CubeLift() {
        Thread thread = new Thread(new ElevatorSafety());
        thread.start();
        this.rightElevatorSlave.follow(rightElevatorLeader);
        this.leftElevatorSlaveA.follow(rightElevatorLeader);
        this.leftElevatorSlaveB.follow(rightElevatorLeader);
        this.rightElevatorLeader.configPeakOutputForward(ELEVATOR_HI_POW, 0);
        this.rightElevatorLeader.configPeakOutputReverse(ELEVATOR_LOW_POW, 0);
        this.rightElevatorLeader.configNominalOutputForward(0.0, 0);
        this.rightElevatorLeader.configNominalOutputReverse(0.0, 0);
        this.rightElevatorSlave.configPeakOutputForward(ELEVATOR_HI_POW, 0);
        this.rightElevatorSlave.configPeakOutputReverse(ELEVATOR_LOW_POW, 0);
        this.rightElevatorSlave.configNominalOutputForward(0.0, 0);
        this.rightElevatorSlave.configNominalOutputReverse(0.0, 0);
        this.leftElevatorSlaveA.configPeakOutputForward(ELEVATOR_HI_POW, 0);
        this.leftElevatorSlaveA.configPeakOutputReverse(ELEVATOR_LOW_POW, 0);
        this.leftElevatorSlaveA.configNominalOutputForward(0.0, 0);
        this.leftElevatorSlaveA.configNominalOutputReverse(0.0, 0);
        this.leftElevatorSlaveB.configPeakOutputForward(ELEVATOR_HI_POW, 0);
        this.leftElevatorSlaveB.configPeakOutputReverse(ELEVATOR_LOW_POW, 0);
        this.leftElevatorSlaveB.configNominalOutputForward(0.0, 0);
        this.leftElevatorSlaveB.configNominalOutputReverse(0.0, 0);

        //Encoder
        this.rightElevatorLeader.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);

        this.rightElevatorLeader.setSensorPhase(true);

        //Neutral mode
        this.rightElevatorLeader.setNeutralMode(NeutralMode.Brake);
        this.rightElevatorSlave.setNeutralMode(NeutralMode.Brake);
        this.leftElevatorSlaveA.setNeutralMode(NeutralMode.Brake);
        this.leftElevatorSlaveB.setNeutralMode(NeutralMode.Brake);

        this.leftElevatorSlaveA.setInverted(true);
        this.leftElevatorSlaveB.setInverted(true);
        this.rightElevatorLeader.setInverted(false);
        this.rightElevatorSlave.setInverted(false);

        //resetEncoders();
        configPIDF(kP, kI, kD, kF); // TODO: Tune lol
        configMotionMagic(CRUISE_VELOCITY, CRUISE_ACCELERATION); // TODO: Also tune lol

        //Hard coded to false to reduce chance of breaking...
        grabbers.set(false);
        releasePin.set(false);
    }

    public void resetEncoders() {
        this.rightElevatorLeader.setSelectedSensorPosition(0, 0, 0);
    }

    public int getEncPos() {
        return rightElevatorLeader.getSelectedSensorPosition(0);
    }

    public void startMotionMagic(Positions pos) {
        if(getEncPos() > pos.getPosition()) {
            setState(LiftState.GoingDown);
            configMotionMagic(CRUISE_VELOCITY_DOWN, CRUISE_ACCELERATION_DOWN);
        } else if(getEncPos() < pos.getPosition()) {
            setState(LiftState.GoingUp);
            configMotionMagic(CRUISE_VELOCITY, CRUISE_ACCELERATION);
        }

        rightElevatorLeader.set(ControlMode.MotionMagic, pos.getPosition());
    }

    public void checkMotionMagicTermination(Positions pos) {
        if(pos == Positions.Intake) {
            if(getEncPos() <= TOLERANCE * 2) {
                state = LiftState.Stationary;
                stopElevator();
                position = pos;
            }
        } else if(Math.abs(pos.getPosition() - getEncPos()) <= TOLERANCE) {
            state = LiftState.Stationary;
            stopElevator();
            position = pos;
        }
        SmartDashboard.putString("Desired Pos Enum", pos.toString());
        SmartDashboard.putNumber("Desired Pos Num", pos.getPosition());
        SmartDashboard.putNumber("closed loop err", Math.abs(pos.getPosition() - getEncPos()));
    }

    public void openGrabbers() {
        grabbers.set(UNGRAB_SET);
    }

    public void closeGrabbers() {
        grabbers.set(GRAB_SET);
    }

    public void toggleGrabbers() {
        grabberToggle = !grabberToggle;
        grabbers.set(grabberToggle);
    }

    public void unpin() {
        releasePin.set(UNPIN_SET);
    }

    public void pin(){
        releasePin.set(PIN_SET);
    }

    public void togglePin() {
        pinToggle = !pinToggle;
        releasePin.set(pinToggle);
    }

    public void stopElevator() {
        rightElevatorLeader.set(ControlMode.PercentOutput, 0.0);
    }

    public void directElevate(double pow) {
        if(getState() == LiftState.HallEffect && pow < 0.0){
            return;
        }
        if(getState() == LiftState.ToppedOut && pow > 0.0) {
            return;
        }
        if (pow > 0.0) {
            setState(LiftState.GoingUp);
        }
        if (pow < 0.0) {
            setState(LiftState.GoingDown);
        }
        if (pow == 0.0) {
            setState(LiftState.Stationary);
        }
        rightElevatorLeader.set(ControlMode.PercentOutput, pow);
    }

    private void checkIfBottomedOut() {
        hallEffectDebouncer.update(!hallEffectSensor.get());
        if (hallEffectDebouncer.get() && getState() != LiftState.GoingUp) {
            setState(LiftState.HallEffect);
            setPosition(Positions.Intake);
            resetEncoders();
            stopElevator();
        }
    }

    private void checkIfToppedOut() {
        if (getEncPos() >= Positions.Top.getPosition()) {
            setState(LiftState.ToppedOut);
            setPosition(Positions.Top);
            stopElevator();
        }
    }

    public boolean getHallEffectDebouncer(){
        return this.hallEffectDebouncer.get();
    }

    public boolean getRawHallEffectSensor() {
        return hallEffectSensor.get();
    }

    public void configPIDF(double kP, double kI, double kD, double kF) {
        rightElevatorLeader.config_kP(0, kP, 0);
        rightElevatorLeader.config_kI(0, kI, 0);
        rightElevatorLeader.config_kD(0, kD, 0);
        rightElevatorLeader.config_kF(0, kF, 0);
    }

    /**
     * Set parameters for motion magic control
     *
     * @param cruiseVelocity cruise velocity in sensorUnits per 100ms
     * @param acceleration   cruise acceleration in sensorUnits per 100ms
     */
    public void configMotionMagic(int cruiseVelocity, int acceleration) {
        rightElevatorLeader.configMotionCruiseVelocity(cruiseVelocity, 0);
        rightElevatorLeader.configMotionAcceleration(acceleration, 0);
    }

    public void updatePIDFOnDashboard() {
        SmartDashboard.putNumber("kP", kP);
        SmartDashboard.putNumber("kI", kI);
        SmartDashboard.putNumber("kD", kD);
        SmartDashboard.putNumber("kF", kF);
    }

    public void updatePIDFFromDashboard() {
        kP = SmartDashboard.getNumber("kP", kP);
        kI = SmartDashboard.getNumber("kI", kI);
        kD = SmartDashboard.getNumber("kD", kD);
        kF = SmartDashboard.getNumber("kF", kF);
        configPIDF(kP, kI, kD, kF);
    }

    public void initDefaultCommand() {
        setDefaultCommand(new ElevatorDriveCmd());
    }
}

