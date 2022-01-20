package com.esgi.booklib.entities;

import com.esgi.booklib.repositories.UserRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(name = "book")
public class Book implements Serializable {

    @Id
    @Column(name = "isbn")
    private String isbn;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "overview")
    private String overview;

    @Column(name = "picture")
    private String picture;

    @Column(name = "read_count")
    private int read_count;

    @Column(name = "is_delete")
    private boolean isDelete;

    @Column(name = "date_delete")
    private Date dateDelete;

    @Column(name = "date_create")
    private Date dateCreate;

    @ManyToOne
    @JoinColumn(name="id_user_delete")
    private User userDelete;

    @ManyToOne
    @JoinColumn(name="id_user_create")
    private User userCreate;

    public String getIsbn() {
        return isbn;
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

    public boolean isDelete() {
        return isDelete;
    }

    public Date getDateDelete() {
        return dateDelete;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public User getUserDelete() {
        return userDelete;
    }

    public User getUserCreate() {
        return userCreate;
    }

    public void setIsbn(String isbn){
        this.isbn = isbn;
    }

    public void updateBook(Book newBook){
        this.author = newBook.author;
        this.title = newBook.title;
        this.overview = newBook.overview;
        this.picture = newBook.picture;
        this.read_count = newBook.read_count;
    }

    public void deleteBook(int usrId, UserRepository userRepository){
        Optional<User> optUser = userRepository.findById(usrId);
        optUser.ifPresent(user -> this.userDelete = user);
        this.isDelete = true;
        this.dateDelete = new Date();
    }

    public void createBook(int usrId, UserRepository userRepository){
        Optional<User> optUser = userRepository.findById(usrId);
        optUser.ifPresent(user -> this.userCreate = user);
        this.isDelete = false;
        this.userDelete = null;
        this.dateCreate = new Date();
    }

    public DetailBook getDetailBook(){
        return new DetailBook(title, author, overview, picture, read_count);
    }

}
