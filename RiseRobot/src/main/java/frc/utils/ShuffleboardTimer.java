package frc.utils;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import java.util.function.Supplier;

/**
 * A ShuffleboardTimer can be sent to NetworkTables so that it can be displayed on Shuffleboard with
 * our Timer widget.
 *
 **/
public class ShuffleboardTimer {

  private final NetworkTable table;
  public final Supplier<Double> timeSupplier;
  public int precision;
  public String onColor;
  public String offColor;

  /**
   * @param precision how many points after the decimal to keep track of.
   * @param onColor a hex string representation ("0x0000FF" is blue), which can be generated with
   * {@link ShuffleboardTimer#colorHexFromRGB(int, int, int)}.
   * @param offColor a hex string representation ("0x0000FF" is blue)
   * @see ShuffleboardTimer#colorHexFromRGB(int, int, int).
   */
  public ShuffleboardTimer(String name, Supplier<Double> timeSupplier, int precision,
      String onColor, String offColor) {
    this.timeSupplier = timeSupplier;
    this.precision = precision;
    this.onColor = onColor;
    this.offColor = offColor;

    table = NetworkTableInstance.getDefault().getTable(name);
    sendToShuffleboard();
  }

  public ShuffleboardTimer(String name, Supplier<Double> timeSupplier, int precision) {
    this(name, timeSupplier, precision, null, null);
  }

  public static String colorHexFromRGB(int r, int g, int b) {
    return String.format("0x%02x%02x%02x", r, g, b);
  }

  public void sendToShuffleboard() {
    table.getEntry(".type").setString("Timer");
    table.getEntry("Current Time").setDouble(timeSupplier.get());
    table.getEntry("Precision").setNumber(precision);
    if(onColor != null) {
      table.getEntry("On Color").setString(onColor);
    } else {
      table.delete("On Color");
    }
    if(offColor != null) {
      table.getEntry("Off Color").setString(offColor);
    } else {
      table.delete("Off Color");
    }
  }
}
