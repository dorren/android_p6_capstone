package com.dorren.eventhub.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by dorrenchen on 7/27/17.
 */

public class GeoUtil {
    private static final String TAG = GeoUtil.class.getSimpleName();

    public static LatLng addressToLatLng(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null || address.size() == 0) {
                return null;
            }
            Log.d(TAG, address.toString());
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(),location.getLongitude());

        }catch (IOException ex){
            ex.printStackTrace();
        }
        return p1;
    }

    public static Location address2Location(Context context, String address){
        Location location = null;
        LatLng point = GeoUtil.addressToLatLng(context, address);

        if(point != null) {
            location = new Location("");
            location.setLatitude(point.latitude);
            location.setLongitude(point.longitude);
        }

        return location;
    }

    public static LatLng location2LatLng(Location location){
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        return new LatLng(lat, lng);
    }
}
