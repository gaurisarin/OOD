package cs3500.animator.model;

/**
 * A RotateTransform is a transform representing rotating a shape at a specified rate.
 */
public class RotateTransform implements ITransform {
  private final double rate;

  /**
   * Returns a new RotateTransform with the specified rate.
   *
   * @param rate the amount to rotate the shape every tick
   */
  public RotateTransform(double rate) {
    this.rate = rate;
  }

  @Override
  public void applyTransform(IShape shape) {
    shape.rotate(rate);
  }

  @Override
  public void undo(IShape shape, int ticks) {
    shape.rotate(-1 * ticks * rate);
  }

  @Override
  public void transformState(String[] s, int amount) {
    NoTransform.goodTransformState(s, amount);
    s[5] = String.valueOf(Math.round(Double.parseDouble(s[0]) + amount * rate));
  }

  @Override
  public String animateSVG(String[] s, float start, float dur, int amt, String coordType) {
    NoTransform.goodAnimateSVG(s, start, dur, amt, coordType);
    //TODO
    return "";
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof MoveTransform)) {
      return false;
    }

    return diffRate(this.rate) < 0.01;
  }

  @Override
  public int hashCode() {
    return Double.hashCode(this.rate);
  }

  // returns the difference in the rates
  private double diffRate(double rate) {
    return Math.abs(this.rate - rate);
  }
}
