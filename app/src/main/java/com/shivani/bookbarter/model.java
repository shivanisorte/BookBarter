package com.shivani.bookbarter;


public class model
{
    String name,author,email,purl,genre, ownerpincode, bookname, personrequesting, timendate, location,ownerUserId;
    model()
    {

    }
    public model(String name, String author, String email, String purl, String genre, String ownerpincode,String ownerUserId, String bookname, String personrequesting, String timendate, String location) {
        this.name = name;
        this.author = author;
        this.email = email;
        this.purl = purl;
        this.genre=genre;
        this.ownerUserId = ownerUserId;
        this.ownerpincode=ownerpincode;
        this.bookname=bookname;
        this.personrequesting=personrequesting;
        this.timendate=timendate;
        this.location=location;
    }
    public String getOwnerUserId()
    {
        return ownerUserId;
    }

    public  void setOwnerUserId()
    {
        this.ownerUserId =ownerUserId;
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

    public void setPincode(String ownerpincode)
    {
        this.ownerpincode = ownerpincode;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getbookname() {
        return bookname;
    }

    public String getpersonrequesting() {
        return personrequesting;
    }

    public String getTimendate(){
        return timendate;
    }

    public String getlocation(){
        return location;
    }
}
