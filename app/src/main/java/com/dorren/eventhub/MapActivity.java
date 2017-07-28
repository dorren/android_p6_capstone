package com.dorren.eventhub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dorren.eventhub.util.GeoPoint;
import com.dorren.eventhub.util.GeoUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        String address = "Flushing Meadows Corona Park Aquatic Center, Queens, NY";
        GeoPoint point = GeoUtil.addressToGeo(this, address);
        //double lat = 40.7344387;
        //double lng = -73.8404625;
        double lat = point.latitude;
        double lng = point.longitude;

        map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Marker"));

        CameraUpdate center= CameraUpdateFactory.newLatLng(new LatLng(lat, lng));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

        map.moveCamera(center);
        map.animateCamera(zoom);
    }
}
