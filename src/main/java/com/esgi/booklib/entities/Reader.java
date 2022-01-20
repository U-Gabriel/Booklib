package com.esgi.booklib.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="reader")
public class Reader implements Serializable {

    @Id
    @Column(name= "id_reader")
    private String id;

    @Column(name= "date_reader")
    private Date date;

    @ManyToOne
    @JoinColumn(name= "isbn")
    private Book book;

    @ManyToOne
    @JoinColumn(name= "id_user")
    private User user;

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Book getBook() {
        return book;
    }

    public User getUser() {
        return user;
    }

}
