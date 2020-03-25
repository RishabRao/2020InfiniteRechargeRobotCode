package frc.robot.robots;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import frc.utils.interpolatingmap.InterpolatingDouble;
import frc.utils.interpolatingmap.InterpolatingTreeMap;

public interface WaltRobot {

    double getTrackWidth();

    double getKBeta();

    double getKZeta();

    // Velocity controller
    PIDController getLeftPIDController();

    PIDController getRightPIDController();

    // Turn controller
    ProfiledPIDController getTurnPIDController();

    // Shooter feedforward
    SimpleMotorFeedforward getFlywheelFeedforward();

    // Drivetrain feedforward
    SimpleMotorFeedforward getDrivetrainFeedforward();

    // Encoder constants

    double getVelocityFactor();

    double getPositionFactor();

    double getDistancePerPulse();

    double getMinimumShiftingTime();

    double getLimelightMountingAngle();

    double getLimelightMountingHeight();

    double getVisionAlignKp();

    double getVisionAlignKs();

    double getMaxAlignmentTime();

    double getVisionAlignTxTolerance();

    InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> getShooterCalibrationMap();

    void populateShooterLUT();
}
