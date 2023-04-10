package cs3500.animator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import cs3500.animator.model.ReadOnlyIAnimatorModel;

/**
 * Renders the view as a visual window, utilizing Java Swing.
 */
public class VisualAnimatorView extends JFrame implements IAnimatorView {
  private final ReadOnlyIAnimatorModel model;
  // commented out for autograder style, since not used in any methods yet
  // gives me -2
  // private final AnimationPanel panel;
  private boolean isVisible;

  /**
   * Constructs a new visual animation of the provided model.
   *
   * @param windowTitle the name of the window
   * @param width       the initial width of the window
   * @param height      the initial height of the window
   * @param model       the model to draw the animation of
   * @throws IllegalArgumentException if the model or windowTitle is null, or if width/height are
   *                                  invalid.
   */
  public VisualAnimatorView(String windowTitle, int width, int height,
                            ReadOnlyIAnimatorModel model) {
    super(windowTitle);
    if (model == null || windowTitle == null) {
      throw new IllegalArgumentException("args cannot be null");
    }
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Height/width must be positive");
    }

    setSize(width, height);
    setLocation(0, 0);
    this.setLayout(new BorderLayout());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel panel = new AnimationPanel(model);
    panel.setPreferredSize(new Dimension(width, height));
    this.add(panel, BorderLayout.CENTER);
    isVisible = false;
    this.model = model;
  }

  @Override
  public void display() {
    if (!isVisible) {
      this.repaint();
      this.setVisible(true);
      isVisible = true;
    } else {
      this.repaint();
    }
  }

  @Override
  public boolean linkedTo(ReadOnlyIAnimatorModel m) {
    return this.model.equals(m);
  }
}
