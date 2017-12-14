package edu.buffalo.cse;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * Defines a panel in which a single digit is drawn in the manner of the digital clocks from the 1980s.
 *
 * @author Matthew Hertz
 */
public class LCDDigitPanel extends JPanel {

  /**
   * Valid digits to be coded by our LCDPanel
   *
   * @author Matthew Hertz
   */
  public enum LCDDigit {
    ZERO(true, true, true, false, true, true, true), ONE(false, false, true, false, false, true, false),
    TWO(true, false, true, true, true, false, true), THREE(true, false, true, true, false, true, true),
    FOUR(false, true, true, true, false, true, false), FIVE(true, true, false, true, false, true, true),
    SIX(false, true, false, true, true, true, true), SEVEN(true, false, true, false, false, true, false),
    EIGHT(true, true, true, true, true, true, true), NINE(true, true, true, true, false, true, false);
    /**
     * The display works by turning on or off each of 7 different sections. Sections are numbered from top-to-bottom and
     * left-to-right.
     */
    private boolean[] sectionUsed;

    /**
     * Creates a new digit in which each of the seven sections are turned on (true) or off (false). This will be used to
     * actually draw the representation of our digit.
     *
     * @param section0 True when the top-most section should be "on"
     * @param section1 True when the section just down and to the left of section 0 should be "on"
     * @param section2 True when the section just down and to the right of section 0 should be "on"
     * @param section3 True when the center section of the panel should be "on"
     * @param section4 True when the section just down and to the left of section 3 should be "on"
     * @param section5 True when the section just down and to the right of section 3 should be "on"
     * @param section6 True when the bottom-most section should be "on" should be "on"
     */
    private LCDDigit(boolean section0, boolean section1, boolean section2, boolean section3, boolean section4,
                     boolean section5, boolean section6) {
      sectionUsed = new boolean[7];
      sectionUsed[0] = section0;
      sectionUsed[1] = section1;
      sectionUsed[2] = section2;
      sectionUsed[3] = section3;
      sectionUsed[4] = section4;
      sectionUsed[5] = section5;
      sectionUsed[6] = section6;
    }

    /**
     * Returns if a section of the display should be considered "on". When the section is to be seen, then this returns
     * true; otherwise false is returned.<br/>
     * Precondition: Only sections 0 - 6 are valid. The caller must be certain the value of {@code section} is between 0
     * and 6
     *
     * @param section Number of the section which we want to know if it is "on"
     * @return True when the section is "on"; false if it is "off".
     */
    public boolean isSectionOn(int section) {
      return sectionUsed[section];
    }
  }

  /**
   * The digit to display. The enum limits jokesters from screwing this up.
   */
  private LCDDigit digit;

  /**
   * To keep the effect of an LCD screen, we may want to keep the panel's size, but not have it display anything.
   * Towards this end, this field determines if any drawing should occur.
   */
  private boolean drawVisible;

  /** Usable width of the panel; used to draw the segments */
  private int usableWidth;

  /** Usable height of the panel; used to draw the segments */
  private int usableHeight;

  /** Padding above and below the digit to keep the window looking good */
  private int paddingHeight;

  /** Create a new panel using the default digit to be displayed. */
  public LCDDigitPanel() {
    super();
    // What happened when the power went out? The clock reset to all 8s
    digit = LCDDigit.EIGHT;
    setBackground(LCDAppColors.BACKGROUND_COLOR);
    drawVisible = true;

  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);
    usableWidth = width;
    if (((width * 2) + (width / 2)) < height) {
      usableHeight = (width * 2) + (width / 2);
      paddingHeight = (height - usableHeight) / 2;
    } else {
      usableHeight = height;
      paddingHeight = 0;
    }
  }

  /**
   * Create a new panel in which the specified digit will be displayed.
   *
   * @param dig Digit to display in this panel
   */
  public LCDDigitPanel(LCDDigit dig) {
    this();
    digit = dig;
  }

  public int getDigit() {
    return digit.ordinal();
  }

  /**
   * Update the display to show the new digit.
   *
   * @param dig Digit to be displayed. If this is null, the system resets to displaying an 8.
   */
  public void setDigit(LCDDigit dig) {
    if (dig == null) {
      digit = LCDDigit.EIGHT;
    } else {
      digit = dig;
    }
  }

  /** Method in which the current LCD digit is drawn. */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (drawVisible) {
      Graphics2D g2 = (Graphics2D) g;
      if (digit.isSectionOn(0)) {
        g2.setColor(LCDAppColors.ON_COLOR);
      } else {
        g2.setColor(LCDAppColors.OFF_COLOR);
      }
      int eighthWidth = (usableWidth / 8);
      int[] trapezoidX = new int[4];
      int[] trapezoidY = new int[4];
      trapezoidX[0] = 0;
      trapezoidX[1] = usableWidth;
      trapezoidX[2] = usableWidth - eighthWidth;
      trapezoidX[3] = eighthWidth;
      trapezoidY[0] = paddingHeight;
      trapezoidY[1] = trapezoidY[0];
      trapezoidY[2] = (usableHeight / 19) + paddingHeight;
      trapezoidY[3] = trapezoidY[2];
      g2.fillPolygon(trapezoidX, trapezoidY, 4);

      if (digit.isSectionOn(6)) {
        g2.setColor(LCDAppColors.ON_COLOR);
      } else {
        g2.setColor(LCDAppColors.OFF_COLOR);
      }
      trapezoidY[0] = usableHeight + paddingHeight;
      trapezoidY[1] = trapezoidY[0];
      trapezoidY[2] = ((usableHeight * 18) / 19) + paddingHeight;
      trapezoidY[3] = trapezoidY[2];
      g2.fillPolygon(trapezoidX, trapezoidY, 4);

      if (digit.isSectionOn(1)) {
        g2.setColor(LCDAppColors.ON_COLOR);
      } else {
        g2.setColor(LCDAppColors.OFF_COLOR);
      }
      trapezoidX[0] = 0;
      trapezoidX[1] = eighthWidth;
      trapezoidX[2] = trapezoidX[1];
      trapezoidX[3] = trapezoidX[0];
      trapezoidY[0] = (usableHeight / 38) + paddingHeight;
      trapezoidY[1] = ((usableHeight * 3) / 38) + paddingHeight;
      trapezoidY[2] = ((usableHeight * 17) / 38) + paddingHeight;
      trapezoidY[3] = ((usableHeight * 18) / 38) + paddingHeight;
      g2.fillPolygon(trapezoidX, trapezoidY, 4);

      if (digit.isSectionOn(2)) {
        g2.setColor(LCDAppColors.ON_COLOR);
      } else {
        g2.setColor(LCDAppColors.OFF_COLOR);
      }
      trapezoidX[0] = usableWidth;
      trapezoidX[1] = usableWidth - eighthWidth;
      trapezoidX[2] = trapezoidX[1];
      trapezoidX[3] = trapezoidX[0];
      g2.fillPolygon(trapezoidX, trapezoidY, 4);

      if (digit.isSectionOn(4)) {
        g2.setColor(LCDAppColors.ON_COLOR);
      } else {
        g2.setColor(LCDAppColors.OFF_COLOR);
      }
      trapezoidX[0] = 0;
      trapezoidX[1] = eighthWidth;
      trapezoidX[2] = trapezoidX[1];
      trapezoidX[3] = trapezoidX[0];
      trapezoidY[0] = ((usableHeight * 20) / 38) + paddingHeight;
      trapezoidY[1] = ((usableHeight * 21) / 38) + paddingHeight;
      trapezoidY[2] = ((usableHeight * 35) / 38) + paddingHeight;
      trapezoidY[3] = ((usableHeight * 37) / 38) + paddingHeight;
      g2.fillPolygon(trapezoidX, trapezoidY, 4);

      if (digit.isSectionOn(5)) {
        g2.setColor(LCDAppColors.ON_COLOR);
      } else {
        g2.setColor(LCDAppColors.OFF_COLOR);
      }
      trapezoidX[0] = usableWidth;
      trapezoidX[1] = usableWidth - eighthWidth;
      trapezoidX[2] = trapezoidX[1];
      trapezoidX[3] = trapezoidX[0];
      g2.fillPolygon(trapezoidX, trapezoidY, 4);

      if (digit.isSectionOn(3)) {
        g2.setColor(LCDAppColors.ON_COLOR);
      } else {
        g2.setColor(LCDAppColors.OFF_COLOR);
      }
      int[] hexagonX = new int[6];
      int[] hexagonY = new int[6];
      hexagonX[0] = 0;
      hexagonX[1] = eighthWidth;
      hexagonX[2] = usableWidth - eighthWidth;
      hexagonX[3] = usableWidth;
      hexagonX[4] = usableWidth - eighthWidth;
      hexagonX[5] = eighthWidth;
      hexagonY[0] = ((usableHeight * 19) / 38) + paddingHeight;
      hexagonY[1] = ((usableHeight * 18) / 38) + paddingHeight;
      hexagonY[2] = hexagonY[1];
      hexagonY[3] = hexagonY[0];
      hexagonY[4] = ((usableHeight * 20) / 38) + paddingHeight;
      hexagonY[5] = hexagonY[4];
      g2.fillPolygon(hexagonX, hexagonY, 6);
    }
  }

  public void active(boolean isActive) {
    drawVisible = isActive;
  }
}
