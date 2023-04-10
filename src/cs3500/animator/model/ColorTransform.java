package cs3500.animator.model;

/**
 * A ColorTransform is a transform representing changing a shape's color values at a given rate.
 */
public class ColorTransform implements ITransform {
  private final float redRate;
  private final float greenRate;
  private final float blueRate;

  /**
   * Returns a new cs3500.animator.model.ColorTransform with the specified color change rate.
   *
   * @param rR the rate of change in red
   * @param gR the rate of change in green
   * @param bR the rate of change in blue
   */
  public ColorTransform(float rR, float gR, float bR) {
    this.redRate = rR;
    this.greenRate = gR;
    this.blueRate = bR;
  }

  @Override
  public void applyTransform(IShape shape) {
    shape.changeColor(redRate, greenRate, blueRate);
  }

  @Override
  public void undo(IShape shape, int ticks) {
    shape.changeColor(-1 * ticks * redRate, -1 * ticks * greenRate, -1 * ticks * blueRate);
  }

  @Override
  public void transformState(String[] s, int amount) {
    NoTransform.goodTransformState(s, amount);
    s[2] = String.valueOf(Math.round(Double.parseDouble(s[2]) + amount * redRate));
    s[3] = String.valueOf(Math.round(Double.parseDouble(s[3]) + amount * greenRate));
    s[4] = String.valueOf(Math.round(Double.parseDouble(s[4]) + amount * blueRate));
  }

  @Override
  public String animateSVG(String[] s, float start, float dur, int amt, String coordType) {
    NoTransform.goodAnimateSVG(s, start, dur, amt, coordType);
    String out = "";
    Color oldC = new Color(Float.parseFloat(s[2]), Float.parseFloat(s[3]), Float.parseFloat(s[4]));
    String initC = "rgb(" + oldC.red() + ", " + oldC.green() + ", " + oldC.blue() + ")";
    Color newC = oldC.addColor(redRate * amt, greenRate * amt, blueRate * amt);
    String finalC = "rgb(" + newC.red() + ", " + newC.green() + ", " + newC.blue() + ")";
    out += "<animate attributeName=\"fill\" attributeType=\"CSS\" begin=\"" + start
            + "s\" dur=\"" + dur
            + "s\" fill=\"freeze\" from=\"" + initC
            + "\" to=\"" + finalC + "\"  />\n";

    return out;
  }


  @Override
  public boolean equals(Object o) {
    if (!(o instanceof MoveTransform)) {
      return false;
    }

    return diffRate(redRate, greenRate, blueRate) < 0.03;
  }

  @Override
  public int hashCode() {
    return Float.hashCode(redRate + greenRate + blueRate);
  }

  // returns the difference in the rates
  private float diffRate(float red, float green, float blue) {
    return Math.abs(this.redRate - red)
            + Math.abs(this.greenRate - green)
            + Math.abs(this.blueRate - blue);
  }
}
