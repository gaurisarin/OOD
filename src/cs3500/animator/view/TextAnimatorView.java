package cs3500.animator.view;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import cs3500.animator.model.IShape;
import cs3500.animator.model.ReadOnlyIAnimatorModel;

/**
 * A simple view that outputs the entire animation as a list of shapes with a
 * list of timestamped transformations with their start and end values.
 */
public class TextAnimatorView implements IAnimatorView {
  private final ReadOnlyIAnimatorModel model;
  private final Appendable out;
  private final double tempo;
  private boolean prevOutput;

  /**
   * Constructs a new TextAnimatorView, which renders the entire animation details
   * as one string.
   *
   * @param model the IAnimatorModel to display the state of
   * @param tempo the speed of the animation, in ticks per second.
   * @throws IllegalArgumentException if the model or appendable is null,
   *                                  or if the tempo is invalid.
   */
  public TextAnimatorView(ReadOnlyIAnimatorModel model, Appendable out, double tempo) {
    if (model == null || out == null || tempo <= 0) {
      throw new IllegalArgumentException("Invalid arguments");
    }

    this.model = model;
    this.out = out;
    this.tempo = tempo;
    this.prevOutput = false;
  }

  @Override
  public void display() throws IOException {
    StringBuilder res = new StringBuilder();
    for (Map.Entry<String, IShape> entry : model.getShapes().entrySet()) {
      res.append("Shape ")
              .append(entry.getKey())
              .append(" ")
              .append(entry.getValue().toStandardString(tempo))
              .append("\n");
    }

    if (!prevOutput) {
      out.append(res.toString());
      prevOutput = true;
      if (out instanceof FileWriter) {
        FileWriter o2 = (FileWriter) out;
        o2.close();
      }
    }
  }

  @Override
  public boolean linkedTo(ReadOnlyIAnimatorModel m) {
    return this.model.equals(m);
  }
}
