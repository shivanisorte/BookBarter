package com.shivani.bookbarter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Belal on 2/26/2017.
 */
@IgnoreExtraProperties
public class AuthorActivity{
    private String authorId;
    private String authorName;
    private String authorGenre;

    public AuthorActivity(){
        //this constructor is required
    }

    public AuthorActivity(String AuthorId, String AuthorName, String AuthorGenre) {
        this.authorId = AuthorId;
        this.authorName = AuthorName;
        this.authorGenre = AuthorGenre;
    }

    public String getArtistId() {
        return authorId;
    }

    public String getArtistName() {
        return authorName;
    }

    public String getArtistGenre() {
        return authorGenre;
    }
}
