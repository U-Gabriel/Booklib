package com.esgi.booklib.entities;

public class Connection {

    private String login;
    private String password;

    public Connection() {
    }

    public Connection(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
