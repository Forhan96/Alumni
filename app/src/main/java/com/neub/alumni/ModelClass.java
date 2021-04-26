package com.neub.alumni;

public class ModelClass {
    private String user;
    private String text;

    public ModelClass(String user, String text) {
        this.user = user;
        this.text = text;
    }

    public String getUser() {return user; }

    public String getBody() {
        return text;
    }

}
