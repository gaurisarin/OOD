package cs3500.animator.model;

/**
 * A VisibleTransform represents changing a shape's visibility.
 */
public class VisibleTransform implements ITransform {
  private final boolean makeVisible;

  public VisibleTransform(boolean makeVisible) {
    this.makeVisible = makeVisible;
  }

  @Override
  public void applyTransform(IShape shape) {
    shape.toggleVisibility(makeVisible);
  }

  @Override
  public void undo(IShape shape, int ticks) {
    shape.toggleVisibility(!makeVisible);
  }

  @Override
  public void transformState(String[] s, int amount) {
    NoTransform.goodTransformState(s, amount);
    if (makeVisible) {
      s[8] = String.valueOf(1);
    } else {
      s[8] = String.valueOf(0);
    }
  }

  @Override
  public String animateSVG(String[] s, float start, float dur, int amt, String coordType) {
    NoTransform.goodAnimateSVG(s, start, dur, amt, coordType);
    String out = "";
    if (s[8].equals("0") && makeVisible
            || s[8].equals("1") && !makeVisible) {

      String to = "visible";
      if (!makeVisible) {
        to = "hidden";
      }

      out += "<animate attributeName=\"visibility\" attributeType=\"XML\" to=\"" + to
              + "\" begin=\"" + start
              + "\" dur=\"" + dur
              + "\" fill=\"freeze\" />\n";
    }
    return out;
  }
}
