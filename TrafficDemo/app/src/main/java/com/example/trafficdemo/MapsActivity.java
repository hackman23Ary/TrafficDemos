package com.example.trafficdemo;




import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.os.Environment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

        private GoogleMap mMap;
        public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
        public String MyTag = "TrafficApp";
        public int N = 0;
        public int W = 1;
        public int S = 2;
        public int E = 3;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_maps);
                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);


        }


        public void AskPermissions(Activity thisActivity, String permission, int permissioncode ) {
                // Here, thisActivity is the current activity
                if(ContextCompat.checkSelfPermission(thisActivity,
                        permission)
                        != PackageManager.PERMISSION_GRANTED)

                {
                        // Permission is not granted
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                                permission)) {
                                // Show an explanation to the user *asynchronously* -- don't block
                                // this thread waiting for the user's response! After the user
                                // sees the explanation, try again to request the permission.
                        } else {
                                // No explanation needed; request the permission
                                ActivityCompat.requestPermissions(thisActivity,
                                        new String[]{permission},
                                        permissioncode);
                                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                // app-defined int constant. The callback method gets the
                                // result of the request.
                        }
                } else

                {
                        // Permission has already been granted
                }

        }

        @Override
        public void onRequestPermissionsResult(int requestCode,
                                               String permissions[], int[] grantResults) {
                switch (requestCode) {
                        case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                                // If request is cancelled, the result arrays are empty.
                                if (grantResults.length > 0
                                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                                        Log.d(MyTag, "WritePermissionGranted");
                                        // permission was granted, yay! Do the
                                        // contacts-related task you need to do.
                                } else {
                                        // permission denied, boo! Disable the
                                        // functionality that depends on this permission.
                                }
                                return;
                        }
                        case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                                // If request is cancelled, the result arrays are empty.
                                if (grantResults.length > 0
                                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                                        Log.d(MyTag, "ReadPermissionGranted");
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

        /** Anirudh's code **/

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                // Add a marker in Hope Farm and move the camera
                LatLng hopefarm = new LatLng(12.9837667, 77.7523578);
                mMap.addMarker(new MarkerOptions().position(hopefarm).title("Marker in Hopefarm"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(hopefarm));
                mMap.setMaxZoomPreference((float) 15.5);
                mMap.setMinZoomPreference((float)15.5);
                mMap.setTrafficEnabled(true);
                AskPermissions(this,Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraUpdateFactory.()));
                //View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                //getScreenShot(rootView);
                //CaptureMapScreen(mMap);
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        public void onMapLoaded() {
                                mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                                        public void onSnapshotReady(Bitmap bitmap) {
                                                /* Write image to disk */
                                                FileOutputStream out = null;
                                                try {
                                                        String filename = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Mymap.png";
                                                        out = new FileOutputStream(filename);
                                                } catch (FileNotFoundException e) {
                                                        e.printStackTrace();
                                                }
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                                AnalyzeBitMap(bitmap);
                                        }
                                });
                        }
                });
        }

        /******************* Image Processing Code *****/


        public float[] AnalyzeBitMap(Bitmap bitmap) {

                int xStart[] ={808, 618, 262, 255, 437, 606, 971, 966};
                int yStart[] ={180, 149, 338, 492, 683, 750, 604, 408};
                int xEnd[] ={741, 560, 505, 417, 466, 648, 710, 771};
                int yEnd[] ={354, 306, 350, 532, 574, 380, 603, 610};
                float trafficlevel[] = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
                float green[] = {35.0f, 25.0f, 40.0f, 20.0f};

                int NI = 0;
                int NO = 1;
                int WI = 2;
                int WO = 3;
                int SI = 4;
                int SO = 5;
                int EI = 6;
                int EO = 7;
                int road;

                for (road = 0; road <1; road++)
                        // trafficlevel[road] = AnalyzeRoad(bitmap, xStart[road], xEnd[road], yStart[road], yEnd[road]);
                        trafficlevel[road] = AnalyzeRoad(bitmap, xStart[road], 0, yStart[road],0);
                // Add logic to convert trafficlevel to compute green values
                // More logic needed

                green[N] = (trafficlevel[NI] -
                        ((trafficlevel[SO] + trafficlevel[WO] + trafficlevel[EO])/3));
                return (green);
        }


        public float AnalyzeRoad(Bitmap bitmap, int xs, int xe, int ys, int ye) {
                int xs1 = 590;  //638
                int ys1 = 716;  //417
                int xs2 = 708;  //638
                int ys2 = 429;
                int height = bitmap.getHeight();
                int width = bitmap.getWidth();
                Log.d(MyTag, "width = " + width + " height = " + height);
                int pixel = bitmap.getPixel(xs1,ys1);
                int pixel2 = bitmap.getPixel(xs2,ys2);
                int redValue = Color.red(pixel);
                int greenValue = Color.green(pixel);
                int blueValue = Color.blue(pixel);
                int redValue2 = Color.red(pixel2);
                int greenValue2 = Color.green(pixel2);
                int blueValue2 = Color.blue(pixel2);
                float colourScale = 0.0f;
                Log.d(MyTag, "The red value is:" + redValue + "The green value is:" + greenValue + "The blue value is:" + blueValue);
                Log.d(MyTag, "The red value is:" + redValue2+ "The green value is:" + greenValue2 + "The blue value is:" + blueValue2);
                colourScale = (((255-greenValue)/255.0f) + ((255-greenValue2)/255.0f))/2 ;
                Log.d(MyTag, "The color scale generated is:" + colourScale);
                return 0.0f;
        }

}





