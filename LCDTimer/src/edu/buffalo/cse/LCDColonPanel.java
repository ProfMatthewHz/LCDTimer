package edu.buffalo.cse;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * Defines a panel in which a colon is drawn in the manner of the digital clocks from the 1980s.
 *
 * @author Matthew Hertz
 */
public class LCDColonPanel extends JPanel {
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

  /** Create a new panel using the default display. */
  public LCDColonPanel() {
    super();
    drawVisible = true;
    setBackground(LCDAppColors.BACKGROUND_COLOR);
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

  /** Method in which the current LCD digit is drawn. */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (drawVisible) {
      Graphics2D g2 = (Graphics2D) g;
      g2.setColor(LCDAppColors.ON_COLOR);
      int[] diamondX = new int[4];
      int[] diamondY = new int[4];
      diamondX[0] = usableWidth / 2;
      diamondX[1] = (usableWidth * 3) / 4;
      diamondX[2] = diamondX[0];
      diamondX[3] = usableWidth / 4;
      diamondY[0] = ((usableHeight * 3) / 16) + paddingHeight;
      diamondY[1] = (usableHeight / 4) + paddingHeight;
      diamondY[2] = ((usableHeight * 5) / 16) + paddingHeight;
      diamondY[3] = diamondY[1];
      g2.fillPolygon(diamondX, diamondY, 4);

      diamondY[0] = ((usableHeight * 11) / 16) + paddingHeight;
      diamondY[1] = ((usableHeight * 6) / 8)+ paddingHeight;
      diamondY[2] = ((usableHeight * 13) / 16)+ paddingHeight;
      diamondY[3] = diamondY[1];
      g2.fillPolygon(diamondX, diamondY, 4);
    }
  }

  public void active(boolean isActive) {
    drawVisible = isActive;
  }
}
