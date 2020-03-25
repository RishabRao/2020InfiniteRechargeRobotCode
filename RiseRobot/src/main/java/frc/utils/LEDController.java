package frc.utils;


import static frc.robot.Constants.DioIDs.LED1_ID;
import static frc.robot.Constants.DioIDs.LED2_ID;

import edu.wpi.first.wpilibj.DigitalOutput;

public final class LEDController {

  private static DigitalOutput LED1 = new DigitalOutput(LED1_ID);
  private static DigitalOutput LED2 = new DigitalOutput(LED2_ID);

  public static void setLEDClimbingMode() {
    LED1.set(true);
    LED2.set(true);
  }

  public static void setLEDTurnRightMode() {
    LED1.set(false);
    LED2.set(true);
  }

  public static void setLEDTurnLeftMode() {
    LED1.set(true);
    LED2.set(false);
  }

  public static void setLEDPassiveMode() {
    LED1.set(false);
    LED2.set(false);
  }
}
