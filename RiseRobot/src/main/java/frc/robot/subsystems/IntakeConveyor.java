package frc.robot.subsystems;

import static frc.robot.Constants.CANBusIDs.CONVEYOR_BACK_ID;
import static frc.robot.Constants.CANBusIDs.CONVEYOR_FRONT_ID;
import static frc.robot.Constants.CANBusIDs.INTAKE_ID;
import static frc.robot.Constants.DioIDs.BACK_CONVEYOR_SENSOR_ID;
import static frc.robot.Constants.DioIDs.FRONT_CONVEYOR_SENSOR_ID;
import static frc.robot.Constants.PneumaticIDs.INTAKE_TOGGLE_ID;
import static frc.robot.OI.*;
import static frc.robot.OI.intakeDownButton;
import static frc.robot.Robot.intakeConveyor;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.utils.EnhancedBoolean;
import frc.utils.IRSensor;

public class IntakeConveyor extends SubsystemBase {

  public static final double INTAKE_POWER = 0.8; // 0.75   // 1    // 0.875
  public static final double SPIN_BACK_POWER = -0.4;
//  public static final double CENTERING_POWER = 0.75;
  public static final double FRONT_CONVEYOR_POWER = 1;
  public static final double BACK_CONVEYOR_POWER = 1;
  public static final double PULSE_POWER = 0.60;
  public static final double PULSE_TIME = 0.29; // seconds  TODO adjust
  public static final double RAMP_TIME = 0.45;
  public static final double INTAKE_ACTUATION_TIME = 0.2;

  private final VictorSPX intakeMotor = new VictorSPX(INTAKE_ID);
//  private final VictorSPX centeringMotors = new VictorSPX(CENTERING_ID);  // May end up being PWM
  private final VictorSPX frontConveyorMotor = new VictorSPX(CONVEYOR_FRONT_ID);
  private final VictorSPX backConveyorMotor = new VictorSPX(CONVEYOR_BACK_ID);

  private final Solenoid intakeToggle = new Solenoid(INTAKE_TOGGLE_ID);

//  private final DigitalInput frontConveyorSensor = new DigitalInput(FRONT_CONVEYOR_SENSOR_ID);
//  private final DigitalInput backConveyorSensor = new DigitalInput(BACK_CONVEYOR_SENSOR_ID);
  private int ballCount;
  private final IRSensor frontConveyorSensor = new IRSensor(FRONT_CONVEYOR_SENSOR_ID);
  private final EnhancedBoolean frontConveyorSensorBool = new EnhancedBoolean();
  private final IRSensor backConveyorSensor = new IRSensor(BACK_CONVEYOR_SENSOR_ID);
  private final EnhancedBoolean backConveyorSensorBool = new EnhancedBoolean();

//  private final EnhancedBoolean toggleBool = new EnhancedBoolean();

  public boolean autoShouldIntake = false;
  public boolean spinBack = false;

  public IntakeConveyor() {
    intakeMotor.setInverted(false);
//    centeringMotors.setInverted(true);
    frontConveyorMotor.setInverted(true);
    ballCount = 0;

    overrideFrontConveyorButton.whenPressed(() -> setFrontConveyorMotorOutput(FRONT_CONVEYOR_POWER));
    overrideBackConveyorButton.whenPressed(() -> setBackConveyorMotorOutput(BACK_CONVEYOR_POWER));
    outtakeButton.whenPressed(() -> setIntakeMotorOutput(SPIN_BACK_POWER));
//    overrideIntakeButton.whenPressed(() -> setIntakeMotorOutput(INTAKE_POWER));
//    overrideCenteringButton.whenPressed(() -> setCenteringMotorsOutput(CENTERING_POWER));

    intakeUpButton.whenPressed(() -> intakeConveyor.setIntakeToggle(false));
    intakeDownButton.whenPressed(() -> intakeConveyor.setIntakeToggle(true));
    resetBallCountButton.whenPressed(this::resetBallCount);
//    intakeButton.whenPressed(() -> intakeConveyor.setIntakeToggle(true));
//    intakeButton.whenReleased(() -> intakeConveyor.setIntakeToggle(false));

    // TODO use openLoopRamping for conveyor
    frontConveyorMotor.configOpenloopRamp(RAMP_TIME);
    backConveyorMotor.configOpenloopRamp(RAMP_TIME);

  }

  @Override
  public void periodic() {

    frontConveyorSensor.update();
    backConveyorSensor.update();

    frontConveyorSensorBool.set(!frontConveyorSensor.get());
    backConveyorSensorBool.set(!backConveyorSensor.get());

//    toggleBool.set(intakeToggle.get());
//    if(toggleBool.isRisingEdge()) {
//      CommandScheduler.getInstance().schedule(
//          new SequentialCommandGroup(
//              new WaitCommand(INTAKE_ACTUATION_TIME),
//              new InstantCommand(() -> spinBack = true)));
//    }
//    if(toggleBool.isFallingEdge()) {
//      spinBack = false;
//    }

    // Update front sensor ball identification
    if(frontConveyorSensorBool.isRisingEdge()) {
      ballCount++;
    }
    if(backConveyorSensorBool.isFallingEdge()) {
      ballCount--;
    }

    ballCount = Math.max(0, ballCount);

//    setIntakeMotorOutput(INTAKE_POWER);
//    setBackConveyorMotorOutput(BACK_CONVEYOR_POWER);
//    setFrontConveyorMotorOutput(FRONT_CONVEYOR_POWER);

    SmartDashboard.putNumber("Ball Count", ballCount);
  }

  public void setIntakeToggle(boolean state) {
    intakeToggle.set(state);
  }

  public void setIntakeMotorOutput(double output) {
    intakeMotor.set(ControlMode.PercentOutput, output);
  }

//  public void setCenteringMotorsOutput(double output) {
//    centeringMotors.set(ControlMode.PercentOutput, output);
//  }

  public void setFrontConveyorMotorOutput(double output) {
    frontConveyorMotor.set(ControlMode.PercentOutput, output);
  }

  public void setBackConveyorMotorOutput(double output) {
    backConveyorMotor.set(ControlMode.PercentOutput, output);
  }

  public void setBallCount(int ballCount) {
    this.ballCount = ballCount;
  }

  public void resetBallCount() {
    setBallCount(0);
  }

  public int getBallCount() {
    return ballCount;
  }

  public boolean canPulse() {
    return ballCount < 4 &&
        frontConveyorSensorBool.get();
  }

}
