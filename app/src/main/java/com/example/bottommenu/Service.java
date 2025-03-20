package com.example.bottommenu;

import android.app.Person;

public class Service {
    private String name;
    private String time;
    private int style_img;

    public Service() {
    }

    public Service(String name, String time, int style_img) {
        this.name = name;
        this.time = time;
        this.style_img = style_img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStyle_img() {
        return style_img;
    }

    public void setStyle_img(int style_img) {
        this.style_img = style_img;
    }
}
