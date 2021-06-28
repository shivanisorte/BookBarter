package com.shivani.bookbarter;


public class model
{
    String name,author,email,purl,genre, ownerpincode;
    model()
    {

    }
    public model(String name, String author, String email, String purl, String genre, String ownerpincode) {
        this.name = name;
        this.author = author;
        this.email = email;
        this.purl = purl;
        this.genre=genre;
        this.ownerpincode=ownerpincode;
    }

    public String getGenre()
    {
        return genre;
    }

    public  String getPincode()
    {
        return ownerpincode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public  void setGenre (String genre)
    {
        this.genre = genre;
    }

    public void setPincode(String pincode)
    {
        this.ownerpincode = pincode;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }
}
