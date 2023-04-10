package cs3500.animator.model;

import java.util.Map;

/**
 * <p>An interface for a simple read-only 2D animator model.</p>
 * <p>A read-only model is a collection of shapes, which each have a collection of transforms.</p>
 * <p>Transforms alter the properties of those shapes over a period of time dictated by their
 * definition, and the controller used for the animation. Transforms are stored as a collection
 * in the shape they will animate.</p>
 * <p>To modify the 2D animator model, use an IAnimatorModel, which extends this interface
 * with several methods to modify the state of itself.</p>
 */
public interface ReadOnlyIAnimatorModel {

  /**
   * Returns a copy of all the shapes in the model.
   *
   * @return A new map of id-cs3500.animator.model.IShape pairs
   */
  public Map<String, IShape> getShapes();

  /**
   * Returns the tick number the model is on.
   *
   * @return the tick number, as an integer
   */
  public int getTick();

  /**
   * Returns the width of the model canvas.
   *
   * @return the width, as an integer
   */
  public int getWidth();

  /**
   * Returns the height of the model canvas.
   *
   * @return the height, as an integer
   */
  public int getHeight();

  /**
   * Returns if the animation has reached the end.
   */
  public boolean hasEnded();
}
