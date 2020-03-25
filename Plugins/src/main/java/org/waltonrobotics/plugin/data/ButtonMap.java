package org.waltonrobotics.plugin.data;

import com.google.common.collect.ImmutableMap;
import edu.wpi.first.shuffleboard.api.data.ComplexData;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Russell Newton, Walton Robotics
 **/
public class ButtonMap extends ComplexData<ButtonMap> {

  public static final String subTableName = "Mappings";
  public static ButtonMap defaultMappings;
  private final ImmutableMap<String, ButtonMapping> mappings;

  public ButtonMap(Map<String, ButtonMapping> mappings) {
    this.mappings = ImmutableMap.copyOf(mappings);
  }

  public ImmutableMap<String, ButtonMapping> getMappings() {
    return mappings;
  }

  public ButtonMap addMapping(String key, ButtonMapping mapping) {
    Map<String, ButtonMapping> newMappings = new HashMap<>(mappings);
    newMappings.put(key, mapping);
    return new ButtonMap(newMappings);
  }

  public ButtonMap removeMapping(String key) {
    if (mappings.containsKey(key)) {
      Map<String, ButtonMapping> newMappings = new HashMap<>(mappings);
      newMappings.remove(key);
      return new ButtonMap(newMappings);
    }
    return this;
  }

  public static void setDefaultMappings(ButtonMap map) {
    defaultMappings = map;
  }

  /**
   * The map needs to be sent back to NetworkTables in the same way that it was received.
   *
   * @see ButtonMapType
   */
  @Override
  public Map<String, Object> asMap() {
    HashMap<String, Object> map = new HashMap<>();
    mappings.forEach((k, v) -> {
      map.put(String.format("%s/%s/%s", subTableName, k, "Joystick"), (double) v.getJoystick());
      map.put(String.format("%s/%s/%s", subTableName, k, "Index"), (double) v.getIndex());
    });
    return map;
  }

  @Override
  public int hashCode() {
    return mappings.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ButtonMap) {
      return ((ButtonMap) obj).mappings.equals(mappings);
    }
    return false;
  }

  @Override
  public String toHumanReadableString() {
    return String
        .format("ButtonMap[%s]", mappings.toString());
  }
}
