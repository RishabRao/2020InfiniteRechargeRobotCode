package frc.robot.commands.teleop;

import static com.ctre.phoenix.motorcontrol.TalonFXControlMode.PercentOutput;
import static frc.robot.OI.climberLockButton;
import static frc.robot.OI.climberToggleButton;
import static frc.robot.OI.gamepad;
import static frc.robot.Robot.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.utils.LEDController;

public class ClimbCommand extends CommandBase {

  private static final double HOLD_POWER = 0.075;
  private static final double HOLD_POWER_TWO = 0.12;
  private static final double DEADBAND = 0.125;
  //  private static final double DEADBAND_LOW = -0.125;
  private static final double MAX_EXTEND_POWER = 0.7;
  private boolean climbed;

  public ClimbCommand() {
    addRequirements(climber);
    climbed = false;
  }

  @Override
  public void execute() {
    double climbCommand = -gamepad.getLeftY();

      if (!climber.getClimberToggle() && Math.abs(climbCommand) > DEADBAND) {
        climber.setClimberMotorOutput(PercentOutput, Math.min(climbCommand, MAX_EXTEND_POWER));
        LEDController.setLEDClimbingMode();
        climbed = true;
      } else {
        climber.setClimberMotorOutput(PercentOutput, -HOLD_POWER);
        if (climbed) {
          LEDController.setLEDClimbingMode();
        } else {
          LEDController.setLEDPassiveMode();
        }
      }
  }

  @Override
  public void end(boolean interrupted) {
    climber.setClimberMotorOutput(PercentOutput, 0);
//    climber.setClimberLock(false);
  }

}
