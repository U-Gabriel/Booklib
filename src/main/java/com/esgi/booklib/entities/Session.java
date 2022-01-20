package com.esgi.booklib.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="session")
public class Session implements Serializable {

    @Id
    @Column(name= "token")
    private String token;

    @ManyToOne
    @JoinColumn(name= "id_user")
    private User user;

}
