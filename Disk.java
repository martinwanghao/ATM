import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Disk {
  void Save(String fileName, ArrayList<Saver> list, Runnable onSuccess) throws IOException {
    File f = new File(fileName);
    if (f.exists())
      f.delete();
    PrintWriter out = new PrintWriter(fileName);
    for (int i = 0; i < list.size(); i++) {
      list.get(i).Write(out);
      if (onSuccess != null)
        onSuccess.run();
    }
    out.close();
  }

  void Save(String fileName, Saver obj, Runnable onSuccess) throws IOException {
    File f = new File(fileName);
    if (f.exists())
      f.delete();
    PrintWriter out = new PrintWriter(fileName);
    obj.Write(out);
    out.close();
    if (onSuccess != null)
      onSuccess.run();
  }

  <T> void LoadObject(String fileName, Loader<T> funLoad, Runnable onSuccess) throws Exception {
    File f = new File(fileName);
    if (!f.exists())
      throw new Exception("File not found");
    Scanner in = new Scanner(f);
    funLoad.Load(in);
    in.close();
    if (onSuccess != null)
      onSuccess.run();
  }

  <T> ArrayList<T> LoadList(String fileName, Loader<T> funLoad, Runnable onSuccess) throws Exception {
    File f = new File(fileName);
    if (!f.exists())
      throw new Exception("File not found");
    Scanner in = new Scanner(f);
    ArrayList<T> list = new ArrayList<T>();
    while (in.hasNextLine()) {
      T objNew = funLoad.Load(in);
      if (objNew == null)
        break;
      list.add(objNew);
      if (onSuccess != null)
        onSuccess.run();
    }
    return list;
  }
}