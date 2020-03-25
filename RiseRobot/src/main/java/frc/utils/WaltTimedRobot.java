package frc.utils;

import edu.wpi.first.hal.FRCNetComm;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.hal.NotifierJNI;
import edu.wpi.first.wpilibj.RobotController;

public class WaltTimedRobot extends WaltIterativeRobotBase {

    public static final double kDefaultPeriod = 0.02;

    // The C pointer to the notifier object. We don't use it directly, it is
    // just passed to the JNI bindings.
    private final int m_notifier = NotifierJNI.initializeNotifier();

    // The absolute expiration time
    private double m_expirationTime;

    /**
     * Constructor for WaltTimedRobot.
     */
    protected WaltTimedRobot() {
        this(kDefaultPeriod);
    }

    /**
     * Constructor for WaltTimedRobot.
     *
     * @param period Period in seconds.
     */
    protected WaltTimedRobot(double period) {
        super(period);
        NotifierJNI.setNotifierName(m_notifier, "TimedRobot");

        HAL.report(FRCNetComm.tResourceType.kResourceType_Framework, FRCNetComm.tInstances.kFramework_Timed);
    }

    @Override
    @SuppressWarnings("NoFinalizer")
    protected void finalize() {
        NotifierJNI.stopNotifier(m_notifier);
        NotifierJNI.cleanNotifier(m_notifier);
    }

    /**
     * Provide an alternate "main loop" via startCompetition().
     */
    @Override
    @SuppressWarnings("UnsafeFinalization")
    public void startCompetition() {
        robotInit();

        // Tell the DS that the robot is ready to be enabled
        HAL.observeUserProgramStarting();

        m_expirationTime = RobotController.getFPGATime() * 1e-6 + m_period;
        updateAlarm();

        // Loop forever, calling the appropriate mode-dependent function
        while (true) {
            long curTime = NotifierJNI.waitForNotifierAlarm(m_notifier);
            if (curTime == 0) {
                break;
            }

            m_expirationTime += m_period;
            updateAlarm();

            loopFunc();
        }
    }

    /**
     * Ends the main loop in startCompetition().
     */
    @Override
    public void endCompetition() {
        NotifierJNI.stopNotifier(m_notifier);
    }

    /**
     * Get time period between calls to Periodic() functions.
     */
    public double getPeriod() {
        return m_period;
    }

    /**
     * Update the alarm hardware to reflect the next alarm.
     */
    @SuppressWarnings("UnsafeFinalization")
    private void updateAlarm() {
        NotifierJNI.updateNotifierAlarm(m_notifier, (long) (m_expirationTime * 1e6));
    }

}
