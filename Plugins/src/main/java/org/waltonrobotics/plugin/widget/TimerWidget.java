package org.waltonrobotics.plugin.widget;

import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;
import org.waltonrobotics.plugin.data.Timer;
import org.waltonrobotics.plugin.data.TimerType;

/**
 * Displays a timer as a 7 segment display. Uses a modified version of Clock and Digit from
 * <a href=https://github.com/shemnon/javafx-gradle/blob/master/samples/Ensemble2/src/main/java/ensemble/samples/graphics/DigitalClock.java>This
 * code by Danno Ferrin.</a>
 *
 * @author Russell Newton, Walton Robotics
 **/
@Description(name = "Timer", dataTypes = TimerType.class)
@ParametrizedController("TimerWidget.fxml")
public class TimerWidget extends SimpleAnnotatedWidget<Timer> {

  public static final Color DEFAULT_ON_COLOR = Color.ORANGERED;
  public static final Color DEFAULT_OFF_COLOR = Color.rgb(50, 50, 50);
  private static final double DEFAULT_WIDTH = 400;
  private static final double DEFAULT_HEIGHT = 200;
  private static final double DEFAULT_SPACING = 12;
  private static double percentGrowX = 1;
  private static double percentGrowY = 1;

  @FXML
  private HBox clock;

  @FXML
  private void initialize() {
    dataOrDefault.addListener((__, oldValue, newValue) -> updateDisplay(newValue));
    clock.widthProperty().addListener((__, oldWidth, newWidth) -> {
      percentGrowX = newWidth.doubleValue() / DEFAULT_WIDTH;
      clock.setSpacing(DEFAULT_SPACING * (percentGrowX * 1.6));
      updateDisplay(dataOrDefault.get());
    });
    clock.heightProperty().addListener((__, oldHeight, newHeight) -> {
      percentGrowY = newHeight.doubleValue() / DEFAULT_HEIGHT;
      updateDisplay(dataOrDefault.get());
    });
  }

  private void updateDisplay(Timer newTimer) {
    clock.getChildren().clear();

    if (Double.isNaN(newTimer.getCurrentTime())) {
      clock.getChildren().add(new Digit());
    } else {

      int seconds = (int) newTimer.getCurrentTime();

      int hours = seconds / 3600;
      seconds -= hours * 3600;

      int minutes = seconds / 60;
      seconds -= minutes * 60;

      // Add hours
      for (; hours != 0; hours /= 10) {
        clock.getChildren().add(0, new Digit(hours % 10,
            newTimer.getOnColor(), newTimer.getOffColor()));
      }

      // Add minutes
      if (clock.getChildren().size() > 0) {
        clock.getChildren().add(getColon(newTimer.getOnColor()));
        clock.getChildren().add(new Digit(minutes / 10,
            newTimer.getOnColor(), newTimer.getOffColor()));
        clock.getChildren().add(new Digit(minutes % 10,
            newTimer.getOnColor(), newTimer.getOffColor()));
      } else if (minutes != 0) {
        clock.getChildren().add(new Digit(minutes % 10,
            newTimer.getOnColor(), newTimer.getOffColor()));
        if (minutes >= 10) {
          clock.getChildren().add(0, new Digit(minutes / 10,
              newTimer.getOnColor(), newTimer.getOffColor()));
        }
      }

      // Add seconds
      if (clock.getChildren().size() > 0) {
        clock.getChildren().add(getColon(newTimer.getOnColor()));
        clock.getChildren().add(new Digit(seconds / 10,
            newTimer.getOnColor(), newTimer.getOffColor()));
        clock.getChildren().add(new Digit(seconds % 10,
            newTimer.getOnColor(), newTimer.getOffColor()));
      } else {
        clock.getChildren().add(new Digit(seconds % 10,
            newTimer.getOnColor(), newTimer.getOffColor()));
        if (seconds >= 10) {
          clock.getChildren().add(0, new Digit(seconds / 10,
              newTimer.getOnColor(), newTimer.getOffColor()));
        }
      }

      // Add partial seconds
      if (newTimer.getPrecision() > 0) {
        clock.getChildren().add(getColon(newTimer.getOnColor()));
        for (int i = 0; i < newTimer.getPrecision(); i++) {
          clock.getChildren().add(
              new Digit(((int) (newTimer.getCurrentTime() * Math.pow(10, i + 1))) % 10,
                  newTimer.getOnColor(), newTimer.getOffColor()));
        }
      }

      // Add negative
      if (newTimer.getCurrentTime() < 0) {
        clock.getChildren().add(0, new Digit());
      }
    }

  }

  private Group getColon(Color color) {
    Group colon = new Group(
        new Circle(12, 46, 6, color),
        new Circle(12, 80, 6, color)
    );
    colon.getTransforms().add(new Shear(-0.1, 0));
    colon.getTransforms().add(new Scale(percentGrowX * 0.6, percentGrowY));

    return colon;
  }

  private Group getDecimal(Color color) {
    Group decimal = new Group(new Circle(0, 94, 6, color));
    decimal.getTransforms().add(new Shear(-0.1, 0));
    decimal.getTransforms().add(new Scale(percentGrowX * 0.6, percentGrowY));

    return decimal;
  }

  @Override
  public Pane getView() {
    return clock;
  }


  /**
   * Simple 7 segment LED style digit. It supports the numbers 0 through 9.
   */
  public static class Digit extends Parent {

    private static final boolean[][] DIGIT_COMBINATIONS = new boolean[][]{
        new boolean[]{true, false, true, true, true, true, true},
        new boolean[]{false, false, false, false, true, false, true},
        new boolean[]{true, true, true, false, true, true, false},
        new boolean[]{true, true, true, false, true, false, true},
        new boolean[]{false, true, false, true, true, false, true},
        new boolean[]{true, true, true, true, false, false, true},
        new boolean[]{true, true, true, true, false, true, true},
        new boolean[]{true, false, false, false, true, false, true},
        new boolean[]{true, true, true, true, true, true, true},
        new boolean[]{true, true, false, true, true, false, true},
        new boolean[]{false, true, false, false, false, false, false}};
    private final Polygon[] polygons = new Polygon[]{
        new Polygon(2, 0, 52, 0, 42, 10, 12, 10),
        new Polygon(12, 49, 42, 49, 52, 54, 42, 59, 12f, 59f, 2f, 54f),
        new Polygon(12, 98, 42, 98, 52, 108, 2, 108),
        new Polygon(0, 2, 10, 12, 10, 47, 0, 52),
        new Polygon(44, 12, 54, 2, 54, 52, 44, 47),
        new Polygon(0, 56, 10, 61, 10, 96, 0, 106),
        new Polygon(44, 61, 54, 56, 54, 106, 44, 96)};

    private final Color onColor;
    private final Color offColor;

    public Digit(int num, Color onColor, Color offColor) {
      this.onColor = onColor;
      this.offColor = offColor;
      getChildren().addAll(polygons);
      getTransforms().add(new Shear(-0.1, 0));
      getTransforms().add(new Scale(percentGrowX * 0.6, percentGrowY));
      showNumber(num);
    }

    public Digit(Color onColor, Color offColor) {
      this(10, onColor, offColor);
    }

    public Digit() {
      this(DEFAULT_ON_COLOR, DEFAULT_OFF_COLOR);
    }

    private void showNumber(Integer num) {
      if (num < 0 || num > 10) {
        num = 10; // default to 0 for non-valid numbers
      }
      for (int i = 0; i < 7; i++) {
        polygons[i].setFill(DIGIT_COMBINATIONS[num][i] ? onColor : offColor);
      }
    }
  }
}
