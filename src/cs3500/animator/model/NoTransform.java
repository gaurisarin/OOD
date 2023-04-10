package cs3500.animator.model;

/**
 * A NoTransform represents doing nothing. Adding this transform to an IShape
 * means it does nothing for the specified time. Shapes cannot have gaps
 * in time when no transforms occur on them, so this transform is used to fill
 * the gaps.
 */
public class NoTransform implements ITransform {
  @Override
  public void applyTransform(IShape shape) {
    // do nothing
  }

  @Override
  public void undo(IShape shape, int ticks) {
    // do nothing
  }

  @Override
  public void transformState(String[] s, int amount) {
    NoTransform.goodTransformState(s, amount);
  }

  @Override
  public String animateSVG(String[] s, float start, float dur, int amt, String coordType) {
    goodAnimateSVG(s, start, dur, amt, coordType);
    return "";
  }

  protected static void goodAnimateSVG(String[] s, float start, float dur,
                                       int amt, String coordType) {
    if (start < 0 || dur <= 0 || amt <= 0) {
      throw new IllegalArgumentException("Invalid args");
    }
    if (!coordType.equals("") && !coordType.equals("c")) {
      throw new IllegalArgumentException("Invalid coord type");
    }
    goodState(s);
  }

  protected static void goodTransformState(String[] s, int amt) {
    if (amt <= 0) {
      throw new IllegalArgumentException("Amount must be positive");
    }
    goodState(s);
  }

  private static void goodState(String[] s) {
    if (s == null || s.length != 9) {
      throw new IllegalArgumentException("Invalid state");
    }
  }
}
