public class Menu extends Option {
  public Menu(String strTitle, Option... arrOptions) {
    super(strTitle, () -> {
      ShowMenu(arrOptions);
    });
  }

  private static void ShowMenu(Option[] arrOptions) {

  }
}