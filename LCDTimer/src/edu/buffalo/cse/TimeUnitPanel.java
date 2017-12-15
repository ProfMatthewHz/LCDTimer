package edu.buffalo.cse;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import edu.buffalo.cse.LCDDigitPanel.LCDDigit;

@SuppressWarnings("serial")
public class TimeUnitPanel extends JPanel {

  private LCDDigitPanel tensDigit;
  private LCDDigitPanel onesDigit;
  private LCDColonPanel colon;

  /**
   * Create the panel.
   *
   * @param showColon True if the panel should include a colon; false if the colon should be omitted
   */
  public TimeUnitPanel(boolean showColon) {
    setLayout(new GridBagLayout());
    double weightDigit;
    int currentColumn;
    if (showColon) {
      colon = new LCDColonPanel();
      GridBagConstraints gbcColon = new GridBagConstraints();
      gbcColon.insets = new Insets(10, 0, 10, 0);
      gbcColon.fill = GridBagConstraints.BOTH;
      gbcColon.gridx = 0;
      gbcColon.weightx = 0.2;
      gbcColon.weighty = 1.0;
      add(colon, gbcColon);
      weightDigit = 0.4;
      currentColumn = 1;
    } else {
      weightDigit = 0.5;
      currentColumn = 0;
    }

    tensDigit = new LCDDigitPanel();
    GridBagConstraints gbcTensDigit = new GridBagConstraints();
    gbcTensDigit.fill = GridBagConstraints.BOTH;
    if (showColon) {
      gbcTensDigit.insets = new Insets(10, 10, 10, 0);
    } else {
      gbcTensDigit.insets = new Insets(10, 0, 10, 0);
    }
    gbcTensDigit.gridx = currentColumn;
    gbcTensDigit.weightx = weightDigit;
    gbcTensDigit.weighty = 1.0;
    add(tensDigit, gbcTensDigit);
    currentColumn += 1;

    onesDigit = new LCDDigitPanel();
    GridBagConstraints gbcOnesDigit = new GridBagConstraints();
    gbcOnesDigit.fill = GridBagConstraints.BOTH;
    gbcOnesDigit.insets = new Insets(10, 10, 10, 0);
    gbcOnesDigit.gridx = currentColumn;
    gbcOnesDigit.weightx = weightDigit;
    gbcOnesDigit.weighty = 1.0;
    add(onesDigit, gbcOnesDigit);
  }

  public void updateTime(int newTime) {
    int tens = newTime / 10;
    int ones = newTime % 10;
    setTensDigit(LCDDigit.values()[tens]);
    setOnesDigit(LCDDigit.values()[ones]);
  }

  public int getTime() {
    int tens = tensDigit.getDigit() * 10;
    int ones = onesDigit.getDigit();
    return tens + ones;
  }

  private void setTensDigit(LCDDigit dig) {
    tensDigit.setDigit(dig);
    tensDigit.repaint();
  }

  private void setOnesDigit(LCDDigit dig) {
    onesDigit.setDigit(dig);
    onesDigit.repaint();
  }

  public void active(boolean isActive) {
    tensDigit.active(isActive);
    onesDigit.active(isActive);
    if (colon != null) {
      colon.active(isActive);
    }
  }
}
