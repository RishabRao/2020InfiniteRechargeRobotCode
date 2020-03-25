package frc.robot;

import static frc.robot.Robot.currentRobot;
import static frc.robot.Robot.drivetrain;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveKinematicsConstraint;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj.util.Units;
import java.util.Arrays;

public class Paths {

  public static class TestTrajectories {

    public static Trajectory testForward = generateTestForward();

    public static Trajectory generateTestForward() {
      TrajectoryConfig config = new TrajectoryConfig(
          Units.feetToMeters(11), Units.feetToMeters(5));
      // config.addConstraint(new CentripetalAccelerationConstraint(Units.feetToMeters(4)));
      config.setKinematics(drivetrain.getDriveKinematics());

      return TrajectoryGenerator.generateTrajectory(
          Arrays.asList(
              new Pose2d(Units.feetToMeters(0), Units.feetToMeters(0), Rotation2d.fromDegrees(0.0)),
              new Pose2d(Units.feetToMeters(15), Units.feetToMeters(0),
                  Rotation2d.fromDegrees(0.0))),
          config
      );
    }
  }

  public static class CrossBaseline {

    public static Trajectory forwards = generateForwards();
    public static Trajectory backwards = generateBackwards();

    public static Trajectory generateForwards() {
      TrajectoryConfig config = new TrajectoryConfig(
          Units.feetToMeters(4), Units.feetToMeters(3));
      config.addConstraint(
          new DifferentialDriveVoltageConstraint(currentRobot.getDrivetrainFeedforward(),
              drivetrain.getDriveKinematics(), 10.0));
      config.setKinematics(drivetrain.getDriveKinematics());

      return TrajectoryGenerator.generateTrajectory(
          Arrays.asList(new Pose2d(Units.feetToMeters(10.0), Units.feetToMeters(13.0),
                  Rotation2d.fromDegrees(0.0)),
              new Pose2d(Units.feetToMeters(15.0), Units.feetToMeters(13.0),
                  Rotation2d.fromDegrees(0.0))),
          config
      );
    }

    public static Trajectory generateBackwards() {
      TrajectoryConfig config = new TrajectoryConfig(
          Units.feetToMeters(4), Units.feetToMeters(3));
      config.addConstraint(
          new DifferentialDriveKinematicsConstraint(drivetrain.getDriveKinematics(),
              Units.feetToMeters(17)));
      config.addConstraint(
          new DifferentialDriveVoltageConstraint(currentRobot.getDrivetrainFeedforward(),
              drivetrain.getDriveKinematics(), 10.0));
      config.setKinematics(drivetrain.getDriveKinematics());
      config.setReversed(true);

      return TrajectoryGenerator.generateTrajectory(
          Arrays.asList(new Pose2d(Units.feetToMeters(15.0), Units.feetToMeters(13.0),
                  Rotation2d.fromDegrees(0.0)),
              new Pose2d(Units.feetToMeters(10), Units.feetToMeters(13.0),
                  Rotation2d.fromDegrees(0.0))),
          config
      );
    }
  }

  public static class Two {

    //        public static Trajectory trenchPickup = generateTwoTrenchPickup();
//        public static Trajectory intakeThreeBalls = generateTwoIntakeThreeBalls();
    public static Trajectory backUpToShootTrench = generateTwoBackUpShoot();
    public static Trajectory goShoot = generateTwoGoShoot();
    public static Trajectory trenchAndIntake = generateTwoTrenchAndIntake();
    public static Trajectory backUpAndTurn = generateBackUpAndTurn();
//        public static Trajectory trenchBackup = generateTwoTrenchBackup();
//        public static Trajectory generatorPickupTwoBalls = generateTwoGeneratorPickupTwoBalls();
//        public static Trajectory generatorBackupToShoot = generateTwoGeneratorBackupToShoot();

//        public static Trajectory generateTwoTrenchPickup() {
//            TrajectoryConfig config = new TrajectoryConfig(
//                    Units.feetToMeters(12.75), Units.feetToMeters(6));
//            config.addConstraint(new DifferentialDriveVoltageConstraint(currentRobot.getDrivetrainFeedforward(), drivetrain.getDriveKinematics(), 10.0));
//            config.addConstraint(new CentripetalAccelerationConstraint(3.25));
//            config.setKinematics(drivetrain.getDriveKinematics());
//            config.setStartVelocity(0);
//            config.setEndVelocity(4);
//
//            return TrajectoryGenerator.generateTrajectory(
//                    Arrays.asList(new Pose2d(Units.feetToMeters(10.609), Units.feetToMeters(19.13), Rotation2d.fromDegrees(50)),
//                            new Pose2d(Units.feetToMeters(17.529), Units.feetToMeters(24.576), Rotation2d.fromDegrees(0.0))),
//                    config
//            );
//        }

//        public static Trajectory generateTwoIntakeThreeBalls() {
//            TrajectoryConfig config = new TrajectoryConfig(
//                    Units.feetToMeters(8.5), Units.feetToMeters(6));
//            config.addConstraint(new DifferentialDriveVoltageConstraint(currentRobot.getDrivetrainFeedforward(), drivetrain.getDriveKinematics(), 10.0));
//            config.setKinematics(drivetrain.getDriveKinematics());
//
//            config.setStartVelocity(4);
//            config.setEndVelocity(0);
//
//            return TrajectoryGenerator.generateTrajectory(
//                    Arrays.asList(new Pose2d(Units.feetToMeters(17.529), Units.feetToMeters(24.576), Rotation2d.fromDegrees(0.0)),
//                            new Pose2d(Units.feetToMeters(25.904), Units.feetToMeters(24.487), Rotation2d.fromDegrees(0.0))),
//                    config
//            );
//        }

    public static Trajectory generateTwoTrenchAndIntake() {
      TrajectoryConfig config = new TrajectoryConfig(
          Units.feetToMeters(11.5), Units.feetToMeters(7));
      config.addConstraint(
          new DifferentialDriveVoltageConstraint(currentRobot.getDrivetrainFeedforward(),
              drivetrain.getDriveKinematics(), 10.0));
      config.addConstraint(new CentripetalAccelerationConstraint(3.25));
      config.setKinematics(drivetrain.getDriveKinematics());
      config.setStartVelocity(0);
      config.setEndVelocity(0);

      return TrajectoryGenerator.generateTrajectory(
          Arrays.asList(new Pose2d(Units.feetToMeters(10.609), Units.feetToMeters(19.13),
                  Rotation2d.fromDegrees(26)),
              new Pose2d(Units.feetToMeters(25.904), Units.feetToMeters(24.487),
                  Rotation2d.fromDegrees(0.0))),
          config
      );
    }

    public static Trajectory generateTwoBackUpShoot() {
      TrajectoryConfig config = new TrajectoryConfig(
          Units.feetToMeters(11.5), Units.feetToMeters(7));
      config.addConstraint(
          new DifferentialDriveVoltageConstraint(currentRobot.getDrivetrainFeedforward(),
              drivetrain.getDriveKinematics(), 10.0));
      config.setKinematics(drivetrain.getDriveKinematics());

      config.setReversed(true);

      return TrajectoryGenerator.generateTrajectory(
          Arrays.asList(new Pose2d(Units.feetToMeters(25.904), Units.feetToMeters(24.487),
                  Rotation2d.fromDegrees(0.0)),
              new Pose2d(Units.feetToMeters(19.958), Units.feetToMeters(24.487),
                  Rotation2d.fromDegrees(0.0))),
          config
      );
    }

    public static Trajectory generateBackUpAndTurn() {
      TrajectoryConfig config = new TrajectoryConfig(
          Units.feetToMeters(11.5), Units.feetToMeters(7));
      config.addConstraint(
          new DifferentialDriveVoltageConstraint(currentRobot.getDrivetrainFeedforward(),
              drivetrain.getDriveKinematics(), 10.0));
      config.addConstraint(new CentripetalAccelerationConstraint(4));
      config.setKinematics(drivetrain.getDriveKinematics());

      config.setReversed(true);

      return TrajectoryGenerator.generateTrajectory(
          Arrays.asList(new Pose2d(Units.feetToMeters(25.904), Units.feetToMeters(24.487),
                  Rotation2d.fromDegrees(0)),
              new Pose2d(Units.feetToMeters(18.827), Units.feetToMeters(24.375),
                  Rotation2d.fromDegrees(3.336)),
              new Pose2d(Units.feetToMeters(13.083), Units.feetToMeters(21.291),
                  Rotation2d.fromDegrees(105.0)),
              new Pose2d(Units.feetToMeters(16.093), Units.feetToMeters(19.296),
                  Rotation2d.fromDegrees(-175))),
          config
      );
    }


    public static Trajectory generateTwoGoShoot() {
      TrajectoryConfig config = new TrajectoryConfig(
          Units.feetToMeters(4.0), Units.feetToMeters(3));
      config.addConstraint(
          new DifferentialDriveVoltageConstraint(currentRobot.getDrivetrainFeedforward(),
              drivetrain.getDriveKinematics(), 10.0));
      config.setKinematics(drivetrain.getDriveKinematics());
      config.setReversed(true);

      return TrajectoryGenerator.generateTrajectory(
          Arrays.asList(new Pose2d(Units.feetToMeters(25.904), Units.feetToMeters(24.487),
                  Rotation2d.fromDegrees(0.0)),
              new Pose2d(Units.feetToMeters(14.581), Units.feetToMeters(18.964),
                  Rotation2d.fromDegrees(0.0))),
          config
      );
    }

//        public static Trajectory generateTwoTrenchBackup() {
//            TrajectoryConfig config = new TrajectoryConfig(
//                    Units.feetToMeters(10), Units.feetToMeters(6));
//            config.addConstraint(new DifferentialDriveVoltageConstraint(currentRobot.getDrivetrainFeedforward(), drivetrain.getDriveKinematics(), 10.0));
//            config.setKinematics(drivetrain.getDriveKinematics());
//            config.setReversed(true);
//
//            return TrajectoryGenerator.generateTrajectory(
//                    Arrays.asList(new Pose2d(Units.feetToMeters(25.678), Units.feetToMeters(24.39), Rotation2d.fromDegrees(0)),
//                            new Pose2d(Units.feetToMeters(18.965), Units.feetToMeters(21.707), Rotation2d.fromDegrees(-75.0))),
//                    config
//            );
//        }
//
//        public static Trajectory generateTwoGeneratorPickupTwoBalls() {
//            TrajectoryConfig config = new TrajectoryConfig(
//                    Units.feetToMeters(10), Units.feetToMeters(6));
//            config.addConstraint(new DifferentialDriveVoltageConstraint(currentRobot.getDrivetrainFeedforward(), drivetrain.getDriveKinematics(), 10.0));
//            config.setKinematics(drivetrain.getDriveKinematics());
//
//            return TrajectoryGenerator.generateTrajectory(
//                    Arrays.asList(new Pose2d(Units.feetToMeters(18.965), Units.feetToMeters(21.707), Rotation2d.fromDegrees(-75.0)),
//                            new Pose2d(Units.feetToMeters(19.579), Units.feetToMeters(19.46), Rotation2d.fromDegrees(-75.0))),
//                    config
//            );
//        }
//
//        public static Trajectory generateTwoGeneratorBackupToShoot() {
//            TrajectoryConfig config = new TrajectoryConfig(
//                    Units.feetToMeters(10), Units.feetToMeters(6));
//            config.addConstraint(new DifferentialDriveVoltageConstraint(currentRobot.getDrivetrainFeedforward(), drivetrain.getDriveKinematics(), 10.0));
//            config.setKinematics(drivetrain.getDriveKinematics());
//            config.setReversed(true);
//
//            return TrajectoryGenerator.generateTrajectory(
//                    Arrays.asList(new Pose2d(Units.feetToMeters(19.579), Units.feetToMeters(19.46), Rotation2d.fromDegrees(-75.0)),
//                            new Pose2d(Units.feetToMeters(22.104), Units.feetToMeters(20.889), Rotation2d.fromDegrees(168.0))),
//                    config
//            );
//        }
  }

//    public static class Four {
//
//        public static Trajectory intakeTwo = generateFourIntakeTwo();
//        public static Trajectory backupFromTrench = generateFourBackupFromTrench();
//
//        public static Trajectory generateFourIntakeTwo() {
//            TrajectoryConfig config = new TrajectoryConfig(
//                    Units.feetToMeters(10), Units.feetToMeters(6));
//            config.addConstraint(new DifferentialDriveVoltageConstraint(currentRobot.getDrivetrainFeedforward(), drivetrain.getDriveKinematics(), 10.0));
//            config.setKinematics(drivetrain.getDriveKinematics());
//
//            return TrajectoryGenerator.generateTrajectory(
//                    Arrays.asList(new Pose2d(Units.feetToMeters(10.821), Units.feetToMeters(2.25), Rotation2d.fromDegrees(0.0)),
//                            new Pose2d(Units.feetToMeters(20.004), Units.feetToMeters(2.488), Rotation2d.fromDegrees(0.0))),
//                    config
//            );
//        }
//
//        public static Trajectory generateFourBackupFromTrench() {
//            TrajectoryConfig config = new TrajectoryConfig(
//                    Units.feetToMeters(10), Units.feetToMeters(6));
//            config.addConstraint(new DifferentialDriveVoltageConstraint(currentRobot.getDrivetrainFeedforward(), drivetrain.getDriveKinematics(), 10.0));
//            config.setKinematics(drivetrain.getDriveKinematics());
//            config.setReversed(true);
//
//            return TrajectoryGenerator.generateTrajectory(
//                    Arrays.asList(new Pose2d(Units.feetToMeters(20.004), Units.feetToMeters(2.488), Rotation2d.fromDegrees(0.0)),
//                            new Pose2d(Units.feetToMeters(17.047), Units.feetToMeters(13.224), Rotation2d.fromDegrees(150.0))),
//                    config
//            );
//        }
//    }

  public static class Four {

    public static Trajectory intakeTwo = generateFiveIntakeTwo();
    public static Trajectory getAwayFromTrenchFor6 = generateFiveGetAwayFromTrenchFor6();
    public static Trajectory getAwayFromTrenchFor4 = generateFiveGetAwayFromTrenchFor4();
    public static Trajectory threeBallTrench = generateFiveThreeBallTrenchPickUp();
    public static Trajectory trenchToGenerator = generateFromTrenchToGenerator();

    public static Trajectory generateFiveIntakeTwo() {
      TrajectoryConfig config = new TrajectoryConfig(
          Units.feetToMeters(11.5), Units.feetToMeters(7));
      config.addConstraint(
          new DifferentialDriveVoltageConstraint(currentRobot.getDrivetrainFeedforward(),
              drivetrain.getDriveKinematics(), 10.0));
      config.setKinematics(drivetrain.getDriveKinematics());

      return TrajectoryGenerator.generateTrajectory(
          Arrays.asList(new Pose2d(Units.feetToMeters(10.821), Units.feetToMeters(2.25),
                  Rotation2d.fromDegrees(0.0)),
              new Pose2d(Units.feetToMeters(20.504), Units.feetToMeters(2.25),
                  Rotation2d.fromDegrees(0.0))),
          config
      );
    }

    public static Trajectory generateFiveGetAwayFromTrenchFor6() {
      TrajectoryConfig config = new TrajectoryConfig(
          Units.feetToMeters(11.5), Units.feetToMeters(6));
      config.addConstraint(
          new DifferentialDriveVoltageConstraint(currentRobot.getDrivetrainFeedforward(),
              drivetrain.getDriveKinematics(), 10.0));
//      config.addConstraint(new CentripetalAccelerationConstraint(3.25));
      config.setKinematics(drivetrain.getDriveKinematics());
      config.setReversed(true);

      return TrajectoryGenerator.generateTrajectory(
          Arrays.asList(
              new Pose2d(Units.feetToMeters(20.504), Units.feetToMeters(2.25),
                  Rotation2d.fromDegrees(0)),
              new Pose2d(Units.feetToMeters(13.238), Units.feetToMeters(6.498),
                  Rotation2d.fromDegrees(-60.481)),
              new Pose2d(Units.feetToMeters(12.904), Units.feetToMeters(11.83),
                  Rotation2d.fromDegrees(-101.496)),
              new Pose2d(Units.feetToMeters(16.485), Units.feetToMeters(16.799),
                  Rotation2d.fromDegrees(176.59))),
          config
      );
    }

    public static Trajectory generateFiveGetAwayFromTrenchFor4() {
      TrajectoryConfig config = new TrajectoryConfig(
          Units.feetToMeters(11.5), Units.feetToMeters(6));
      config.addConstraint(
          new DifferentialDriveVoltageConstraint(currentRobot.getDrivetrainFeedforward(),
              drivetrain.getDriveKinematics(), 10.0));
//      config.addConstraint(new CentripetalAccelerationConstraint(3.25));
      config.setKinematics(drivetrain.getDriveKinematics());
      config.setReversed(true);

      return TrajectoryGenerator.generateTrajectory(
          Arrays.asList(
              new Pose2d(Units.feetToMeters(20.504), Units.feetToMeters(2.25),
                  Rotation2d.fromDegrees(0)),
              new Pose2d(Units.feetToMeters(13.238), Units.feetToMeters(6.498),
                  Rotation2d.fromDegrees(-60.481)),
              new Pose2d(Units.feetToMeters(12.904), Units.feetToMeters(11.83),
                  Rotation2d.fromDegrees(-101.496)),
              new Pose2d(Units.feetToMeters(16.514), Units.feetToMeters(14.725),
                  Rotation2d.fromDegrees(171.752))),
          config
      );
    }

    public static Trajectory generateFiveThreeBallTrenchPickUp() {
      TrajectoryConfig config = new TrajectoryConfig(
          Units.feetToMeters(10), Units.feetToMeters(6));
      config.addConstraint(
          new DifferentialDriveVoltageConstraint(currentRobot.getDrivetrainFeedforward(),
              drivetrain.getDriveKinematics(), 10.0));
      config.setKinematics(drivetrain.getDriveKinematics());

      return TrajectoryGenerator.generateTrajectory(
          Arrays.asList(
              new Pose2d(Units.feetToMeters(17.047), Units.feetToMeters(13.224),
                  Rotation2d.fromDegrees(150.0)),
              new Pose2d(Units.feetToMeters(15.114), Units.feetToMeters(17.862),
                  Rotation2d.fromDegrees(90.0)),
              new Pose2d(Units.feetToMeters(17.575), Units.feetToMeters(24.658),
                  Rotation2d.fromDegrees(0.0)),
              new Pose2d(Units.feetToMeters(25.748), Units.feetToMeters(24.704),
                  Rotation2d.fromDegrees(0.0))),
          config
      );
    }

    public static Trajectory generateFromTrenchToGenerator() {
      TrajectoryConfig config = new TrajectoryConfig(
          Units.feetToMeters(10), Units.feetToMeters(6));
      config.addConstraint(
          new DifferentialDriveVoltageConstraint(currentRobot.getDrivetrainFeedforward(),
              drivetrain.getDriveKinematics(), 10.0));
      config.setKinematics(drivetrain.getDriveKinematics());
      config.setReversed(true);

      return TrajectoryGenerator.generateTrajectory(
          Arrays.asList(new Pose2d(Units.feetToMeters(25.748), Units.feetToMeters(24.704),
                  Rotation2d.fromDegrees(0.0)),
              new Pose2d(Units.feetToMeters(20.0), Units.feetToMeters(20.066),
                  Rotation2d.fromDegrees(-70.0))),
          config
      );
    }

  }
}
