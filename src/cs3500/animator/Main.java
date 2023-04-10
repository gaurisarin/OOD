package cs3500.animator;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;

import cs3500.animator.controller.IAnimatorController;
import cs3500.animator.controller.InteractiveAnimatorController;
import cs3500.animator.io.AnimationBuilder;
import cs3500.animator.io.AnimationFileReader;
import cs3500.animator.io.TweenModelBuilder;
import cs3500.animator.model.IAnimatorModel;
import cs3500.animator.view.CompositeAnimatorView;
import cs3500.animator.view.IAnimatorView;
import cs3500.animator.view.SVGAnimatorView;
import cs3500.animator.view.TextAnimatorView;
import cs3500.animator.view.VisualAnimatorView;

/**
 * Main class that holds the main method to start the animator.
 */
public class Main {

  /**
   * Starts a new animation using the provided arguments.
   * @param args an array of strings representing different parameters.
   *             <p>-in [input.txt] - What file to animate. Must be in the correct format.</p>
   *             <p>-view [visual | svg | text] - the type of animation to do.</p>
   *             <p>-out [output.txt] - the file to output the result of the animation,
   *             if applicable.</p>
   *             <p>-speed [ticks per second] - the speed to play the animation at.</p>
   * @throws IllegalArgumentException if any of the arguments are invalid
   */
  public static void main(String... args) {
    String inputFile = null;
    String output = "System.out";
    String viewType = null;
    int ticksPerSecond = 1;

    // validate args
    for (int i = 0; i < args.length; i += 2) {
      String pairType = args[i];
      try {
        switch (pairType) {
          case "-in":
            inputFile = args[i + 1];
            break;
          case "-view":
            String v = args[i + 1];
            if (v.equals("text") || v.equals("visual") || v.equals("svg")
                    || v.equals("interactive")) {
              viewType = args[i + 1];
            }
            break;
          case "-out":
            output = args[i + 1];
            break;
          case "-speed":
            ticksPerSecond = Integer.parseInt(args[i + 1]);
            break;
          default:
            throw new IllegalArgumentException("Unknown parameter");
        }
      } catch (Exception e) {
        throw new IllegalArgumentException("Invalid args: " + e.getMessage());
      }
    }

    if (inputFile == null || viewType == null) {
      throw new IllegalArgumentException("Must specify valid input file and view type");
    }

    // generate model
    AnimationFileReader reader = new AnimationFileReader();
    TweenModelBuilder<IAnimatorModel> builder = new AnimationBuilder();
    IAnimatorModel m;
    try {
      m = reader.readFile(inputFile, builder);
    } catch (FileNotFoundException fnfe) {
      throw new IllegalArgumentException("File not found");
    } catch (InputMismatchException | IllegalStateException e) {
      throw new IllegalArgumentException("Invalid file: " + e.getMessage());
    }

    Appendable out;
    // determine output
    if (output.equals("System.out")) {
      out = System.out;
    } else {
      try {
        out = new FileWriter(output, false);
      } catch (IOException e) {
        throw new IllegalArgumentException("Error setting output file");
      }
    }

    // generate view
    IAnimatorView v = null;
    switch (viewType) {
      case "text":
        v = new TextAnimatorView(m, out, ticksPerSecond);
        break;
      case "visual":
        v = new VisualAnimatorView("Animation", m.getWidth(), m.getHeight(), m);
        break;
      case "svg":
        v = new SVGAnimatorView(m, out, ticksPerSecond);
        break;
      case "interactive":
        v = new CompositeAnimatorView("Animation", m.getWidth(), m.getHeight(), m);
        break;
      default:
        throw new IllegalArgumentException("Invalid view type");
    }

    IAnimatorController c = new InteractiveAnimatorController(ticksPerSecond, m, v, false, null);
    c.play();
  }
}
