package cs3500.animator.view;

/**
 * Enumaration that assigns a string to the control
 * pannel used for Composite ANiamtorView.
 * Helps to make view interactive.
 */
public enum ControlPanel {
  PLAY("Pause"),
  RESTART("Restart"),
  ENABLE("Loop"),
  SPEEDUP("Faster"),
  SLOWDOWN("Slower");

  private final String symbol;
  ControlPanel(String symbol) {
    this.symbol = symbol;
  }

  public String getString() {
    return this.symbol;
  }


}
