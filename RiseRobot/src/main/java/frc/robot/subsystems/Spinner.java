package frc.robot.subsystems;

import static frc.robot.Constants.CANBusIDs.SPINNER_ID;
import static frc.robot.Constants.PneumaticIDs.SPINNER_TOGGLE_ID;
import static frc.robot.OI.actuateSpinnerToggleButton;
import static frc.robot.OI.spinControlPanelButton;
import static frc.robot.OI.spinToColorButton;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.teleop.SpinnerStage2Command;
import frc.robot.commands.teleop.SpinnerStage3Command;
import frc.utils.ColorSensorHelper;

public class Spinner extends SubsystemBase {

  private final VictorSPX spinnerMotor = new VictorSPX(SPINNER_ID);
  private final Solenoid spinnerToggle = new Solenoid(SPINNER_TOGGLE_ID);

  public boolean hasSpunStage2 = false;
  public boolean hasSpunStage3 = false;

  public Spinner() {
    actuateSpinnerToggleButton.whenPressed(() -> spinnerToggle.set(true));
    actuateSpinnerToggleButton.whenReleased(() -> spinnerToggle.set(false));

    spinControlPanelButton.whenPressed(() ->
        CommandScheduler.getInstance().schedule(new SpinnerStage2Command()));
    spinToColorButton.whenPressed(() ->
        CommandScheduler.getInstance().schedule(new SpinnerStage3Command()));

  }

  @Override
  public void periodic() {

  }

  public void set(ControlMode controlMode, double speed) {
    spinnerMotor.set(controlMode, speed);
  }
}
