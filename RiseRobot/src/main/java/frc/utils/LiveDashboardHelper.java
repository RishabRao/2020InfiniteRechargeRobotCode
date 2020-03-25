package frc.utils;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.util.Units;
import frc.robot.commands.auton.LiveDashboard;

import static frc.robot.Constants.FieldConfiguration.DISTANCE_TO_REFLECTION_LINE;
import static frc.robot.Constants.FieldConfiguration.LIVE_DASHBOARD_FIELD_HEIGHT;
import static frc.robot.Robot.isBlue;

public class LiveDashboardHelper {

    public static void putRobotData(Pose2d currentPose) {
        if (isBlue) {
            LiveDashboard.getInstance().setRobotX(Units.metersToFeet(DISTANCE_TO_REFLECTION_LINE * 2 - currentPose.getTranslation().getX()));
            LiveDashboard.getInstance().setRobotY(Units.metersToFeet(LIVE_DASHBOARD_FIELD_HEIGHT - currentPose.getTranslation().getY()));
            LiveDashboard.getInstance().setRobotHeading(currentPose.getRotation().plus(Rotation2d.fromDegrees(180)).getRadians());
        } else {
            LiveDashboard.getInstance().setRobotX(Units.metersToFeet(currentPose.getTranslation().getX()));
            LiveDashboard.getInstance().setRobotY(Units.metersToFeet(currentPose.getTranslation().getY()));
            LiveDashboard.getInstance().setRobotHeading(currentPose.getRotation().getRadians());
        }
    }

    public static void putTrajectoryData(Pose2d trajectoryPose) {
        if (isBlue) {
            LiveDashboard.getInstance().setPathX(Units.metersToFeet(DISTANCE_TO_REFLECTION_LINE * 2 - trajectoryPose.getTranslation().getX()));
            LiveDashboard.getInstance().setPathY(Units.metersToFeet(LIVE_DASHBOARD_FIELD_HEIGHT - trajectoryPose.getTranslation().getY()));
            LiveDashboard.getInstance().setPathHeading(trajectoryPose.getRotation().plus(Rotation2d.fromDegrees(180)).getRadians());
        } else {
            LiveDashboard.getInstance().setPathX(Units.metersToFeet(trajectoryPose.getTranslation().getX()));
            LiveDashboard.getInstance().setPathY(Units.metersToFeet(trajectoryPose.getTranslation().getY()));
            LiveDashboard.getInstance().setPathHeading(trajectoryPose.getRotation().getRadians());
        }
    }

}
