package frc.robot.commands.auton;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.utils.LiveDashboardHelper;

import java.util.function.Supplier;

import static edu.wpi.first.wpilibj.util.ErrorMessages.requireNonNullParam;
import static frc.robot.Robot.currentRobot;
import static frc.robot.Robot.drivetrain;

/**
 * A command that uses a RAMSETE controller ({@link RamseteController}) to follow a trajectory
 * {@link Trajectory} with a differential drive.
 *
 * <p>The command handles trajectory-following, PID calculations, and feedforwards internally.  This
 * is intended to be a more-or-less "complete solution" that can be used by teams without a great
 * deal of controls expertise.
 *
 * <p>Advanced teams seeking more flexibility (for example, those who wish to use the onboard
 * PID functionality of a "smart" motor controller) may use the secondary constructor that omits
 * the PID and feedforward functionality, returning only the raw wheel speeds from the RAMSETE
 * controller.
 */
@SuppressWarnings("PMD.TooManyFields")
public class RamseteTrackingCommand extends CommandBase {
    private final Timer m_timer = new Timer();
    private final boolean m_useSparkPID;
    private final Trajectory m_trajectory;
    private final Supplier<Pose2d> m_pose;
    private final RamseteController m_follower;
    private final SimpleMotorFeedforward m_feedforward;
    private final DifferentialDriveKinematics m_kinematics;
    private final Supplier<DifferentialDriveWheelSpeeds> m_speeds;
    private final PIDController m_leftController;
    private final PIDController m_rightController;
    private DifferentialDriveWheelSpeeds m_prevSpeeds;
    private double m_prevTime;
    private final int m_sparkMaxPIDSlot;

    /**
     * Constructs a new RamseteCommand that, when executed, will follow the provided trajectory.
     * PID control and feedforward are handled internally, and outputs are scaled -12 to 12
     * representing units of volts.
     *
     * <p>Note: The controller will *not* set the outputVolts to zero upon completion of the path -
     * this
     * is left to the user, since it is not appropriate for paths with nonstationary endstates.
     *
     * @param trajectory      The trajectory to follow.
     */
    @SuppressWarnings("PMD.ExcessiveParameterList")
    public RamseteTrackingCommand(Trajectory trajectory) {
        m_trajectory = trajectory;
        m_pose = drivetrain::getRobotPose;
        m_follower = drivetrain.getRamseteController();
        m_feedforward = currentRobot.getDrivetrainFeedforward();
        m_kinematics = drivetrain.getDriveKinematics();
        m_speeds = drivetrain::getSpeeds;
        m_leftController = currentRobot.getLeftPIDController();
        m_rightController = currentRobot.getRightPIDController();

        m_useSparkPID = false;
        m_sparkMaxPIDSlot = 0;

        addRequirements(drivetrain);
    }

    /**
     * Constructs a new RamseteCommand that, when executed, will follow the provided trajectory.
     * Performs no PID control and calculates no feedforwards; outputs are the raw wheel speeds
     * in meters per second from the RAMSETE controller, and will need to be converted into a usable form by the user.
     *
     * @param trajectory            The trajectory to follow.
     */
    public RamseteTrackingCommand(Trajectory trajectory, int sparkMaxPIDSlot) {
        m_trajectory = requireNonNullParam(trajectory, "trajectory", "RamseteCommand");
        m_pose = drivetrain::getRobotPose;
        m_follower = drivetrain.getRamseteController();
        m_kinematics = drivetrain.getDriveKinematics();

        m_feedforward = currentRobot.getDrivetrainFeedforward();
        m_speeds = drivetrain::getSpeeds;
        m_leftController = currentRobot.getLeftPIDController();
        m_rightController = currentRobot.getRightPIDController();

        m_sparkMaxPIDSlot = sparkMaxPIDSlot;

        m_useSparkPID = true;

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        m_prevTime = 0;
        var initialState = m_trajectory.sample(0);
        m_prevSpeeds = m_kinematics.toWheelSpeeds(
                new ChassisSpeeds(initialState.velocityMetersPerSecond,
                        0,
                        initialState.curvatureRadPerMeter
                                * initialState.velocityMetersPerSecond));
        m_timer.reset();
        m_timer.start();
        if (!m_useSparkPID) {
            m_leftController.reset();
            m_rightController.reset();
        }

        LiveDashboard.getInstance().setFollowingPath(true);


        LiveDashboardHelper.putRobotData(drivetrain.getRobotPose());
        LiveDashboardHelper.putTrajectoryData(m_trajectory.getInitialPose());
    }

    @Override
    public void execute() {
        double curTime = m_timer.get();
        double dt = curTime - m_prevTime;

        var targetWheelSpeeds = m_kinematics.toWheelSpeeds(
                m_follower.calculate(m_pose.get(), m_trajectory.sample(curTime)));

        var leftSpeedSetpoint = targetWheelSpeeds.leftMetersPerSecond;
        var rightSpeedSetpoint = targetWheelSpeeds.rightMetersPerSecond;

        double leftOutput;
        double rightOutput;

        if (!m_useSparkPID) {
            double leftFeedforward =
                    m_feedforward.calculate(leftSpeedSetpoint,
                            (leftSpeedSetpoint - m_prevSpeeds.leftMetersPerSecond) / dt);

            double rightFeedforward =
                    m_feedforward.calculate(rightSpeedSetpoint,
                            (rightSpeedSetpoint - m_prevSpeeds.rightMetersPerSecond) / dt);

            leftOutput = leftFeedforward
                    + m_leftController.calculate(m_speeds.get().leftMetersPerSecond,
                    leftSpeedSetpoint);

            rightOutput = rightFeedforward
                    + m_rightController.calculate(m_speeds.get().rightMetersPerSecond,
                    rightSpeedSetpoint);

            drivetrain.setVoltages(leftOutput, rightOutput);

        } else {
            double leftFeedforward =
                    m_feedforward.calculate(leftSpeedSetpoint,
                            (leftSpeedSetpoint - m_prevSpeeds.leftMetersPerSecond) / dt);

//          System.out.printf("%f, %f\n", (leftSpeedSetpoint - m_prevSpeeds.leftMetersPerSecond), dt);


            double rightFeedforward =
                    m_feedforward.calculate(rightSpeedSetpoint,
                            (rightSpeedSetpoint - m_prevSpeeds.rightMetersPerSecond) / dt);

            drivetrain.setVelocities(leftSpeedSetpoint, leftFeedforward, rightSpeedSetpoint, rightFeedforward, m_sparkMaxPIDSlot);
        }

        m_prevTime = curTime;
        m_prevSpeeds = targetWheelSpeeds;

        LiveDashboardHelper.putRobotData(drivetrain.getRobotPose());
        LiveDashboardHelper.putTrajectoryData(m_trajectory.sample(curTime).poseMeters);
    }

    @Override
    public void end(boolean interrupted) {
        m_timer.stop();
        LiveDashboard.getInstance().setFollowingPath(false);
    }

    @Override
    public boolean isFinished() {
        return m_timer.hasPeriodPassed(m_trajectory.getTotalTimeSeconds());
    }
}