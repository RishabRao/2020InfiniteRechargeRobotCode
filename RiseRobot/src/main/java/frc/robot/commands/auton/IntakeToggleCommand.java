package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import static frc.robot.Robot.intakeConveyor;

public class IntakeToggleCommand extends SequentialCommandGroup {

    public IntakeToggleCommand(boolean deployed, double wait) {
        addCommands(new InstantCommand(() -> intakeConveyor.setIntakeToggle(deployed)));
        addCommands(new WaitCommand(wait));
    }

}
