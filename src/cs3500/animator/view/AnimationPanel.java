package cs3500.animator.view;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import cs3500.animator.model.IShape;
import cs3500.animator.model.ReadOnlyIAnimatorModel;

/**
 * Represents the animation window for a visual view of the animation.
 */
public class AnimationPanel extends JPanel {
  private final ReadOnlyIAnimatorModel model;

  /**
   * Constructs a new panel to draw the model provided.
   *
   * @param model the model to draw on the panel
   * @throws IllegalArgumentException if the model is null
   */
  public AnimationPanel(ReadOnlyIAnimatorModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }

    this.model = model;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    for (IShape shape : model.getShapes().values()) {
      if (shape.isVisible()) {
        shape.drawWith(g2);
      }
    }
  }
}
