package frc.robot.commands.teleop;

import static edu.wpi.first.wpilibj.Timer.getFPGATimestamp;
import static frc.robot.OI.*;
import static frc.robot.Robot.intakeConveyor;
import static frc.robot.Robot.turretShooter;
import static frc.robot.subsystems.IntakeConveyor.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class IntakeConveyorCommand extends CommandBase {

  private static double pulseStart;
  private State currentState;

  public IntakeConveyorCommand() {
    addRequirements(intakeConveyor);
    currentState = State.OFF;
    pulseStart = getFPGATimestamp();
  }

  @Override
  public void execute() {
    currentState = currentState.execute();
    SmartDashboard.putString("Intake Conveyor State", currentState.name());
    SmartDashboard.putBoolean("Should Pulse", shouldPulse());
  }

  private static boolean shouldPulse() {
    // getFPGATimestamp() - pulseStart will always be almost 0, unless we are in a pulsing state
    return (getFPGATimestamp() - pulseStart < PULSE_TIME && getFPGATimestamp() - pulseStart > 0.02) ||
        intakeConveyor.canPulse();
  }

  private enum State {
    OFF {
      @Override
      public State execute() {
        // Timer stops resetting only when it enters a pulsing state
        pulseStart = getFPGATimestamp();

        if(!outtakeButton.get()) {
          intakeConveyor.setIntakeMotorOutput(0);
        }
//        intakeConveyor.setCenteringMotorsOutput(0);
        if(!overrideFrontConveyorButton.get()) {
          intakeConveyor.setFrontConveyorMotorOutput(0);
        }
        if(!overrideBackConveyorButton.get()) {
          intakeConveyor.setBackConveyorMotorOutput(0);
        }

        return determineState();
      }
    }, INTAKING {
      @Override
      public State execute() {
        pulseStart = getFPGATimestamp();

        intakeConveyor.setIntakeMotorOutput(INTAKE_POWER);
//        intakeConveyor.setCenteringMotorsOutput(CENTERING_POWER);
        if(intakeConveyor.getBallCount() < 3) {
          intakeConveyor.setFrontConveyorMotorOutput(FRONT_CONVEYOR_POWER);
        } else {
          intakeConveyor.setFrontConveyorMotorOutput(0);
        }
        if(!overrideBackConveyorButton.get()) {
          intakeConveyor.setBackConveyorMotorOutput(0);
        }

        return determineState();
      }
    }, OUTTAKING {
      @Override
      public State execute() {
        pulseStart = getFPGATimestamp();

        intakeConveyor.setIntakeMotorOutput(0);
        intakeConveyor.setFrontConveyorMotorOutput(FRONT_CONVEYOR_POWER);
        intakeConveyor.setBackConveyorMotorOutput(BACK_CONVEYOR_POWER);

        return determineState();
      }
    }, IN_AND_OUT {
      @Override
      public State execute() {
        pulseStart = getFPGATimestamp();
        intakeConveyor.setIntakeMotorOutput(INTAKE_POWER);
//        intakeConveyor.setCenteringMotorsOutput(CENTERING_POWER);
        intakeConveyor.setFrontConveyorMotorOutput(FRONT_CONVEYOR_POWER);
        intakeConveyor.setBackConveyorMotorOutput(BACK_CONVEYOR_POWER);

        return determineState();
      }
    }, PULSING {
      @Override
      public State execute() {

        intakeConveyor.setIntakeMotorOutput(0);
        intakeConveyor.setFrontConveyorMotorOutput(0);

        intakeConveyor.setBackConveyorMotorOutput(PULSE_POWER);

        return determineState();
      }
    }, IN_AND_PULSING {
      @Override
      public State execute() {
        intakeConveyor.setIntakeMotorOutput(INTAKE_POWER);
//        intakeConveyor.setCenteringMotorsOutput(CENTERING_POWER);
        if(intakeConveyor.getBallCount() < 4) {
          intakeConveyor.setFrontConveyorMotorOutput(FRONT_CONVEYOR_POWER);
        } else {
          intakeConveyor.setFrontConveyorMotorOutput(0);
        }

        intakeConveyor.setBackConveyorMotorOutput(PULSE_POWER);

        return determineState();
      }
    };

    protected State determineState() {
      if(intakeButton.get() || (Robot.isAuto && intakeConveyor.autoShouldIntake)) {
        if(turretShooter.isReadyToShoot) {
          return IN_AND_OUT;
        }
        if(shouldPulse()) {
          return IN_AND_PULSING;
        }
        return INTAKING;
      } else {
        if(turretShooter.isReadyToShoot) {
          return OUTTAKING;
        }
        if(shouldPulse()) {
          return PULSING;
        }
        return OFF;
      }
    }

    public abstract State execute();
  }
}
