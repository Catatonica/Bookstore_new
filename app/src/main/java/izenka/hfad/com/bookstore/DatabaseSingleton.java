package izenka.hfad.com.bookstore;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import izenka.hfad.com.bookstore.account.User;
import izenka.hfad.com.bookstore.basket.BookInBasketModel;
import izenka.hfad.com.bookstore.model.db_classes.Author;
import izenka.hfad.com.bookstore.model.db_classes.Book;
import izenka.hfad.com.bookstore.model.db_classes.Publisher;
import izenka.hfad.com.bookstore.order_registration.OrderRegistrationModel;
import izenka.hfad.com.bookstore.orders.BookInOrderModel;
import izenka.hfad.com.bookstore.stores_map.StoreModel;


public class DatabaseSingleton {

    private static DatabaseSingleton singleton = new DatabaseSingleton();

    private DatabaseSingleton() {

    }

    public static DatabaseSingleton getInstance() {
        return singleton;
    }

    public void getCategorizedPagedBookList(String categoryID,
                                            int startPosition,
                                            int loadSize,
                                            DatabaseCallback<List<Book>> callback) {
        FirebaseDatabase
                .getInstance()
                .getReference("bookstore")
                .child("book")
                .orderByChild("Categories/" + categoryID)
                .equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<DataSnapshot> bookSnapshotsList = new ArrayList<>();
                        for (DataSnapshot bookData : dataSnapshot.getChildren()) {
                            bookSnapshotsList.add(bookData);
                        }
                        callback.onResult(getBookList(bookSnapshotsList, startPosition, loadSize));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private List<Book> getBookList(List<DataSnapshot> bookSnapshotsList, int startPosition, int loadSize) {
        List<Book> bookList = new ArrayList<>();
        int rightLoadSize;
        if (bookSnapshotsList.size() - startPosition < loadSize) {
            rightLoadSize = bookSnapshotsList.size() - startPosition;
        } else {
            rightLoadSize = loadSize;
        }

        for (DataSnapshot bookData : bookSnapshotsList.subList(startPosition, startPosition + rightLoadSize)) {
            Book book = bookData.getValue(Book.class);
            for (DataSnapshot imagesID : bookData.child("Images").getChildren()) {
                book.getImagesPaths().add(imagesID.getValue().toString());
            }
            for (DataSnapshot authID : bookData.child("Authors").getChildren()) {
                book.getAuthorsIDs().add(String.valueOf(authID.getKey()));
            }
            bookList.add(book);
        }
        return bookList;
    }

    public void getAuthorList(List<String> authorsIDs, DatabaseCallback<List<Author>> callback) {
        List<Author> authorList = new ArrayList<>();
        for (String authorID : authorsIDs) {
            FirebaseDatabase
                    .getInstance()
                    .getReference("bookstore")
                    .child("author")
                    .child(authorID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Author author = dataSnapshot.getValue(Author.class);
                            authorList.add(author);
                            if (authorList.size() == authorsIDs.size()) {
                                callback.onResult(authorList);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    public void getBook(String bookID, DatabaseCallback<Book> callback) {
        FirebaseDatabase
                .getInstance()
                .getReference("bookstore")
                .child("book")
                .child(bookID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot data) {
                        Book book = data.getValue(Book.class);
                        for (DataSnapshot imagesID : data.child("Images").getChildren()) {
                            book.getImagesPaths().add(imagesID.getValue().toString());
                        }
                        for (DataSnapshot authID : data.child("Authors").getChildren()) {
                            book.getAuthorsIDs().add(String.valueOf(authID.getKey()));
                        }
                        callback.onResult(book);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void addBookToUserBasket(String bookID) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userID = user.getUid();
            FirebaseDatabase
                    .getInstance()
                    .getReference("bookstore")
                    .child("users/" + userID)
                    .child("Basket")
                    .child(bookID)
                    .setValue(1); /* book id and count*/
        }
    }


    public void getBookCount(String bookID, DatabaseCallback<Integer> callback) {
        FirebaseDatabase
                .getInstance()
                .getReference("bookstore")
                .child("book")
                .child(bookID)
                .child("count")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count = dataSnapshot.getValue(Integer.TYPE);
                        callback.onResult(count);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void getUser(DatabaseCallback<User> callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userID = user.getUid();
            FirebaseDatabase
                    .getInstance()
                    .getReference("bookstore")
                    .child("users")
                    .child(userID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            callback.onResult(user);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    public void createUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userID = user.getUid();
            FirebaseDatabase
                    .getInstance()
                    .getReference("bookstore")
                    .child("users")
                    .child(userID)
                    .child("email")
                    .setValue(user.getEmail());
        }
    }

    public void updateUserInfo(Map<String, Object> updates) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userID = user.getUid();
            FirebaseDatabase
                    .getInstance()
                    .getReference("bookstore")
                    .child("users")
                    .child(userID)
                    .updateChildren(updates);
        }
    }

    public void deleteBookFromBasket(String bookID) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userID = user.getUid();
            FirebaseDatabase
                    .getInstance()
                    .getReference("bookstore")
                    .child("users/" + userID)
                    .child("Basket")
                    .child(bookID)
                    .removeValue();
        }
    }

    /*
    public void getPagedBooksFromBasket(Map<String, Integer> booksIDsAndCount,
                                        int startPosition,
                                        int loadSize,
                                        BooksFromBasketCallback callback) {

        List<BookInBasketModel> bookInBasketModelList = new ArrayList<>();

        String[] booksIDs = booksIDsAndCount.keySet().toArray(new String[0]);
        for (int i = startPosition; i < loadSize; i++) {
            int finalI = i;
            FirebaseDatabase
                    .getInstance()
                    .getReference("bookstore")
                    .child("book")
                    .child(booksIDs[i])
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            BookInBasketModel bookInBasketModel = new BookInBasketModel();
                            Book book = dataSnapshot.getValue(Book.class);
                            book.setImagesPaths(new ArrayList<>());

                            //TODO: auto-convert : List -> Map
                            for (DataSnapshot imagesID : dataSnapshot.child("Images").getChildren()) {
                                book.getImagesPaths().add(imagesID.getValue().toString());
                            }
                            for (DataSnapshot authID : dataSnapshot.child("Authors").getChildren()) {
                                book.getAuthorsIDs().add(String.valueOf(authID.getKey()));
                            }

                            bookInBasketModel.count = booksIDsAndCount.get(booksIDs[finalI]);
                            bookInBasketModel.book = book;
                            bookInBasketModelList.add(bookInBasketModel);
                            if (bookInBasketModelList.size() == booksIDs.length) {
                                callback.onBooksLoaded(bookInBasketModelList);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

    }
    */

    public void getBooksFromBasket(Map<String, Integer> booksIDsAndCount,
                                   DatabaseCallback<List<BookInBasketModel>> callback) {
        List<BookInBasketModel> bookInBasketModelList = new ArrayList<>();
        for (String bookID : booksIDsAndCount.keySet()) {
            FirebaseDatabase
                    .getInstance()
                    .getReference("bookstore")
                    .child("book")
                    .child(bookID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            BookInBasketModel bookInBasketModel = new BookInBasketModel();
                            Book book = dataSnapshot.getValue(Book.class);
                            book.setImagesPaths(new ArrayList<>());

                            //TODO: auto-convert : List -> Map
                            for (DataSnapshot imagesID : dataSnapshot.child("Images").getChildren()) {
                                book.getImagesPaths().add(imagesID.getValue().toString());
                            }
                            for (DataSnapshot authID : dataSnapshot.child("Authors").getChildren()) {
                                book.getAuthorsIDs().add(String.valueOf(authID.getKey()));
                            }

                            bookInBasketModel.count = booksIDsAndCount.get(bookID);
                            bookInBasketModel.book = book;
                            bookInBasketModelList.add(bookInBasketModel);
                            if (bookInBasketModelList.size() == booksIDsAndCount.size()) {
                                callback.onResult(bookInBasketModelList);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

    }

    public void getBookPublisher(String publisherID, DatabaseCallback<Publisher> callback) {
        FirebaseDatabase
                .getInstance()
                .getReference("bookstore")
                .child("publisher")
                .child(publisherID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        callback.onResult(dataSnapshot.getValue(Publisher.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void writeNewOrder(OrderRegistrationModel orderModel) {
        Map<String, Object> newOrder = orderModel.toMap();
        String key = FirebaseDatabase
                .getInstance()
                .getReference("bookstore")
                .child("orders")
                .push()
                .getKey();
        FirebaseDatabase
                .getInstance()
                .getReference("bookstore")
                .child("order")
                .child(key)
                .setValue(newOrder).addOnSuccessListener(aVoid -> subtractBookCount(orderModel));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userID = user.getUid();
            FirebaseDatabase
                    .getInstance()
                    .getReference("bookstore")
                    .child("users")
                    .child(userID)
                    .child("Orders")
                    .child(orderModel.getDate())
                    .setValue(key)
                    .addOnSuccessListener(aVoid -> cleanBasket(orderModel));
        }
    }

    private void cleanBasket(OrderRegistrationModel orderModel) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userID = user.getUid();
            for (String bookID : orderModel.getBooks().keySet()) {
                FirebaseDatabase
                        .getInstance()
                        .getReference("bookstore")
                        .child("users")
                        .child(userID)
                        .child("Basket")
                        .child(bookID)
                        .removeValue();
            }
        }
    }

    private void subtractBookCount(OrderRegistrationModel orderModel) {
        for (Map.Entry<String, Integer> bookIDAndCount : orderModel.getBooks().entrySet()) {
            FirebaseDatabase
                    .getInstance()
                    .getReference("bookstore")
                    .child("book")
                    .child(bookIDAndCount.getKey())
                    .child("count")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            long oldCount = (long) dataSnapshot.getValue();
                            long newCount = oldCount - bookIDAndCount.getValue();
                            FirebaseDatabase
                                    .getInstance()
                                    .getReference("bookstore")
                                    .child("book")
                                    .child(bookIDAndCount.getKey())
                                    .child("count")
                                    .setValue(newCount);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    public void getOrderList(Map<String, String> ordersIDs, DatabaseCallback<List<OrderRegistrationModel>> callback) {
        List<OrderRegistrationModel> orderList = new ArrayList<>();
        for (String orderID : ordersIDs.values()) {
            FirebaseDatabase
                    .getInstance()
                    .getReference("bookstore")
                    .child("order")
                    .child(orderID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            OrderRegistrationModel order = dataSnapshot.getValue(OrderRegistrationModel.class);
                            orderList.add(order);
                            if (orderList.size() == ordersIDs.size()) {
                                callback.onResult(orderList);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    public void getBookAndCountList(Map<String, Integer> booksIDsAndCount,
                                    DatabaseCallback<List<BookInOrderModel>> callback) {
        List<BookInOrderModel> bookInOrderModelList = new ArrayList<>();
        for (String bookID : booksIDsAndCount.keySet()) {
            FirebaseDatabase
                    .getInstance()
                    .getReference("bookstore")
                    .child("book")
                    .child(bookID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            BookInOrderModel bookInOrderModel = new BookInOrderModel();
                            bookInOrderModel.setBook(dataSnapshot.getValue(Book.class));
                            bookInOrderModel.setCount(booksIDsAndCount.get(bookID));
                            bookInOrderModelList.add(bookInOrderModel);
                            if (bookInOrderModelList.size() == booksIDsAndCount.size()) {
                                callback.onResult(bookInOrderModelList);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    public void getStoreList(DatabaseCallback<List<StoreModel>> callback) {
        FirebaseDatabase
                .getInstance()
                .getReference("bookstore")
                .child("store")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<StoreModel> storeList = new ArrayList<>();
                        for (DataSnapshot storeDataSnapshot : dataSnapshot.getChildren()) {
                            storeList.add(storeDataSnapshot.getValue(StoreModel.class));
                        }
                        callback.onResult(storeList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void getSearchedBookList(String searchedText, DatabaseCallback<List<Book>> callback) {
        FirebaseDatabase
                .getInstance()
                .getReference("bookstore")
                .child("book")
                .orderByKey()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Book> bookList = new ArrayList<>();
                        for (DataSnapshot bookData : dataSnapshot.getChildren()) {
                            Book book = bookData.getValue(Book.class);
                            for (DataSnapshot imagesID : bookData.child("Images").getChildren()) {
                                book.getImagesPaths().add(imagesID.getValue().toString());
                            }
                            for (DataSnapshot authID : bookData.child("Authors").getChildren()) {
                                book.getAuthorsIDs().add(String.valueOf(authID.getKey()));
                            }
                            if (book.getTitle().toLowerCase().contains(searchedText.toLowerCase())) {
                                bookList.add(book);
                            }
                            searchAuthors(book.getAuthorsIDs(), searchedText, bool -> {
                                if (bool && !bookList.contains(book)) {
                                    bookList.add(book);
                                }
                            });
                        }
                        callback.onResult(bookList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void getSearchedCategorizedBookList(String categoryID,
                                               String searchedText,
                                               DatabaseCallback<List<Book>> callback) {
        FirebaseDatabase
                .getInstance()
                .getReference("bookstore")
                .child("book")
                .orderByChild("Categories/" + categoryID)
                .equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Book> bookList = new ArrayList<>();
                        for (DataSnapshot bookData : dataSnapshot.getChildren()) {
                            Book book = bookData.getValue(Book.class);
                            for (DataSnapshot imagesID : bookData.child("Images").getChildren()) {
                                book.getImagesPaths().add(imagesID.getValue().toString());
                            }
                            for (DataSnapshot authID : bookData.child("Authors").getChildren()) {
                                book.getAuthorsIDs().add(String.valueOf(authID.getKey()));
                            }
                            if (book.getTitle().toLowerCase().contains(searchedText.toLowerCase())) {
                                bookList.add(book);
                            }
                            searchAuthors(book.getAuthorsIDs(), searchedText, bool -> {
                                if (bool && !bookList.contains(book)) {
                                    bookList.add(book);
                                }
                            });
                        }
                        callback.onResult(bookList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void searchAuthors(List<String> authorsIDs, String searchedText, DatabaseCallback<Boolean> callback) {
        for (String authorID : authorsIDs) {
            FirebaseDatabase
                    .getInstance()
                    .getReference("bookstore")
                    .child("author/" + authorID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Author author = dataSnapshot.getValue(Author.class);
                            if ((author.author_name.toLowerCase().contains(searchedText.toLowerCase()) ||
                                    author.author_surname.toLowerCase().contains(searchedText.toLowerCase()))) {
                                callback.onResult(true);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }
}
