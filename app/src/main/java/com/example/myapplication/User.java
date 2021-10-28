package com.example.myapplication;

public class User {
    String name;
    String surname;
    String email;
    int num;

    public User(String name, String surname, String email, int num) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.num = num;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}
