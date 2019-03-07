CREATE TABLE category(
category_id integer (10) NOT NULL DEFAULT 1,
category_name varchar(32) NOT NULL DEFAULT '',
PRIMARY KEY (category_id)
);

 INSERT INTO category values (1, "Иностранная литература");
 INSERT INTO category values (2, "Детская литература");
 INSERT INTO category values (3, "Бизнес-литература");
 INSERT INTO category values (4, "Художественная литература");
 INSERT INTO category values (5, "Учебная литература");
 INSERT INTO category values (6, "Нехудожественная литература");

CREATE TABLE book(
book_id integer(1000) NOT NULL ,
book_title varchar(32) NOT NULL DEFAULT '',
book_prise varchar(32) NOT NULL DEFAULT '0',
PRIMARY KEY(book_id)
);

 INSERT INTO book values (1, "Амулет Самарканда", "30 р.");
 INSERT INTO book values (2, "Глаз Голема", "32 р.");
 INSERT INTO book values (3, "Врата Птолемея", "33 р.");

CREATE TABLE booksCategories(
book_id integer(1000) NOT NULL ,
category_id integer (10),
FOREIGN KEY (book_id) REFERENCES book (book_id),
FOREIGN KEY (category_id) REFERENCES category (category_id),
);

 INSERT INTO booksCategories values (1, 4);
 INSERT INTO booksCategories values (2, 4);
 INSERT INTO booksCategories values (3, 4);

CREATE TABLE author(
author_id integer(5000) NOT NULL ,
author_name varchar(32) NOT NULL DEFAULT '',
PRIMARY KEY (author_id)
);

 INSERT INTO author values (1, "Страуд Д.");


CREATE TABLE booksAuthors(
book_id integer(1000) NOT NULL ,
author_id integer(5000) NOT NULL,
FOREIGN KEY (book_id) REFERENCES book (book_id),
FOREIGN KEY (author_id) REFERENCES author (book_id)
);

 INSERT INTO booksAuthors values (1, 1);
 INSERT INTO booksAuthors values (2, 1);
 INSERT INTO booksAuthors values (3, 1);
