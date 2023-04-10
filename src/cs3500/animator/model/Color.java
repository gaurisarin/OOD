package cs3500.animator.model;

/**
 * A Color represents a color made out of three values, one for each of Red, Green, and Blue.
 * The values for each color are stored as floats, but can only be accessed as the integer nearest
 * to the value.
 */
public class Color {
  private final float red;
  private final float green;
  private final float blue;

  /**
   * Returns a new Color with the specified RGB values, from 0 to 255.
   *
   * @param red   the amount of red
   * @param green the amount of green
   * @param blue  the amount of blue
   * @throws IllegalArgumentException if any of the RGB values are out of bounds
   */
  public Color(float red, float green, float blue) {
    if (outOfRange(red) || outOfRange(green) || outOfRange(blue)) {
      throw new IllegalArgumentException("RGB values must be 0-255");
    }
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  private boolean outOfRange(float val) {
    return (val < 0) || (val > 255);
  }

  /**
   * Returns the rounded value for the intensity of red for this Color.
   *
   * @return the intensity of red
   */
  public int red() {
    return Math.round(this.red);
  }

  /**
   * Returns the rounded value for the intensity of green for this Color.
   *
   * @return the intensity of green
   */
  public int green() {
    return Math.round(this.green);
  }

  /**
   * Returns the rounded value for the intensity of blue for this Color.
   *
   * @return the intensity of blue
   */
  public int blue() {
    return Math.round(this.blue);
  }

  /**
   * Returns a new Color, who's values are the sum of this color and the amounts specified.
   * If the change creates an invalid color, snap to the closest valid value.
   *
   * @param redAmt   the amount of red to add to this red value
   * @param greenAmt the amount of green to add to this green value
   * @param blueAmt  the amount of blue to add to this blue value
   * @return the new Color
   */
  public Color addColor(float redAmt, float greenAmt, float blueAmt) {
    return new Color(validAdd(red, redAmt), validAdd(green, greenAmt), validAdd(blue, blueAmt));
  }

  private float validAdd(float one, float two) {
    if (one + two > 255) {
      return 255;
    }
    if (one + two < 0) {
      return 0;
    }
    return one + two;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Color)) {
      return false;
    }

    Color other = (Color) o;
    return this.red() == other.red()
            && this.green() == other.green()
            && this.blue() == other.blue();
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(this.red() + this.green() + this.blue());
  }
}
