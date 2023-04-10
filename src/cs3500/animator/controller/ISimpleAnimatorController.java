package cs3500.animator.controller;

/**
 * Represents a controller of an animation. Simple in that it does not
 * have any interactivity.
 */
public interface ISimpleAnimatorController {

  /**
   * Plays the animation at the set speed (ticks / second). Stops when the
   * animation is finished.
   */
  public void play();

  /**
   * Returns the tempo of the animation, in ticks per second.
   */
  public double getTempo();
}
