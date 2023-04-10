import java.io.IOException;

/**
 * Simulates an error in a appendable for JUnit tests.
 */
class BadAppendable implements Appendable {

  @Override
  public Appendable append(CharSequence csq) throws IOException {
    throw new IOException("bad");
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    throw new IOException("bad");
  }

  @Override
  public Appendable append(char c) throws IOException {
    throw new IOException("bad");
  }
}
