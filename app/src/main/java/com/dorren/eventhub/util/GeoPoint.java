package com.dorren.eventhub.util;

/**
 * Created by dorrenchen on 7/27/17.
 */

public class GeoPoint {
    public double latitude;
    public double longitude;

    public GeoPoint(double lat, double lng){
        latitude = lat;
        longitude = lng;
    }

    public String toString() {
        return latitude + ", " + longitude;
    }
}
