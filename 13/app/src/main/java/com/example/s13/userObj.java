package com.example.s13;

import androidx.dynamicanimation.animation.SpringAnimation;

public class userObj {
    private String name;
    private String phone;



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
}
