package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.IntakeConveyor;

import static frc.robot.Robot.intakeConveyor;
import static frc.robot.subsystems.IntakeConveyor.INTAKE_POWER;

/**
 * Does not end by default. Should be interrupted.
 */
public class EnableIntakeCommand extends CommandBase {
    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        intakeConveyor.autoShouldIntake = true;
    }

    @Override
    public void end(boolean interrupted) {
        intakeConveyor.autoShouldIntake = false;
    }
}
