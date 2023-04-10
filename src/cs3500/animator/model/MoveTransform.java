package cs3500.animator.model;

/**
 * A cs3500.animator.model.MoveTransform represents moving a shape at a certain rate.
 */
public class MoveTransform implements ITransform {
  private final double xRate;
  private final double yRate;

  /**
   * Constructs a new cs3500.animator.model.MoveTransform, which, when applied, moves a shape.
   *
   * @param xRate the amount to change the x coordinate by every tick
   * @param yRate the amount to change the y coordinate by every tick
   */
  public MoveTransform(double xRate, double yRate) {
    this.xRate = xRate;
    this.yRate = yRate;
  }

  @Override
  public void applyTransform(IShape shape) {
    shape.move(xRate, yRate);
  }

  @Override
  public void undo(IShape shape, int ticks) {
    shape.move(-1 * ticks * xRate, -1 * ticks * yRate);
  }

  @Override
  public void transformState(String[] s, int amount) {
    NoTransform.goodTransformState(s, amount);
    s[0] = String.valueOf(Math.round(Double.parseDouble(s[0]) + amount * xRate));
    s[1] = String.valueOf(Math.round(Double.parseDouble(s[1]) + amount * yRate));
  }

  @Override
  public String animateSVG(String[] s, float start, float dur, int amt, String coordType) {
    NoTransform.goodAnimateSVG(s, start, dur, amt, coordType);
    String out = "";
    if (Math.abs(xRate) >= 0.01) {
      double xAmt = xRate * amt;
      out += "<animate attributeName=\"" + coordType
              + "x\" attributeType=\"XML\" begin=\"" + start
              + "s\" dur=\"" + dur
              + "s\" fill=\"freeze\" by=\"" + xAmt + "\" />\n";
    }
    if (Math.abs(yRate) >= 0.01) {
      double yAmt = yRate * amt;
      out += "<animate attributeName=\"" + coordType
              + "y\" attributeType=\"XML\" begin=\"" + start
              + "s\" dur=\"" + dur
              + "s\" fill=\"freeze\" by=\"" + yAmt + "\" />\n";
    }

    return out;
  }


  @Override
  public boolean equals(Object o) {
    if (!(o instanceof MoveTransform)) {
      return false;
    }

    return diffRate(this.xRate, this.yRate) < 0.01;
  }

  @Override
  public int hashCode() {
    return Double.hashCode(this.xRate * this.yRate);
  }

  // returns the difference in the rates
  private double diffRate(double xRate, double yRate) {
    return Math.abs(this.xRate - xRate) + Math.abs(this.yRate - yRate);
  }
}
