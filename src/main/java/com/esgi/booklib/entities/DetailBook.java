package com.esgi.booklib.entities;

public class DetailBook {

    private String title;
    private String author;
    private String overview;
    private String picture;
    private int read_count;

    public DetailBook(String title, String author, String overview, String picture, int read_count) {
        this.title = title;
        this.author = author;
        this.overview = overview;
        this.picture = picture;
        this.read_count = read_count;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getOverview() {
        return overview;
    }

    public String getPicture() {
        return picture;
    }

    public int getRead_count() {
        return read_count;
    }
}
