package com.shivani.bookbarter;

import android.os.Parcelable;
import android.os.Parcel;
import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Track implements Parcelable {
    private String id;
    private String trackName;
    private int rating;
    private String price;
    private String booktype;
    private String email;
    private Integer count;
    public Track() {

    }

    public Track(String id, String trackName, int rating, String price,String booktype, String email) {
        this.trackName = trackName;
        this.rating = rating;
        this.id = id;
        this.price=price;
        this.booktype=booktype;
        this.email=email;
        count=0;
    }

    public String getTrackName() {
        return trackName;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void setTrackName(String TrackName){
        this.trackName=TrackName;
    }
    public String getPrice() {

        return price;
    }
    public int getRating() {
        return rating;
    }
    public void setBooktype(String booktype) {
        this.booktype = booktype;
    }


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void increment()
    {
        this.count ++;
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public static Creator<Track> getCreator() { return CREATOR;}

    public Track(Parcel parcel){
        this.trackName = parcel.readString();
        this.price = parcel.readString();
        this.booktype = parcel.readString();
        this.email=parcel.readString();
        this.id=parcel.readString();
        this.count=parcel.readInt();
    }


    public static final Parcelable.Creator<Track> CREATOR
            = new Parcelable.Creator<Track>() {
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        public Track[] newArray(int size) {
            return new Track[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.trackName);
        parcel.writeString(this.price);
        parcel.writeString(this.booktype);
        parcel.writeString(this.email);
        parcel.writeString(this.id);
        parcel.writeInt(this.count);
    }
}

