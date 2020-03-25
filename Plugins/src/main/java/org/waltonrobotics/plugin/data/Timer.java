package org.waltonrobotics.plugin.data;

import edu.wpi.first.shuffleboard.api.data.ComplexData;
import java.util.Map;
import javafx.scene.paint.Color;

/**
 * @author Russell Newton
 **/
public class Timer extends ComplexData<Timer> {

  private final Double currentTime;
  private final Integer precision;
  private final Color onColor;
  private final Color offColor;

  public Timer(double currentTime, int precision, Color onColor, Color offColor) {
    this.currentTime = currentTime;
    this.precision = precision;
    this.onColor = onColor;
    this.offColor = offColor;
  }

  public double getCurrentTime() {
    return currentTime;
  }

  public int getPrecision() {
    return precision;
  }

  public Color getOnColor() {
    return onColor;
  }

  public Color getOffColor() {
    return offColor;
  }

  @Override
  public Map<String, Object> asMap() {
    return Map.of(
        "Current Time", currentTime,
        "Precision", precision,
        "On Color", onColor.toString(),
        "Off Color", offColor.toString()
    );
  }
}
