package com.esgi.booklib.entities;

public class DisplayBook {

    private String isbn;
    private String title;

    public DisplayBook(Book book){
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }
}
