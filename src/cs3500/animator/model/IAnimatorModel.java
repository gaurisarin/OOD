package cs3500.animator.model;

/**
 * <p>An interface for a simple 2D animator model.</p>
 * <p>A model is a collection of shapes that it can add and remove from. The model also can add
 * and remove transforms to and from those shapes. Finally, the model animates the shapes by
 * managing the animation's progress via its tick count, running the shapes' transforms one tick
 * at a time, and resetting the animation when requested.</p>
 * Transforms alter the properties of those shapes over a period of time dictated by their
 * definition, and the controller used for the animation. Transforms are stored as a collection
 * in the shape they will animate.
 */
public interface IAnimatorModel extends ReadOnlyIAnimatorModel {
  /**
   * Adds a shape to the model. The shape appears at the beginning of the animation.
   *
   * @param id    the unique identifier for this shape in the model
   * @param shape the shape to add to the model
   * @throws IllegalArgumentException if either of the arguments are null, or if the ID
   *                                  already exists.
   */
  public void addShape(String id, IShape shape);

  /**
   * Removes a shape from the model corresponding to the provided id.
   *
   * @param id the unique identifier of the shape in the model
   * @throws IllegalArgumentException         if the id is null
   * @throws java.util.NoSuchElementException if no shape corresponding to the id exists
   */
  public void removeShape(String id);

  /**
   * Adds an event-transform entry to the shape corresponding to the provided id.
   *
   * @param id the unique identifier of the shape in the model
   * @param e  the event data for the transform (start tick, end tick, event type)
   * @param t  the transform class to add
   * @throws IllegalArgumentException         if any of the arguments are null, or if the transform
   *                                          is invalid
   * @throws java.util.NoSuchElementException if no shape corresponding to the id exists
   */
  public void addTransformTo(String id, Event e, ITransform t);

  /**
   * Removes an event-transform entry from the shape corresponding to the provided id.
   *
   * @param id the unique identifier of the shape in the model
   * @param e  the event data of the transform to remove (start tick, end tick, event type)
   * @throws IllegalArgumentException         if either of the arguments are null
   * @throws java.util.NoSuchElementException if no shape corresponding to the id exists, or no
   *                                          transform corresponding to the given event exists for
   *                                          the shape
   */
  public void removeTransformFrom(String id, Event e);

  /**
   * Advances the model forward 1 tick. Performs any active transforms on the shapes.
   */
  public void run();

  /**
   * Resets a model back to tick 0. All transforms on all shapes are reverted.
   */
  public void reset();
}
