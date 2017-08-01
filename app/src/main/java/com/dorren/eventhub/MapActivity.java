package com.dorren.eventhub;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.dorren.eventhub.data.model.Event;
import com.dorren.eventhub.util.GeoUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final String TAG = MapActivity.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;
    private boolean mLocationPermissionGranted;
    private int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Location mTargetLocation;
    private Location mUserLocation;
    private GoogleMap mMap;
    private String mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            mAddress = intent.getStringExtra(Intent.EXTRA_TEXT);
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finishAfterTransition();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mTargetLocation = GeoUtil.address2Location(this, mAddress);

        if(mTargetLocation == null){
            Log.d(TAG, "fail to find address " + mAddress);
            openExternalMap(mAddress);
            finish();
        }else {
            LatLng ll = GeoUtil.location2LatLng(mTargetLocation);
            map.addMarker(new MarkerOptions().position(ll).title("Destination"));
            getDeviceLocation();
            renderMap();
        }
    }

    private void renderMap() {
        GoogleMap map = mMap;

        if(mUserLocation != null) {
            map.setPadding(100, 100, 100, 100);
            LatLng ll = GeoUtil.location2LatLng(mUserLocation);
            map.addMarker(new MarkerOptions().position(ll).title("You"));

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(GeoUtil.location2LatLng(mTargetLocation));
            builder.include(GeoUtil.location2LatLng(mUserLocation));
            LatLngBounds bounds = builder.build();
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
        }else {
            LatLng ll = GeoUtil.location2LatLng(mTargetLocation);
            CameraUpdate center= CameraUpdateFactory.newLatLng(ll);
            CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

            map.moveCamera(center);
            map.animateCamera(zoom);
        }
    }

    private void getDeviceLocation() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        if (mLocationPermissionGranted) {
            mUserLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }else{
            Log.d(TAG, "location permission not granted");
        }
        if (mUserLocation == null) {
        //if(false){
            LocationRequest locReq =  LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                    .setFastestInterval(1 * 1000);


            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locReq, this);
        }
        Log.d(TAG, "----- userLocation " + mUserLocation);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mUserLocation = location;
        Log.d(TAG, "----- onLocationChanged " + mUserLocation);

        renderMap();
    }

    private void openExternalMap(String location) {
        Uri uri = Uri.parse("geo:0,0?q=" + location);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(TAG, "Couldn't call " + uri.toString()
                     + ", no receiving apps installed!");
        }
    }
}
