package izenka.hfad.com.bookstore.category;

import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;

import java.util.List;

import izenka.hfad.com.bookstore.DatabaseCallback;
import izenka.hfad.com.bookstore.DatabaseSingleton;
import izenka.hfad.com.bookstore.model.db_classes.Book;

public class CategorizedBooksDataSource extends PositionalDataSource<Book> {

    private final String categoryID;

    @Override
    public void loadInitial(@NonNull final LoadInitialParams params, @NonNull final LoadInitialCallback<Book> callback) {
        final DatabaseCallback<List<Book>> categorizedBooksCallback = bookList -> callback.onResult(bookList, params.requestedStartPosition);
        DatabaseSingleton.getInstance().getCategorizedPagedBookList(categoryID,
                params.requestedStartPosition,
                params.requestedLoadSize,
                categorizedBooksCallback);
    }

    @Override
    public void loadRange(@NonNull final LoadRangeParams params, @NonNull final LoadRangeCallback<Book> callback) {
        final DatabaseCallback<List<Book>> categorizedBooksCallback = callback::onResult;
        DatabaseSingleton.getInstance().getCategorizedPagedBookList(categoryID,
                params.startPosition,
                params.loadSize,
                categorizedBooksCallback);
    }

    CategorizedBooksDataSource(final String categoryID) {
        this.categoryID = categoryID;
    }

}
