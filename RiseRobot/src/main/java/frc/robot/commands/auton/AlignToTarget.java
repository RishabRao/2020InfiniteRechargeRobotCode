package frc.robot.commands.auton;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.Robot;
import frc.utils.LimelightHelper;
import java.util.function.DoubleSupplier;

import static frc.robot.OI.turnToTargetButton;
import static frc.robot.Robot.drivetrain;

public class AlignToTarget extends CommandBase {

    private static final DoubleSupplier angleSupplier = LimelightHelper::getTX;
    private static final PIDController controller = new PIDController(0.056, 0, 0);
    private int atTargetCount;
    private double targetAngle;
    
    public AlignToTarget() {
        addRequirements(drivetrain);
        
        atTargetCount = 0;
    }

    @Override
    public void initialize() {
        atTargetCount = 0;
        targetAngle = drivetrain.getHeading().getDegrees() - angleSupplier.getAsDouble();
        System.out.println("turning to " + targetAngle);
        System.out.println("Auto Align");

        controller.reset();
        controller.setTolerance(1);
        atTargetCount = 0;

        System.out.println("P CONSTANT: " + controller.getP());
        System.out.println("I CONSTANT: " + controller.getI());
//        controller.setP(SmartDashboard.getNumber("Turn P", 0.056));
    }

    @Override
    public void execute() {
        double turnRate = -controller.calculate(drivetrain.getHeading().getDegrees(), targetAngle);
        SmartDashboard.putNumber("Velocity error", controller.getVelocityError());
        SmartDashboard.putNumber("Position error", controller.getPositionError());
        System.out.println(controller.getPositionError());
        drivetrain.setDutyCycles(turnRate, -turnRate);

        if (controller.atSetpoint()) {
            atTargetCount++;
        } else {
            atTargetCount = 0;
        }

    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("AT TARGET COUNT: " + atTargetCount);
        drivetrain.setDutyCycles(0, 0);
    }

    @Override
    public boolean isFinished() {
        return controller.atSetpoint() || (!Robot.isAuto && !turnToTargetButton.get());
    }

}
