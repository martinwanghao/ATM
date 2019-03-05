import java.io.IOException;
import java.io.PrintWriter;

public interface Saver {
  void Write(PrintWriter out) throws IOException;
}