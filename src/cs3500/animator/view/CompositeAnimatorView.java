package cs3500.animator.view;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JButton;

import cs3500.animator.model.ReadOnlyIAnimatorModel;

/**
 * A composite animator view extends the functionality of a visual animator view, adding
 * user interactivity through buttons that adjust the playback, speed, and looping of the
 * animation.
 */
public class CompositeAnimatorView extends VisualAnimatorView implements IAnimatorView {
  private final Map<String, JButton> buttons;

  /**
   * Constructs a new CompositeAnimatorView.
   * @param windowTitle the name of the animation window
   * @param width the width of the animation window
   * @param height the height of the animation window
   * @param model the model to control the animation of
   * @throws IllegalArgumentException if any of the params are invalid
   */
  public CompositeAnimatorView(String windowTitle, int width, int height,
                               ReadOnlyIAnimatorModel model) {
    super(windowTitle, width, height, model);
    this.buttons = new HashMap<>();
    addButtons();

    this.pack();
  }

  private void addButtons() {
    String[] commands = new String[]{ "play-pause", "restart", "loop", "faster", "slower" };

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());
    this.add(buttonPanel, BorderLayout.SOUTH);

    for (ControlPanel b : ControlPanel.values()) {
      JButton button = new JButton(b.getString());

      String internalName = commands[b.ordinal()];
      button.setActionCommand(internalName);
      buttonPanel.add(button);
      buttons.put(internalName, button);
    }
  }

  @Override
  public Map<String, JButton> getButtons() {
    return buttons;
  }

  @Override
  public void addListener(ActionListener listener) {
    for (JButton button : buttons.values()) {
      button.addActionListener(listener);
    }
  }
}
