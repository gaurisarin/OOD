import org.junit.Test;

import cs3500.animator.model.Color;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * JUnit tests for the cs3500.animator.model.Color class.
 */
public class ColorTest {
  @Test(expected = IllegalArgumentException.class)
  public void testColorBadRed() {
    Color color = new Color(-0.01f, 10, 20);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testColorBadGreen() {
    Color color = new Color(5, 257, 20);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testColorBadBlue() {
    Color color = new Color(5, 10, 255.01f);
  }

  @Test
  public void testGetColorValues() {
    Color c = new Color(0.01f, 200.501f, 254.99f);

    assertEquals(0, c.red());
    assertEquals(201, c.green());
    assertEquals(255, c.blue());
  }

  @Test
  public void testAddColor() {
    Color c = new Color(0.01f, 200.49f, 254.99f);
    assertEquals(0, c.red());
    assertEquals(200, c.green());
    assertEquals(255, c.blue());
    c = c.addColor(-5.2f, 0.02f, 20);
    assertEquals(0, c.red());
    assertEquals(201, c.green());
    assertEquals(255, c.blue());
  }

  @Test
  public void testEquals() {
    Color c = new Color(5, 10, 15);
    Color c2 = new Color(5, 10, 15);
    Color c3 = new Color(5, 10, 16);

    assertEquals(c, c);
    assertEquals(c, c2);
    assertNotEquals(c, c3);
    assertNotEquals(c, null);
  }
}
