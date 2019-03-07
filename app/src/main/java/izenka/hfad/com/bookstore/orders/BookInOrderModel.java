package izenka.hfad.com.bookstore.orders;


import izenka.hfad.com.bookstore.model.db_classes.Book;

public class BookInOrderModel {
    private Book book;
    private int count;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
