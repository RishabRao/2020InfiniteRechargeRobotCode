package frc.robot.robots;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import frc.utils.interpolatingmap.InterpolatingDouble;
import frc.utils.interpolatingmap.InterpolatingTreeMap;

public class CompDeepSpace implements WaltRobot {

    // Config constants

    private final PIDController leftPIDController = new PIDController(2, 0, 0);   //maybe 2.53    //maybe 1.74? maybe 1.62?
    private final PIDController rightPIDController = new PIDController(2, 0, 0);  //maybe 2.53

    private final SimpleMotorFeedforward drivetrainFeedforward = new SimpleMotorFeedforward(0.199, 2.13, 0.534);

    private ProfiledPIDController turnPIDController = new ProfiledPIDController(0.011, 0, 0,
            new TrapezoidProfile.Constraints(360, 80)); //0.009

    private final InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> shooterCalibrationMap = new InterpolatingTreeMap<>();

    public CompDeepSpace() {
        turnPIDController.enableContinuousInput(-180f, 180f);
        turnPIDController.setTolerance(1, 1);

        populateShooterLUT();
    }

    // 32 l
    // 38 w

    @Override
    public double getTrackWidth() {
        return 0.79872077;
    }

    @Override
    public double getKBeta() {
        return 2.0;
    }

    @Override
    public double getKZeta() {
        return 0.7;
    }

    @Override
    public PIDController getLeftPIDController() {
        return leftPIDController;
    }

    @Override
    public PIDController getRightPIDController() {
        return rightPIDController;
    }

    @Override
    public ProfiledPIDController getTurnPIDController() {
        return turnPIDController;
    }

    @Override
    public SimpleMotorFeedforward getFlywheelFeedforward() {
        return null;
    }

    @Override
    public SimpleMotorFeedforward getDrivetrainFeedforward() {
        return drivetrainFeedforward;
    }

    @Override
    public double getVelocityFactor() {
        return 0.06038 / 60.;
    }

    @Override
    public double getPositionFactor() {
        return 0.06038;
    }

    @Override
    public double getDistancePerPulse() {
        return 0.0005706796580;
    }

    @Override
    public double getMinimumShiftingTime() {
        return 1.0;
    }

    @Override
    public double getLimelightMountingAngle() {
        return 20;
    }

    @Override
    public double getLimelightMountingHeight() {
        return 10;
    }

    @Override
    public double getVisionAlignKp() {
        return 0;
    }

    @Override
    public double getVisionAlignKs() {
        return 0;
    }

    @Override
    public double getMaxAlignmentTime() {
        return 2.5;
    }

    @Override
    public double getVisionAlignTxTolerance() {
        return 0;
    }

    @Override
    public InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> getShooterCalibrationMap() {
        return shooterCalibrationMap;
    }

    @Override
    public void populateShooterLUT() {
        shooterCalibrationMap.put(new InterpolatingDouble(0.0), new InterpolatingDouble(0.0));
        shooterCalibrationMap.put(new InterpolatingDouble(1.0), new InterpolatingDouble(1.0));
        shooterCalibrationMap.put(new InterpolatingDouble(2.0), new InterpolatingDouble(2.0));
    }

    @Override
    public String toString() {
        return "Competition Deep Space";
    }
}
