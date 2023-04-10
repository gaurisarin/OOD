import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;

import cs3500.animator.controller.AnimationListener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the aniamtion panel.
 */
public class AnimationListenerTest {
  private AnimationListener a;
  private StringBuilder log;

  @Before
  public void init() {
    log = new StringBuilder();
    a = new AnimationListener(new MockAnimatorController(log));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNoController() {
    AnimationListener b = new AnimationListener(null);
  }

  @Test
  public void testActionNothing() {
    ActionEvent e = new ActionEvent(this, 1, "something else");
    a.actionPerformed(e);
    assertEquals("", log.toString());
  }

  @Test
  public void testActionPlayPause() {
    ActionEvent e = new ActionEvent(this, 1, "play-pause");
    a.actionPerformed(e);
    assertTrue(log.toString().contains("toggled play"));
  }

  @Test
  public void testActionLoop() {
    ActionEvent e = new ActionEvent(this, 1, "loop");
    a.actionPerformed(e);
    assertTrue(log.toString().contains("toggled loop"));
  }

  @Test
  public void testActionFaster() {
    ActionEvent e = new ActionEvent(this, 1, "faster");
    a.actionPerformed(e);
    assertTrue(log.toString().contains("changed tempo by: 2.0"));
  }

  @Test
  public void testActionSlower() {
    ActionEvent e = new ActionEvent(this, 1, "slower");
    a.actionPerformed(e);
    assertTrue(log.toString().contains("changed tempo by: 0.5"));
  }
}
