package frc.robot.robots;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import frc.utils.interpolatingmap.InterpolatingDouble;
import frc.utils.interpolatingmap.InterpolatingTreeMap;

public class PracticeRise implements WaltRobot {

    private final PIDController leftPIDController = new PIDController(2.37, 0, 0);
    private final PIDController rightPIDController = new PIDController(2.37, 0, 0);

    private final SimpleMotorFeedforward drivetrainFeedforward = new SimpleMotorFeedforward(0.165, 2.04,0); //0.487

    private ProfiledPIDController turnPIDController = new ProfiledPIDController(0.013, 0, 0,
            new TrapezoidProfile.Constraints(400, 400)); //0.009

    private final InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> shooterCalibrationMap = new InterpolatingTreeMap<>();

    public PracticeRise() {
        turnPIDController.enableContinuousInput(-180f, 180f);
        turnPIDController.setTolerance(1, 1);
        populateShooterLUT();
    }

    @Override
    public double getTrackWidth() {
        return 0.729784476;
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
        return 0.0610634006918382 / 60.;
    }

    @Override
    public double getPositionFactor() {
        return 0.0610634006918382;
    }

    @Override
    public double getDistancePerPulse() {
        return 1;
    }

    @Override
    public double getMinimumShiftingTime() {
        return 0;
    }

    @Override
    public double getLimelightMountingAngle() {
        return 35;
    }

    @Override
    public double getLimelightMountingHeight() {
        return 24;
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
        return 0;
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
//        1.2 Mechanical Advantage
//        shooterCalibrationMap.put(new InterpolatingDouble(10.05), new InterpolatingDouble(14100.0));
//        shooterCalibrationMap.put(new InterpolatingDouble(12.75), new InterpolatingDouble(13800.0));
//        shooterCalibrationMap.put(new InterpolatingDouble(15.35), new InterpolatingDouble(13900.0));
//        shooterCalibrationMap.put(new InterpolatingDouble(17.16), new InterpolatingDouble(14400.0));
//        shooterCalibrationMap.put(new InterpolatingDouble(20.07), new InterpolatingDouble(14850.0));
//        shooterCalibrationMap.put(new InterpolatingDouble(7.292000000000001), new InterpolatingDouble(18000.0));
//        shooterCalibrationMap.put(new InterpolatingDouble(22.3), new InterpolatingDouble(15750.0));

        shooterCalibrationMap.put(new InterpolatingDouble(10.03), new InterpolatingDouble(12650.0));
        shooterCalibrationMap.put(new InterpolatingDouble(12.72), new InterpolatingDouble(12300.0));
        shooterCalibrationMap.put(new InterpolatingDouble(15.3), new InterpolatingDouble(12350.0));
        shooterCalibrationMap.put(new InterpolatingDouble(17.17), new InterpolatingDouble(12650.0));
        shooterCalibrationMap.put(new InterpolatingDouble(18.9), new InterpolatingDouble(12650.0));
        shooterCalibrationMap.put(new InterpolatingDouble(19.97), new InterpolatingDouble(12750.0));
        shooterCalibrationMap.put(new InterpolatingDouble(8.38), new InterpolatingDouble(12700.0));
        shooterCalibrationMap.put(new InterpolatingDouble(22.9), new InterpolatingDouble(13400.0));
        shooterCalibrationMap.put(new InterpolatingDouble(23.2), new InterpolatingDouble(13800.0));

    }

    @Override
    public String toString() {
        return "Practice Rise";
    }

}
