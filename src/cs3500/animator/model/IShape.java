package cs3500.animator.model;

import java.awt.Graphics2D;
import java.util.NavigableMap;

/**
 * <p>An IShape is the interface for all shapes that can be drawn in the animator.</p>
 * <p>An IShape contains a map of Transforms, which are ordered by the order in which they start.
 * So, a transform set to start at tick 5 and ending at tick 100 is sorted before a transform
 * starting at tick 50 and ending at tick 150.</p>
 * <p>A shape's properties are stored as decimals, but can only be accessed as the rounded integer.
 * This is done to make displaying shapes simple, while preserving accuracy and precision.
 * Additionally, the rotation of shapes is bounded to a between 0 and 360 degrees.</p>
 * <p>Shapes start off visible, requiring a transform on them to be made not visible.</p>
 */
public interface IShape {

  /**
   * Adds a transform to the IShape. Cannot add a transform if another transform of the
   * same type already is set to occur during the same ticks.
   *
   * @param e the start and end ticks for the transform, represented as an Event.
   * @param t the ITransform to apply to the shape.
   * @throws IllegalArgumentException if the transform is null, if another transform of the same
   *                                  type exists in the time range, or if the event type does not
   *                                  correspond to the transform given.
   */
  public void addTransform(Event e, ITransform t);

  /**
   * Removes the transform from the IShape that corresponds to the event specified.
   *
   * @param e the event parameters of the transform to remove
   * @throws IllegalArgumentException         if the event specified is null
   * @throws java.util.NoSuchElementException if a transform corresponding to the event does not
   *                                          exist
   */
  public void removeTransform(Event e);

  /**
   * Returns a copy of all the IShape's transforms.
   */
  public NavigableMap<Event, ITransform> getTransforms();

  /**
   * Returns the end tick of the transform that ends the latest.
   */
  public int lastTick();

  /**
   * Retrieves the x coordinate. Rounds to the nearest grid point.
   *
   * @return the x coordinate, as an integer.
   */
  public double getX();

  /**
   * Retrieves the y coordinate. Rounds to the nearest grid point.
   *
   * @return the y coordinate, as an integer.
   */
  public double getY();

  /**
   * Retrieves the color of the shape.
   *
   * @return the RGB color, as a Color
   */
  public Color getColor();

  /**
   * Retrieves the width of the shape.
   *
   * @return the width, as an integer.
   */
  public int getWidth();

  /**
   * Retrieves the height of the shape.
   *
   * @return the height, as an integer.
   */
  public int getHeight();

  /**
   * Retrieves the rotation of the shape. Rounds to the nearest whole degree.
   *
   * @return the degrees of rotation, as an integer.
   */
  public int getDeg();

  /**
   * Returns if the shape is currently visible.
   *
   * @return if the shape is visible
   */
  public boolean isVisible();

  /**
   * Moves the IShape by the specified amount.
   *
   * @param x the amount to change the x coordinate by
   * @param y the amount to change the y coordinate by
   */
  public void move(double x, double y);

  /**
   * Rotates the IShape by the given amount of degrees.
   *
   * @param deg the degrees to rotate this IShape
   */
  public void rotate(double deg);

  /**
   * Change the color of this IShape by the given RGB values.
   *
   * @param redAmt   the amount to change the intensity of red by
   * @param greenAmt the amount to change the intensity of green by
   * @param blueAmt  the amount to change the intensity of blue by
   * @throws IllegalArgumentException if the change specified would result in an invalid color
   */
  public void changeColor(float redAmt, float greenAmt, float blueAmt);

  /**
   * Toggles if the shape is set to be visible at a given time. Starts visible.
   *
   * @param makeVisible if the command should make the shape visible or invisible.
   */
  public void toggleVisibility(boolean makeVisible);

  /**
   * Changes the size of the shape on the provided axis by the provided amount.
   *
   * @param axis a string, either "x" or "y"
   * @param amt  the amount to scale the axis by
   * @throws IllegalArgumentException if the axis is invalid.
   */
  public void scaleAxis(String axis, double amt);

  /**
   * Returns a copy of this IShape.
   *
   * @return the new copy of this IShape
   */
  public IShape copy();

  /**
   * Returns a string, representing the shape and its transforms.
   *
   * @param tempo the speed, in ticks/second, of the animation.
   * @return the string.
   * @throws IllegalArgumentException if the tempo is invalid.
   */
  public String toStandardString(double tempo);

  /**
   * Draws the shape as a visual element using the Graphics2D instance provided.
   *
   * @param g2 the graphics instance to draw this shape with
   * @throws IllegalArgumentException if the graphics is null
   */
  public void drawWith(Graphics2D g2);

  /**
   * Returns a string, representing the shape and its transforms, by SVG specifications.
   * When rendered, produces the shape animated by the transforms applied to it.
   *
   * @param name   the name of the shape in the animation, as a string.
   * @param tempo  the speed, in ticks/second, of the animation.
   * @param width  the width of the canvas, in pixels
   * @param height the height of the canvas, in pixels
   * @return an SVG-formatted string.
   * @throws IllegalArgumentException if the tempo, width, or height is invalid, or name is null
   */
  public String toSVGString(String name, double tempo, int width, int height);
}
