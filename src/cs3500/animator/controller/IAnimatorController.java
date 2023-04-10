package cs3500.animator.controller;

/**
 * IAnimatorControllers extend simple animator controllers with the ability
 * interact with the animation as it is playing.
 */
public interface IAnimatorController extends ISimpleAnimatorController {

  /**
   * Restarts the animation from the beginning.
   */
  public void restart();

  /**
   * Toggles the animation between playing and pausing.
   */
  public void togglePlay();

  /**
   * Toggles the animation between looping and not looping to the start when it finishes.
   */
  public void toggleLoop();

  /**
   * Change the tempo of the animation by the provided multiple.
   * @param rate the rate to change the animation speed by
   */
  public void changeTempo(double rate);

}
