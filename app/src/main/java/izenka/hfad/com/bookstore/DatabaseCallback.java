package izenka.hfad.com.bookstore;

@FunctionalInterface
public interface DatabaseCallback<T> {
    void onResult(T t);
}
