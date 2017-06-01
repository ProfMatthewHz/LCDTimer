package edu.buffalo.cse;

import java.awt.Color;

/**
 * Interface holding the constants with which we draw the digits and timer bar.
 *
 * @author Matthew Hertz
 */
public interface LCDAppColors {
  /** Color used to draw the sections of the LCD characters which are "on". */
  public static final Color ON_COLOR = new Color(36, 242, 36);
  /** Color used to draw the sections of the LCD characters which are "off". */
  public static final Color OFF_COLOR = new Color(36, 242, 26, 10);
  /** Color used for the background of the app. */
  public static final Color BACKGROUND_COLOR = Color.DARK_GRAY;
  /** Color used to draw the countdown timer off to the side of the app screen. */
  public static final Color TIMER_COLOR = ON_COLOR;

}
