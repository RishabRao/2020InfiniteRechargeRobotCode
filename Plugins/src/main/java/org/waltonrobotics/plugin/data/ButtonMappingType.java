package org.waltonrobotics.plugin.data;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;
import edu.wpi.first.shuffleboard.api.util.Maps;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Russell Newton, Walton Robotics
 **/
public class ButtonMappingType extends ComplexDataType<ButtonMapping> {

  private static final String NAME = "ButtonMapping";
  public static final ButtonMappingType INSTANCE = new ButtonMappingType();

  public ButtonMappingType() {
    super(NAME, ButtonMapping.class);
  }

  @Override
  public Function<Map<String, Object>, ButtonMapping> fromMap() {
    return map -> new ButtonMapping(
        Maps.getOrDefault(map, "Joystick", -1),
        Maps.getOrDefault(map, "Index", -1)
    );
  }

  @Override
  public ButtonMapping getDefaultValue() {
    return new ButtonMapping(-1, -1);
  }
}
