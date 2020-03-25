package frc.robot.commands.auton;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

import static frc.robot.Robot.turretShooter;


public class ShootAllBalls extends CommandBase {

    private Timer timer;
    private double totalTime;

    public ShootAllBalls(double time) {
        timer = new Timer();
        totalTime = time;
    }


    @Override
    public void initialize() {
        timer.start();
//
//        if(rpm != -1) {
//            rpm = (int)turretShooter.estimateTargetSpeed(LimelightHelper.getDistanceMeters());
//        }
        turretShooter.autoShouldShoot = true;
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interrupted) {
        turretShooter.autoShouldShoot = false;
    }

    @Override
    public boolean isFinished() {
        return timer.get() > totalTime;
    }
}
