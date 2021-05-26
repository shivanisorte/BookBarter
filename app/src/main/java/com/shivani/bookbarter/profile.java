package com.shivani.bookbarter;

import android.os.Parcel;
import android.os.Parcelable;

public class profile implements Parcelable {
    private String name;
    private String photourl;
    private String email;
    private String phoneno;
    private String Uid;

    public profile() { super(); }
    public profile(String name, String photourl, String email, String phoneno,String Uid){
        this.name = name;
        this.photourl = photourl;
        this.email = email;
        this.phoneno = phoneno;
        this.Uid = Uid;;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setPhotourl(String photourl){
        this.photourl = photourl;
    }
    public String getPhotourl(){
        return photourl;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }
    public void setPhoneno(String phoneno){
        this.phoneno = phoneno;
    }
    public String getPhoneno(){
        return phoneno;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    protected profile(Parcel in) {
        this.name = in.readString();
        this.photourl = in.readString();
        this.email = in.readString();
        this.phoneno = in.readString();
        this.Uid = in.readString();
    }
    public static final Creator<profile> CREATOR = new Creator<profile>() {
        @Override
        public profile createFromParcel(Parcel in) {
            return new profile(in);
        }
        @Override
        public profile[] newArray(int size) {
            return new profile[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.photourl);
        parcel.writeString(this.email);
        parcel.writeString(this.phoneno);
        parcel.writeString(this.Uid);
    }
}