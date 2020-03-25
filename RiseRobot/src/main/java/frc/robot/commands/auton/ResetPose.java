package frc.robot.commands.auton;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.CommandBase;

import static frc.robot.Robot.drivetrain;

public class ResetPose extends CommandBase {

    private Pose2d pose;

    public ResetPose() {
        this.pose = new Pose2d();
    }

    public ResetPose(Pose2d pose) {
        this.pose = pose;
    }

    @Override
    public void initialize() {
        drivetrain.resetOdometry(pose);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
