package com.example.mobilecoursework;
//Marc Leese
//S1827987

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1337;
    private GoogleMap mMap;



    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_maps);




            Intent intent = getIntent();
            intent.getStringExtra("title");
            intent.getStringExtra("location");



            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);

            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else {
            }



        }

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onMapReady(GoogleMap googleMap) {


            mMap = googleMap;
            mMap.setMyLocationEnabled(true);
            mMap.setTrafficEnabled(true);
            // Add a marker in Sydney and move the camera
            Intent intent = getIntent();
            String TAG = "Maps Activity";
            final String rss_link = getIntent().getStringExtra("rssLink");
            double lat = intent.getDoubleExtra("lat", 0);
            double lng = intent.getDoubleExtra("lng", 0);
            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("description");
            String desc1 = intent.getStringExtra("desc1");
            String pubDate = intent.getStringExtra("pubDate");
            LatLng location = new LatLng(lat, lng);

            Calendar cal = Calendar.getInstance();
            assert pubDate != null;
                DateFormat change = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
            Date newDate = null;
            try {
                newDate = change.parse(pubDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println("DATE: " + newDate);

            googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));


            Calendar week = Calendar.getInstance();
            Calendar two = Calendar.getInstance();
            week.add(Calendar.DAY_OF_MONTH, -7);
            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
            Date weekDate = week.getTime();
            String Week = format1.format(weekDate);
            Log.i(TAG, "Week" + Week);
            two.add(Calendar.DAY_OF_MONTH, -2);
            Date twoDate = two.getTime();
            String Two = format1.format(twoDate);
            Log.i(TAG, "Two" + Two);

            assert rss_link != null;
            if (rss_link.equals("https://trafficscotland.org/rss/feeds/currentincidents.aspx")) {

                Marker rw = googleMap.addMarker(new MarkerOptions().position(location).title(title).snippet(desc1));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                // Zoom in, animating the camera.
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                assert newDate != null;
                if (newDate.before(weekDate) || newDate.equals(weekDate)) {
                    rw.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (newDate.after(twoDate) && newDate.before(weekDate)) {
                    rw.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                } else {
                    rw.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }

            } else {
                Marker rw = googleMap.addMarker(new MarkerOptions().position(location).title(title).snippet(description));
                rw.showInfoWindow();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                // Zoom in, animating the camera.
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                assert newDate != null;
                if (newDate.before(weekDate) || newDate.equals(weekDate)) {
                    rw.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (newDate.before(twoDate) && newDate.after(weekDate)) {
                    rw.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                } else if (newDate.equals(twoDate) || newDate.after(twoDate)){
                    rw.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));


        }


        private void goToLocationZoom(double lat, double lng, float zoom) {
            LatLng ll = new LatLng(lat, lng);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
            mMap.moveCamera(update);
        }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }





}
