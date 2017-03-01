package com.boardwords.modal;


public class ItemObject {

    private String name;
    private int color;

    public ItemObject(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int photo) {
        this.color = photo;
    }

}
