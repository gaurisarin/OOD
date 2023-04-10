package cs3500.animator.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NoSuchElementException;

/**
 * EasyAnimatorModel implements animation described in IAnimatorModel.
 */
public class EasyAnimatorModel implements IAnimatorModel {
  private int tick;
  private final Map<String, IShape> shapes;
  private int width;
  private int height;

  /**
   * Returns a new empty EasyAnimatorModel, with no shapes described.
   *
   * @throws IllegalArgumentException if the canvas size provided is invalid
   */
  public EasyAnimatorModel(int width, int height) {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Canvas size invalid");
    }
    this.tick = 0;
    this.shapes = new LinkedHashMap<>();
    this.width = width;
    this.height = height;
  }

  private EasyAnimatorModel(IAnimatorModel m) {
    this.tick = m.getTick();
    this.shapes = m.getShapes();
  }

  /**
   * Returns a new EasyAnimatorModel, with a map of shapes already defined.
   *
   * @param shapes the map of string-IShape pairs to add to the new model
   * @param width  the width of the canvas
   * @param height the height of the canvas
   * @throws IllegalArgumentException if the width or height is invalid
   */
  public EasyAnimatorModel(Map<String, IShape> shapes, int width, int height) {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("invalid canvas size parameters");
    }
    this.tick = 0;
    this.width = width;
    this.height = height;
    if (shapes == null) {
      this.shapes = new LinkedHashMap<>();
    } else {
      this.shapes = shapes;
    }

  }

  @Override
  public void addShape(String id, IShape shape) {
    if (shape == null) {
      throw new IllegalArgumentException("must specify shape to add with addShape");
    }

    try {
      // if no shape exists with this id, throws NSE. If it does exist, cannot add shape,
      // throw error
      getShape(id);
      throw new IllegalArgumentException("shape id already exists in animation");
    } catch (NoSuchElementException nse) {
      shapes.put(id, shape.copy());
    }
  }

  @Override
  public void removeShape(String id) {
    // checks that string id is right and shape exists in model
    getShape(id);
    shapes.remove(id);
  }

  @Override
  public Map<String, IShape> getShapes() {
    return new LinkedHashMap<>(this.shapes);
  }

  @Override
  public int getTick() {
    return this.tick;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public void addTransformTo(String id, Event e, ITransform t) {
    if (e == null || t == null) {
      throw new IllegalArgumentException("addTransformTo arguments cannot be null");
    }
    IShape shape = getShape(id);
    shape.addTransform(e, t);
  }

  @Override
  public void removeTransformFrom(String id, Event e) {
    if (e == null) {
      throw new IllegalArgumentException("removeTransformFrom argument cannot be null");
    }
    IShape shape = getShape(id);
    shape.removeTransform(e);
  }

  private IShape getShape(String id) {
    if (id == null) {
      throw new IllegalArgumentException("Shape id cannot be null");
    }
    IShape toGet = this.shapes.get(id);
    if (toGet == null) {
      throw new NoSuchElementException("Shape not found in model");
    }
    return toGet;
  }

  @Override
  public void run() {
    for (IShape shape : shapes.values()) {
      NavigableMap<Event, ITransform> transforms = shape.getTransforms();
      for (Map.Entry<Event, ITransform> e : transforms.entrySet()) {
        if (e.getKey().during(new Event(tick, tick, EventType.NOTHING)) >= 0
                && e.getKey().getEnd() != tick) {
          e.getValue().applyTransform(shape);
        }
      }
    }

    tick++;
  }

  @Override
  public boolean hasEnded() {
    int highest = 0;
    for (IShape shape : shapes.values()) {
      if (shape.lastTick() > highest) {
        highest = shape.lastTick();
      }
    }
    return highest <= tick;
  }

  @Override
  public void reset() {
    if (tick == 0) {
      return;
    }

    for (int backTick = tick; backTick >= 0; backTick--) {
      for (IShape shape : shapes.values()) {
        NavigableMap<Event, ITransform> transforms = shape.getTransforms();
        for (Map.Entry<Event, ITransform> e : transforms.entrySet()) {
          if (e.getKey().during(new Event(backTick, backTick, EventType.NOTHING)) >= 0
                  && e.getKey().getStart() != backTick) {
            e.getValue().undo(shape, 1);
          }
        }
      }
    }
    tick = 0;
  }

  // removed functionality from model,
  private String toStandardString(double tempo) {
    if (tempo <= 0) {
      throw new IllegalArgumentException("Invalid tempo");
    }
    StringBuilder out = new StringBuilder();
    for (Map.Entry<String, IShape> entry : shapes.entrySet()) {
      out.append("Shape ")
              .append(entry.getKey())
              .append(" ")
              .append(entry.getValue().toStandardString(1))
              .append("\n");
    }

    return out.toString();
  }

  @Override
  public String toString() {
    StringBuilder out = new StringBuilder();
    for (Map.Entry<String, IShape> entry : shapes.entrySet()) {
      out.append("Shape ")
              .append(entry.getKey())
              .append(" ")
              .append(entry.getValue().toStandardString(1))
              .append("\n");
    }

    return out.toString();
  }


}
