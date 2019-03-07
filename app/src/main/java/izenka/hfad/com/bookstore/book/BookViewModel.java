package izenka.hfad.com.bookstore.book;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import izenka.hfad.com.bookstore.DatabaseSingleton;
import izenka.hfad.com.bookstore.model.db_classes.Author;
import izenka.hfad.com.bookstore.model.db_classes.Book;
import izenka.hfad.com.bookstore.model.db_classes.Publisher;

public class BookViewModel extends ViewModel {

    private String bookID;

    private MutableLiveData<Book> bookLiveData;
    private MutableLiveData<List<Author>> authorListLiveData;
    private MutableLiveData<Publisher> publisherLiveData;

    private BookNavigator navigator;

    public void setBookID(final String bookID) {
        this.bookID = bookID;
    }

    public void setNavigator(final BookNavigator navigator) {
        this.navigator = navigator;
    }

    MutableLiveData<Book> getBookLiveData() {
        if (bookLiveData == null) {
            bookLiveData = new MutableLiveData<>();
            DatabaseSingleton.getInstance().getBook(String.valueOf(bookID),
                                                    book -> bookLiveData.postValue(book));
        }
        return bookLiveData;
    }

    MutableLiveData<List<Author>> getAuthorListLiveData(final List<String> authorIDs) {
        if (authorListLiveData == null) {
            authorListLiveData = new MutableLiveData<>();
            DatabaseSingleton.getInstance().getAuthorList(authorIDs, authorList -> {
                authorListLiveData.postValue(authorList);
            });
        }
        return authorListLiveData;
    }

    MutableLiveData<Publisher> getPublisherLiveData(final String publisherID) {
        if (publisherLiveData == null) {
            publisherLiveData = new MutableLiveData<>();
            DatabaseSingleton.getInstance().getBookPublisher(publisherID, publisher -> {
                publisherLiveData.postValue(publisher);
            });
        }
        return publisherLiveData;
    }

    void onPutInBasketClicked() {
        DatabaseSingleton.getInstance().addBookToUserBasket(bookID);
        navigator.onPutInBasketClicked();
    }

    void notifyOfBookAppearance() {
        DatabaseSingleton.getInstance().getBookCount(String.valueOf(bookID), count -> {
            if (count > 0 && bookLiveData.getValue() != null) {
                navigator.notifyUser(bookLiveData.getValue().getTitle());
            }
        });
    }
}
