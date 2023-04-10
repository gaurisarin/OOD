import org.junit.Test;

import cs3500.animator.model.Event;
import cs3500.animator.model.EventType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * JUnit tests for the cs3500.animator.model.Event class.
 */
public class EventTest {
  @Test(expected = IllegalArgumentException.class)
  public void testEventNoType() {
    Event e = new Event(5, 10, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEventNegStart() {
    Event e = new Event(-1, 10, EventType.COLOR);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEventNotInOrder() {
    Event e = new Event(5, -3, EventType.COLOR);
  }

  @Test
  public void testGetters() {
    Event e = new Event(5, 10, EventType.COLOR);
    assertEquals(5, e.getStart());
    assertEquals(10, e.getEnd());
    assertEquals(EventType.COLOR, e.getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDuringNullEvent() {
    Event e = new Event(5, 10, EventType.COLOR);
    e.during(null);
  }

  @Test
  public void testDuring() {
    Event e = new Event(5, 10, EventType.NOTHING);
    Event e1 = new Event(5, 5, EventType.NOTHING);
    Event e2 = new Event(3, 5, EventType.NOTHING);
    Event e3 = new Event(3, 7, EventType.NOTHING);
    Event e4 = new Event(3, 10, EventType.NOTHING);
    Event e5 = new Event(3, 11, EventType.NOTHING);
    Event e6 = new Event(5, 7, EventType.NOTHING);
    Event e7 = new Event(5, 10, EventType.NOTHING);
    Event e8 = new Event(5, 11, EventType.NOTHING);
    Event e9 = new Event(6, 7, EventType.NOTHING);
    Event e10 = new Event(6, 10, EventType.NOTHING);
    Event e11 = new Event(6, 11, EventType.NOTHING);
    Event e12 = new Event(10, 10, EventType.NOTHING);
    Event e13 = new Event(10, 11, EventType.NOTHING);
    Event e14 = new Event(11, 11, EventType.NOTHING);
    Event e15 = new Event(3, 3, EventType.NOTHING);

    // next to each other
    assertEquals(0, e.during(e1));
    assertEquals(0, e.during(e2));
    assertEquals(0, e.during(e12));
    assertEquals(0, e.during(e13));

    // not connected
    assertEquals(-1, e.during(e15));
    assertEquals(-1, e.during(e14));

    // overlapping
    assertEquals(1, e.during(e3));
    assertEquals(1, e.during(e4));
    assertEquals(1, e.during(e5));
    assertEquals(1, e.during(e6));
    assertEquals(1, e.during(e7));
    assertEquals(1, e.during(e8));
    assertEquals(1, e.during(e9));
    assertEquals(1, e.during(e10));
    assertEquals(1, e.during(e11));

  }

  @Test
  public void testEquals() {
    Event e = new Event(5, 10, EventType.COLOR);
    Event e2 = new Event(5, 10, EventType.COLOR);
    Event e3 = new Event(5, 10, EventType.NOTHING);
    Event e4 = new Event(6, 10, EventType.COLOR);

    assertEquals(e, e);
    assertEquals(e, e2);
    assertNotEquals(e, e3);
    assertNotEquals(e, e4);
  }

  @Test
  public void testCompareTo() {
    Event e = new Event(5, 10, EventType.COLOR);
    Event e2 = new Event(5, 10, EventType.COLOR);
    Event e3 = new Event(5, 10, EventType.NOTHING);
    Event e4 = new Event(6, 10, EventType.COLOR);
    Event e5 = new Event(5, 11, EventType.COLOR);

    assertEquals(0, e.compareTo(e2));
    assertEquals(1, e.compareTo(e3));
    assertEquals(-1, e.compareTo(e4));
    assertEquals(-1, e.compareTo(e5));
  }
}
