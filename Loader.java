import java.util.Scanner;

@FunctionalInterface
public interface Loader<T>  {
    public abstract T Load(Scanner in);
}