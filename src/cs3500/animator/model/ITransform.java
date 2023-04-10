package cs3500.animator.model;

/**
 * An ITransform represents some change over time that can be applied to an IShape.
 * ITransforms only store the rate of change, and not the start and end values,
 * so all ITransforms are continuous by definition.
 */
public interface ITransform {

  /**
   * Applies a transformation to the provided shape.
   *
   * @param shape the shape to apply the transform to
   * @throws IllegalArgumentException if the provided IShape is null
   */
  public void applyTransform(IShape shape);

  /**
   * Reverses a transformation the provided number of ticks.
   *
   * @param shape the shape to perform the reverse transform on
   * @param ticks the number of ticks to reverse transform by
   */
  public void undo(IShape shape, int ticks);

  /**
   * Changes the array of state values for a shape, representing the effect this transform
   * has on the shape with the provided state.
   *
   * @param s      the values for the shape the transform would change
   * @param amount the amount of times to apply the transform
   * @throws IllegalArgumentException if the state is invalid, or amount is non-positive
   */
  public void transformState(String[] s, int amount);

  /**
   * Generates an SVG-compliant string, representing this transform for a provided time.
   *
   * @param s         the state of the Shape that this transform is animating
   * @param start     the start of the animation, in seconds
   * @param dur       the duration of animation, in seconds
   * @param amt       the amount of times to apply the transform in that time
   * @param coordType the type of coordinate system used for the shape.
   *                  "c" if centered, "" otherwise
   * @return the SVG string
   * @throws IllegalArgumentException if the state is invalid, if any values are negative,
   *                                  or if coordType is invalid
   */
  public String animateSVG(String[] s, float start, float dur, int amt, String coordType);
}
