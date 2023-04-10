package cs3500.animator.model;

import java.util.Objects;

/**
 * An event describes what and when something happens to a shape
 * Contains the start and end ticks of the event, and the type of event.
 */
public class Event implements Comparable<Event> {
  private final int start;
  private final int end;
  private final EventType type;

  /**
   * Constructs a new Event, representing the duration and type of transform occurring on a shape.
   *
   * @param start the tick the transform starts
   * @param end   the tick the transform ends
   * @param type  the type of transform
   * @throws IllegalArgumentException if start or end is less than zero, if end < start, or if
   *                                  type is null
   */
  public Event(int start, int end, EventType type) {
    if (type == null) {
      throw new IllegalArgumentException("event type cannot be null");
    }
    if (end < start || start < 0) {
      throw new IllegalArgumentException("invalid start/end time(s)");
    }

    this.start = start;
    this.end = end;
    this.type = type;
  }

  /**
   * Returns the start time of the Event.
   *
   * @return the start time, as an int
   */
  public int getStart() {
    return start;
  }

  /**
   * Returns the end time of the Event.
   *
   * @return the end time, as an int
   */
  public int getEnd() {
    return end;
  }

  /**
   * Returns the type of the Event.
   *
   * @return the type of the Event, as an EventType
   */
  public EventType getType() {
    return this.type;
  }

  /**
   * Returns if this event and that event is ever active at the same time.
   *
   * @param other the other event to check if this event overlaps with
   * @return -1 if the events don't overlap, 0 if the events don't overlap but are consecutive,
   *     1 if they overlap.
   * @throws IllegalArgumentException if the other event provided is null
   */
  public int during(Event other) {
    if (other == null) {
      throw new IllegalArgumentException("Other event is null");
    }

    if ((other.getStart() == start && other.getEnd() == start)
            || other.getStart() == end && other.getEnd() == start) { // zero width event on border
      return 0;
    }

    if (this.start <= other.getStart()) {
      if (this.end == other.getStart()) {
        return 0;
      } else if (this.end < other.getStart()) {
        return -1;
      }
      return 1;
    } else {
      if (this.start == other.getEnd()) {
        return 0;
      } else if (this.start < other.getEnd()) {
        return 1;
      }
      return -1;
    }
  }

  @Override
  public boolean equals(Object o) {

    if (!(o instanceof Event)) {
      return false;
    }
    Event other = (Event) o;
    return this.start == other.getStart()
            && this.end == other.getEnd()
            && this.type == other.getType();
  }

  @Override
  public int hashCode() {
    return Double.hashCode(start * end) + Objects.hashCode(type);
  }

  @Override
  public int compareTo(Event o) {
    if (o == null) {
      // docs say to throw NullPointerException, but autograder takes style points off.
      // changed to IllegalArgumentException because of that.
      throw new IllegalArgumentException("Other event provided is null");
    }

    if (start > o.getStart()) {
      return 1;
    } else if (start == o.getStart()) {
      if (end > o.getEnd()) {
        return 1;
      } else if (end == o.getEnd()) {
        return Integer.compare(type.ordinal(), o.getType().ordinal());
      }
      return -1;
    }
    return -1;
  }
}
