package cs3500.animator.model;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.NoSuchElementException;
import java.util.Map;

/**
 * An abstract class that represents all Shapes supported.
 */
abstract class AShape implements IShape {
  private double x;
  private double y;
  private Color color;
  private double deg;
  private NavigableMap<Event, ITransform> transforms;
  private int highestTick;
  private double width;
  private double height;
  private boolean visible;

  AShape(double x, double y, Color color, double deg, double width, double height) {
    if (color == null) {
      throw new IllegalArgumentException("cs3500.animator.model.Color cannot be null");
    }

    this.x = x;
    this.y = y;
    this.color = color;
    if (deg >= 360) {
      this.deg = deg - Math.floor(deg / 360) * 360;
    } else if (deg < 0) {
      this.deg = deg + Math.floor(deg / -360) * 360;
    } else {
      this.deg = deg;
    }
    this.transforms = new TreeMap<>();
    this.highestTick = 0;
    this.width = width;
    this.height = height;
    this.visible = true;
  }

  @Override
  public void addTransform(Event e, ITransform t) {
    if (e == null || t == null) {
      throw new IllegalArgumentException("Pair or transform cannot be null.");
    }
    if (!e.getType().isType(t)) {
      throw new IllegalArgumentException("cs3500.animator.model.Event type does not"
              + "correspond to transform class");
    }

    if (e.getStart() > highestTick) { // sometime in the future, add with nothing buffer
      transforms.put(new Event(highestTick, e.getStart(), EventType.NOTHING),
              new NoTransform());
      highestTick = e.getEnd();
    } else { // sometime before highest tick, check for conflicts
      for (Event event : this.getTransforms().keySet()) {
        // check if event is overlapping (not just next to each other)
        if (e.during(event) > 0) {
          if (event.getType() == e.getType()) { // conflict, same event type already exists
            throw new IllegalArgumentException("Cannot add transform, transform of "
                    + "same type already exists at the time");
          }

          if (event.getType() == EventType.NOTHING) { // nothing event exists in space
            if (e.getStart() <= event.getStart()) { // new event overlaps START of existing event
              if (e.getEnd() < event.getEnd()) { // ends before existing ends, add spacer
                transforms.put(new Event(e.getEnd(), event.getEnd(), EventType.NOTHING),
                        new NoTransform());
              }
            }
            if (e.getEnd() >= event.getEnd()) { // new event overlaps END of existing event
              if (e.getStart() > event.getStart()) { // starts after existing starts, add spacer
                transforms.put(new Event(event.getStart(), e.getStart(), EventType.NOTHING),
                        new NoTransform());
              }
            }
            transforms.remove(event); // get rid of old existing event
          }
        }
      }
    }
    transforms.put(e, t);
    if (e.getEnd() > highestTick) {
      highestTick = e.getEnd();
    }
  }

  @Override
  public void removeTransform(Event e) {

    // this method is more advanced than it has to be

    if (e == null) {
      throw new IllegalArgumentException("cs3500.animator.model.Event to remove cannot be null");
    }
    ITransform t = transforms.get(e);
    if (t == null) {
      throw new NoSuchElementException("No transform with provided event key");
    }

    if (e.getEnd() == highestTick) { // transform to remove is at the end of the map of transforms
      highestTick = e.getStart();
      transforms.remove(e);
    } else { // transform somewhere in middle, checking if removing adds gaps. If does, add
      // cs3500.animator.model.NoTransform
      int lowBound = e.getStart();
      int highBound = e.getEnd();

      // checking if other transforms fill in the gap created by removing transform
      for (Event event : transforms.keySet()) {
        if (event.during(new Event(lowBound, highBound, EventType.NOTHING)) > 0
                && !event.equals(e)) {
          if (event.getStart() <= lowBound) {
            lowBound = event.getEnd();
          }
          if (event.getEnd() >= highBound) {
            highBound = event.getStart();
          }

          if (lowBound >= highBound) { // found gap covered by other transforms
            transforms.remove(e);
            return;
          }
        }
      }
      // arriving here means it did not fill in gap completely. Fill in with nothing transform
      transforms.remove(e);
      transforms.put(new Event(lowBound, highBound, EventType.NOTHING), new NoTransform());
    }
  }

  @Override
  public NavigableMap<Event, ITransform> getTransforms() {
    return new TreeMap<>(transforms);
  }

  @Override
  public int lastTick() {
    return this.highestTick;
  }

  @Override
  public double getX() {
    return this.x;
  }

  @Override
  public double getY() {
    return this.y;
  }

  @Override
  public Color getColor() {
    return this.color;
  }

  @Override
  public int getDeg() {
    return (int) Math.round(this.deg);
  }

  @Override
  public int getWidth() {
    return (int) Math.round(this.width);
  }

  @Override
  public int getHeight() {
    return (int) Math.round(this.height);
  }

  @Override
  public boolean isVisible() {
    return this.visible;
  }

  @Override
  public IShape copy() {
    AShape copy = this.makeCopy(x, y, color, deg, width, height);
    copy.setState(new TreeMap<>(this.transforms), this.highestTick);
    return copy;
  }

  private void setState(NavigableMap<Event, ITransform> transforms, int tick) {
    this.transforms = transforms;
    this.highestTick = tick;
  }

  abstract AShape makeCopy(double x, double y, Color color,
                           double deg, double width, double height);

  // changes the position of the shape by the given amount
  @Override
  public void move(double xAmt, double yAmt) {
    //throw new IllegalArgumentException();
    this.x += xAmt;
    this.y += yAmt;
  }

  @Override
  public void rotate(double deg) {
    this.deg += deg;
    if (this.deg >= 360) {
      this.deg = this.deg - Math.floor(this.deg / 360) * 360;
    } else if (this.deg < 0) {
      this.deg = this.deg + (Math.floor(this.deg / -360) + 1) * 360;
    }
  }

  @Override
  public void changeColor(float redAmt, float greenAmt, float blueAmt) {
    this.color = color.addColor(redAmt, greenAmt, blueAmt);
  }

  @Override
  public void toggleVisibility(boolean makeVisible) {
    visible = makeVisible;
  }

  @Override
  public void scaleAxis(String axis, double amt) {
    switch (axis) {
      case "x":
        this.width += amt;
        break;
      case "y":
        this.height += amt;
        break;
      default:
        throw new IllegalArgumentException("Invalid axis");
    }
  }

  // returns the initial values for the shape
  abstract String[] initialState();

  @Override
  public String toStandardString(double tempo) {
    if (tempo <= 0) {
      throw new IllegalArgumentException("Tempo must be positive");
    }

    StringBuilder out = new StringBuilder();
    out.append(this.shapeType()).append("\n").append(this.stringHeader()).append("\n");
    String[] initial = this.initialState();
    for (Map.Entry<Event, ITransform> entry : transforms.entrySet()) {
      Event e = entry.getKey();
      ITransform t = entry.getValue();
      int start = e.getStart();
      int end = e.getEnd();

      out.append(describeState(initial, start, tempo)).append("  |  ");
      t.transformState(initial, end - start + 1);
      out.append(describeState(initial, end, tempo)).append("\n");
    }

    return out.toString();
  }

  public String toSVGString(String name, double tempo, int width, int height) {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null");
    }
    if (tempo <= 0 || width <= 0 || height <= 0) {
      throw new IllegalArgumentException("tempo/width/height invalid");
    }
    StringBuilder out = new StringBuilder();

    // svg shape declarations
    out.append(this.uniqueSVGString(name));
    out.append("fill=\"rgb(")
            .append(color.red()).append(", ")
            .append(color.green()).append(", ")
            .append(color.blue())
            .append(")\"  >\n");

    String coordType = "";
    if (this.shapeType().equals("ellipse")) {
      coordType = "c";
    }

    // animations
    String[] initial = this.initialState();
    for (Map.Entry<Event, ITransform> entry : transforms.entrySet()) {
      Event e = entry.getKey();
      ITransform t = entry.getValue();
      float start = (float) (e.getStart() / tempo);
      int amt = e.getEnd() - e.getStart();

      out.append(t.animateSVG(initial, start,
              (float) ((e.getEnd() - e.getStart()) / tempo),
              amt, coordType));
      t.transformState(initial, amt);
    }

    out.append("</")
            .append(this.shapeType())
            .append(">");

    return out.toString();
  }

  // generates the bits of SVG that are different from shape to shape.
  abstract String uniqueSVGString(String name);

  private String describeState(String[] state, int time, double tempo) {
    //#        t  x   y   w  h   r   g  b    t   x   y   w  h   r   g  b
    //motion R 1  200 200 50 100 255 0  0    10  200 200 50 100 255 0  0
    // number of times transform set to apply to shape
    if (Math.abs(tempo - 1) <= 0.01) {
      tempo = 1;
    }
    return (time * tempo) + " " + String.join(" ", roundDoubles(state));
  }

  // rounds the doubles provided by shape property string arrays
  // useful helper method for transforms report formatting.
  private static String[] roundDoubles(String[] s) {
    String[] rounded = new String[s.length];
    for (int i = 0; i < s.length; i++) {
      String e = s[i];
      double val = Double.parseDouble(e);
      String roundS = String.valueOf((int) Math.round(val));
      rounded[i] = roundS;
    }

    return rounded;
  }

  @Override
  public String toString() {
    return toStandardString(1);
  }

  // generates the header of the string output
  abstract String stringHeader();

  // returns the type of shape as a string.
  abstract String shapeType();

  boolean sameParams(AShape other) {
    return (Math.abs(this.getX() - other.getX()) < 0.01
            && Math.abs(this.getY() - other.getY()) < 0.01
            && this.getColor().equals(other.getColor())
            && Math.abs(this.getDeg() - other.getDeg()) < 0.01
            && this.getWidth() == other.getWidth()
            && this.getHeight() == other.getHeight());
  }

  int sameHash() {
    return Double.hashCode(this.getX() + this.getY() + this.getDeg()
            + color.hashCode() + this.getWidth() + this.getHeight());
  }


}
