package edu.buffalo.cse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class InitializationPanel extends JDialog {

  private final class TimeSpinnerListener implements ChangeListener {
    private final JSpinner mySpinner;
    private final int myLocation;

    public TimeSpinnerListener(JSpinner timeSpinner, int loc) {
      mySpinner = timeSpinner;
      myLocation = loc;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
      Date updatedTime = (Date) mySpinner.getValue();
      long timeOfDay = updatedTime.getTime() % (24 * 60 * 60 * 1000);
      for (int i = myLocation + 1; i < endTimes.size(); i++ ) {
        JSpinner nextSpinner = endTimes.get(i);
        Date nextTime = (Date) nextSpinner.getValue();
        long nextTimeOfDay = nextTime.getTime() % (24 * 60 * 60 * 1000);
        if (timeOfDay < nextTimeOfDay) {
          break;
        }
        JSpinner nextUpdate = updateTimes.get(i);
        int countdownTime = (int) nextUpdate.getValue();
        nextTime = new Date(updatedTime.getTime() + countdownTime);
        nextSpinner.setValue(nextTime);
        updatedTime = nextTime;
      }
    }
  }

  private final JPanel contentPanel = new JPanel();
  private final List<JSpinner> updateTimes = new LinkedList<>();
  private final List<JSpinner> endTimes = new LinkedList<>();
  private final List<JCheckBox> showSeconds = new LinkedList<>();
  private final List<JLabel> updateLabels = new LinkedList<>();
  private final List<JLabel> endLabels = new LinkedList<>();

  /**
   * Launch the application.
   *
   * @param args Command-line argument that will be ignored
   */
  public static void main(String[] args) {
    try {
      InitializationPanel dialog = new InitializationPanel();
      dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      dialog.pack();
      dialog.setVisible(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Create the dialog.
   */
  public InitializationPanel() {
    getContentPane().setLayout(new BorderLayout());
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(contentPanel, BorderLayout.CENTER);
    GridBagLayout gblContentPanel = new GridBagLayout();
    gblContentPanel.rowWeights = new double[] { 0.0, 0.0 };
    gblContentPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
    contentPanel.setLayout(gblContentPanel);
    addTimerRow();
    {
      JLabel lblRemoveLast = new JLabel("Remove Last...");
      lblRemoveLast.setForeground(Color.BLUE);
      GridBagConstraints gbcLblRemoveLast = new GridBagConstraints();

      JLabel lblAddAnother = new JLabel("Add Another...");
      lblAddAnother.setForeground(Color.BLUE);
      GridBagConstraints gbcLblAddAnother = new GridBagConstraints();

      gbcLblRemoveLast.anchor = GridBagConstraints.ABOVE_BASELINE;
      gbcLblRemoveLast.insets = new Insets(10, 0, 0, 5);
      gbcLblRemoveLast.gridx = 0;
      gbcLblRemoveLast.gridy = 1;
      gbcLblRemoveLast.gridwidth = 2;
      contentPanel.add(lblRemoveLast, gbcLblRemoveLast);
      lblRemoveLast.addMouseListener(new MouseAdapter() {
        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void mouseExited(MouseEvent e) {
          // Change the font back to its original state.
          Font currentFont = lblRemoveLast.getFont();
          Map fontAttributes = currentFont.getAttributes();
          fontAttributes.put(TextAttribute.UNDERLINE, -1);
          lblRemoveLast.setFont(lblRemoveLast.getFont().deriveFont(fontAttributes));
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void mouseEntered(MouseEvent e) {
          Font currentFont = lblRemoveLast.getFont();
          Map fontAttributes = currentFont.getAttributes();
          fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
          lblRemoveLast.setFont(lblRemoveLast.getFont().deriveFont(fontAttributes));
        }

        @Override
        public void mouseClicked(MouseEvent e) {
          removeTimerRow();
          gbcLblRemoveLast.gridy = updateTimes.size();
          gbcLblAddAnother.gridy = updateTimes.size();
          contentPanel.add(lblRemoveLast, gbcLblRemoveLast);
          contentPanel.add(lblAddAnother, gbcLblAddAnother);
          if (updateTimes.size() == 1) {
            lblRemoveLast.setVisible(false);
          }
          pack();
        }
      });
      lblRemoveLast.setVisible(false);

      gbcLblAddAnother.anchor = GridBagConstraints.ABOVE_BASELINE;
      gbcLblAddAnother.insets = new Insets(10, 0, 0, 5);
      gbcLblAddAnother.gridx = 4;
      gbcLblAddAnother.gridy = 1;
      gbcLblAddAnother.gridwidth = 2;
      contentPanel.add(lblAddAnother, gbcLblAddAnother);
      lblAddAnother.addMouseListener(new MouseAdapter() {
        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void mouseExited(MouseEvent e) {
          // Change the font back to its original state.
          Font currentFont = lblAddAnother.getFont();
          Map fontAttributes = currentFont.getAttributes();
          fontAttributes.put(TextAttribute.UNDERLINE, -1);
          lblAddAnother.setFont(lblAddAnother.getFont().deriveFont(fontAttributes));
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void mouseEntered(MouseEvent e) {
          Font currentFont = lblAddAnother.getFont();
          Map fontAttributes = currentFont.getAttributes();
          fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
          lblAddAnother.setFont(lblAddAnother.getFont().deriveFont(fontAttributes));
        }

        @Override
        public void mouseClicked(MouseEvent e) {
          addTimerRow();
          gbcLblAddAnother.gridy = updateTimes.size();
          gbcLblRemoveLast.gridy = updateTimes.size();
          contentPanel.add(lblAddAnother, gbcLblAddAnother);
          contentPanel.add(lblRemoveLast, gbcLblRemoveLast);
          lblRemoveLast.setVisible(true);
          pack();
        }
      });
    }
    {

      JPanel buttonPane = new JPanel();

      getContentPane().add(buttonPane, BorderLayout.SOUTH);
      buttonPane.setLayout(new BorderLayout(0, 0));
      buttonPane.setBorder(new EmptyBorder(0, 10, 5, 10));
      {
        JButton quitButton = new JButton("Quit");
        quitButton.setActionCommand("Quit");
        quitButton.addActionListener(e -> System.exit(0));
        buttonPane.add(quitButton, BorderLayout.WEST);
      }
      {
        JButton nextButton = new JButton("Next >");
        nextButton.setActionCommand("Next >");
        nextButton.addActionListener(e -> {
          dispose();
          LCDTimer app = new LCDTimer(updateTimes, endTimes, showSeconds);
          app.setVisible(true);
        });
        buttonPane.add(nextButton, BorderLayout.EAST);
        getRootPane().setDefaultButton(nextButton);
      }
    }

  }

  private void addTimerRow() {
    int rowToAdd = updateTimes.size();
    {
      JLabel lblUpdateCountdown = new JLabel();
      if (rowToAdd == 0) {
        lblUpdateCountdown.setText("Update every:");
      } else if (rowToAdd == 1) {
        lblUpdateCountdown.setText("and then:");
      }
      lblUpdateCountdown.setFont(UIManager.getFont("Label.font"));
      GridBagConstraints gbcLblUpdateCountdown = new GridBagConstraints();
      gbcLblUpdateCountdown.anchor = GridBagConstraints.WEST;
      gbcLblUpdateCountdown.insets = new Insets(0, 0, 5, 5);
      gbcLblUpdateCountdown.gridx = 0;
      gbcLblUpdateCountdown.gridy = rowToAdd;
      contentPanel.add(lblUpdateCountdown, gbcLblUpdateCountdown);
      updateLabels.add(lblUpdateCountdown);
    }
    {
      JSpinner updateTime = new JSpinner();
      updateTime.setModel(new SpinnerNumberModel(60, 1, 3600, 1));
      GridBagConstraints gbcUpdateTime = new GridBagConstraints();
      gbcUpdateTime.insets = new Insets(0, 0, 5, 5);
      gbcUpdateTime.gridx = 1;
      gbcUpdateTime.gridy = rowToAdd;
      contentPanel.add(updateTime, gbcUpdateTime);
      updateTimes.add(updateTime);
    }
    {
      JLabel lblCountdownEnd = new JLabel("seconds until");
      lblCountdownEnd.setFont(UIManager.getFont("Label.font"));
      GridBagConstraints gbcCoundownEnd = new GridBagConstraints();
      gbcCoundownEnd.insets = new Insets(0, 0, 5, 5);
      gbcCoundownEnd.fill = GridBagConstraints.BOTH;
      gbcCoundownEnd.gridx = 2;
      gbcCoundownEnd.gridy = rowToAdd;
      contentPanel.add(lblCountdownEnd, gbcCoundownEnd);
      endLabels.add(lblCountdownEnd);
    }
    {
      Calendar cal = Calendar.getInstance();
      if (rowToAdd == 0) {
        cal.add(Calendar.HOUR, 1);
      } else {
        JSpinner nextSpinner = endTimes.get(rowToAdd - 1);
        Date nextTime = (Date) nextSpinner.getValue();
        cal.setTimeInMillis(nextTime.getTime() + 60000);
      }

      Calendar latest = Calendar.getInstance();
      latest.set(Calendar.HOUR_OF_DAY, 23);
      latest.set(Calendar.MINUTE, 59);
      latest.set(Calendar.SECOND, 59);
      // System.err.println(earliest.getTime() + " --> " + cal.getTime() + " --> " + latest.getTime());
      SpinnerDateModel timeModel = new SpinnerDateModel(cal.getTime(), null, latest.getTime(), Calendar.SECOND);
      JSpinner timeSpinner = new JSpinner(timeModel);
      JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "hh:mm:ss a");
      timeSpinner.setEditor(timeEditor);
      GridBagConstraints gbcTimeSpinner = new GridBagConstraints();
      gbcTimeSpinner.insets = new Insets(0, 0, 5, 5);
      gbcTimeSpinner.gridx = 3;
      gbcTimeSpinner.gridy = rowToAdd;
      contentPanel.add(timeSpinner, gbcTimeSpinner);
      timeSpinner.addChangeListener(new TimeSpinnerListener(timeSpinner, endTimes.size()));
      endTimes.add(timeSpinner);
    }
    {
      JCheckBox chckbxShowSeconds = new JCheckBox("Show Seconds");
      chckbxShowSeconds.setFont(UIManager.getFont("Label.font"));
      chckbxShowSeconds.setSelected(true);
      GridBagConstraints gbcChckbxShowSeconds = new GridBagConstraints();
      gbcChckbxShowSeconds.insets = new Insets(0, 0, 5, 0);
      gbcChckbxShowSeconds.gridx = 4;
      gbcChckbxShowSeconds.gridy = rowToAdd;
      contentPanel.add(chckbxShowSeconds, gbcChckbxShowSeconds);
      showSeconds.add(chckbxShowSeconds);
    }
  }

  private void removeTimerRow() {
    int rowToRemove = updateTimes.size() - 1;
    {
      JLabel lblUpdateCountdown = updateLabels.remove(rowToRemove);
      contentPanel.remove(lblUpdateCountdown);
    }
    {
      JSpinner updateTime = updateTimes.remove(rowToRemove);
      contentPanel.remove(updateTime);
    }
    {
      JLabel lblCountdownEnd = endLabels.remove(rowToRemove);
      contentPanel.remove(lblCountdownEnd);
    }
    {
      JSpinner endTime = endTimes.remove(rowToRemove);
      contentPanel.remove(endTime);
    }
    {
      JCheckBox showSec = showSeconds.remove(rowToRemove);
      contentPanel.remove(showSec);
    }
  }
}
