package com.example.android.bakingapp;

/**
 * Created by Hrishikesh Kadam on 17/12/2017
 */

public class AdapterReadyData {

    public ViewType viewType;
    public Object data;

    public AdapterReadyData(ViewType viewType, Object data) {
        this.viewType = viewType;
        this.data = data;
    }
}
