/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import static frc.robot.Constants.Joysticks.GAMEPAD_PORT;
import static frc.robot.Constants.Joysticks.LEFT_JOYSTICK_PORT;
import static frc.robot.Constants.Joysticks.RIGHT_JOYSTICK_PORT;
import static frc.utils.EnhancedJoystickButton.*;

import edu.wpi.first.wpilibj.util.Units;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

  public static class CANBusIDs {

    public static final int DRIVE_RIGHT_MASTER_ID = 1;
    public static final int DRIVE_RIGHT_SLAVE_ID = 2;
    public static final int DRIVE_LEFT_MASTER_ID = 3;
    public static final int DRIVE_LEFT_SLAVE_ID = 4;

    public static final int INTAKE_ID = 5;
    //        public static final int CENTERING_ID = 6;
    public static final int CONVEYOR_FRONT_ID = 7;
    public static final int CONVEYOR_BACK_ID = 8;

    public static final int SHOOTER_FLYWHEEL_MASTER_ID = 9;
    public static final int SHOOTER_FLYWHEEL_SLAVE_ID = 10;
    public static final int SHOOTER_TURRET_ID = 11;

    public static final int CLIMBER_ID = 12;

    public static final int SPINNER_ID = 13;

  }

  public static class PneumaticIDs {

    public static final int INTAKE_TOGGLE_ID = 1;

    public static final int CLIMBER_LOCK_ID = 2;
    public static final int CLIMBER_TOGGLE_ID = 3;

    public static final int SPINNER_TOGGLE_ID = 4;

  }

  public static class DioIDs {

    public static final int FRONT_CONVEYOR_SENSOR_ID = 4;
    public static final int BACK_CONVEYOR_SENSOR_ID = 5;
    public static final int LED1_ID = 6;
    public static final int LED2_ID = 7;

  }

  /**
   * The Joysticks class contains port mappings for the HID controllers.
   */
  public static class Joysticks {

    public static final int LEFT_JOYSTICK_PORT = 0;
    public static final int RIGHT_JOYSTICK_PORT = 1;
    public static final int GAMEPAD_PORT = 2;

  }

  /**
   * The ButtonMapDefaults class contains button mappings represented by String keys in a Map. Use
   * {@code getButtonIndex()} and {@code setButtonIndex()} in order to get the button indices.
   * IntSuppliers are used so that the Map can be dynamic.
   */
  public static class ButtonMapDefaults {

    /*
    Add button keys here. When you add new ones, be sure to add their default mappings below.
    Default indexes can be negative.
     */
    public static final String INTAKE_UP_KEY = "Intake Up";
    public static final String INTAKE_DOWN_KEY = "Intake Down";
    public static final String INTAKE_ON_KEY = "Intake";
    public static final String INTAKE_REVERSE_KEY = "Reverse Intake";
    public static final String SHOOT_KEY = "Shoot";
    public static final String BARF_KEY = "Slow outtake";
    public static final String CLIMBER_TOGGLE_KEY = "Climber Toggle";
    public static final String CLIMBER_LOCK_KEY = "Unlock Climber";
    public static final String OVERRIDE_FRONT_CONVEYOR_ON_KEY = "Override Front Conveyor On";
    public static final String OVERRIDE_BACK_CONVEYOR_ON_KEY = "Override Back Conveyor On";
    //        public static final String OVERRIDE_INTAKE_ON = "Override Intake On";
//        public static final String OVERRIDE_CENTERING_ON = "Override Centering On";
    public static final String TURN_TO_TARGET_KEY = "Turn To Target";
    public static final String RESET_BALL_COUNT_KEY = "Reset Ball Count";
    public static final String ACTUATE_SPINNER_TOGGLE_KEY = "Spinner Toggle Hold";
    public static final String SPIN_WHEEL_KEY = "Spin Control Panel";
    public static final String SPIN_TO_COLOR_KEY = "Spin To Designated Color";
    public static final String LIMELIGHT_TOGGLE_KEY = "Limelight LED Toggle";

    /*
    Add default mappings here, following the template
     */
    public static final Map<String, int[]> defaultMappings = Stream.of(new Object[][]{

//      {BUTTON_KEY,          new int[]{CONTROLLER_PORT, INDEX}},
        {INTAKE_UP_KEY, new int[]{GAMEPAD_PORT, POV_N}},
        {INTAKE_DOWN_KEY, new int[]{GAMEPAD_PORT, POV_S}},
        {INTAKE_ON_KEY, new int[]{GAMEPAD_PORT, 7}},                  // Left Trigger
        {INTAKE_REVERSE_KEY, new int[]{GAMEPAD_PORT, 5}},             // Left Bumper
        {SHOOT_KEY, new int[]{GAMEPAD_PORT, 8}},                      // Right Trigger
        {BARF_KEY, new int[]{GAMEPAD_PORT, 6}},                       // Right Bumper
        {CLIMBER_TOGGLE_KEY, new int[]{LEFT_JOYSTICK_PORT, 1}},
        {CLIMBER_LOCK_KEY, new int[]{GAMEPAD_PORT, 1}},               // XBox X, PS Square
        {OVERRIDE_FRONT_CONVEYOR_ON_KEY, new int[]{GAMEPAD_PORT, 9}}, // XBox "Select", PS "Share"
        {OVERRIDE_BACK_CONVEYOR_ON_KEY, new int[]{GAMEPAD_PORT, 10}}, // XBox & PS "Start"
//        {OVERRIDE_INTAKE_ON, new int[]{GAMEPAD_PORT, UNBOUND}},
//        {OVERRIDE_CENTERING_ON, new int[]{RIGHT_JOYSTICK_PORT, UNBOUND}},
        {TURN_TO_TARGET_KEY, new int[]{RIGHT_JOYSTICK_PORT, 1}},
        {RESET_BALL_COUNT_KEY, new int[]{GAMEPAD_PORT, 3}},           // XBox B, PS Circle
        {ACTUATE_SPINNER_TOGGLE_KEY, new int[]{GAMEPAD_PORT, POV_E}},
        {SPIN_WHEEL_KEY, new int[]{GAMEPAD_PORT, UNBOUND}},
        {SPIN_TO_COLOR_KEY, new int[]{GAMEPAD_PORT, UNBOUND}},
        {LIMELIGHT_TOGGLE_KEY, new int[]{GAMEPAD_PORT, 4}},

    }).collect(Collectors.toMap(n -> (String) n[0], n -> (int[]) n[1]));
  }

  public static class SmartDashboardKeys {

    public static final String AUTON_SELECT_ID = "Auton Select Id";
    public static final String IS_BLUE = "Is Blue";

  }

  public static class FieldConfiguration {

    public static final double DISTANCE_TO_REFLECTION_LINE = Units.feetToMeters(54) / 2;
    public static final double LIVE_DASHBOARD_FIELD_HEIGHT = Units.feetToMeters(27);

  }

  public static class Turret {

    public static final int TURRET_ENCODER_PORT_1 = 1;
    public static final int TURRET_ENCODER_PORT_2 = 2;

    public static final int TURRET_ROTATIONS_PER_TICK = 1;

  }

  public static class Shooter {

    public static final int defaultShooterRPM = 12500;
    public static final int shooterTolerance = 500;

  }
}