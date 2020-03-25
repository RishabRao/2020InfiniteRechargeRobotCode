package frc.robot.commands.teleop;

import static edu.wpi.first.wpilibj.Timer.getFPGATimestamp;
import static frc.robot.OI.barfButton;
import static frc.robot.OI.gamepad;
import static frc.robot.OI.shootButton;
import static frc.robot.Robot.intakeConveyor;
import static frc.robot.Robot.turretShooter;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.utils.LimelightHelper;

public class TurretShooterCommand extends CommandBase {

  private static final double JOYSTICK_DEADBAND = 0.125;
  private static final int BARF_SPEED = 6000;
  private static final int SPEED_ERROR_LIMIT = 200;
  private static final double SHOOTING_DELAY_TIME = 0.25;
  private static final int SHOOTING_ADDITIVE = 300;
  protected static int targetSpeed = 0;
  private FlywheelState currentFlywheelState, previousFlywheelState;
  private TurretState currentTurretState;

  public TurretShooterCommand() {
    addRequirements(turretShooter);

    currentFlywheelState = previousFlywheelState = FlywheelState.OFF;
    currentTurretState = TurretState.MANUAL;
  }

  @Override
  public void execute() {

    if (previousFlywheelState != currentFlywheelState) {
      currentFlywheelState.initialize();
    }
    previousFlywheelState = currentFlywheelState;

    // Run the states' execute methods, and update the pointers, as necessary.
    currentFlywheelState = currentFlywheelState.execute();
//    currentTurretState = currentTurretState.execute();

    SmartDashboard.putString("Flywheel State", currentFlywheelState.name());
//      SmartDashboard.putString("Turret State", currentTurretState.name());
    SmartDashboard.putNumber("Target Speed", targetSpeed);
  }


  private enum TurretState {
    MANUAL {
      @Override
      public TurretState execute() {
        double rotateCommand = gamepad.getRightX();
        if (Math.abs(rotateCommand) > JOYSTICK_DEADBAND) {
          turretShooter.setOpenLoopTurretOutput(rotateCommand);
        } else if (LimelightHelper.getTV() == 1) {
          // If it has found a single target, switch
          turretShooter.setOpenLoopTurretOutput(0);
          return TRACKING;
        }
        return this;
      }
    },
    TRACKING {
      @Override
      public TurretState execute() {
        // If it has no targets or more than one, switch
        if (LimelightHelper.getTV() != 1 || Math.abs(gamepad.getRightX()) > JOYSTICK_DEADBAND) {
          turretShooter.setOpenLoopTurretOutput(0);
          return MANUAL;
        }
        // TODO adjust minus/plus to match
        Rotation2d targetAngle =
            Rotation2d.fromDegrees(LimelightHelper.getTX()).minus(turretShooter.getTurretAngle());
        turretShooter.setTurretAngle(targetAngle, false);

        return this;
      }
    };

    public abstract TurretState execute();
  }


  private enum FlywheelState {
    OFF {
      @Override
      public void initialize() {
        targetSpeed = 0;
      }

      @Override
      public FlywheelState execute() {
//        if (shootButton.get() || barfButton.get()) {
//          return SPINNING_UP;
//        }
        if (shootButton.get() || (Robot.isAuto && turretShooter.autoShouldShoot)) {
          // If shooting, estimate the target speed
          targetSpeed = (int) turretShooter.estimateTargetSpeed(LimelightHelper.getDistanceFeet());
          return SPINNING_UP;
        }
        if (barfButton.get()) {
          targetSpeed = BARF_SPEED;
          return SPINNING_UP;
        }
        return this;
      }
    },
    SPINNING_UP {
      @Override
      public void initialize() {
        turretShooter
            .setFlywheelOutput(TalonFXControlMode.Velocity, targetSpeed/* + SHOOTING_ADDITIVE*/);
        turretShooter.isReadyToShoot = false;
        turretShooter.switchProfileSlot(0);
//        System.out.println("Spinning up, error: " + turretShooter.getClosedLoopFlywheelError());
      }

      @Override
      public FlywheelState execute() {
        turretShooter
            .setFlywheelOutput(TalonFXControlMode.Velocity, targetSpeed/* + SHOOTING_ADDITIVE*/);
//        System.out.println("Spinning up " + turretShooter.getFlywheelSpeed());
        if ((!Robot.isAuto && !(shootButton.get() || barfButton.get())) ||
                (Robot.isAuto && !turretShooter.autoShouldShoot)) {
          return SPINNING_DOWN;
        }
        if (turretShooter.getClosedLoopFlywheelError() < 0 ||
                Math.abs(turretShooter.getClosedLoopFlywheelError()) < SPEED_ERROR_LIMIT) {
          return SHOOTING;
        }
        return this;
      }
    },
    SHOOTING {
      private double delayStart;

      @Override
      public void initialize() {
        turretShooter.setFlywheelOutput(TalonFXControlMode.Velocity, targetSpeed);
        turretShooter.isReadyToShoot = true;
        turretShooter.switchProfileSlot(1);
        delayStart = getFPGATimestamp();
//        System.out.println("Shooting, error: " + turretShooter.getClosedLoopFlywheelError());

      }

      @Override
      public FlywheelState execute() {
//        intakeConveyor.resetBallCount();
//        SmartDashboard.putNumber("Delay Time", getFPGATimestamp() - delayStart);
        turretShooter.setFlywheelOutput(TalonFXControlMode.Velocity, targetSpeed);
        if ((!Robot.isAuto && !(shootButton.get() || barfButton.get())) ||
                (Robot.isAuto && !turretShooter.autoShouldShoot)) {
          return SPINNING_DOWN;
        }
        if (turretShooter.getClosedLoopFlywheelError() >= 0 &&
            Math.abs(turretShooter.getClosedLoopFlywheelError()) > SPEED_ERROR_LIMIT) {
          return SPINNING_UP;
        }
        return this;
      }
    },
    SPINNING_DOWN {
      @Override
      public void initialize() {
        turretShooter.isReadyToShoot = false;
        turretShooter.switchProfileSlot(0);
        targetSpeed = 0;
      }

      @Override
      public FlywheelState execute() {
        turretShooter.setFlywheelOutput(TalonFXControlMode.PercentOutput, 0);
        if (shootButton.get() || (Robot.isAuto && turretShooter.autoShouldShoot)) {
          // If shooting, estimate the target speed
          targetSpeed = (int) turretShooter.estimateTargetSpeed(LimelightHelper.getDistanceFeet());
          return SPINNING_UP;
        }
        if (barfButton.get()) {
          targetSpeed = BARF_SPEED;
          return SPINNING_UP;
        }
        if (Math.abs(turretShooter.getFlywheelSpeed()) < SPEED_ERROR_LIMIT) {
          return OFF;
        }
        return this;
      }
    };

    public abstract void initialize();

    public abstract FlywheelState execute();
  }
}

