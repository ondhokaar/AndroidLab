package com.example.s13;

import androidx.dynamicanimation.animation.SpringAnimation;

public class userObj {
    private String name;
    private String phone;
    private String uid;
    private Boolean isUser = false;
    private boolean isMe = false;


    public userObj(String uid, String name, String phone) {
        this.uid = uid;
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

    public void setMe(boolean me) {
        isMe = me;
    }

    public boolean isMe() {
        return isMe;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
