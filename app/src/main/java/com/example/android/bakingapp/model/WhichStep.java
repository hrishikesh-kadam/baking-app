package com.example.android.bakingapp.model;

/**
 * Created by Hrishikesh Kadam on 23/12/2017
 */

public class WhichStep {

    private String shortDescription;
    private String type;
    private int index;

    public WhichStep(String shortDescription, String type, int index) {
        this.shortDescription = shortDescription;
        this.type = type;
        this.index = index;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "WhichStep{" +
                "shortDescription='" + shortDescription + '\'' +
                ", type='" + type + '\'' +
                ", index=" + index +
                '}';
    }
}
