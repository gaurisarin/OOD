import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;

import cs3500.animator.model.EasyAnimatorModel;
import cs3500.animator.model.ReadOnlyIAnimatorModel;
import cs3500.animator.view.CompositeAnimatorView;
import cs3500.animator.view.IAnimatorView;

class MockAnimatorView implements IAnimatorView {
  private final StringBuilder log;
  private ActionListener l;
  private final Map<String, JButton> buttons;

  MockAnimatorView(StringBuilder log) {
    this.log = log;
    // get list of buttons in composite view
    IAnimatorView v = new CompositeAnimatorView("d", 1, 1,
            new EasyAnimatorModel(1, 1));
    buttons = new HashMap<>(v.getButtons());
  }

  @Override
  public void addListener(ActionListener listener) {
    l = listener;
    log.append("Added listener to view");
  }

  @Override
  public Map<String, JButton> getButtons() {
    log.append("asked for view buttons");
    return buttons;
  }

  @Override
  public void display() throws IOException {
    log.append("Set to display");
  }

  @Override
  public boolean linkedTo(ReadOnlyIAnimatorModel m) {
    log.append("Asked about link");
    return true;
  }

  public void callActionEvent(ActionEvent e) {
    log.append("Called action event: ").append(e.getActionCommand());
    l.actionPerformed(e);
  }
}