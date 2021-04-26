package com.neub.alumni;

public class SearchItem {
    private String imageResource, name, dept, session;
    public SearchItem(String imageResource, String name, String dept, String session) {
        this.imageResource = imageResource;
        this.name = name;
        this.dept = dept;
        this.session = session;
    }
    public String getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public String getDept() {
        return dept;
    }

    //Returns designation for faculty instead of Session
    public String getSession() {return session;}
}
