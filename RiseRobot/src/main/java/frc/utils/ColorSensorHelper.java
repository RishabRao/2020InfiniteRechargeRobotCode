package frc.utils;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

public class ColorSensorHelper {

  public static final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
  public static final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
  public static final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
  public static final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

  private static final ColorSensorV3 colorSensor = new ColorSensorV3(Port.kOnboard);
  private static final ColorMatch colorMatcher = loadColorMatcher();
  private static final int targetDistanceLimit = 200;

  public enum ControlPanelColor {
    RED, GREEN, BLUE, YELLOW, NONE;   // In CW order

    public boolean moveCW(ControlPanelColor targetColor) {
      int comparison = this.compareTo(targetColor);
      if(comparison < 0) {
        if(Math.abs(comparison) < 2) {
          return true;
        }
        return false;
      }
      if(Math.abs(comparison) > 2) {
        return true;
      }
      return false;
    }
  }

  public static ControlPanelColor getColorMatch() {
    Color detectedColor = colorSensor.getColor();
    ColorMatchResult match = colorMatcher.matchClosestColor(detectedColor);
    if (getProximity() > targetDistanceLimit) {
      if (match.color.equals(kRedTarget)) {
        return ControlPanelColor.RED;
      } else if (match.color.equals(kYellowTarget)) {
        return ControlPanelColor.YELLOW;
      } else if (match.color.equals(kGreenTarget)) {
        return ControlPanelColor.GREEN;
      } else if (match.color.equals(kBlueTarget)) {
        return ControlPanelColor.BLUE;
      }
    }
    return ControlPanelColor.NONE;
  }

  private static int getProximity(){
    return colorSensor.getProximity();
  }

  private static ColorMatch loadColorMatcher() {
    ColorMatch colorMatcher = new ColorMatch();
    colorMatcher.addColorMatch(kBlueTarget);
    colorMatcher.addColorMatch(kGreenTarget);
    colorMatcher.addColorMatch(kRedTarget);
    colorMatcher.addColorMatch(kYellowTarget);
    return colorMatcher;
  }
}
