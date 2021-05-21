package com.shivani.bookbarter;

import com.google.firebase.database.IgnoreExtraProperties;


public class BookName {
    private String id;
    private String bookName;
    private int rating;

    public BookName() {

    }

    public BookName(String id, String bookName, int rating) {
        this.bookName = bookName;
        this.rating = rating;
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public int getRating() {
        return rating;
    }
}