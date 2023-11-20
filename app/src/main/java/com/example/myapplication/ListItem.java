package com.example.myapplication;

public class ListItem {
    public static final int TYPE_COURSE = 0;
    public static final int TYPE_USER = 1;

    private int type;
    private String text;

    public ListItem(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public String getText() {
        return text;
    }
}
