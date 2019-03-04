import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Disk {
  void Save(String fileName, ArrayList<Save> list) throws IOException {
    PrintWriter out = new PrintWriter(fileName);
    for (int i = 0; i < list.size(); i++) {
      list.get(i).Write(out);
    }
    out.close();
  }

  void Save(String fileName, Save obj) throws IOException {
    PrintWriter out = new PrintWriter(fileName);
    obj.Write(out);
    out.close();
  }

  <T> void LoadObject(String fileName, Loader<T> funLoad) {
    try {
      Scanner in = new Scanner(Paths.get(fileName));
      funLoad.Load(in);
    } catch (IOException e) {
    }
  }

  <T> ArrayList<T> LoadList(String fileName, Loader<T> funLoad) throws IOException {
    Scanner in = new Scanner(Paths.get(fileName));
    ArrayList<T> list = new ArrayList<T>();
    while (true) {
      T objNew = funLoad.Load(in);
      if (objNew == null)
        break;
      list.add(objNew);
    }
    return list;
  }
}