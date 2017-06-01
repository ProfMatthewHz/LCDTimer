package edu.buffalo.cse;

public class CountdownDuration {
  /** True if we should display seconds during this duration; false otherwise. */
  private boolean showSeconds;

  /** Number of seconds to pause before updating the countdown timer. */
  private int secondsUntilUpdate;

  /** Time at which this duration will be over. */
  private long durationEnd;

  public CountdownDuration(boolean displaySec, int update, long end) {
    showSeconds = displaySec;
    secondsUntilUpdate = update;
    durationEnd = end;
  }

  public boolean showSeconds() {
    return showSeconds;
  }

  public int getSecondsUntilUpdate() {
    return secondsUntilUpdate;
  }

  public long getDurationEnd() {
    return durationEnd;
  }

}
