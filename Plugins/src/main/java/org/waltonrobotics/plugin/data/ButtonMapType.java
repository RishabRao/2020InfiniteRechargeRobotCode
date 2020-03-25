package org.waltonrobotics.plugin.data;

import static org.waltonrobotics.plugin.data.ButtonMap.subTableName;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Although the NetworkTable appears like this on Shuffleboard, this isn't how the data is sent to
 * be deserialized.
 * <pre>
 * /table
 *   Button Map
 *   │  .type = ButtonMap
 *   │  Mappings
 *      │   [BUTTON NAME]
 *      │   │   .type = ButtonMapping
 *      │   │   Joystick = -1
 *      │   │   Index = -1
 *      │
 *      │   [BUTTON NAME]
 *      │   │   .type = ButtonMapping
 *      │   │   Joystick = -1
 *      │   │   Index = -1
 *      │   etc...
 * </pre>
 * Instead, it looks like this:
 * <pre>
 * /table
 *   Button Map
 *   │  .type = ButtonMap
 *   │
 *   │  Mappings/[BUTTON NAME]/.type = ButtonMapping
 *   │  Mappings/[BUTTON NAME]/Joystick = -1
 *   │  Mappings/[BUTTON NAME]/Index = -1
 *   │
 *   │  Mappings/[BUTTON NAME]/.type = ButtonMapping
 *   │  Mappings/[BUTTON NAME]/Joystick = -1
 *   │  Mappings/[BUTTON NAME]/Index = -1
 *   │  etc...
 * </pre>
 * So deserializing is complicated...
 *
 * @author Russell Newton, Walton Robotics
 * @see ButtonMapType#fromMap()
 **/
public class ButtonMapType extends ComplexDataType<ButtonMap> {

  public static final ButtonMapType INSTANCE = new ButtonMapType();
  private static final String NAME = "ButtonMap";

  public ButtonMapType() {
    super(NAME, ButtonMap.class);
  }

  public static ButtonMap deserializeButtonMap(Map<String, Object> map, String subTableName) {
    /*
    Let's follow a mapping from NT to ButtonMapping.
    We start with:
        │   Mappings/[BUTTON NAME]/.type = ButtonMapping
        │   Mappings/[BUTTON Name]/Joystick = -1
        │   Mappings/[BUTTON Name]/Index = -1
     */


      /*
      This step simplifies the mapping down to:
        │   [BUTTON Name]/Joystick = -1
        │   [BUTTON Name]/Index = -1

      But if multiple mappings are on the NT, they may not be in this order and may be separated by
      other mappings' data.
       */
    List<Entry<String, Object>> unpairedMappings =
        map.entrySet().stream().filter(n -> n.getKey().startsWith(subTableName)).map(n ->
            new Map.Entry<String, Object>() {
              @Override
              public String getKey() {
                return n.getKey().substring(subTableName.length() + 1);
              }

              @Override
              public Object getValue() {
                return n.getValue();
              }

              @Override
              public Object setValue(Object value) {
                return null;
              }
            }).collect(Collectors.toList());

      /*
      This step pairs up the data into maps that look like:
        [BUTTON NAME] = {
            "Joystick" = -1,
            "Index" = -1
        }
       */
    HashMap<String, Map<String, Object>> pairedMappings = new HashMap<>();
    unpairedMappings.forEach(n -> {
      int lastSlash = n.getKey().lastIndexOf("/");
      String buttonName = n.getKey().substring(0, lastSlash);
      if (n.getKey().substring(lastSlash + 1).equals("Joystick") ||
          n.getKey().substring(lastSlash + 1).equals("Index")) {
        if (!pairedMappings.containsKey(buttonName)) {
          HashMap<String, Object> tempMap = new HashMap<>();
          tempMap.put(n.getKey().substring(lastSlash + 1), n.getValue());
          pairedMappings.put(buttonName, tempMap);
        } else {
          pairedMappings.get(buttonName).put(n.getKey().substring(lastSlash + 1), n.getValue());
        }
      }
    });

    HashMap<String, ButtonMapping> finalMappings = new HashMap<>();

      /*
      I filter out any mapping pairs that do not have two entries, just in case.
       */
    pairedMappings.entrySet().stream().filter(n -> n.getValue().size() == 2).forEach(n -> {
      Map<String, Object> values = n.getValue();
      int joystick = ((Double) values.get("Joystick")).intValue();
      int index = ((Double) values.get("Index")).intValue();
      finalMappings.put(n.getKey(), new ButtonMapping(joystick, index));
    });

    return new ButtonMap(finalMappings);
  }

  /**
   * This is magic code. It works if the data is properly sent to NetworkTables by WaltonRobotics's
   * DynamicButtonMap. Entries are gathered up as the key-value pairs from the "Button Map" entry in
   * the NT.
   * <p>The first step to deserializing is to filter out entries that don't start with "Mappings".
   * Then, the keys are trimmed to not include "Mappings/".
   * <p>The second step is to sort the entries into pairs based on their corresponding button name.
   * Entries are added to a map of maps, keyed by the button's name and valued by pairs of Joystick
   * and Index entries.
   * <p>Each of these pairs are then converted into ButtonMappings and added to the ButtonMap.
   * <p>The source code has been loosely commented. If your goal is to figure out how it works,
   * good luck.
   *
   * @see ButtonMapType
   */
  @Override
  public Function<Map<String, Object>, ButtonMap> fromMap() {
    return map -> {
      ButtonMap.setDefaultMappings(deserializeButtonMap(map, "Default " + subTableName));
      return deserializeButtonMap(map, subTableName);
    };
  }

  @Override
  public ButtonMap getDefaultValue() {
    return new ButtonMap(Stream.of(new Object[][]{
        {"", new int[]{-1, -1}}
    }).collect(Collectors
        .toMap(n -> (String) n[0], n -> new ButtonMapping(((int[]) n[1])[0], ((int[]) n[1])[1]))));
  }
}
