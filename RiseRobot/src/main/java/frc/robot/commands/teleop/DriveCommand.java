package frc.robot.commands.teleop;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.utils.LimelightHelper;

import static frc.robot.OI.*;
import static frc.robot.Robot.drivetrain;

public class DriveCommand extends CommandBase {

    private double deadBand = 0.1;

    public DriveCommand() {
        addRequirements(drivetrain);
    }

    public double getLeftJoystickY() {
        return Math.abs(leftJoystick.getY()) > deadBand ? -leftJoystick.getY() : 0;
    }

    public double getRightJoystickY() {
        return Math.abs(rightJoystick.getY()) > deadBand ? -rightJoystick.getY() : 0;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        // (Joysticks inverted because limelight is facing backwards)

//        if(turnToTargetButton.get()) {
//            double steerCmd = LimelightHelper.getTX() * 0.05;
//            drivetrain.setArcadeSpeeds(0 , steerCmd);
//            System.out.println(steerCmd);
//        }
////
//        else {
            drivetrain.setDutyCycles(getLeftJoystickY(), getRightJoystickY());
//        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
