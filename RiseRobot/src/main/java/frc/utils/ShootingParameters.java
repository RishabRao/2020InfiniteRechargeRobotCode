package frc.utils;

import edu.wpi.first.wpilibj.geometry.Rotation2d;

public class ShootingParameters {

    double distance;
    Rotation2d angle;

    public ShootingParameters(double distance, Rotation2d angle) {
        this.distance = distance;
        this.angle = angle;
    }

    public double getDistance() {
        return distance;
    }

    public Rotation2d getAngle() {
        return angle;
    }

    /**
     * @return a length 2 {@code Double[]} ("Tuple") with the distance and the angle (in radians).
     */
    public Double[] asTuple() {
        return new Double[] {distance, angle.getRadians()};
    }
}
