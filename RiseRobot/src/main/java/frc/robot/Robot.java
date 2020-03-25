package frc.robot;

import static frc.robot.Constants.SmartDashboardKeys.AUTON_SELECT_ID;
import static frc.robot.Constants.SmartDashboardKeys.IS_BLUE;
import static frc.robot.OI.*;
import static frc.utils.AutonSelector.DO_NOTHING;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.auton.AlignToTarget;
import frc.robot.commands.teleop.ClimbCommand;
import frc.robot.commands.teleop.DriveCommand;
import frc.robot.commands.teleop.IntakeConveyorCommand;
import frc.robot.commands.teleop.TurretShooterCommand;
import frc.robot.commands.teleop.UnlockClimberCommand;
import frc.robot.robots.RobotIdentifier;
import frc.robot.robots.WaltRobot;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeConveyor;
import frc.robot.subsystems.Spinner;
import frc.robot.subsystems.TurretShooter;
import frc.utils.AutonSelector;
import frc.utils.LEDController;
import frc.utils.LimelightHelper;
import frc.utils.LiveDashboardHelper;
import frc.utils.ShuffleboardTimer;
import frc.utils.WaltTimedRobot;
import java.util.Arrays;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends WaltTimedRobot {

  public static Drivetrain drivetrain;
  public static Spinner spinner;
  public static TurretShooter turretShooter;
  public static Climber climber;
  public static IntakeConveyor intakeConveyor;

  public static WaltRobot currentRobot;
  public static boolean isBlue = true;
  public static boolean isAuto = true;
  private static ShuffleboardTimer matchTimer;
  private static SendableChooser<Integer> autonChooser;
  public static Jaguar jaguar = new Jaguar(19);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    currentRobot = RobotIdentifier.findByInputs(new DigitalInput(8).get(),
        new DigitalInput(9).get()).getCurrentRobot();

    System.out.println("Starting robot code. Current Robot is " + currentRobot.toString());

    buttonMap.sendToNetworkTable();

    drivetrain = new Drivetrain();
//    spinner = new Spinner();
    turretShooter = new TurretShooter();
    climber = new Climber();
    intakeConveyor = new IntakeConveyor();

    matchTimer = new ShuffleboardTimer("Match Timer", Timer::getMatchTime, 0,
        "0x0024D6", "0x000b40");

    SmartDashboard.putBoolean(IS_BLUE, false);
    SmartDashboard.putNumber(AUTON_SELECT_ID, 0);
    CommandScheduler.getInstance().setDefaultCommand(drivetrain, new DriveCommand());
    CommandScheduler.getInstance().setDefaultCommand(intakeConveyor, new IntakeConveyorCommand());
    CommandScheduler.getInstance().setDefaultCommand(turretShooter, new TurretShooterCommand());
    CommandScheduler.getInstance().setDefaultCommand(climber, new ClimbCommand());

    turnToTargetButton.whenPressed(new AlignToTarget().withTimeout(1.5));

    autonChooser = new SendableChooser<>();
    autonChooser.setDefaultOption(DO_NOTHING.name(), DO_NOTHING.getId());
    Arrays.stream(AutonSelector.values()).forEach(n -> autonChooser.addOption(n.name(), n.getId()));
    SmartDashboard.putData("Auton Selector", autonChooser);

    LEDController.setLEDPassiveMode();
    LimelightHelper.setLedMode(true);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
//    SmartDashboard.putNumber(AUTON_SELECT_ID, SmartDashboard.getNumber(AUTON_SELECT_ID, 0));
    CommandScheduler.getInstance().run();
    if (DriverStation.getInstance().getAlliance() != DriverStation.Alliance.Invalid) {
      isBlue = SmartDashboard.getBoolean(IS_BLUE, false);
    } else {
      isBlue = (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Blue);
    }
    matchTimer.sendToShuffleboard();
    if (matchTimer.timeSupplier.get() < 30 && matchTimer.precision == 0) {
      matchTimer.precision = 1;
      // Default red-orange colors
      matchTimer.onColor = null;
      matchTimer.offColor = null;
    }
    SmartDashboard.putNumber("distance", LimelightHelper.getDistanceFeet());
//    SmartDashboard.putNumber("Turn P", SmartDashboard.getNumber("Turn P", 0.04));

    if (LimelightHelper.getTV() == 1) {                     // Limelight sees a target
      if (Math.abs(LimelightHelper.getTX()) <= 1) {         // Within angle tolerance
        LEDController.setLEDPassiveMode();
      } else if (LimelightHelper.getTX() < 0) {             // Target is to the left
        LEDController.setLEDTurnLeftMode();
      } else {                                              // Target is to the right
        LEDController.setLEDTurnRightMode();
      }
    } else {
      LEDController.setLEDPassiveMode();
    }
  }

    /**
     * This function is called once each time the robot enters Disabled mode.
     */
    @Override
    public void disabledInit () {
      SmartDashboard.putData("Auton Selector", autonChooser);
      LimelightHelper.setPipeline(1);
    }

    @Override
    public void disabledPeriodic () {
      CommandScheduler.getInstance().run();
      LimelightHelper.setPipeline(1);
    }

    /**
     * This autonomous runs the autonomous command selected
     */
    @Override
    public void autonomousInit () {
      isAuto = true;
      turretShooter.autoShouldShoot = false;
      intakeConveyor.setBallCount(3);
      intakeConveyor.setIntakeToggle(false);
//      climber.setClimberToggle(false);
      drivetrain.resetHardware();
      LimelightHelper.setPipeline(0);
      LimelightHelper.setLedMode(true);
      AutonSelector.findById(autonChooser.getSelected()).getCommandGroup().schedule();
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic () {
      CommandScheduler.getInstance().run();
    }

    @Override
    public void teleopInit () {
      isAuto = false;
      drivetrain.resetHardware();
      LimelightHelper.setLedMode(false);
//      LimelightHelper.setLedMode(false);
      LimelightHelper.setPipeline(0);
      CommandScheduler.getInstance().schedule(new UnlockClimberCommand());
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic () {
      CommandScheduler.getInstance().run();

      LiveDashboardHelper.putRobotData(drivetrain.getRobotPose());
    }

    @Override
    public void testInit () {
      CommandScheduler.getInstance().cancelAll();
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic () {
    }

  }
