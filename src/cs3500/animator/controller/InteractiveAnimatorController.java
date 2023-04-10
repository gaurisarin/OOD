package cs3500.animator.controller;

import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.Timer;

import cs3500.animator.model.IAnimatorModel;
import cs3500.animator.view.IAnimatorView;

/**
 * Controller for CompositeAnimatorView, implements IAnimatorController.
 */
public class InteractiveAnimatorController implements IAnimatorController {
  private double tempo;
  private IAnimatorModel model;
  private IAnimatorView view;
  private Timer timer;
  private boolean loop;

  /**
   * Constructs a new InteractiveAnimatorController.
   * @param tempo the tempo of the animation, in ticks per second. Maximum of 1000 ticks per second
   * @param model the model to animate
   * @param view the view to show the animation in
   * @param loop if the animation should loop to the start when it ends
   * @param timer the timer object to run the animation on. Creates one if none is provided
   * @throws IllegalArgumentException if the tempo is non-positive, the model or view are null,
   *     or if the view is not already linked with the model provided in this constructor
   */
  public InteractiveAnimatorController(double tempo, IAnimatorModel model,
                                       IAnimatorView view, boolean loop, Timer timer) {
    if (tempo <= 0) {
      throw new IllegalArgumentException("Tempo must be positive");
    }
    if (model == null || view == null || !view.linkedTo(model)) {
      throw new IllegalArgumentException("Invalid model/view");
    }
    this.tempo = tempo;
    this.model = model;
    this.view = view;
    if (loop) {
      JButton b = view.getButtons().get("loop");
      b.setText("No Loop");
    }
    this.loop = loop;
    if (timer == null) {
      this.timer = new Timer((int) Math.round(1000 / tempo), null);
    } else {
      this.timer = timer;
    }
    AnimationListener listener = new AnimationListener(this);
    view.addListener(listener);
  }


  @Override
  public void play() {

    ActionListener l = e -> {
      if (model.hasEnded()) {
        if (loop) {
          model.reset();
        } else {
          timer.stop();
          display();
        }
      } else {
        model.run();
        display();
      }
    };

    if (display()) {
      timer.addActionListener(l);
      timer.start();
    }
  }

  private boolean display() {
    try {
      view.display();
      return true;
    } catch (IOException ioe) {
      System.out.println("Error writing to output\n");
      if (timer.isRunning()) {
        timer.stop();
      }
      return false;
    }
  }

  @Override
  public double getTempo() {
    return this.tempo;
  }

  @Override
  public void changeTempo(double rate) {
    this.tempo *= rate;
    timer.setDelay((int) Math.round(1000 / tempo));
  }

  @Override
  public void restart() {
    timer.stop();
    model.reset();
    togglePlay();
  }

  @Override
  public void togglePlay() {
    JButton b = view.getButtons().get("play-pause");
    if (!timer.isRunning()) {
      timer.start();
      if (b != null) {
        b.setText("Pause");
      }
    } else {
      timer.stop();
      if (b != null) {
        b.setText("Play");
      }
    }
  }


  @Override
  public void toggleLoop() {
    JButton b = view.getButtons().get("loop");
    if (loop) {
      this.loop = false;
      if (b != null) {
        b.setText("Loop");
      }
    } else {
      this.loop = true;
      if (b != null) {
        b.setText("No Loop");
      }
    }
  }
}
