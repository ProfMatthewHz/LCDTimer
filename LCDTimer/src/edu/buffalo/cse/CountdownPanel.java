package edu.buffalo.cse;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class CountdownPanel extends JPanel {

  public class SizeChangeListener extends ComponentAdapter {

    @Override
    public void componentResized(ComponentEvent arg0) {
      super.componentResized(arg0);
      // Trash the current buffer and restart so that we are using the new size.
      buffer = null;
    }
  }

  private long totalCountdownTime;
  private float progress;
  private BufferedImage buffer;
  private Timer timer;
  private ArrayList<ActionListener> listeners;

  /**
   * Create the panel.
   */
  public CountdownPanel() {
    listeners = new ArrayList<>();
    addComponentListener(new SizeChangeListener());
  }

  public void startCountdown(int secondsToCountDown) {
    totalCountdownTime = secondsToCountDown * 1000;
    final long startTime = System.currentTimeMillis();
    // Setup the timer to enable 60 fps animation
    timer = new Timer(16, e -> {
      long curTime = System.currentTimeMillis();
      long timeElapsed = curTime - startTime;
      progress = (float) timeElapsed / totalCountdownTime;
      /*
       * if (progress > 1.0) { progress = 1.0f; }
       */
      updateBuffer();
      repaint();
      if (timeElapsed >= totalCountdownTime) {
        timer.stop();
        notifyObserver(totalCountdownTime / 1000);
      }
    });
    timer.setInitialDelay(0);
    timer.start();
  }

  public void addActionListener(ActionListener al) {
    listeners.remove(al);
    listeners.add(al);
  }

  private void notifyObserver(long secondsToComplete) {
    ActionEvent ae = new ActionEvent(this, (int) secondsToComplete, null);
    for (ActionListener al : listeners) {
      EventQueue.invokeLater(() -> {
        al.actionPerformed(ae);
      });
    }
  }

  private void updateBuffer() {
    if (buffer == null) {
      int height = getHeight();
      buffer = new BufferedImage(10, height, BufferedImage.TYPE_INT_RGB);
      buffer.setAccelerationPriority(1.0f);
      Graphics2D g = buffer.createGraphics();
      g.setColor(LCDAppColors.TIMER_COLOR);
      g.fillRect(0, 0, 10, height);
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    int heightUsed = (int) (progress * getHeight());
    g.setColor(getBackground());
    g.fillRect(0, 0, getWidth(), getHeight());
    g.drawImage(buffer, 0, heightUsed, null);
  }
}
