package cs3500.animator.io;

import java.util.LinkedHashMap;
import java.util.Map;

import cs3500.animator.model.Color;
import cs3500.animator.model.ColorTransform;
import cs3500.animator.model.EasyAnimatorModel;
import cs3500.animator.model.Ellipse;
import cs3500.animator.model.Event;
import cs3500.animator.model.EventType;
import cs3500.animator.model.IAnimatorModel;
import cs3500.animator.model.IShape;
import cs3500.animator.model.MoveTransform;
import cs3500.animator.model.Rectangle;
import cs3500.animator.model.ScaleTransform;
import cs3500.animator.model.VisibleTransform;

/**
 * This class builds an animation.
 */
public class AnimationBuilder implements TweenModelBuilder<IAnimatorModel> {
  private int xBound;
  private int yBound;
  private Map<String, IShape> shapes;

  /**
   * Constructs a new animation builder with a default canvas size and no shapes.
   */
  public AnimationBuilder() {
    this.xBound = 100;
    this.yBound = 100;
    this.shapes = new LinkedHashMap<>();
  }

  @Override
  public TweenModelBuilder<IAnimatorModel> setBounds(int width, int height) {
    this.xBound = width;
    this.yBound = height;
    return this;
  }

  @Override
  public TweenModelBuilder<IAnimatorModel> addOval(String name, float cx, float cy,
                                                   float xRadius, float yRadius, float red,
                                                   float green, float blue, int startOfLife,
                                                   int endOfLife) {
    IShape e = new Ellipse(cx, cy, new Color(red * 255, green * 255, blue * 255),
            0, xRadius * 2, yRadius * 2);
    addStartEndTo(e, startOfLife, endOfLife);
    shapes.put(name, e);
    return this;
  }

  @Override
  public TweenModelBuilder<IAnimatorModel> addRectangle(String name, float lx, float ly,
                                                        float width, float height, float red,
                                                        float green, float blue, int startOfLife,
                                                        int endOfLife) {

    IShape r = new Rectangle(lx, ly, new Color(red * 255, green * 255, blue * 255),
            0, width, height);
    addStartEndTo(r, startOfLife, endOfLife);
    shapes.put(name, r);

    return this;
  }

  // adds start and end times to when the shape is visible
  private void addStartEndTo(IShape s, int start, int end) {
    if (start != 0) {
      s.toggleVisibility(false);
      s.addTransform(new Event(0, 1, EventType.VISIBILITY),
              new VisibleTransform(false));
    }
    s.addTransform(new Event(start, start + 1, EventType.VISIBILITY),
            new VisibleTransform(true));
    s.addTransform(new Event(end, end + 1, EventType.VISIBILITY),
            new VisibleTransform(false));
  }

  @Override
  public TweenModelBuilder<IAnimatorModel> addMove(String name, float moveFromX, float moveFromY,
                                                   float moveToX, float moveToY, int startTime,
                                                   int endTime) {
    IShape s = shapes.get(name);
    if (s == null) {
      throw new IllegalArgumentException("No shape by that name");
    }

    int time = endTime - startTime;
    double xRate = (moveToX - moveFromX) / time;
    double yRate = (moveToY - moveFromY) / time;

    s.addTransform(new Event(startTime, endTime, EventType.MOVE),
            new MoveTransform(xRate, yRate));

    return this;
  }

  @Override
  public TweenModelBuilder<IAnimatorModel> addColorChange(String name, float oldR, float oldG,
                                                          float oldB, float newR, float newG,
                                                          float newB, int startTime, int endTime) {

    IShape s = shapes.get(name);
    if (s == null) {
      throw new IllegalArgumentException("No shape by that name");
    }

    int time = endTime - startTime;
    float rRate = (newR - oldR) / time * 255;
    float gRate = (newG - oldG) / time * 255;
    float bRate = (newB - oldB) / time * 255;

    s.addTransform(new Event(startTime, endTime, EventType.COLOR),
            new ColorTransform(rRate, gRate, bRate));

    return this;
  }

  @Override
  public TweenModelBuilder<IAnimatorModel> addScaleToChange(String name, float fromSx, float fromSy,
                                                            float toSx, float toSy, int startTime,
                                                            int endTime) {

    IShape s = shapes.get(name);
    if (s == null) {
      throw new IllegalArgumentException("No shape by that name");
    }

    int time = endTime - startTime;
    double xRate = (toSx - fromSx) / time;
    double yRate = (toSy - fromSy) / time;

    s.addTransform(new Event(startTime, endTime, EventType.SCALE),
            new ScaleTransform(xRate, yRate));

    return this;
  }

  @Override
  public IAnimatorModel build() {
    return new EasyAnimatorModel(shapes, xBound, yBound);
  }
}
