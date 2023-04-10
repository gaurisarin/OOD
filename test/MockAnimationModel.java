import java.util.Map;

import cs3500.animator.model.Event;
import cs3500.animator.model.IAnimatorModel;
import cs3500.animator.model.IShape;
import cs3500.animator.model.ITransform;

class MockAnimationModel implements IAnimatorModel {
  private final StringBuilder log;
  private int tick;

  MockAnimationModel(StringBuilder log) {
    this.log = log;
    this.tick = 0;
  }

  @Override
  public void addShape(String id, IShape shape) {
    log.append("Added shape " + id);
  }

  @Override
  public void removeShape(String id) {
    log.append("Removed shape " + id);
  }

  @Override
  public Map<String, IShape> getShapes() {
    log.append("Got shapes");
    return null;
  }

  @Override
  public int getTick() {
    log.append("Got tick");
    return 0;
  }

  @Override
  public int getWidth() {
    log.append("Got width");
    return 0;
  }

  @Override
  public int getHeight() {
    log.append("Got height");
    return 0;
  }

  @Override
  public void addTransformTo(String id, Event e, ITransform t) {
    log.append("Added transform to " + id);
  }

  @Override
  public void removeTransformFrom(String id, Event e) {
    log.append("Removed transform from " + id);
  }

  @Override
  public void run() {
    log.append("Ran model");
    this.tick = 2;
  }

  @Override
  public boolean hasEnded() {
    log.append("Checked if ended");
    return this.tick == 2;
  }

  @Override
  public void reset() {
    log.append("Reset model");
  }
}
