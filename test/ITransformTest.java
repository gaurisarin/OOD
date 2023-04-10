import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs3500.animator.model.Color;
import cs3500.animator.model.ColorTransform;
import cs3500.animator.model.Ellipse;
import cs3500.animator.model.IShape;
import cs3500.animator.model.ITransform;
import cs3500.animator.model.MoveTransform;
import cs3500.animator.model.NoTransform;
import cs3500.animator.model.Rectangle;
import cs3500.animator.model.RotateTransform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * JUnit tests for ITransforms.
 */
public class ITransformTest {
  List<IShape> shapes;
  List<IShape> initShapes;

  @Before
  public void init() {
    shapes = new ArrayList<>();
    shapes.add(new Ellipse(-5, 0, new Color(0, 0.00001f, 50), 0, 0.1, 0));
    shapes.add(new Ellipse(100, 5000, new Color(50, 254.9999f, 254), 720, 50, 0.999f));
    shapes.add(new Rectangle(-5, 0, new Color(0, 0.0001f, 90.0001f), 190, 0.01, 0.5));
    shapes.add(new Rectangle(1200, 5000, new Color(70, 254.9999f, 254), 721, 50, 100));
    initShapes = new ArrayList<>(shapes);
  }

  // run transform on list of shapes
  private void runTransform(ITransform t) {
    for (IShape shape : shapes) {
      t.applyTransform(shape);
    }
  }

  // undoes transform 1 step
  private void undoTransform(ITransform t) {
    for (IShape shape : shapes) {
      t.undo(shape, 1);
    }
  }

  // test two arrays are the same, recursively
  private boolean testElementsEqual(List<?> a, List<?> b) {
    assertEquals(a.size(), b.size());
    for (int i = 0; i < a.size(); i++) {
      assertEquals(a.get(i), b.get(i));
    }
    return true;
  }

  @Test
  public void testColorLimit() {
    ITransform color = new ColorTransform(5, -3, 10);
    runTransform(color);
    assertEquals(255, shapes.get(1).getColor().blue());
    assertEquals(0, shapes.get(2).getColor().green());
  }

  @Test
  public void testColor() {
    ITransform color = new ColorTransform(5, 0, -20);
    runTransform(color);
    assertEquals(shapes.get(0).getColor(), new Color(5, 0.001f, 30));
    assertEquals(shapes.get(1).getColor(), new Color(55, 255, 233.5001f));
    undoTransform(color);
    assertTrue(testElementsEqual(shapes, initShapes));
  }

  @Test
  public void testMove() {
    ITransform move = new MoveTransform(-5.1, 200);
    runTransform(move);
    assertEquals(shapes.get(0).getX(), -10, 0.1);
    assertEquals(shapes.get(0).getY(), 200, 0.01);
    assertEquals(shapes.get(3).getX(), 1195, 0.1);
    assertEquals(shapes.get(3).getY(), 5200, 0.01);
    undoTransform(move);
    assertTrue(testElementsEqual(shapes, initShapes));
  }

  @Test
  public void testRotate() {
    ITransform rotate = new RotateTransform(180);
    runTransform(rotate);
    assertEquals(shapes.get(0).getDeg(), 180);
    assertEquals(shapes.get(1).getDeg(), 180);
    assertEquals(shapes.get(2).getDeg(), 10);
    assertEquals(shapes.get(3).getDeg(), 181);
    undoTransform(rotate);
    assertTrue(testElementsEqual(shapes, initShapes));
  }

  @Test
  public void testNone() {
    ITransform nothing = new NoTransform();
    assertTrue(testElementsEqual(shapes, initShapes));
    undoTransform(nothing);
    assertTrue(testElementsEqual(shapes, initShapes));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTransformStateNoState() {
    ITransform color = new ColorTransform(10, 5, 30);
    color.transformState(null, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTransformStateBadState() {
    ITransform color = new ColorTransform(10, 5, 30);
    color.transformState(null, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTransformStateBadAmt() {
    ITransform color = new ColorTransform(10, 5, 30);
    color.transformState(new String[]{"5"}, 0);
  }

  @Test
  public void testTransformState() {
    ITransform color = new ColorTransform(10, 5, 30);
    ITransform move = new MoveTransform(-10, 20);
    String[] state1 = new String[]{"50", "0", "100", "205", "10", "", "", "", ""};
    String[] state2 = new String[]{"20", "60", "110", "210", "40", "", "", "", ""};
    color.transformState(state1, 1);
    move.transformState(state1, 3);
    for (int i = 0; i < 9; i++) {
      assertEquals(state2[i], state1[i]);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnimateSVGBadState() {
    ITransform color = new ColorTransform(10, 5, 30);
    color.animateSVG(null, 5.5f, 7.2f, 4, "c");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnimateSVGBadStart() {
    ITransform color = new ColorTransform(10, 5, 30);
    String[] state = new String[]{"0", "0", "100", "205", "10", "", "", "", ""};
    color.animateSVG(state, -0.1f, 7.2f, 4, "c");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnimateSVGBadDur() {
    ITransform color = new ColorTransform(10, 5, 30);
    String[] state = new String[]{"0", "0", "100", "205", "10", "", "", "", ""};
    color.animateSVG(state, 5.5f, -0.3f, 4, "c");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnimateSVGBadAmt() {
    ITransform color = new ColorTransform(10, 5, 30);
    String[] state = new String[]{"0", "0", "100", "205", "10", "", "", "", ""};
    color.animateSVG(state, 5.5f, 7.2f, 0, "");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnimateSVGBadCoordType() {
    ITransform color = new ColorTransform(10, 5, 30);
    String[] state = new String[]{"0", "0", "100", "205", "10", "", "", "", ""};
    color.animateSVG(state, 5.5f, 7.2f, 4, "ya");
  }

  @Test
  public void testAnimateSVG() {
    ITransform color = new ColorTransform(10, 5, 30);
    String corr = "<animate attributeName=\"fill\" attributeType=\"CSS\" begin=\"5.5s\" "
            + "dur=\"7.2s\" fill=\"freeze\" from=\"rgb(100, 205, 10)\" to=\"rgb(140, 225, 130)\""
            + "  />\n";
    String[] state = new String[]{"0", "0", "100", "205", "10", "", "", "", ""};
    String act = color.animateSVG(state, 5.5f, 7.2f, 4, "c");
    assertEquals(corr, act);
  }
}
