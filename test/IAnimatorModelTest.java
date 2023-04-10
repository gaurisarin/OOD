import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import cs3500.animator.model.Color;
import cs3500.animator.model.ColorTransform;
import cs3500.animator.model.EasyAnimatorModel;
import cs3500.animator.model.Ellipse;
import cs3500.animator.model.Event;
import cs3500.animator.model.EventType;
import cs3500.animator.model.IAnimatorModel;
import cs3500.animator.model.IShape;
import cs3500.animator.model.MoveTransform;
import cs3500.animator.model.NoTransform;
import cs3500.animator.model.Rectangle;
import cs3500.animator.model.RotateTransform;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNull;

/**
 * JUnit tests to test an cs3500.animator.model.IAnimatorModel implementation.
 */
public class IAnimatorModelTest {
  private IAnimatorModel emptyM;
  private IAnimatorModel m;

  // to test
  // run
  // reset

  @Before
  public void init() {
    emptyM = new EasyAnimatorModel(500, 500);
    Map<String, IShape> shapes = new HashMap<>();

    Color blue = new Color(0, 0, 250);
    Color red = new Color(250, 0, 0);
    Color mid = new Color(150, 150, 150);

    Ellipse e1 = new Ellipse(5, 10, red, 10, 5, 5);
    e1.addTransform(new Event(5, 20, EventType.MOVE), new MoveTransform(5, 10));

    shapes.put("e1", e1);
    shapes.put("e2", new Ellipse(20, 75, blue, 0, 7.3, 0));
    shapes.put("r1", new Rectangle(20.5, 3.6, mid, 50, 5.9, 20.3));
    shapes.put("r2", new Rectangle(20, 20, red, 0, 15, 15));
    m = new EasyAnimatorModel(shapes, 200, 200);
  }

  private int countTransforms() {
    int count = 0;
    for (IShape shape : m.getShapes().values()) {
      for (Event e : shape.getTransforms().keySet()) {
        count++;
      }
    }
    return count;
  }

  @Test
  public void testGetTick() {
    assertEquals(0, emptyM.getTick());
    emptyM.run();
    emptyM.run();
    assertEquals(2, emptyM.getTick());
    emptyM.reset();
    assertEquals(0, emptyM.getTick());
  }

  @Test
  public void testGetShapes() {
    assertEquals(0, emptyM.getShapes().size());
    assertEquals(4, m.getShapes().size());
    assertNotNull(m.getShapes().get("r1"));
    assertNotNull(m.getShapes().get("e2"));
  }

  @Test
  public void testAddShapeNoId() {
    try {
      m.addShape(null, m.getShapes().get("e2"));
      fail();
    } catch (IllegalArgumentException iae) {
      assertEquals(4, m.getShapes().size());
    }
  }

  @Test
  public void testAddShapeNoShape() {
    try {
      m.addShape("ye", null);
      fail();
    } catch (IllegalArgumentException iae) {
      assertEquals(4, m.getShapes().size());
    }
  }

  @Test
  public void testAddShapeBadId() {
    try {
      m.addShape("e2", new Ellipse(5, 10, new Color(5, 5, 5), 10, 5, 0.1));
      fail();
    } catch (IllegalArgumentException iae) {
      assertEquals(4, m.getShapes().size());
    }

  }

  @Test
  public void testAddShape() {
    IShape e = new Ellipse(5, 10, new Color(5, 5, 5), 10, 5, 5);
    m.addShape("e3", e);
    assertEquals(e, m.getShapes().get("e3"));
  }

  @Test
  public void testRemoveShapeNoId() {
    try {
      m.removeShape(null);
      fail();
    } catch (IllegalArgumentException iae) {
      assertEquals(4, m.getShapes().size());
    }
  }

  @Test
  public void testRemoveShapeBadId() {
    try {
      m.removeShape("da");
      fail();
    } catch (NoSuchElementException nse) {
      assertEquals(4, m.getShapes().size());
    }
  }

  @Test
  public void testRemoveShape() {
    m.removeShape("e2");
    assertEquals(3, m.getShapes().size());
    assertNull(m.getShapes().get("e2"));
  }

  @Test
  public void testAddTransformToNoId() {
    try {
      m.addTransformTo(null, new Event(5, 6, EventType.NOTHING), new NoTransform());
      fail();
    } catch (IllegalArgumentException iae) {
      assertEquals(2, countTransforms());
    }
  }

  @Test
  public void testAddTransformToBadId() {
    try {
      m.addTransformTo("ye", new Event(5, 6, EventType.NOTHING), new NoTransform());
      fail();
    } catch (NoSuchElementException nse) {
      assertEquals(2, countTransforms());
    }
  }

  @Test
  public void testAddTransformToNoEvent() {
    try {
      m.addTransformTo("e1", null, new NoTransform());
      fail();
    } catch (IllegalArgumentException iae) {
      assertEquals(2, countTransforms());
    }
  }

  @Test
  public void testAddTransformToNoTransform() {
    try {
      m.addTransformTo("r1", new Event(5, 6, EventType.NOTHING), null);
      fail();
    } catch (IllegalArgumentException iae) {
      assertEquals(2, countTransforms());
    }
  }

  @Test
  public void testAddTransformToBadTransformNoSpace() {
    try {
      m.addTransformTo("e1", new Event(3, 8, EventType.MOVE), new MoveTransform(3, 4));
    } catch (IllegalArgumentException iae) {
      assertEquals(2, countTransforms());
    }
  }

  @Test
  public void testAddTransformToBadTransformNoMatch() {
    try {
      m.addTransformTo("e2", new Event(3, 8, EventType.ROTATE), new MoveTransform(3, 4));
    } catch (IllegalArgumentException iae) {
      assertEquals(2, countTransforms());
    }
  }

  @Test
  public void testAddTransformTo() {
    m.addTransformTo("r1", new Event(3, 8, EventType.MOVE), new MoveTransform(3, 4));
    assertEquals(2, m.getShapes().get("r1").getTransforms().size());
    assertEquals(4, countTransforms()); // added two, one for gap
    m.addTransformTo("e2", new Event(0, 10, EventType.ROTATE), new RotateTransform(5));
    assertEquals(1, m.getShapes().get("e2").getTransforms().size());
    assertEquals(5, countTransforms()); // added one, no gap

    // when adding one that overlaps some of a nothing, remove the nothing that overlaps
    Event n = new Event(0, 5, EventType.NOTHING);
    Event n2 = new Event(0, 3, EventType.NOTHING);
    assertNotNull(m.getShapes().get("e1").getTransforms().get(n));
    assertNull(m.getShapes().get("e1").getTransforms().get(n2));
    m.addTransformTo("e1", new Event(3, 10, EventType.COLOR), new ColorTransform(5, 10, -3));
    assertNotNull(m.getShapes().get("e1").getTransforms().get(n2));
    assertNull(m.getShapes().get("e1").getTransforms().get(n));
    assertEquals(6, countTransforms());
  }

  @Test
  public void testRemoveTransformFromNoId() {
    try {
      m.removeTransformFrom(null, new Event(5, 10, EventType.COLOR));
      fail();
    } catch (IllegalArgumentException iae) {
      assertEquals(2, countTransforms());
    }
  }

  @Test
  public void testRemoveTransformFromBadId() {
    try {
      m.removeTransformFrom("ye", new Event(5, 10, EventType.COLOR));
      fail();
    } catch (NoSuchElementException nse) {
      assertEquals(2, countTransforms());
    }
  }

  @Test
  public void testRemoveTransformFromNoEvent() {
    try {
      m.removeTransformFrom("e1", null);
      fail();
    } catch (IllegalArgumentException iae) {
      assertEquals(2, countTransforms());
    }
  }

  @Test
  public void testRemoveTransformFromBadEvent() {
    try {
      m.removeTransformFrom("e1", new Event(5, 10, EventType.COLOR));
      fail();
    } catch (NoSuchElementException iae) {
      assertEquals(2, countTransforms());
    }
  }

  @Test
  public void testRemoveTransformFromBasic() {
    Event event = new Event(5, 20, EventType.MOVE);
    m.removeTransformFrom("e1", event);
    assertEquals(1, countTransforms());
    assertNull(m.getShapes().get("e1").getTransforms().get(event));
  }

  @Test
  public void testRemoveTransformFromGaps() {
    Event n = new Event(0, 5, EventType.NOTHING);
    m.removeTransformFrom("e1", n);
    // cant remove, gets added right back
    assertEquals(2, countTransforms());
    assertNotNull(m.getShapes().get("e1").getTransforms().get(n));
  }

  @Test
  public void testRun() {
    IShape e1 = new Ellipse(5, 10, new Color(250, 0, 0), 10, 5, 5);
    assertEquals(0, m.getTick());
    m.run();
    assertEquals(1, m.getTick());
    m.run();
    assertEquals(2, m.getTick());
    assertEquals(m.getShapes().get("e1"), e1);
    m.run();
    m.run();
    assertEquals(m.getShapes().get("e1"), e1);
    m.run();
    assertEquals(m.getShapes().get("e1"), e1);
    m.run();
    assertNotEquals(m.getShapes().get("e1"), e1);
  }

  @Test
  public void testReset() {
    IShape e1 = new Ellipse(5, 10, new Color(250, 0, 0), 10, 5, 5);
    m.run();
    m.run();
    m.run();
    m.run();
    m.run();
    assertEquals(m.getShapes().get("e1"), e1);
    m.run();
    assertNotEquals(m.getShapes().get("e1"), e1);
    m.reset();
    m.addTransformTo("e1", new Event(20, 30, EventType.MOVE), new MoveTransform(20, 40));
    m.toString();
    assertEquals(m.getShapes().get("e1"), e1);
  }

  @Test
  public void testToString() {
    String correct1 = "Shape r2 rect\n"
            + "t  x  y  red gre blu deg w  h vis\n"
            + "\n"
            + "Shape e1 ellipse\n"
            + "t  x  y red gre blu deg xdia ydia vis\n"
            + "0.0 5 10 250 0 0 10 5 5 1  |  5.0 5 10 250 0 0 10 5 5 1\n"
            + "5.0 5 10 250 0 0 10 5 5 1  |  20.0 85 170 250 0 0 10 5 5 1\n"
            + "\n"
            + "Shape e2 ellipse\n"
            + "t  x  y red gre blu deg xdia ydia vis\n"
            + "\n"
            + "Shape r1 rect\n"
            + "t  x  y  red gre blu deg w  h vis\n\n";
    String correct2 = "";
    assertEquals(correct1, m.toString());
    assertEquals(correct2, emptyM.toString());
  }

  @Test
  public void testHasEnded() {
    assertTrue(emptyM.hasEnded());
    assertFalse(m.hasEnded());
    for (int i = 0; i < 19; i++) {
      m.run();
    }
    assertFalse(m.hasEnded());
    m.run();
    assertTrue(m.hasEnded());
  }
}
