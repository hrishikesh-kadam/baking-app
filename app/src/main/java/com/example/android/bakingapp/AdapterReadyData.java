package com.example.android.bakingapp;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by Hrishikesh Kadam on 17/12/2017
 */

public class AdapterReadyData {

    public int viewType;
    public Object data;

    public AdapterReadyData(int viewType, Object data) {
        this.viewType = viewType;
        this.data = data;
    }

    public String getViewTypeString() {

        Field[] fields = ViewType.class.getFields();

        for (Field field : fields) {

            if (Modifier.isFinal(field.getModifiers())) {

                int tempViewType = -1;

                try {

                    tempViewType = field.getInt(null);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {

                }

                if (tempViewType == viewType)
                    return field.getName();
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "AdapterReadyData { " +
                "viewType = " + getViewTypeString() +
                ", data = " + data +
                " }";
    }
}
