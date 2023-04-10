package cs3500.animator.view;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;

import cs3500.animator.model.ReadOnlyIAnimatorModel;

/**
 * <p>An interface for a simple 2D animation view.</p>
 * <p>Renders the model to display the shapes and how they change over time.</p>
 */
public interface IAnimatorView {

  /**
   * Set up a listener to handle interactive events in this view.
   *
   * @param listener the handler for user interaction
   * @throws IllegalArgumentException if the listener is null
   */
  public default void addListener(ActionListener listener) {
    // do nothing as default
  }

  /**
   * Returns a Map of buttons for this view.
   * @return a map of button name - JButton pairs
   */
  public default Map<String, JButton> getButtons() {
    return new HashMap<>();
  }

  /**
   * Renders the model. Depending on implementation,
   * may render entire model or just the current state of the model.
   */
  public void display() throws IOException;

  /**
   * Returns if this view is initialized to render to the model provided.
   * @param m the model to check if this view is linked to
   */
  public boolean linkedTo(ReadOnlyIAnimatorModel m);
}
