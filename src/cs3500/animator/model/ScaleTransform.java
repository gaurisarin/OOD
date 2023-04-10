package cs3500.animator.model;

/**
 * A RotateTransform is a transform representing scaling a shapes width and height
 * by specified rates.
 */
public class ScaleTransform implements ITransform {
  private final double xRate;
  private final double yRate;

  public ScaleTransform(double xRate, double yRate) {
    this.xRate = xRate;
    this.yRate = yRate;
  }

  @Override
  public void applyTransform(IShape shape) {
    shape.scaleAxis("x", xRate);
    shape.scaleAxis("y", yRate);
  }

  @Override
  public void undo(IShape shape, int ticks) {
    shape.scaleAxis("x", -1 * xRate);
    shape.scaleAxis("y", -1 * yRate);
  }

  @Override
  public void transformState(String[] s, int amount) {
    NoTransform.goodTransformState(s, amount);
    s[0] = String.valueOf(Math.round(Double.parseDouble(s[6]) + amount * xRate));
    s[1] = String.valueOf(Math.round(Double.parseDouble(s[7]) + amount * yRate));
  }

  @Override
  public String animateSVG(String[] s, float start, float dur, int amt, String coordType) {
    NoTransform.goodAnimateSVG(s, start, dur, amt, coordType);
    String xScale;
    String yScale;

    switch (coordType) {
      case "":
        xScale = "width";
        yScale = "height";
        break;
      case "c":
        xScale = "rx";
        yScale = "ry";
        break;
      default:
        throw new IllegalArgumentException("Invalid coord type");
    }

    String out = "";
    if (Math.abs(xRate) >= 0.01) {
      double xAmt = xRate * amt;
      out += "<animate attributeName=\"" + xScale
              + "\" attributeType=\"XML\" begin=\"" + start
              + "s\" dur=\"" + dur
              + "s\" fill=\"freeze\" by=\"" + xAmt + "\" />\n";
    }
    if (Math.abs(yRate) >= 0.01) {
      double yAmt = yRate * amt;
      out += "<animate attributeName=\"" + yScale
              + "\" attributeType=\"XML\" begin=\"" + start
              + "s\" dur=\"" + dur
              + "s\" fill=\"freeze\" by=\"" + yAmt + "\" />\n";
    }
    return out;
  }
}
