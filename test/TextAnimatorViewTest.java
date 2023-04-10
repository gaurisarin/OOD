import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cs3500.animator.model.Color;
import cs3500.animator.model.EasyAnimatorModel;
import cs3500.animator.model.Ellipse;
import cs3500.animator.model.Event;
import cs3500.animator.model.EventType;
import cs3500.animator.model.IAnimatorModel;
import cs3500.animator.model.IShape;
import cs3500.animator.model.MoveTransform;
import cs3500.animator.model.Rectangle;
import cs3500.animator.view.IAnimatorView;
import cs3500.animator.view.TextAnimatorView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * JUnit tests for the TextAnimatorView class.
 */
public class TextAnimatorViewTest {
  private IAnimatorModel emptyM;
  private IAnimatorModel m;

  @Before
  public void init() {
    emptyM = new EasyAnimatorModel(500, 500);
    Map<String, IShape> shapes = new HashMap<>();

    Color blue = new Color(0, 0, 250);
    Color red = new Color(250, 0, 0);
    Color mid = new Color(150, 150, 150);

    Ellipse e1 = new Ellipse(5, 10, red, 10, 20, 20);
    e1.addTransform(new Event(5, 20, EventType.MOVE),
            new MoveTransform(5, 10));
    e1.addTransform(new Event(22, 30, EventType.MOVE),
            new MoveTransform(0, -5));

    IShape e2 = new Ellipse(20, 75, blue, 0, 7.3, 20);

    shapes.put("e1", e1);
    shapes.put("e2", e2);
    shapes.put("r1", new Rectangle(20.5, 3.6, mid, 50, 5.9, 20.3));
    shapes.put("r2", new Rectangle(20, 20, red, 0, 15, 15));
    m = new EasyAnimatorModel(shapes, 200, 200);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createViewNoModel() {
    IAnimatorView v = new TextAnimatorView(null, new BadAppendable(), 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createViewNoOut() {
    IAnimatorView v = new TextAnimatorView(m, null, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createViewBadTempo() {
    IAnimatorView v = new TextAnimatorView(m, new BadAppendable(), 0);
  }

  @Test
  public void testLinkedTo() {
    IAnimatorView v = new TextAnimatorView(m, new BadAppendable(), 5);
    assertTrue(v.linkedTo(m));
    assertFalse(v.linkedTo(null));
    assertFalse(v.linkedTo(emptyM));
  }

  @Test
  public void testDisplayIsConstant() throws IOException {
    Appendable out = new StringBuilder();
    IAnimatorView v = new TextAnimatorView(m, out, 5);
    v.display();
    String one = out.toString();
    v.display();
    v.display();
    assertEquals(one, out.toString());
  }

  @Test
  public void testDisplayContent() throws IOException {
    Appendable out = new StringBuilder();
    IAnimatorView v = new TextAnimatorView(m, out, 5);
    v.display();
    String corr = "Shape r2 rect\n"
            + "t  x  y  red gre blu deg w  h vis\n"
            + "\n"
            + "Shape e1 ellipse\n"
            + "t  x  y red gre blu deg xdia ydia vis\n"
            + "0.0 5 10 250 0 0 10 20 20 1  |  25.0 5 10 250 0 0 10 20 20 1\n"
            + "25.0 5 10 250 0 0 10 20 20 1  |  100.0 85 170 250 0 0 10 20 20 1\n"
            + "100.0 85 170 250 0 0 10 20 20 1  |  110.0 85 170 250 0 0 10 20 20 1\n"
            + "110.0 85 170 250 0 0 10 20 20 1  |  150.0 85 125 250 0 0 10 20 20 1\n"
            + "\n"
            + "Shape e2 ellipse\n"
            + "t  x  y red gre blu deg xdia ydia vis\n"
            + "\n"
            + "Shape r1 rect\n"
            + "t  x  y  red gre blu deg w  h vis\n\n";
    assertEquals(corr, out.toString());
  }

  @Test(expected = IOException.class)
  public void testDisplayBadAppendable() throws IOException {
    IAnimatorView v = new TextAnimatorView(m, new BadAppendable(), 5);
    v.display();
  }
}
