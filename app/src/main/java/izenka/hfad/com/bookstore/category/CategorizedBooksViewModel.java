package izenka.hfad.com.bookstore.category;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import java.util.concurrent.Executors;

import izenka.hfad.com.bookstore.MainThreadExecutor;
import izenka.hfad.com.bookstore.model.db_classes.Book;

public class CategorizedBooksViewModel extends ViewModel {

    private String categoryID;
    private CategoryNavigator navigator;
    private MutableLiveData<PagedList<Book>> bookPagedListLiveData;

    public void setNavigator(final CategoryNavigator navigator) {
        this.navigator = navigator;
    }

    void setCategoryID(final String categoryID) {
        this.categoryID = categoryID;
    }

    void onBookClicked(final Book book) {
        navigator.onBookClicked(book.getBook_id());
    }

    void onSearchInCategoryClicked() {
        navigator.onSearchInCategoryClicked();
    }

    MutableLiveData<PagedList<Book>> getBookListLiveData() {
        if (bookPagedListLiveData == null) {
            bookPagedListLiveData = new MutableLiveData<>();

            final CategorizedBooksDataSource dataSource = new CategorizedBooksDataSource(categoryID);

            final PagedList.Config config = new PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build();

            final PagedList<Book> bookList = new PagedList.Builder<>(dataSource, config)
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .setNotifyExecutor(new MainThreadExecutor())
                    .build();

            bookPagedListLiveData.postValue(bookList);
        }
        return bookPagedListLiveData;
    }
}
