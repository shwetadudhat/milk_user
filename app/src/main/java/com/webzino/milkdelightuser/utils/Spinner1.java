package com.webzino.milkdelightuser.utils;

public class Spinner1 {
    public Spinner1(String ID, String title) {
        this.ID = ID;
        this.title = title;
    }

    public Spinner1(String ID, String title, long duration, double price) {
        this.ID = ID;
        this.title = title;
        this.duration = duration;
        this.price = price;
    }

    public Spinner1(String title) {
        this.title = title;
    }

    public String ID;
    public String title;
    public long duration;
    public double price;

    public boolean isSelected;

    public Spinner1 setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        return this;
    }

    @Override
    public String toString() {
        return "Spinner1{" +
                "ID='" + ID + '\'' +
                ", title='" + title + '\'' +
                ", duration=" + duration +
                ", price=" + price +
                ", isSelected=" + isSelected +
                '}';
    }
}
