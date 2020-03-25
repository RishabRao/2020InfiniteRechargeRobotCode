package frc.robot.commands.teleop;

import static com.ctre.phoenix.motorcontrol.TalonFXControlMode.PercentOutput;
import static edu.wpi.first.wpilibj.Timer.getFPGATimestamp;
import static frc.robot.Robot.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class UnlockClimberCommand extends CommandBase {

  private static final double KICK_BACK_POWER = 0.5;
  private static final double KICK_BACK_TIME = 0.1;

  private double startTime;

  public UnlockClimberCommand() {
    addRequirements(climber);
  }

  @Override
  public void initialize() {
    climber.setClimberLock(true);
    startTime = getFPGATimestamp();
  }

  @Override
  public void execute() {
    climber.setClimberMotorOutput(PercentOutput, -KICK_BACK_POWER);
  }

  @Override
  public void end(boolean interrupted) {
    climber.setClimberMotorOutput(PercentOutput, 0);
  }

  @Override
  public boolean isFinished() {
    return getFPGATimestamp() - startTime > KICK_BACK_TIME;
  }
}
