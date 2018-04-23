package com.devilslegacy.moviesearch.model;

import com.google.gson.annotations.SerializedName;

public class Dates {
    @SerializedName("maximum")
    private String maximum;
    @SerializedName("minimum")
    private String minimum;

    public Dates(String maximum, String minimum) {
        this.maximum = maximum;
        this.minimum = minimum;
    }

    public String getMaximum() {
        return maximum;
    }

    public String getMinimum() {
        return minimum;
    }
}
