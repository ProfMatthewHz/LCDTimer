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

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;

public class LCDTimer extends JFrame implements ActionListener {

  private List<CountdownDuration> countdowns;
  private TimeUnitPanel seconds;
  private TimeUnitPanel minutes;
  private TimeUnitPanel hours;

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
      long timeOfDay = (updatedTime.getTime() - (updatedTime.getTimezoneOffset() * 60 * 1000)) % (24 * 60 * 60 * 1000);
      CountdownDuration next = new CountdownDuration(showIt, update, timeOfDay);
      countdowns.add(next);
    }
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 1800, 300);
    JPanel contentPane = new JPanel();
    contentPane.setBackground(LCDAppColors.BACKGROUND_COLOR);
    setContentPane(contentPane);
    contentPane.setLayout(new GridBagLayout());
    CountdownPanel countdown = new CountdownPanel();
    countdown.setBackground(contentPane.getBackground());
    GridBagConstraints gbcCountdown = new GridBagConstraints();
    gbcCountdown.fill = GridBagConstraints.BOTH;
    gbcCountdown.gridx = 0;
    gbcCountdown.weightx = 0.001;
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
    gbcHours.gridx = 1;
    gbcHours.weightx = 0.333;
    gbcHours.weighty = 1.0;
    contentPane.add(hours, gbcHours);
    int diffMin = minutesRemaining(timeRemaining);
    minutes = new TimeUnitPanel(true);
    minutes.updateTime(diffMin);
    minutes.setBackground(contentPane.getBackground());
    GridBagConstraints gbcMinutes = new GridBagConstraints();
    gbcMinutes.fill = GridBagConstraints.BOTH;
    gbcMinutes.insets = new Insets(0, 10, 0, 0);
    gbcMinutes.gridx = 2;
    gbcMinutes.weightx = 0.333;
    gbcMinutes.weighty = 1.0;
    contentPane.add(minutes, gbcMinutes);
    int diffSec = secondsRemaining(timeRemaining);
    seconds = new TimeUnitPanel(true);
    seconds.updateTime(diffSec);
    seconds.setBackground(contentPane.getBackground());
    GridBagConstraints gbcSeconds = new GridBagConstraints();
    gbcSeconds.fill = GridBagConstraints.BOTH;
    gbcSeconds.insets = new Insets(0, 10, 0, 0);
    gbcSeconds.gridx = 3;
    gbcSeconds.weightx = 0.333;
    gbcSeconds.weighty = 1.0;
    contentPane.add(seconds, gbcSeconds);
    countdown.addActionListener(this);
    seconds.active(countdowns.get(0).showSeconds());
    if (timeRemaining > 0) {
      countdown.startCountdown(countdowns.get(0).getSecondsUntilUpdate());
    }
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
      int updateTime = Math.min(timeLeft, countdowns.get(0).getSecondsUntilUpdate());
      cp.startCountdown(updateTime);
    }
  }
}
