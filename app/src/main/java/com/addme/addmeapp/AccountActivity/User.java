package com.addme.addmeapp.AccountActivity;

public class User {

    String fullname;
    String email;
    String facebook;
    String instagram;
    String github;
    String snapchat;
    String twitter;
    String viber;
    String whatsapp;

    public User(){
        //constructor
    }

    public User(String fullname, String email, String facebook, String instagram, String github,
                String snapchat, String twitter, String viber, String whatsapp) {

        this.fullname = fullname;
        this.email = email;
        this.facebook = facebook;
        this.instagram = instagram;
        this.github = github;
        this.snapchat = snapchat;
        this.twitter = twitter;
        this.viber = viber;
        this.whatsapp = whatsapp;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() { return email;}

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFacebook() { return facebook; }

    public void setFacebook(String facebook) { this.facebook = facebook; }

    public String getInstagram() { return instagram; }

    public void setInstagram(String instagram) { this.instagram = instagram; }

    public String getGithub() { return github; }

    public void setGithub(String github) { this.github = github; }

    public String getSnapchat() { return snapchat; }

    public void setSnapchat(String snapchat) { this.snapchat = snapchat; }

    public String getTwitter() { return twitter; }

    public void setTwitter(String twitter) { this.twitter = twitter; }

    public String getViber() { return viber; }

    public void setViber(String viber) { this.viber = viber; }

    public String getWhatsapp() { return whatsapp; }

    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }
}
