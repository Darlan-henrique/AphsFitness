package com.example.aphsfitness;

public class MainItem {
    private int id;
    private int textStringId;
    private int drawabledId;
    private int color;

    public MainItem(int id, int drawabledId, int textStringId, int color) {
        this.id = id;
        this.textStringId = textStringId;
        this.drawabledId = drawabledId;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTextStringId(int textStringId) {
        this.textStringId = textStringId;
    }

    public int getTextStringId() {
        return textStringId;
    }

    public int getDrawabledId() {
        return drawabledId;
    }

    public void setDrawabledId(int drawabledId) {
        this.drawabledId = drawabledId;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
