package cs3500.animator.model;

import java.awt.Graphics2D;

/**
 * A Rectangle is one of the shapes supported by the animation.
 */
public class Rectangle extends AShape {
  private final String[] initial;

  /**
   * Constructs a new Rectangle.
   *
   * @param x      the x coordinate of the Rectangle (left side)
   * @param y      the y coordinate of the Rectangle (top side)
   * @param color  the RGB color of the Rectangle
   * @param deg    the degrees the Rectangle is rotated from zero
   * @param width  the width of the Rectangle
   * @param height the height of the Rectangle
   * @throws IllegalArgumentException if the color is null or the width/height are zero or negative
   */
  public Rectangle(double x, double y, Color color, double deg, double width, double height) {
    super(x, y, color, deg, width, height);
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("width and height must be greater than zero");
    }

    this.initial = new String[]{
            String.valueOf(x), // 0
            String.valueOf(y), // 1
            String.valueOf(color.red()), // 2
            String.valueOf(color.green()), // 3
            String.valueOf(color.blue()), // 4
            String.valueOf(deg), // 5
            String.valueOf(width), // 6
            String.valueOf(height), // 7
            String.valueOf(1) // 8
    };
  }

  @Override
  AShape makeCopy(double x, double y, Color color, double deg, double width, double height) {
    return new Rectangle(x, y, color, deg, width, height);
  }

  @Override
  String[] initialState() {
    return initial.clone();
  }

  @Override
  String uniqueSVGString(String name) {
    return "<rect id=\"" + name
            + "\" x=\"" + this.getX()
            + "\" y=\"" + this.getY()
            + "\" width=\"" + this.getWidth()
            + "\" height=\"" + this.getHeight() + "\" ";
  }

  @Override
  String stringHeader() {
    return "t  x  y  red gre blu deg w  h vis";
  }

  @Override
  String shapeType() {
    return "rect";
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Rectangle)) {
      return false;
    }

    Rectangle e2 = (Rectangle) o;
    return this.sameParams(e2);
  }

  @Override
  public int hashCode() {
    return Double.hashCode(this.sameHash());
  }

  @Override
  public void drawWith(Graphics2D g2) {
    Color c = this.getColor();
    g2.setColor(new java.awt.Color(c.red(), c.green(), c.blue()));
    int width = this.getWidth();
    int height = this.getHeight();
    int x = (int) this.getX();
    int y = (int) this.getY();
    g2.drawRect(x, y, width, height);
    g2.fillRect(x, y, width, height);
  }
}
