package com.shivani.bookbarter;

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Track {
    private String id;
    private String trackName;
    private int rating;

    public Track() {

    }

    public Track(String id, String trackName, int rating) {
        this.trackName = trackName;
        this.rating = rating;
        this.id = id;
    }

    public String getTrackName() {
        return trackName;
    }

    public int getRating() {
        return rating;
    }
}