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

  /** Create a new panel using the default display. */
  public LCDColonPanel() {
    super();
    drawVisible = true;
    setBackground(LCDAppColors.BACKGROUND_COLOR);
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
      int panelWidth = getWidth();
      int panelHeight = getHeight();
      diamondX[0] = panelWidth / 2;
      diamondX[1] = (panelWidth * 3) / 4;
      diamondX[2] = diamondX[0];
      diamondX[3] = panelWidth / 4;
      diamondY[0] = (panelHeight * 3) / 16;
      diamondY[1] = panelHeight / 4;
      diamondY[2] = (panelHeight * 5) / 16;
      diamondY[3] = diamondY[1];
      g2.fillPolygon(diamondX, diamondY, 4);

      diamondY[0] = (panelHeight * 11) / 16;
      diamondY[1] = (panelHeight * 6) / 8;
      diamondY[2] = (panelHeight * 13) / 16;
      diamondY[3] = diamondY[1];
      g2.fillPolygon(diamondX, diamondY, 4);
    }
  }

  public void active(boolean isActive) {
    drawVisible = isActive;
  }
}
