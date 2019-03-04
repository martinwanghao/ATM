import java.io.IOException;
import java.io.PrintWriter;

public interface Save {
  void Write(PrintWriter out) throws IOException;
}