import org.junit.Test;

import java.util.NoSuchElementException;

import cs3500.animator.model.Color;
import cs3500.animator.model.ColorTransform;
import cs3500.animator.model.Ellipse;
import cs3500.animator.model.Event;
import cs3500.animator.model.EventType;
import cs3500.animator.model.IShape;
import cs3500.animator.model.ITransform;
import cs3500.animator.model.MoveTransform;
import cs3500.animator.model.NoTransform;
import cs3500.animator.model.Rectangle;
import cs3500.animator.model.RotateTransform;
import cs3500.animator.model.ScaleTransform;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;


/**
 * JUnit tests for IShapes.
 */
public class IShapeTest {
  // universal params for testing abstract methods
  private final double x = 100.47;
  private final double y = 0.01;
  private final Color color = new Color(0, 100, 254.555f);
  private double deg = 366.6;

  // tests for methods in abstract class. Shape given must use universal params
  // these tests ensures no details of any particular shape overwrite or otherwise
  // break functionality every shape should have
  private boolean testAbstractMethods(IShape shape) {
    // test getters
    assertEquals(shape.getX(), 100, 0.5);
    assertEquals(shape.getY(), 0, 0.1);
    assertEquals(shape.getColor(), new Color(0, 100, 255));
    assertEquals(shape.getDeg(), 7, 0.5);

    assertTrue(testCopy(shape));
    IShape unmodified = shape.copy();
    assertTrue(testMove(shape));
    shape = unmodified.copy();
    assertTrue(testRotate(shape));
    shape = unmodified.copy();
    assertTrue(testChangeColor(shape));
    shape = unmodified.copy();
    assertTrue(testFailAddTransform(shape));
    shape = unmodified.copy();
    assertTrue(testGoodAddTransform(shape));
    shape = unmodified.copy();
    assertTrue(testChangeVisibility(shape));
    assertTrue(testScaleAxis(shape));
    shape = unmodified.copy();
    assertTrue(testFailRemoveTransforms(shape));
    shape = unmodified.copy();
    assertTrue(testGoodRemoveTransforms(shape));

    assertTrue(testGetTransforms(shape));

    return true;
  }

  // tests move method works properly. Shape given must use universal params
  private boolean testMove(IShape shape) {
    shape.move(0.5, 10);
    assertEquals(shape.getX(), 101, 0.2);
    assertEquals(shape.getY(), 10, 0.05);
    return true;
  }

  private boolean testCopy(IShape shape) {
    IShape copy = shape.copy();
    assertEquals(copy.getX(), 100, 0.5);
    copy.move(0.5, 10);
    assertEquals(copy.getX(), 101, 0.2);
    assertEquals(shape.getX(), 100, 0.5);
    return true;
  }

  private boolean testRotate(IShape shape) {
    shape.rotate(-50);
    assertEquals(shape.getDeg(), 317);
    shape.rotate(70);
    assertEquals(shape.getDeg(), 27);
    return true;
  }

  private boolean testChangeColor(IShape shape) {
    shape.changeColor(5.501f, -50, 0.01f);
    assertEquals(6, shape.getColor().red());
    assertEquals(50, shape.getColor().green());
    assertEquals(255, shape.getColor().blue());
    shape.changeColor(0, 0, 50);
    assertEquals(255, shape.getColor().blue());

    return true;
  }

  private boolean testChangeVisibility(IShape shape) {
    assertTrue(shape.isVisible());
    shape.toggleVisibility(false);
    assertFalse(shape.isVisible());
    shape.toggleVisibility(true);
    assertTrue(shape.isVisible());
    return true;
  }

  private boolean testScaleAxis(IShape shape) {
    int width = shape.getWidth();
    int height = shape.getHeight();
    shape.scaleAxis("x", 5);
    assertEquals(width + 5, shape.getWidth());
    shape.scaleAxis("y", 7.1);
    assertEquals(height + 7, shape.getHeight());

    return true;
  }

  private boolean testFailAddTransform(IShape shape) {
    // cannot add same type on to of another
    // cannot have transform type not correspond to transform class
    ITransform t1 = new NoTransform();
    ITransform t2 = new MoveTransform(5, 10);
    Event e1 = new Event(0, 50, EventType.NOTHING);
    Event e2 = new Event(49, 50, EventType.NOTHING);
    Event e3 = new Event(10, 20, EventType.ROTATE);

    try {
      shape.addTransform(e1, t1);
      shape.addTransform(e2, t1);
      return false;
    } catch (IllegalArgumentException iae1) {
      try {
        shape.addTransform(e3, t2);
        return false;
      } catch (IllegalArgumentException iae2) {
        return true;
      }
    }
  }

  private boolean testGoodAddTransform(IShape shape) {
    ITransform t1 = new NoTransform();
    ITransform t2 = new MoveTransform(5, 10);
    ITransform t3 = new RotateTransform(20);
    ITransform t4 = new ColorTransform(5, 3, 1);
    Event e1 = new Event(0, 40, EventType.ROTATE);
    Event e2 = new Event(49, 50, EventType.MOVE);
    Event e3 = new Event(45, 59, EventType.COLOR);
    Event e5 = new Event(0, 49, EventType.NOTHING);
    Event e6 = new Event(40, 49, EventType.NOTHING);
    Event e7 = new Event(40, 45, EventType.NOTHING);
    // test: add normal transform with no others
    // add transform at end of other same type transform
    // add other type transform on top of other
    // test adding transform after highestTick adds in nothing transform

    shape.addTransform(e2, t2);
    assertEquals(2, shape.getTransforms().size());

    // make sure new nothing transform is created to fill gap
    if (shape.getTransforms().get(e5) == null) {
      return false;
    }
    shape.addTransform(e1, t3);
    // adds new rotate mostly on top of old nothing, should make old nothing short
    assertNotNull(shape.getTransforms().get(e1));
    assertNull(shape.getTransforms().get(e5));
    assertNotNull(shape.getTransforms().get(e6));
    shape.addTransform(e3, t4);
    assertNull(shape.getTransforms().get(e6));
    assertNotNull(shape.getTransforms().get(e7));
    return true;
  }

  private boolean testFailRemoveTransforms(IShape shape) {
    try { // arg null, error
      shape.removeTransform(null);
      return false;
    } catch (IllegalArgumentException iae) {
      // do nothing
    }

    try { // no transform with that key, error
      shape.removeTransform(new Event(0, 50, EventType.COLOR));
      return false;
    } catch (NoSuchElementException nse) {
      // do nothing
    }

    return true;
  }

  private boolean testGoodRemoveTransforms(IShape shape) {
    // mutates to add transforms
    assertTrue(testGoodAddTransform(shape));
    assertEquals(4, shape.getTransforms().size());
    shape.removeTransform(new Event(49, 50, EventType.MOVE));
    assertEquals(3, shape.getTransforms().size()); // removed old transform, didnt add
    // nothing transform

    Event rotate = new Event(0, 40, EventType.ROTATE);
    Event replaced = new Event(0, 40, EventType.NOTHING);
    shape.removeTransform(rotate);
    assertEquals(3, shape.getTransforms().size()); // removed and replaced with nothing
    assertNull(shape.getTransforms().get(rotate));
    assertNotNull(shape.getTransforms().get(replaced));

    Event color = new Event(45, 59, EventType.COLOR);
    shape.removeTransform(color);
    assertNull(shape.getTransforms().get(color));
    assertEquals(2, shape.getTransforms().size());
    return true;
  }

  private boolean testGetTransforms(IShape shape) {
    assertEquals(2, shape.getTransforms().size());
    assertNotNull(shape.getTransforms().get(new Event(0, 40, EventType.NOTHING)));
    assertNotNull(shape.getTransforms().get(new Event(40, 45, EventType.NOTHING)));
    IShape shape2 = new Ellipse(5, 10, new Color(0, 10, 20), 10, 10, 0.5);
    assertEquals(0, shape2.getTransforms().size());
    return true;
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEllipseSmallSquish() {
    IShape ellipse = new Ellipse(x, y, color, deg, 10, -0.1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEllipseNoColor() {
    IShape ellipse = new Ellipse(x, y, null, deg, 10, 0.5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEllipseBadRadius() {
    IShape ellipse = new Ellipse(x, y, null, deg, -0.1, 0.5);
  }

  @Test
  public void testEllipse() {
    IShape ellipse = new Ellipse(x, y, color, deg, 10, 0.1);
    assertTrue(testAbstractMethods(ellipse));
  }

  @Test
  public void testEllipseEquals() {
    IShape e = new Ellipse(x, y, color, deg, 10, 0.1);
    IShape e2 = new Ellipse(x, y, color, deg, 10, 0.1);
    IShape e3 = new Ellipse(x, y, color, deg, 10, 0.5);
    IShape e4 = new Ellipse(x, y, color, deg - 2, 10, 0.1);

    assertEquals(e, e2);
    assertNotEquals(e, e3);
    assertNotEquals(e, e4);
    assertNotEquals(e, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRectNoColor() {
    IShape rect = new Rectangle(x, y, null, deg, 10, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRectZeroWidth() {
    IShape rect = new Rectangle(x, y, color, deg, 0, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRectZeroHeight() {
    IShape rect = new Rectangle(x, y, color, deg, 5, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRectNegWidth() {
    IShape rect = new Rectangle(x, y, color, deg, -1, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRectNegHeight() {
    IShape rect = new Rectangle(x, y, color, deg, 5, -0.1);
  }

  @Test
  public void testRect() {
    IShape rect = new Rectangle(x, y, color, deg, 5, 10);
    assertTrue(testAbstractMethods(rect));
  }

  @Test
  public void testRectEquals() {
    IShape rect = new Rectangle(x, y, color, deg, 5, 10);
    IShape rect2 = new Rectangle(x, y, color, deg, 5, 10);
    IShape rect3 = new Rectangle(x, y, color, deg, 7, 10);
    IShape rect4 = new Rectangle(x, y + 2, color, deg, 7, 10);

    assertEquals(rect, rect2);
    assertNotEquals(rect, rect3);
    assertNotEquals(rect, rect4);
    assertNotEquals(rect, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToStandardStringBadTempo() {
    IShape rect = new Rectangle(x, y, color, deg, 5, 10);
    rect.toStandardString(0);
  }

  @Test
  public void testToStandardString() {
    IShape rect = new Rectangle(x, y, color, deg, 5, 10);
    rect.addTransform(new Event(0, 10, EventType.MOVE),
            new MoveTransform(1, 1));
    String corRect = "rect\n"
            + "t  x  y  red gre blu deg w  h vis\n"
            + "0.0 100 0 0 100 255 367 5 10 1  |  10.0 111 11 0 100 255 367 5 10 1\n";

    IShape ell = new Ellipse(x, y, color, deg, 10, 15);
    ell.addTransform(new Event(1, 5, EventType.SCALE),
            new ScaleTransform(2, 4));
    String corEll = "ellipse\n"
            + "t  x  y red gre blu deg xdia ydia vis\n"
            + "0.0 100 0 0 100 255 367 10 15 1  |  5.0 100 0 0 100 255 367 10 15 1\n"
            + "5.0 100 0 0 100 255 367 10 15 1  |  25.0 20 35 0 100 255 367 10 15 1\n";

    assertEquals(corRect, rect.toStandardString(1));
    assertEquals(corEll, ell.toStandardString(5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToSVGStringBadName() {
    IShape rect = new Rectangle(x, y, color, deg, 5, 10);
    rect.toSVGString(null, 5, 10, 20);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToSVGStringBadTempo() {
    IShape rect = new Rectangle(x, y, color, deg, 5, 10);
    rect.toSVGString("test", 0, 10, 20);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToSVGStringBadWidth() {
    IShape rect = new Rectangle(x, y, color, deg, 5, 10);
    rect.toSVGString("yo", 5, 0, 20);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToSVGStringBadHeight() {
    IShape rect = new Rectangle(x, y, color, deg, 5, 10);
    rect.toSVGString("hi TA :)", 5, 10, 0);
  }

  @Test
  public void testToSVGString() {
    IShape rect = new Rectangle(x, y, color, deg, 5, 10);
    rect.addTransform(new Event(0, 10, EventType.MOVE),
            new MoveTransform(1, 1));
    String corRect = "<rect id=\"rect1\" x=\"100.47\" y=\"0.01\" width=\"5\" height=\"10\" "
            + "fill=\"rgb(0, 100, 255)\"  >\n"
            + "<animate attributeName=\"x\" attributeType=\"XML\" begin=\"0.0s\" dur=\"10.0s\" "
            + "fill=\"freeze\" by=\"10.0\" />\n"
            + "<animate attributeName=\"y\" attributeType=\"XML\" begin=\"0.0s\" dur=\"10.0s\" "
            + "fill=\"freeze\" by=\"10.0\" />\n"
            + "</rect>";

    IShape ell = new Ellipse(x, y, color, deg, 10, 15);
    ell.addTransform(new Event(1, 5, EventType.SCALE),
            new ScaleTransform(2, 4));
    String corEll = "<ellipse id=\"ell1\" cx=\"100.47\" cy=\"0.01\" rx=\"5\" ry=\"7\" "
            + "fill=\"rgb(0, 100, 255)\"  >\n"
            + "<animate attributeName=\"rx\" attributeType=\"XML\" begin=\"0.2s\" dur=\"0.8s\" "
            + "fill=\"freeze\" by=\"8.0\" />\n"
            + "<animate attributeName=\"ry\" attributeType=\"XML\" begin=\"0.2s\" dur=\"0.8s\" "
            + "fill=\"freeze\" by=\"16.0\" />\n"
            + "</ellipse>";

    assertEquals(corRect, rect.toSVGString("rect1", 1, 100, 200));
    assertEquals(corEll, ell.toSVGString("ell1", 5, 500, 100));
  }
}
