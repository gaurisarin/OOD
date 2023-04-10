package cs3500.animator.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An animation listener reacts to user interaction in an animation. Handles button presses
 * that control the playback, speed, and looping of an interactive animation.
 */
public class AnimationListener implements ActionListener {
  private final IAnimatorController c;

  /**
   * Constructs a new AnimationListener, linked to the provided controller.
   * @param c the controller to send changes to
   * @throws IllegalArgumentException if the controller is null
   */
  public AnimationListener(IAnimatorController c) {
    if (c == null) {
      throw new IllegalArgumentException("Controller cannot be null");
    }
    this.c = c;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "play-pause":
        c.togglePlay();
        break;
      case "restart":
        c.restart();
        break;
      case "loop":
        c.toggleLoop();
        break;
      case "faster":
        c.changeTempo(2);
        break;
      case "slower":
        c.changeTempo(0.5);
        break;
      default:
        return;
    }
  }
}
