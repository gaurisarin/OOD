import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import cs3500.animator.controller.IAnimatorController;
import cs3500.animator.controller.InteractiveAnimatorController;
import cs3500.animator.model.Color;
import cs3500.animator.model.EasyAnimatorModel;
import cs3500.animator.model.Event;
import cs3500.animator.model.EventType;
import cs3500.animator.model.IAnimatorModel;
import cs3500.animator.model.IShape;
import cs3500.animator.model.MoveTransform;
import cs3500.animator.model.Rectangle;
import cs3500.animator.view.IAnimatorView;
import cs3500.animator.view.SVGAnimatorView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests to tets the composite animator controller.
 * Uses a mock.
 */
public class InteractiveAnimationControllerTest {
  private Timer t;
  private IAnimatorController c;
  private StringBuilder log;
  private IAnimatorModel m;
  private MockAnimatorView v;

  @Before
  public void init() {
    t = new Timer(1000, null);
    t.setInitialDelay(0);
    log = new StringBuilder();
    m = new EasyAnimatorModel(500, 500);
    IShape testShape = new Rectangle(5, 5, new Color(100, 100, 100), 0, 50, 50);
    m.addShape("test", testShape);
    m.addTransformTo("test", new Event(0, 500, EventType.MOVE), new MoveTransform(5, 10));
    v = new MockAnimatorView(log);
    c = new InteractiveAnimatorController(1, m, v, false, t);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstrNoModel() {
    IAnimatorController cb = new InteractiveAnimatorController(1, null, v, false, null);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstrNoView() {
    IAnimatorController cb = new InteractiveAnimatorController(1, m, null, false, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstrBadTempo() {
    IAnimatorController cb = new InteractiveAnimatorController(-3, m, v, false, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstrBadView() {
    IAnimatorView v = new SVGAnimatorView(new EasyAnimatorModel(200, 200), log, 5);
    IAnimatorController cb = new InteractiveAnimatorController(1, m, v, false, null);
  }

  @Test
  public void testPlay() {
    ActionListener l = e -> {
      assertTrue(m.getTick() > 0);
      assertTrue(log.toString().contains("Set to display"));
      t.stop();
    };

    t.addActionListener(l);
    c.play();
    while (t.isRunning()) {
      // wait for timer to end
    }
  }

  @Test
  public void testPlayBadAppendable() {
    IAnimatorView v2 = new SVGAnimatorView(m, new BadAppendable(), 5);
    c = new InteractiveAnimatorController(1, m, v2, false, t);

    ActionListener l = e -> t.stop();
    t.addActionListener(l);
    c.play();
    while (t.isRunning()) {
      // wait for timer to end
    }
    assertEquals(m.getTick(), 0);
    assertFalse(log.toString().contains("Set to display"));
  }

  @Test
  public void testChangeTempo() {
    c.changeTempo(2);
    assertEquals(2, c.getTempo(), 0.01);
    assertEquals(500, t.getDelay());
    c.changeTempo(0.25);
    assertEquals(0.5, c.getTempo(), 0.01);
    assertEquals(t.getDelay(), 2000);
  }

  @Test
  public void testIfTextIsChanged() {
    c = new InteractiveAnimatorController(1, m, v, true, t);
    ActionListener l = e -> {
      assertEquals("Pause", v.getButtons().get("play-pause").getText());
      assertEquals("No Loop", v.getButtons().get("loop").getText());
      v.callActionEvent(new ActionEvent(this, 1, "play-pause"));
      v.callActionEvent(new ActionEvent(this, 1, "loop"));
      assertEquals("Play", v.getButtons().get("play-pause").getText());
      assertEquals("Loop", v.getButtons().get("loop").getText());
      t.stop();
    };
    t.addActionListener(l);
    t.start();
    while (t.isRunning()) {
      // wait for timer to end
    }
  }
}