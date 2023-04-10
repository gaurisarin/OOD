package cs3500.animator.model;

/**
 * This enum represents the list of possible transforms that can be. Used
 * as part of the Event key to distinguish different kinds of transforms that occur
 * at the same time.
 */
public enum EventType {
  NOTHING {
    @Override
    public boolean isType(ITransform t) {
      return t instanceof NoTransform;
    }
  }, MOVE {
    @Override
    public boolean isType(ITransform t) {
      return t instanceof MoveTransform;
    }
  }, COLOR {
    @Override
    public boolean isType(ITransform t) {
      return t instanceof ColorTransform;
    }
  }, ROTATE {
    @Override
    public boolean isType(ITransform t) {
      return t instanceof RotateTransform;
    }
  }, VISIBILITY {
    @Override
    public boolean isType(ITransform t) {
      return t instanceof VisibleTransform;
    }
  }, SCALE {
    @Override
    public boolean isType(ITransform t) {
      return t instanceof ScaleTransform;
    }
  };

  public abstract boolean isType(ITransform t);
}
