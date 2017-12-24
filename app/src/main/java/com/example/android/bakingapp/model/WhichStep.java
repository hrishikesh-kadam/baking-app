package com.example.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hrishikesh Kadam on 23/12/2017
 */

public class WhichStep implements Parcelable {

    public static final Creator<WhichStep> CREATOR = new Creator<WhichStep>() {
        @Override
        public WhichStep createFromParcel(Parcel in) {
            return new WhichStep(in);
        }

        @Override
        public WhichStep[] newArray(int size) {
            return new WhichStep[size];
        }
    };

    private String shortDescription;
    private String type;
    private int index;

    public WhichStep(String shortDescription, String type, int index) {
        this.shortDescription = shortDescription;
        this.type = type;
        this.index = index;
    }

    protected WhichStep(Parcel in) {
        shortDescription = in.readString();
        type = in.readString();
        index = in.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shortDescription);
        dest.writeString(type);
        dest.writeInt(index);
    }
}
