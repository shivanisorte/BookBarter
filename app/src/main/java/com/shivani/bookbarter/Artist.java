package com.shivani.bookbarter;

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Artist {
    private static String artistId;
    private static String artistName;
    private static String artistGenre;

    public Artist() {
        //this constructor is required
    }

    public Artist(String artistId, String artistName, String artistGenre) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.artistGenre = artistGenre;
    }

    public static String getArtistId() {
        return artistId;
    }

    public static String getArtistName() {
        return artistName;
    }

    public static String getArtistGenre() {
        return artistGenre;
    }
}