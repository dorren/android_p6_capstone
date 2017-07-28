package com.dorren.eventhub.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;

/**
 * Created by dorrenchen on 7/27/17.
 */

public class GeoUtil {

    public static GeoPoint addressToGeo(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new GeoPoint(location.getLatitude(),location.getLongitude());

        }catch (IOException ex){
            ex.printStackTrace();
        }
        return p1;
    }
}