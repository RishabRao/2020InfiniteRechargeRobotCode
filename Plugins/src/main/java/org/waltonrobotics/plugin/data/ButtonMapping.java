package org.waltonrobotics.plugin.data;

import edu.wpi.first.shuffleboard.api.data.ComplexData;
import java.util.Map;

/**
 * @author Russell Newton, Walton Robotics
 **/
public class ButtonMapping extends ComplexData<ButtonMapping> {

  private final int joystick;
  private final int index;

  public ButtonMapping(int joystick, int index) {
    this.joystick = joystick;
    this.index = index;
  }

  public int getJoystick() {
    return joystick;
  }

  public int getIndex() {
    return index;
  }

  public ButtonMapping withJoystick(int joystick) {
    return new ButtonMapping(joystick, index);
  }

  public ButtonMapping withIndex(int index) {
    return new ButtonMapping(joystick, index);
  }

  @Override
  public Map<String, Object> asMap() {
    return Map.of(
        "Joystick", joystick,
        "Index", index
        );
  }

  @Override
  public boolean equals(Object obj) {
    if(obj instanceof  ButtonMapping) {
      return ((ButtonMapping) obj).joystick == joystick && ((ButtonMapping) obj).index == index;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return (17 * 31 + index + 31) + joystick;
  }

  @Override
  public String toHumanReadableString() {
    return String.format("ButtonMapping[Joystick=%d, Index=%d]", joystick, index);
  }
}
