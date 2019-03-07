package izenka.hfad.com.bookstore.model.db_classes;

import java.io.Serializable;

public class Author implements Serializable {

    public String author_id;
    public String author_name;
    public String author_surname;

    public Author() { }

    public String toString() {
        return "Author { author_id=" +
                author_id + ", author_name=" +
                author_name + ", author_surname=" +
                author_surname + "}";
    }
}

