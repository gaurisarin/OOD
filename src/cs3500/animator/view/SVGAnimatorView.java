package cs3500.animator.view;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import cs3500.animator.model.IShape;
import cs3500.animator.model.ReadOnlyIAnimatorModel;

/**
 * A SVGAnimatorView renders the animation as text that adheres to the
 * SVG image format standard.
 */
public class SVGAnimatorView implements IAnimatorView {
  private final ReadOnlyIAnimatorModel model;
  private final Appendable out;
  private final double tempo;
  private boolean prevOutput;

  /**
   * Constructs a new view, which outputs the animation as an SVG-standard compliant string.
   *
   * @param model the model to create the SVG string for
   * @param out   where to put the SVG string
   * @throws IllegalArgumentException if either the model or appendable is null, or if tempo is
   *                                  invalid
   */
  public SVGAnimatorView(ReadOnlyIAnimatorModel model, Appendable out, double tempo) {
    if (model == null || out == null) {
      throw new IllegalArgumentException("Model or appendable cannot be null");
    }
    if (tempo <= 0) {
      throw new IllegalArgumentException("Tempo cannot be <= 0");
    }
    this.model = model;
    this.out = out;
    this.tempo = tempo;
    this.prevOutput = false;
  }

  @Override
  public void display() throws IOException {

    if (prevOutput) {
      return;
    }
    out.append("<?xml version=\"1.0\" standalone=\"yes\"?>\n");
    out.append("<svg xmlns=\"http://www.w3.org/2000/svg\" "
            + "width=\"" + model.getWidth() + "px\" height=\"" + model.getHeight()
            + "px\" version=\"1.1\" >\n\n");
    for (Map.Entry<String, IShape> entry : model.getShapes().entrySet()) {
      String name = entry.getKey();
      IShape shape = entry.getValue();
      out.append(shape.toSVGString(name, tempo, model.getWidth(), model.getHeight()))
              .append("\n\n");
    }
    out.append("</svg>");
    prevOutput = true;
    if (out instanceof FileWriter) {
      FileWriter o2 = (FileWriter) out;
      o2.close();
    }
  }

  @Override
  public boolean linkedTo(ReadOnlyIAnimatorModel m) {
    return this.model.equals(m);
  }
}
