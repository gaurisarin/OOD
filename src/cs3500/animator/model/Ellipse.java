package cs3500.animator.model;

import java.awt.Graphics2D;

/**
 * An Ellipse is one of the shapes supported by the animation.
 */
public class Ellipse extends AShape {

  private final String[] initial;

  /**
   * Constructs a new Ellipse.
   *
   * @param x         the x coordinate of the ellipse
   * @param y         the y coordinate of the ellipse
   * @param color     the RGB color of the ellipse
   * @param deg       the degrees the ellipse is rotated from zero
   * @param xDiameter the diameter of the ellipse on the x-axis
   * @param yDiameter the diameter of the ellipse on the y-axis
   * @throws IllegalArgumentException if the color is null, radius is negative,
   *                                  or if squish is out of bounds
   */
  public Ellipse(double x, double y, Color color, double deg, double xDiameter, double yDiameter) {
    super(x, y, color, deg, xDiameter, yDiameter);
    if (xDiameter < 0 || yDiameter < 0) {
      throw new IllegalArgumentException("Invalid Ellipse arguments");
    }

    this.initial = new String[]{
            String.valueOf(x),
            String.valueOf(y),
            String.valueOf(color.red()),
            String.valueOf(color.green()),
            String.valueOf(color.blue()),
            String.valueOf(deg),
            String.valueOf(xDiameter),
            String.valueOf(yDiameter),
            String.valueOf(1)
    };
  }

  @Override
  AShape makeCopy(double x, double y, Color color, double deg, double width, double height) {
    return new Ellipse(x, y, color, deg, width, height);
  }

  @Override
  String[] initialState() {
    return initial.clone();
  }

  @Override
  String uniqueSVGString(String name) {

    return "<ellipse id=\"" + name
            + "\" cx=\"" + this.getX()
            + "\" cy=\"" + this.getY()
            + "\" rx=\"" + this.getWidth() / 2
            + "\" ry=\"" + this.getHeight() / 2 + "\" ";
  }

  @Override
  String stringHeader() {
    return "t  x  y red gre blu deg xdia ydia vis";
  }

  @Override
  String shapeType() {
    return "ellipse";
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Ellipse)) {
      return false;
    }

    Ellipse e2 = (Ellipse) o;
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
    int x = (int) Math.floor(this.getX() - width / 2.0);
    int y = (int) Math.floor(this.getY() - height / 2.0);
    g2.drawOval(x, y, width, height);
    g2.fillOval(x, y, width, height);
  }
}
