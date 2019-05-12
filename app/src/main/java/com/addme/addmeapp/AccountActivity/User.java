package com.addme.addmeapp.AccountActivity;

public class User {

    String userid;
    String fullname;

    public User(){
        //constructor
    }

    public User(String userid, String fullname) {
        this.userid = userid;
        this.fullname = fullname;
    }

    public String getUserid() {
        return userid;
    }

    public String getFullname() {
        return fullname;
    }
}
