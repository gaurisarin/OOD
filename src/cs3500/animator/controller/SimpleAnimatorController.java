package cs3500.animator.controller;

import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Timer;

import cs3500.animator.model.IAnimatorModel;
import cs3500.animator.view.IAnimatorView;

/**
 * An implementation of a simple animator controller. Has no interactivity.
 */
public class SimpleAnimatorController implements ISimpleAnimatorController {
  private final double tempo;
  // ticks per second ^
  private final IAnimatorModel model;
  private final IAnimatorView view;

  /**
   * Constructs a new SimpleAnimatorController.
   * @param tempo the tempo of the animation, in ticks per second. Maximum of 1000 ticks per second
   * @param model the model to animate
   * @param view the view to show the animation in
   * @throws IllegalArgumentException if the tempo is non-positive, the model or view are null,
   *     or if the view is not already linked with the model provided in this constructor
   */
  public SimpleAnimatorController(double tempo, IAnimatorModel model, IAnimatorView view) {
    if (tempo <= 0) {
      throw new IllegalArgumentException("Tempo must be positive");
    }
    if (model == null || view == null || !view.linkedTo(model)) {
      throw new IllegalArgumentException("Invalid model/view");
    }
    this.tempo = tempo;
    this.model = model;
    this.view = view;
  }


  @Override
  public void play() {
    Timer timer = new Timer((int) Math.round(1000 / tempo), null);
    display(timer);
    ActionListener l = e -> {
      if (model.hasEnded()) {
        timer.stop();
      } else {
        model.run();
        display(timer);
      }
    };

    timer.addActionListener(l);
    timer.start();
  }

  private void display(Timer timer) {
    try {
      view.display();
    } catch (IOException ioe) {
      System.out.println("Error writing to output\n");
      if (timer.isRunning()) {
        timer.stop();
      }
    }
  }

  @Override
  public double getTempo() {
    return this.tempo;
  }
}
