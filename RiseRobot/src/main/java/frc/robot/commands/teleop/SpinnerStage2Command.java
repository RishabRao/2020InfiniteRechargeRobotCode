package frc.robot.commands.teleop;

import static frc.robot.OI.spinControlPanelButton;
import static frc.robot.Robot.spinner;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.utils.ColorSensorHelper;
import frc.utils.ColorSensorHelper.ControlPanelColor;
import frc.utils.EnhancedBoolean;

public class SpinnerStage2Command extends CommandBase {

  private final static double SPIN_POWER = 0.75;

  private ControlPanelColor startColor, currentColor;
  private int halfRotations;
  private EnhancedBoolean isSameColor;

  public SpinnerStage2Command() {
    isSameColor = new EnhancedBoolean(true);
  }

  @Override
  public void initialize() {
    currentColor = startColor = ColorSensorHelper.getColorMatch();
    halfRotations = 0;
    isSameColor.set(true);
  }

  @Override
  public void execute() {
    currentColor = ColorSensorHelper.getColorMatch();
    isSameColor.set(currentColor == startColor);
    if(isSameColor.isRisingEdge()) {
      halfRotations++;
    }
    spinner.set(ControlMode.PercentOutput, SPIN_POWER);
  }

  @Override
  public void end(boolean interrupted) {
    spinner.set(ControlMode.PercentOutput, 0);
  }

  @Override
  public boolean isFinished() {
    return !spinControlPanelButton.get() || halfRotations == 6;
  }
}
