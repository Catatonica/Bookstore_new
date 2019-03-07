package izenka.hfad.com.bookstore.search;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import izenka.hfad.com.bookstore.DatabaseSingleton;
import izenka.hfad.com.bookstore.model.db_classes.Book;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<List<Book>> bookListLiveData;
    private MutableLiveData<List<Book>> categorizedBookListLiveData;
    private SearchNavigator navigator;

    public void setNavigator(final SearchNavigator navigator) {
        this.navigator = navigator;
    }

    MutableLiveData<List<Book>> getBookListLiveData(final String searchedText) {
        if (bookListLiveData == null) {
            bookListLiveData = new MutableLiveData<>();
        }
        DatabaseSingleton.getInstance().getSearchedBookList(searchedText, bookList -> {
            bookListLiveData.postValue(bookList);
        });
        return bookListLiveData;
    }

    MutableLiveData<List<Book>> getBookListLiveData(final String categoryID,
                                                    final String searchedText) {
        if (categorizedBookListLiveData == null) {
            categorizedBookListLiveData = new MutableLiveData<>();
        }
        DatabaseSingleton.getInstance().getSearchedCategorizedBookList(categoryID, searchedText, bookList -> {
            categorizedBookListLiveData.postValue(bookList);
        });
        return categorizedBookListLiveData;
    }

    void onBookClicked(final Book book) {
        navigator.onBookClicked(book.getBook_id());
    }
}
