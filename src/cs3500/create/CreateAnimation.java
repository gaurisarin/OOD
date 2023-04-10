package cs3500.create;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.animator.model.Color;

/**
 * Creates an animation of the fox, chicken, and grain puzzle.
 */
public class CreateAnimation {
  private static final String[] NAMES = new String[]{ "farmer", "fox", "chicken", "grain", "boat" };
  private static final Color[] COLORS = new Color[]{
    new Color(254, 220, 200),
    new Color(254, 128, 0),
    new Color(235, 235, 235),
    new Color(254, 254, 51),
    new Color(146, 104, 19)
  };
  private static final int[] Y_POSITIONS = new int[] { 50, 50, 50, 50, 70 };
  private static final int[] X_POSITIONS = new int[] { 20, 40, 60, 80, 50 };
  private static final int MOVE_AMOUNT = 300;
  private static final int MOVE_TIME = 20;
  private static final int CANVAS_SIZE = 500;
  private static final int SHAPE_SIZE = 25;
  private static final int LENGTH = 5000;


  /**
   * main creates animation.
   * @param args takes in multiple Strings.
   */
  public static void main(String... args) {
    // false: not across, true: across
    // in order: farmer, fox, chicken, grain, boat
    List<Boolean> pos = new ArrayList<>(Arrays.asList(false, false, false, false, false));
    List<String> steps = new ArrayList<>();
    describeSetup(steps);
    int count = 0;

    // while not all on other side
    while (!pos.stream().allMatch(p -> p) && count < 1000) {
      List<Boolean> oldPos = new ArrayList<>(pos);
      if (pos.get(0) == pos.get(1) && pos.get(0) == pos.get(2)) {
        pos.set(2, !pos.get(2));
      } else if (!pos.get(0) && !pos.get(1)) {
        pos.set(1, !pos.get(1));
      } else if (pos.get(0) == pos.get(2) && pos.get(0) == pos.get(3)) {
        pos.set(3, !pos.get(3));
      } else if (!pos.get(0) && !pos.get(2)) {
        pos.set(2, !pos.get(2));
      }
      pos.set(0, !pos.get(0));
      pos.set(4, !pos.get(4));
      describeChange(oldPos, pos, steps, count);

      count++;
    }

    writeToFile(steps);
  }

  // initialize shapes
  private static void describeSetup(List<String> res) {
    res.add("canvas " + CANVAS_SIZE + " " + CANVAS_SIZE);
    res.add("rectangle name back min-x 0 min-y 0 width " + CANVAS_SIZE + " height " + CANVAS_SIZE
            + " color 0 0 0.8 from 0 to " + LENGTH);
    res.add("oval name is1 center-x " + X_POSITIONS[4] + " center-y " + Y_POSITIONS[0]
            + " x-radius " + (SHAPE_SIZE * 3) + " y-radius " + (SHAPE_SIZE * 2)
            + " color 0 0.8 0 from 0 to " + LENGTH);
    res.add("oval name is2 center-x " + (X_POSITIONS[4] + MOVE_AMOUNT)
            + " center-y " + Y_POSITIONS[0] + " x-radius " + (SHAPE_SIZE * 3) + " y-radius "
            + (SHAPE_SIZE * 2) + " color 0 0.8 0 from 0 to " + LENGTH);
    res.add("rectangle name back min-x 0 min-y 0 width " + CANVAS_SIZE + " height " + CANVAS_SIZE
            + " color 0 0 0.8 from 0 to " + LENGTH);
    for (int i = 4; i >= 0; i--) {
      int size = SHAPE_SIZE;
      if (i == 4) {
        size = SHAPE_SIZE * 3;
      }
      res.add("oval name " + NAMES[i]
              + " center-x " + (X_POSITIONS[i] + SHAPE_SIZE)
              + " center-y " + (Y_POSITIONS[i] + SHAPE_SIZE)
              + " x-radius " + size
              + " y-radius " + SHAPE_SIZE
              + " color " + (COLORS[i].red() / 255f) + " "
              + (COLORS[i].green() / 255f) + " "
              + (COLORS[i].blue() / 255f)
              + " from 0 to " + LENGTH);
    }
  }

  private static void describeChange(List<Boolean> o, List<Boolean> n,
                                     List<String> res, int stepCount) {
    for (int i = 0; i < n.size(); i++) {
      // change has occurred
      if (o.get(i) != n.get(i)) {
        StringBuilder move = new StringBuilder();
        move.append("move name ").append(NAMES[i]).append(" moveto ");
        if (!n.get(i)) {
          move.append(X_POSITIONS[i] + MOVE_AMOUNT).append(" ")
                  .append(Y_POSITIONS[i]).append(" ")
                  .append(X_POSITIONS[i]).append(" ")
                  .append(Y_POSITIONS[i]).append(" ");
        } else {
          move.append(X_POSITIONS[i]).append(" ")
                  .append(Y_POSITIONS[i]).append(" ")
                  .append(X_POSITIONS[i] + MOVE_AMOUNT).append(" ")
                  .append(Y_POSITIONS[i]).append(" ");
        }
        move.append("from ")
                .append(stepCount * MOVE_TIME)
                .append(" to ")
                .append((stepCount + 1) * MOVE_TIME);
        res.add(move.toString());
      }
    }
  }

  private static void writeToFile(List<String> list) {
    try {
      FileOutputStream out = new FileOutputStream("animation.txt");

      for (String str : list) {
        String line = str + "\n";
        out.write(line.getBytes());
      }

      out.close();
    } catch (Exception e) {
      // do nothing
    }
  }

}
