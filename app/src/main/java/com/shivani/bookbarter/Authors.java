package com.shivani.bookbarter;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Authors {
    private String authorId;
    private String authorName;
    private String authorGenre;

    public Authors(){
        //this constructor is required
    }

    public Authors(String authorId, String authorName, String authorGenre) {
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorGenre = authorGenre;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getArtistGenre() {
        return authorGenre;
    }
}