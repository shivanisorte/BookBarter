package com.shivani.bookbarter;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Book {
    private String id;
    private String bookName;


    public Book() {

    }

    public Book(String id, String bookName) {
        this.bookName = bookName;
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }


}
