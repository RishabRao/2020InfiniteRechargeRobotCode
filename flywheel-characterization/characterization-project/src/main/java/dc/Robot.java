/**
* This is a very simple robot program that can be used to send telemetry to
* the data_logger script to characterize your drivetrain. If you wish to use
* your actual robot code, you only need to implement the simple logic in the
* autonomousPeriodic function and change the NetworkTables update rate
*/

package dc;

import java.util.function.Supplier;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {

  Joystick stick;

  private final TalonFX flywheelMaster = new TalonFX(9);
  private final TalonFX flywheelSlave = new TalonFX(10);

  Supplier<Double> encoderPosition;
  Supplier<Double> encoderRate;

  NetworkTableEntry autoSpeedEntry = NetworkTableInstance.getDefault().getEntry("/robot/autospeed");
  NetworkTableEntry telemetryEntry = NetworkTableInstance.getDefault().getEntry("/robot/telemetry");

  double priorAutospeed = 0;
  Number[] numberArray = new Number[6];

  @Override
  public void robotInit() {
    if (!isReal()) SmartDashboard.putData(new SimEnabler());

    stick = new Joystick(0);

    flywheelMaster.selectProfileSlot(0, 0);

    flywheelMaster.setNeutralMode(NeutralMode.Coast);
    flywheelSlave.setNeutralMode(NeutralMode.Coast);

    flywheelMaster.setInverted(true);
    flywheelSlave.setInverted(false);
    flywheelSlave.follow(flywheelMaster);

    flywheelMaster.config_kF(0, 0.06); //0.0452
    flywheelMaster.config_kP(0, 0.0);
//    flywheelMaster.config_kI(0, 0.000);
//    flywheelMaster.config_IntegralZone(0, 150);
    flywheelMaster.config_kD(0, 0);

    flywheelMaster.config_kF(1, 0.06); // 0.04842603550
    flywheelMaster.config_kP(1, 0.01);
    flywheelMaster.config_kD(1, 0.000);

    // This is important
    flywheelMaster.configVoltageCompSaturation(10);
    flywheelMaster.enableVoltageCompensation(true);
    flywheelSlave.configVoltageCompSaturation(10);
    flywheelSlave.enableVoltageCompensation(true);

    //
    // Configure encoder related functions -- getDistance and getrate should return
    // units and units/s
    //

    encoderPosition = () -> (double)flywheelMaster.getSelectedSensorPosition();
    encoderRate = () -> (double)flywheelMaster.getSelectedSensorVelocity();

    // Set the update rate instead of using flush because of a ntcore bug
    // -> probably don't want to do this on a robot in competition
    NetworkTableInstance.getDefault().setUpdateRate(0.010);
  }

  @Override
  public void disabledInit() {
    System.out.println("Robot disabled");
    flywheelMaster.set(TalonFXControlMode.PercentOutput, 0);
  }

  @Override
    public void disabledPeriodic() {
  }

  @Override
  public void robotPeriodic() {
    // feedback for users, but not used by the control program
    SmartDashboard.putNumber("encoder_pos", encoderPosition.get());
    SmartDashboard.putNumber("encoder_rate", encoderRate.get());
  }

  @Override
  public void teleopInit() {
    System.out.println("Robot in operator control mode");
  }

  @Override
  public void teleopPeriodic() {
    flywheelMaster.set(TalonFXControlMode.PercentOutput, -stick.getY());
  }

  @Override
  public void autonomousInit() {
    System.out.println("Robot in autonomous mode");
  }

  /**
  * If you wish to just use your own robot program to use with the data logging
  * program, you only need to copy/paste the logic below into your code and
  * ensure it gets called periodically in autonomous mode
  * 
  * Additionally, you need to set NetworkTables update rate to 10ms using the
  * setUpdateRate call.
  */
  @Override
  public void autonomousPeriodic() {

    // Retrieve values to send back before telling the motors to do something
    double now = Timer.getFPGATimestamp();

    double position = encoderPosition.get();
    double rate = encoderRate.get();

    double battery = RobotController.getBatteryVoltage();
    double motorVolts = battery * Math.abs(priorAutospeed);

    // Retrieve the commanded speed from NetworkTables
    double autospeed = autoSpeedEntry.getDouble(0);
    priorAutospeed = autospeed;

    // command motors to do things
    flywheelMaster.set(TalonFXControlMode.PercentOutput, autospeed);

    // send telemetry data array back to NT
    numberArray[0] = now;
    numberArray[1] = battery;
    numberArray[2] = autospeed;
    numberArray[3] = motorVolts;
    numberArray[4] = position;
    numberArray[5] = rate;

    telemetryEntry.setNumberArray(numberArray);
  }
}