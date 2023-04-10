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
import cs3500.animator.view.SVGAnimatorView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * JUnit tests for the SVGAnimatorView class.
 */
public class SVGAnimatorViewTest {
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
    IAnimatorView v = new SVGAnimatorView(null, new BadAppendable(), 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createViewNoOut() {
    IAnimatorView v = new SVGAnimatorView(m, null, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createViewBadTempo() {
    IAnimatorView v = new SVGAnimatorView(m, new BadAppendable(), 0);
  }

  @Test
  public void testLinkedTo() {
    IAnimatorView v = new SVGAnimatorView(m, new BadAppendable(), 5);
    assertTrue(v.linkedTo(m));
    assertFalse(v.linkedTo(null));
    assertFalse(v.linkedTo(emptyM));
  }

  @Test(expected = IOException.class)
  public void testDisplayBadAppendable() throws IOException {
    IAnimatorView v = new SVGAnimatorView(m, new BadAppendable(), 5);
    v.display();
  }

  @Test
  public void testDisplayIsConstant() throws IOException {
    Appendable out = new StringBuilder();
    IAnimatorView v = new SVGAnimatorView(m, out, 5);
    v.display();
    String one = out.toString();
    v.display();
    v.display();
    assertEquals(one, out.toString());
  }

  @Test
  public void testDisplayContent() throws IOException {
    Appendable out = new StringBuilder();
    IAnimatorView v = new SVGAnimatorView(m, out, 5);
    v.display();
    String corr = "<?xml version=\"1.0\" standalone=\"yes\"?>\n"
            + "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"200px\" height=\"200px\" "
            + "version=\"1.1\" >\n\n"
            + "<rect id=\"r2\" x=\"20.0\" y=\"20.0\" width=\"15\" height=\"15\" "
            + "fill=\"rgb(250, 0, 0)\"  >\n</rect>\n\n<ellipse "
            + "id=\"e1\" cx=\"5.0\" cy=\"10.0\" rx=\"10\" ry=\"10\" fill=\"rgb(250, 0, 0)\"  >\n"
            + "<animate attributeName=\"cx\" attributeType=\"XML\" begin=\"1.0s\" dur=\"3.0s\" "
            + "fill=\"freeze\" by=\"75.0\" />\n"
            + "<animate attributeName=\"cy\" attributeType=\"XML\" begin=\"1.0s\" dur=\"3.0s\" "
            + "fill=\"freeze\" by=\"150.0\" />\n"
            + "<animate attributeName=\"cy\" attributeType=\"XML\" begin=\"4.4s\" dur=\"1.6s\" "
            + "fill=\"freeze\" by=\"-40.0\" />\n</ellipse>\n\n"
            + "<ellipse id=\"e2\" cx=\"20.0\" cy=\"75.0\" rx=\"3\" ry=\"10\" "
            + "fill=\"rgb(0, 0, 250)\"  >\n</ellipse>\n\n"
            + "<rect id=\"r1\" x=\"20.5\" y=\"3.6\" width=\"6\" height=\"20\" "
            + "fill=\"rgb(150, 150, 150)\"  >\n"
            + "</rect>\n\n</svg>";
    assertEquals(corr, out.toString());
  }
}
