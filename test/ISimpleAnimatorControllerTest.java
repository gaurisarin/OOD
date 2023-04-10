import org.junit.Test;

import cs3500.animator.controller.ISimpleAnimatorController;
import cs3500.animator.controller.SimpleAnimatorController;
import cs3500.animator.model.EasyAnimatorModel;
import cs3500.animator.model.IAnimatorModel;
import cs3500.animator.view.IAnimatorView;
import cs3500.animator.view.TextAnimatorView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * JUnit tests for an implementation of ISimpleAnimatorController.
 */
public class ISimpleAnimatorControllerTest {
  @Test(expected = IllegalArgumentException.class)
  public void createControllerNoModel() {
    IAnimatorModel m = new EasyAnimatorModel(500, 200);
    IAnimatorView v = new TextAnimatorView(m, new BadAppendable(), 5);
    ISimpleAnimatorController c = new SimpleAnimatorController(5, null, v);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createControllerNoView() {
    IAnimatorModel m = new EasyAnimatorModel(500, 200);
    ISimpleAnimatorController c = new SimpleAnimatorController(5, m, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createControllerBadTempo() {
    IAnimatorModel m = new EasyAnimatorModel(500, 200);
    IAnimatorView v = new TextAnimatorView(m, new BadAppendable(), 5);
    ISimpleAnimatorController c = new SimpleAnimatorController(0, m, v);
  }

  @Test
  public void testPlayBadOut() {
    IAnimatorModel m = new EasyAnimatorModel(500, 200);
    IAnimatorView v = new TextAnimatorView(m, new BadAppendable(), 5);
    ISimpleAnimatorController c = new SimpleAnimatorController(5, m, v);
    c.play();
    // this just here to run test, and to show controller handled IOException
    assertEquals(5, c.getTempo(), 0.01);
  }

  @Test
  public void testPlay() throws InterruptedException {
    StringBuilder logModel = new StringBuilder();
    StringBuilder logView = new StringBuilder();
    IAnimatorModel m = new MockAnimationModel(logModel);
    IAnimatorView v = new MockAnimatorView(logView);
    ISimpleAnimatorController c = new SimpleAnimatorController(5, m, v);
    assertEquals("Asked about link", logView.toString());
    c.play();
    // waiting for whole mock animation to play out
    Thread.sleep(2000);
    assertTrue(logView.toString().contains("Set to display"));
    assertTrue(logModel.toString().contains("Ran model"));
    assertTrue(logModel.toString().contains("Checked if ended"));
  }

  @Test
  public void testGetTempo() {
    IAnimatorModel m = new EasyAnimatorModel(500, 200);
    IAnimatorView v = new TextAnimatorView(m, new BadAppendable(), 5);
    ISimpleAnimatorController c = new SimpleAnimatorController(5, m, v);
    assertEquals(5, c.getTempo(), 0.01);
  }
}
