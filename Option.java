public class Option {
  private String title;
  private Runnable run;

  public void Run() {
    this.run.run();
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  public Option(String strTitle, Runnable run) {
    this.title = strTitle;
    this.run = run;
  }
}