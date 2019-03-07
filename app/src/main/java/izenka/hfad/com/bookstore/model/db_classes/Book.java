package izenka.hfad.com.bookstore.model.db_classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Book implements Serializable {

    private String book_id;
    private String book_publisher_id;
    private int count;
    private String description;
    private int pages_number;
    private String price;
    private int publication_year;
    private String title;
    private String cover;
    private int rating;
    private int discount;
//    public List<Long> authorsIDs;
    private List<String> authorsIDs = new ArrayList<>();
    private List<String> imagesPaths = new ArrayList<>();
    private List<Author> authors;

    public Book() {
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getBook_publisher_id() {
        return book_publisher_id;
    }

    public void setBook_publisher_id(String book_publisher_id) {
        this.book_publisher_id = book_publisher_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPages_number() {
        return pages_number;
    }

    public void setPages_number(int pages_number) {
        this.pages_number = pages_number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getPublication_year() {
        return publication_year;
    }

    public void setPublication_year(int publication_year) {
        this.publication_year = publication_year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public List<String> getAuthorsIDs() {
        return authorsIDs;
    }

    public void setAuthorsIDs(List<String> authorsIDs) {
        this.authorsIDs = authorsIDs;
    }

    public List<String> getImagesPaths() {
        return imagesPaths;
    }

    public void setImagesPaths(List<String> imagesPaths) {
        this.imagesPaths = imagesPaths;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
}
