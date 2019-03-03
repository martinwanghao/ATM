import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Menu {
  @FunctionalInterface
  public interface OptionFun {
    public abstract boolean run();
  }

  private class Option {
    private String title;
    private OptionFun fun;

    public Option(String title, OptionFun fun) {
      this.title = title;
      this.fun = fun;
    }
  }

  private static Pattern patternSelected = Pattern.compile("^\\d+$");

  private String title;
  private Screen screen;
  private List<Option> options = new ArrayList<Option>();

  public Menu(String title, Screen screen) {
    this.title = title;
    this.screen = screen;
  }

  public Menu AddOption(String title, OptionFun fun) {
    this.options.add(new Option(title, fun));
    return this;
  }

  private int GetSelected() {
    while (true) {
      String input = screen.GetInput("\nPlease choose : ", "");
      if (input.isEmpty())
        return 0;
      if (!patternSelected.matcher(input).matches()) {
        screen.ShowMsg("ERROR: invalid option");
        continue;
      }
      int selected = Integer.parseInt(input);
      if (selected < 1 || selected > options.size()) {
        screen.ShowMsg("ERROR: invalid option");
        continue;
      }
      return selected;
    }
  }

  public void Show() {
    while (true) {
      String s = "";
      for (int i = 0; i < this.title.length(); i++)
        s += "=";
      screen.ShowMsg("\n" + this.title + "\n" + s);
      for (int i = 1; i <= options.size(); i++) {
        screen.ShowMsg(String.valueOf(i) + ". " + options.get(i - 1).title);
      }
      int selected = GetSelected();
      if (selected == 0)
        continue;
      Option option = options.get(selected - 1);
      if (option != null) {
        if (option.fun == null)
          break;
        else if (!option.fun.run())
          break;
      }
    }
  }
}