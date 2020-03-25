package org.waltonrobotics.plugin;

import edu.wpi.first.shuffleboard.api.data.DataType;
import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.widget.WidgetType;
import java.util.List;
import java.util.Map;
import org.waltonrobotics.plugin.data.ButtonMapType;
import org.waltonrobotics.plugin.data.ButtonMappingType;
import org.waltonrobotics.plugin.data.TimerType;
import org.waltonrobotics.plugin.widget.ButtonMapWidget;
import org.waltonrobotics.plugin.widget.TimerWidget;

/**
 * If you want to install this plugin, either use the "install" Gradle task on the source, or get
 * the .jar file from the build/libs/ folder.
 *
 * @author Russell Newton
 **/
@Description(
    group = "org.waltonrobotics",
    name = "WRTInfiniteRecharge",
    version = "1.0.2",
    summary = "This plugin provides for widgets and themes used by WaltonRobotics in FRC Infinite Recharge."
)
public class WRTDeepSpacePlugin extends Plugin {

//  TODO this is broken for the current version of Shuffleboard
//  private final Theme wrtTheme =
//      new Theme(WRTDeepSpacePlugin.class, "WRT Theme", "wrt-theme.css");
//
//  @Override
//  public List<Theme> getThemes() {
//    return List.of(wrtTheme);
//  }

  @Override
  public List<DataType> getDataTypes() {
    return List.of(
        ButtonMapType.INSTANCE,
        ButtonMappingType.INSTANCE,
        TimerType.INSTANCE
    );
  }

  @Override
  public Map<DataType, ComponentType> getDefaultComponents() {
    return Map.of(
        ButtonMapType.INSTANCE, WidgetType.forAnnotatedWidget(ButtonMapWidget.class),
        TimerType.INSTANCE, WidgetType.forAnnotatedWidget(TimerWidget.class)
    );
  }

  @Override
  public List<ComponentType> getComponents() {
    return List.of(
        WidgetType.forAnnotatedWidget(ButtonMapWidget.class),
        WidgetType.forAnnotatedWidget(TimerWidget.class)
    );
  }
}
