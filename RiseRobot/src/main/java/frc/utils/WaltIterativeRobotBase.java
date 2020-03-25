package frc.utils;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.hal.NotifierJNI;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class WaltIterativeRobotBase extends RobotBase {
    public static final double kDefaultPeriod = 0.02;
    private final Watchdog m_watchdog;
    // The C pointer to the notifier object. We don't use it directly, it is
    // just passed to the JNI bindings.
    private final int m_notifier = NotifierJNI.initializeNotifier();
    protected double m_period;
    private Mode m_lastMode = Mode.kNone;
    // The absolute expiration time
    private double m_expirationTime;
    private boolean m_rpFirstRun = true;
    private boolean m_dpFirstRun = true;
    private boolean m_apFirstRun = true;

    /* ----------- Overridable initialization code ----------------- */
    private boolean m_tpFirstRun = true;
    private boolean m_tmpFirstRun = true;

    /**
     * Constructor for IterativeRobotBase.
     *
     * @param period Period in seconds.
     */
    protected WaltIterativeRobotBase(double period) {
        m_period = period;
        m_watchdog = new Watchdog(period, this::printLoopOverrunMessage);
    }

    /**
     * Provide an alternate "main loop" via startCompetition().
     */
    @Override
    public abstract void startCompetition();

    /**
     * Robot-wide initialization code should go here.
     *
     * <p>Users should override this method for default Robot-wide initialization which will be called
     * when the robot is first powered on. It will be called exactly one time.
     *
     * <p>Warning: the Driver Station "Robot Code" light and FMS "Robot Ready" indicators will be off
     * until RobotInit() exits. Code in RobotInit() that waits for enable will cause the robot to
     * never indicate that the code is ready, causing the robot to be bypassed in a match.
     */
    public void robotInit() {
        System.out.println("Default robotInit() method... Override me!");
    }

    /* ----------- Overridable periodic code ----------------- */

    /**
     * Initialization code for disabled mode should go here.
     *
     * <p>Users should override this method for initialization code which will be called each time the
     * robot enters disabled mode.
     */
    public void disabledInit() {
        System.out.println("Default disabledInit() method... Override me!");
    }

    /**
     * Initialization code for autonomous mode should go here.
     *
     * <p>Users should override this method for initialization code which will be called each time the
     * robot enters autonomous mode.
     */
    public void autonomousInit() {
        System.out.println("Default autonomousInit() method... Override me!");
    }

    /**
     * Initialization code for teleop mode should go here.
     *
     * <p>Users should override this method for initialization code which will be called each time the
     * robot enters teleop mode.
     */
    public void teleopInit() {
        System.out.println("Default teleopInit() method... Override me!");
    }

    /**
     * Initialization code for test mode should go here.
     *
     * <p>Users should override this method for initialization code which will be called each time the
     * robot enters test mode.
     */
    @SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation")
    public void testInit() {
        System.out.println("Default testInit() method... Override me!");
    }

    /**
     * Periodic code for all robot modes should go here.
     */
    public void robotPeriodic() {
        if (m_rpFirstRun) {
            System.out.println("Default robotPeriodic() method... Override me!");
            m_rpFirstRun = false;
        }
    }

    /**
     * Periodic code for disabled mode should go here.
     */
    public void disabledPeriodic() {
        if (m_dpFirstRun) {
            System.out.println("Default disabledPeriodic() method... Override me!");
            m_dpFirstRun = false;
        }
    }

    /**
     * Periodic code for autonomous mode should go here.
     */
    public void autonomousPeriodic() {
        if (m_apFirstRun) {
            System.out.println("Default autonomousPeriodic() method... Override me!");
            m_apFirstRun = false;
        }
    }

    /**
     * Periodic code for teleop mode should go here.
     */
    public void teleopPeriodic() {
        if (m_tpFirstRun) {
            System.out.println("Default teleopPeriodic() method... Override me!");
            m_tpFirstRun = false;
        }
    }

    /**
     * Periodic code for test mode should go here.
     */
    @SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation")
    public void testPeriodic() {
        if (m_tmpFirstRun) {
            System.out.println("Default testPeriodic() method... Override me!");
            m_tmpFirstRun = false;
        }
    }

    protected void loopFunc() {
        m_watchdog.reset();

        // Call the appropriate function depending upon the current robot mode
        if (isDisabled()) {
            // Call DisabledInit() if we are now just entering disabled mode from either a different mode
            // or from power-on.
            if (m_lastMode != Mode.kDisabled) {
                LiveWindow.setEnabled(false);
                Shuffleboard.disableActuatorWidgets();
                disabledInit();
                m_watchdog.addEpoch("disabledInit()");
                m_lastMode = Mode.kDisabled;
            }

            HAL.observeUserProgramDisabled();
            disabledPeriodic();
            m_watchdog.addEpoch("disablePeriodic()");
        } else if (isAutonomous()) {
            // Call AutonomousInit() if we are now just entering autonomous mode from either a different
            // mode or from power-on.
            if (m_lastMode != Mode.kAutonomous) {
                LiveWindow.setEnabled(false);
                Shuffleboard.disableActuatorWidgets();
                autonomousInit();
                m_watchdog.addEpoch("autonomousInit()");
                m_lastMode = Mode.kAutonomous;
            }

            HAL.observeUserProgramAutonomous();
            autonomousPeriodic();
            m_watchdog.addEpoch("autonomousPeriodic()");
        } else if (isOperatorControl()) {
            // Call TeleopInit() if we are now just entering teleop mode from either a different mode or
            // from power-on.
            if (m_lastMode != Mode.kTeleop) {
                LiveWindow.setEnabled(false);
                Shuffleboard.disableActuatorWidgets();
                teleopInit();
                m_watchdog.addEpoch("teleopInit()");
                m_lastMode = Mode.kTeleop;
            }

            HAL.observeUserProgramTeleop();
            teleopPeriodic();
            m_watchdog.addEpoch("teleopPeriodic()");
        } else {
            // Call TestInit() if we are now just entering test mode from either a different mode or from
            // power-on.
            if (m_lastMode != Mode.kTest) {
                LiveWindow.setEnabled(true);
                Shuffleboard.enableActuatorWidgets();
                testInit();
                m_watchdog.addEpoch("testInit()");
                m_lastMode = Mode.kTest;
            }

            HAL.observeUserProgramTest();
            testPeriodic();
            m_watchdog.addEpoch("testPeriodic()");
        }

        robotPeriodic();
        m_watchdog.addEpoch("robotPeriodic()");

        SmartDashboard.updateValues();
        m_watchdog.addEpoch("SmartDashboard.updateValues()");
        LiveWindow.updateValues();
        m_watchdog.addEpoch("LiveWindow.updateValues()");
        Shuffleboard.update();
        m_watchdog.addEpoch("Shuffleboard.update()");
        m_watchdog.disable();

        // Warn on loop time overruns
//        if (m_watchdog.isExpired()) {
//            m_watchdog.printEpochs();
//        }
    }

    private void printLoopOverrunMessage() {
//        DriverStation.reportWarning("Loop time of " + m_period + "s overrun\n", false);
    }

    private enum Mode {
        kNone,
        kDisabled,
        kAutonomous,
        kTeleop,
        kTest
    }
}
