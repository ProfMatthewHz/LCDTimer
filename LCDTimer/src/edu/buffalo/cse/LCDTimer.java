package edu.buffalo.cse;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

@SuppressWarnings("serial")
public class LCDTimer extends JFrame implements ActionListener {

  private List<CountdownDuration> countdowns;
  private GridBagLayout layout;
  private TimeUnitPanel seconds;
  private TimeUnitPanel minutes;
  private TimeUnitPanel hours;
  private JPanel leftFiller;
  private JPanel rightFiller;

  /**
   * Create the frame.
   *
   * @param showSeconds True when we should be displaying those seconds; false otherwise.
   * @param endTimes Times at which we should change the duration of the timers
   * @param updateTimes Duration of the timers until that end time is reached.
   */
  public LCDTimer(List<JSpinner> updateTimes, List<JSpinner> endTimes, List<JCheckBox> showSeconds) {
    super("Time Remaining");
    countdowns = new LinkedList<>();
    for (int i = 0; i < updateTimes.size(); i++ ) {
      boolean showIt = showSeconds.get(i).isSelected();
      int update = (Integer) updateTimes.get(i).getValue();
      Date updatedTime = (Date) endTimes.get(i).getValue();
      @SuppressWarnings("deprecation")
      long timeOfDay = (updatedTime.getTime() - (updatedTime.getTimezoneOffset() * 60 * 1000)) % (24 * 60 * 60 * 1000);
      CountdownDuration next = new CountdownDuration(showIt, update, timeOfDay);
      countdowns.add(next);
    }
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 1800, 360);
    JPanel contentPane = new JPanel();
    contentPane.setBackground(LCDAppColors.BACKGROUND_COLOR);
    setContentPane(contentPane);
    layout = new GridBagLayout();
    contentPane.setLayout(layout);

    java.net.URL imgURL = this.getClass().getResource("TimeRemain.png");
    ImageIcon remainImage = new ImageIcon(imgURL);
    JLabel label = new JLabel(remainImage);
    label.setBackground(contentPane.getBackground());
    GridBagConstraints gbcLabel = new GridBagConstraints();
    gbcLabel.gridx = 2;
    gbcLabel.gridy = 0;
    gbcLabel.gridwidth = 3;
    gbcLabel.gridheight = 1;
    gbcLabel.weighty = 0.125;
    gbcLabel.anchor = GridBagConstraints.BASELINE_LEADING;
    gbcLabel.insets = new Insets(0, 0, 30, 0);
    contentPane.add(label, gbcLabel);
    
    CountdownPanel countdown = new CountdownPanel();
    countdown.setBackground(contentPane.getBackground());
    GridBagConstraints gbcCountdown = new GridBagConstraints();
    gbcCountdown.fill = GridBagConstraints.BOTH;
    gbcCountdown.gridx = 0;
    gbcCountdown.gridheight = 2;
    gbcCountdown.weightx = 0.01;
    gbcCountdown.weighty = 1.0;
    contentPane.add(countdown, gbcCountdown);
    long now = currentTime();
    long timeRemaining = millisRemaining(now);
    timeRemaining = Math.max(timeRemaining, 0);
    int diffHours = hoursRemaining(timeRemaining);
    hours = new TimeUnitPanel(false);
    hours.updateTime(diffHours);
    hours.setBackground(contentPane.getBackground());
    GridBagConstraints gbcHours = new GridBagConstraints();
    gbcHours.fill = GridBagConstraints.BOTH;
    gbcHours.insets = new Insets(0, 10, 0, 0);
    gbcHours.gridx = 2;
    gbcHours.gridy = 1;
    gbcHours.weightx = 0.2857142857;
    gbcHours.weighty = 0.875;
    contentPane.add(hours, gbcHours);
    int diffMin = minutesRemaining(timeRemaining);
    minutes = new TimeUnitPanel(true);
    minutes.updateTime(diffMin);
    minutes.setBackground(contentPane.getBackground());
    GridBagConstraints gbcMinutes = new GridBagConstraints();
    gbcMinutes.fill = GridBagConstraints.BOTH;
    gbcMinutes.insets = new Insets(0, 10, 0, 0);
    gbcMinutes.gridx = 3;
    gbcMinutes.gridy = 1;
    gbcMinutes.weightx = 0.3571428571;
    gbcMinutes.weighty = 0.875;
    contentPane.add(minutes, gbcMinutes);
    int diffSec = secondsRemaining(timeRemaining);
    seconds = new TimeUnitPanel(true);
    seconds.updateTime(diffSec);
    seconds.setBackground(contentPane.getBackground());
    GridBagConstraints gbcSeconds = new GridBagConstraints();
    GridBagConstraints gbcLeft = new GridBagConstraints();
    GridBagConstraints gbcRight = new GridBagConstraints();
    gbcSeconds.fill = GridBagConstraints.BOTH;
    gbcSeconds.insets = new Insets(0, 10, 0, 0);
    gbcSeconds.gridx = 4;
    gbcSeconds.gridy = 1;
    gbcSeconds.ipady = 200;
    gbcLeft.fill = GridBagConstraints.BOTH;
    gbcLeft.gridx = 1;
    gbcLeft.gridy = 0;
    gbcLeft.gridheight = 2;
    gbcRight.fill = GridBagConstraints.BOTH;
    gbcRight.gridx = 5;
    gbcRight.gridy = 0;
    gbcRight.gridheight = 2;
    if (countdowns.get(0).showSeconds()) {
      gbcSeconds.weightx = 0.3571428571;
      gbcLeft.weightx = 0.001;
      gbcRight.weightx = 0.001;
    } else {
      gbcLeft.weightx = 0.178571429;
      gbcRight.weightx = 0.178571429;
      gbcSeconds.weightx = 0.001;
    }
    gbcLeft.weighty = 1.0;
    gbcRight.weighty = 1.0;
    gbcSeconds.weighty = 0.875;
    contentPane.add(seconds, gbcSeconds);
    countdown.addActionListener(this);
    seconds.active(countdowns.get(0).showSeconds());
    if (timeRemaining > 0) {
      countdown.startCountdown(countdowns.get(0).getSecondsUntilUpdate());
    }
    leftFiller = new JPanel();
    leftFiller.setBackground(contentPane.getBackground());
    contentPane.add(leftFiller, gbcLeft);
    rightFiller = new JPanel();
    rightFiller.setBackground(contentPane.getBackground());
    contentPane.add(rightFiller, gbcRight);
  }

  private int secondsRemaining(long timeRemaining) {
    return (int) (timeRemaining / (1000)) % 60;
  }

  private int minutesRemaining(long timeRemaining) {
    return (int) (timeRemaining / (1000 * 60)) % 60;
  }

  private int hoursRemaining(long timeRemaining) {
    return (int) (timeRemaining / (1000 * 60 * 60));
  }

  private long currentTime() {
    long now = System.currentTimeMillis();
    GregorianCalendar cal = new GregorianCalendar();
    int offset = ((cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET)));
    now = now + offset;
    now = now % (24 * 60 * 60 * 1000);
    return now;
  }

  private long millisRemaining(long currentTime) {
    long timeRemaining = countdowns.get(countdowns.size() - 1).getDurationEnd() - currentTime;
    return timeRemaining;
  }

  @Override
  public void actionPerformed(ActionEvent ae) {
    CountdownPanel cp = (CountdownPanel) (ae.getSource());
    long now = currentTime();
    long timeRemain = millisRemaining(now);
    if (timeRemain <= 1000) {
      hours.updateTime(0);
      minutes.updateTime(0);
      seconds.updateTime(0);
    } else {
      while (now > countdowns.get(0).getDurationEnd()) {
        countdowns.remove(0);
      }
      if ((timeRemain / 1000) < countdowns.get(0).getSecondsUntilUpdate()) {
        timeRemain = countdowns.get(0).getSecondsUntilUpdate() * 1000;
      }
      int hoursLeft = hoursRemaining(timeRemain);
      int minutesLeft = minutesRemaining(timeRemain);
      int secondsLeft = secondsRemaining(timeRemain);
      int timeLeft = secondsLeft;
      timeLeft += minutesLeft * 60;
      timeLeft += hoursLeft * 3600;
      hours.updateTime(hoursLeft);
      minutes.updateTime(minutesLeft);
      seconds.updateTime(secondsLeft);
      GridBagConstraints gbcSeconds = layout.getConstraints(seconds);
      GridBagConstraints gbcLeft = layout.getConstraints(leftFiller);
      GridBagConstraints gbcRight = layout.getConstraints(rightFiller);
      if (countdowns.get(0).showSeconds()) {
        gbcSeconds.weightx = 0.3571428571;
        gbcLeft.weightx = 0.001;
        gbcRight.weightx = 0.001;
        seconds.active(true);
      } else {
        gbcSeconds.weightx = 0.001;
        gbcLeft.weightx = 0.178571429;
        gbcRight.weightx = 0.178571429;
        seconds.active(false);
      }
      layout.setConstraints(seconds, gbcSeconds);
      layout.setConstraints(leftFiller, gbcLeft);
      layout.setConstraints(rightFiller, gbcRight);
      getContentPane().revalidate();
      int updateTime = Math.min(timeLeft, countdowns.get(0).getSecondsUntilUpdate());
      cp.startCountdown(updateTime);
    }
  }
}
