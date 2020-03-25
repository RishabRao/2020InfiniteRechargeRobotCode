package org.waltonrobotics.plugin.data;

import static org.waltonrobotics.plugin.widget.TimerWidget.DEFAULT_OFF_COLOR;
import static org.waltonrobotics.plugin.widget.TimerWidget.DEFAULT_ON_COLOR;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;
import java.util.Map;
import java.util.function.Function;
import javafx.scene.paint.Color;

/**
 * @author Russell Newton
 **/
public class TimerType extends ComplexDataType<Timer> {

  public static final TimerType INSTANCE = new TimerType();
  private static final String NAME = "Timer";

  public TimerType() {
    super(NAME, Timer.class);
  }

  @Override
  public Function<Map<String, Object>, Timer> fromMap() {
    return map -> {
      double currentTime;
      try {
        currentTime = (Double) map.get("Time");
      } catch (NullPointerException e) {
        currentTime = 0;
      }

      int precision;
      try {
        precision = ((Double) map.get("Precision")).intValue();
      } catch (NullPointerException e) {
        precision = 0;
      }

      Color onColor;
      try {
        onColor = Color.web((String) map.get("On Color"));
      } catch (NullPointerException e) {
        onColor = DEFAULT_ON_COLOR;
      }

      Color offColor;
      try {
        offColor = Color.web((String) map.get("Off Color"));
      } catch (NullPointerException e) {
        offColor = DEFAULT_OFF_COLOR;
      }
      return new Timer(currentTime, precision, onColor, offColor);
    };
  }

  @Override
  public Timer getDefaultValue() {
    return new Timer(Double.NaN, 0, DEFAULT_ON_COLOR, DEFAULT_OFF_COLOR);
  }
}
