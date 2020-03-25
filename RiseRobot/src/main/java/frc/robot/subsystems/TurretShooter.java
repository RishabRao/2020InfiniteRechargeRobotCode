package frc.robot.subsystems;

import static frc.robot.Constants.CANBusIDs.SHOOTER_FLYWHEEL_MASTER_ID;
import static frc.robot.Constants.CANBusIDs.SHOOTER_FLYWHEEL_SLAVE_ID;
import static frc.robot.Constants.CANBusIDs.SHOOTER_TURRET_ID;
import static frc.robot.Constants.Shooter.defaultShooterRPM;
import static frc.robot.Constants.Turret.TURRET_ENCODER_PORT_1;
import static frc.robot.Constants.Turret.TURRET_ENCODER_PORT_2;
import static frc.robot.Constants.Turret.TURRET_ROTATIONS_PER_TICK;
import static frc.robot.Robot.currentRobot;
import static frc.robot.Robot.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.utils.MovingAverage;
import frc.utils.interpolatingmap.*;


public class TurretShooter extends SubsystemBase {

  private final TalonFX flywheelMaster = new TalonFX(SHOOTER_FLYWHEEL_MASTER_ID);
  private final TalonFX flywheelSlave = new TalonFX(SHOOTER_FLYWHEEL_SLAVE_ID);
  private final TalonSRX turretMotor = new TalonSRX(SHOOTER_TURRET_ID);
  private final Encoder turretEncoder = new Encoder(TURRET_ENCODER_PORT_1, TURRET_ENCODER_PORT_2);

//  private int previousError = 0;
//  private int errorDelta = 0;

  private double minShootingDistance = 9;
  private double maxShootingDistance = 25;

  private double minDistanceRPM = 12800;
  private double maxDistanceRPM = 13000;

  public boolean isReadyToShoot = false;

//  private MovingAverage closedLoopErrorAverage;

  public boolean autoShouldShoot = false;

  public TurretShooter() {

    flywheelMaster.selectProfileSlot(0, 0);

    flywheelMaster.setNeutralMode(NeutralMode.Brake);
    flywheelSlave.setNeutralMode(NeutralMode.Brake);

    flywheelMaster.setInverted(true);
    flywheelSlave.setInverted(false);
//    flywheelSlave.follow(flywheelMaster);

    flywheelMaster.config_kF(0, 0.056); //0.0452
    flywheelMaster.config_kP(0, 0.013);
//    flywheelMaster.config_kI(0, 0.000);
//    flywheelMaster.config_IntegralZone(0, 150);
    flywheelMaster.config_kD(0, 0);

    flywheelMaster.config_kF(1, 0.056); // 0.04842603550
    flywheelMaster.config_kP(1, 0.01);
    flywheelMaster.config_kD(1, 0.000);

    // This is important
    flywheelMaster.configVoltageCompSaturation(10);
    flywheelMaster.enableVoltageCompensation(true);
    flywheelSlave.configVoltageCompSaturation(10);
    flywheelSlave.enableVoltageCompensation(false);

    SmartDashboard.putNumber("Flywheel Speed", getFlywheelSpeed());

//    SmartDashboard.putNumber("Flywheel I", 0.0001);
//    SmartDashboard.putNumber("Flywheel Izone", 150);
//    closedLoopErrorAverage = new MovingAverage(3, 0);

  }

  @Override
  public void periodic() {
//    setFlywheelOutput(TalonFXControlMode.Velocity, SmartDashboard.getNumber("Flywheel Speed", getFlywheelSpeed()));
//    setFlywheelOutput(TalonFXControlMode.Velocity, 16500);
//    setFlywheelOutput(TalonFXControlMode.PercentOutput, 0.8);
//    flywheelMaster.config_kP(0, SmartDashboard.getNumber("Flywheel P", 0));
//    flywheelMaster.config_kD(0, SmartDashboard.getNumber("Flywheel D", 0));
//    flywheelMaster.config_IntegralZone(0, (int)SmartDashboard.getNumber("Flywheel Izone", 150));
    SmartDashboard.putNumber("Flywheel Speed", getFlywheelSpeed());
    SmartDashboard.putNumber("Closed Loop error", getClosedLoopFlywheelError());
//    SmartDashboard.putNumber("PID Slot")
//    flywheelMaster.set(ControlMode.PercentOutput, 0.8);
//    SmartDashboard.putNumber("Error Delta", errorDelta);
//    errorDelta = getClosedLoopFlywheelError() - previousError;
//    closedLoopErrorAverage.update(getClosedLoopFlywheelError());
//    previousError = getClosedLoopFlywheelError();
  }

  public void setFlywheelOutput(TalonFXControlMode controlMode, double output) {
    flywheelMaster.set(controlMode, output);
    flywheelSlave.set(TalonFXControlMode.Follower, SHOOTER_FLYWHEEL_MASTER_ID);
//    flywheelSlave.set(controlMode, output);
  }

  public double getFlywheelSpeed() {
    return -flywheelMaster.getSensorCollection().getIntegratedSensorVelocity();
  }

//  public double getClosedLoopErrorAverage() {
//    return closedLoopErrorAverage.getDoubleOutput();
//  }

  public int getClosedLoopFlywheelError() {
    return flywheelMaster.getClosedLoopError();
  }

//  public int getClosedLoopErrorDelta() {
//    return errorDelta;
//  }

  public void setTurretAngle(Rotation2d angle, boolean fieldOriented) {
    if (fieldOriented) {
      turretMotor.set(ControlMode.Position,
              angle.getRadians() - drivetrain.getHeading().getRadians() / (2 * Math.PI
                      * TURRET_ROTATIONS_PER_TICK));
    } else {
      turretMotor.set(ControlMode.Position,
              angle.getRadians() / (2 * Math.PI * TURRET_ROTATIONS_PER_TICK));
    }
  }

  public Rotation2d getTurretAngle() {
    return Rotation2d.fromDegrees(turretEncoder.get() / (2 * Math.PI * TURRET_ROTATIONS_PER_TICK));
  }

  public void setOpenLoopTurretOutput(double outputValue) {
    turretMotor.set(ControlMode.PercentOutput, outputValue);
  }

  public void switchProfileSlot(int slot) {
    flywheelMaster.selectProfileSlot(slot, 0);
  }

  /**
   * Returns interpolated target Speed in RPMS using interpolating tree map
   * @param distance the distance to target in meters
   * @return RPMs to set the flywheel
   */
  public double estimateTargetSpeed(double distance) {
    InterpolatingDouble result = currentRobot.getShooterCalibrationMap().getInterpolated(new InterpolatingDouble(distance));
//    System.out.println(currentRobot.getShooterCalibrationMap().toString());
    if(distance < minShootingDistance) {
      return minDistanceRPM;
    }

    if(distance > maxShootingDistance) {
      return maxDistanceRPM;
    }

    if (result != null) {
      System.out.println(result.value);
      return result.value;
    } else {
      return defaultShooterRPM;
    }
  }

  public InterpolatingTreeMap getKnownDataMap() {
    return currentRobot.getShooterCalibrationMap();
  }
}
