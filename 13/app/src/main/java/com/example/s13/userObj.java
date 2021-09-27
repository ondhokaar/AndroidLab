package com.example.s13;

import androidx.dynamicanimation.animation.SpringAnimation;

public class userObj {
    private String name;
    private String phone;
    private Boolean isUser = false;


    public userObj(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }


    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Boolean getUser() {
        return isUser;
    }

    public void setUser(Boolean user) {
        isUser = user;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
