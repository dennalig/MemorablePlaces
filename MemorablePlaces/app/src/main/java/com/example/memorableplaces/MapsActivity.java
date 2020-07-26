package com.example.memorableplaces;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    public static boolean flag=true;

    LocationManager locationManager;
    LocationListener locationListener;

    static LatLng globalLatLng=null;
    static String gLatLngString="";




    Marker locationMarker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        locationListener=null;
//Toast.makeText(MapsActivity.this, "Oncreate", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        try{ // hides the action bar
            this.getActionBar().hide();
        }
        catch(NullPointerException e){

        }






    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        locationListener=null;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


           if(requestCode==1){

            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

                    if(flag) {

//                        Log.i("in Request","request");
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                        locationListener = locationListen();
                        Log.i("Req.Permis", "request Permission");

                        if (Build.VERSION.SDK_INT < 23) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                        } else {
                            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                            } else {
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                                locationListener = locationListen();
                                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                                mMap.clear();

//                                Log.i("In else","else");

                                // Add a marker in Sydney and move the camera
                                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));

                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                            }
                        }

                    }




                }
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        locationListener=null;
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

//        Toast.makeText(MapsActivity.this, "Maps", Toast.LENGTH_SHORT).show();



        // Add a marker in Sydney and move the camera
//        Toast.makeText(MapsActivity.this, "try", Toast.LENGTH_SHORT).show();





        if(flag) {


            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = locationListen();

            Log.i("Map Activity", "Map Activity");


            if (Build.VERSION.SDK_INT < 23) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                } else {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                    mMap.clear();



                        // Add a marker in Sydney and move the camera
                        LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));

                    for(int i=0;i<MainActivity.locations.size();i++){


                            String otherTitle= MainActivity.locationList.get(i);

                            mMap.addMarker(new MarkerOptions().position(MainActivity.locations.get(i)).title(otherTitle)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


                    }



                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));


                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                }
            }

        }
        else{
//         Log.i("Map Activity else","Map activity else ");

            Intent intent= getIntent();

            int index= intent.getIntExtra("ArrayLocation",0);

            LatLng newLatLng=MainActivity.locations.get(index);
            String title=gLatLngString;
            locationMarker = mMap.addMarker(new MarkerOptions().position(newLatLng).title(title));

            Log.i("Location","Marker");

            for(int i=0;i<MainActivity.locations.size();i++){

                if(MainActivity.locations.get(i)!=newLatLng){
                    String otherTitle= MainActivity.locationList.get(i);

                    mMap.addMarker(new MarkerOptions().position(MainActivity.locations.get(i)).title(otherTitle)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                }

            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 15));



//            Toast.makeText(MapsActivity.this,newLatLng.latitude+"",Toast.LENGTH_SHORT).show();



        }







        // Set the gesture detector as the double tap
        // listener.

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
//                Toast.makeText(MapsActivity.this, "Long Press", Toast.LENGTH_SHORT).show();


                LatLng newLocation= latLng;


//                mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));

                Geocoder newGeo= new Geocoder(getApplicationContext(),Locale.getDefault());
                try {

                    //puts the addresses in a list
                    List<Address> listAddresses= newGeo.getFromLocation(newLocation.latitude, newLocation.longitude, 1);




                    if(listAddresses != null && listAddresses.size()>0){

                        String address="";

                        if(listAddresses.get(0).getThoroughfare() != null){
                            address+= listAddresses.get(0).getThoroughfare() +" ";
                        }
                        if(listAddresses.get(0).getLocality() != null){
                            address+= listAddresses.get(0).getLocality() +" ";
                        }
                        if(listAddresses.get(0).getPostalCode() != null){
                            address+= listAddresses.get(0).getPostalCode() +" ";
                        }
                        if(listAddresses.get(0).getAdminArea() != null){
                            address+= listAddresses.get(0).getAdminArea() +" ";
                        }


//                        Log.i("PlaceInfo", listAddresses.get(0).toString());

                        if(address.equals("")){ // if we dont have an address

                            SimpleDateFormat date= new SimpleDateFormat("HH:mm yyyy-MM-dd");
                            address=date.format(new Date());
                        }


                        MarkerOptions newMarker= new MarkerOptions().position(newLocation).title(address);
                        mMap.addMarker(newMarker);

                        Toast.makeText(MapsActivity.this, address +" added. ", Toast.LENGTH_SHORT).show();


                        MainActivity.locationList.add(address);
                        MainActivity.locations.add(newLocation);
                        MainActivity.arrayAdapter.notifyDataSetChanged(); // updates adapter

                        SharedPreferences sharedPreferences= getSharedPreferences("com.example.memorableplaces", Context.MODE_PRIVATE);

                        try{
                            sharedPreferences.edit().putString("places", ObjectSerializer.serialize(MainActivity.locationList)).apply();

                            ArrayList<String> lats= new ArrayList<>();
                            ArrayList<String> longs= new ArrayList<>();

                            for(LatLng coordinates : MainActivity.locations){
                                lats.add(coordinates.latitude+"");
                                longs.add(coordinates.longitude+"");
                            }

                            sharedPreferences.edit().putString("lats", ObjectSerializer.serialize(lats)).apply();
                            sharedPreferences.edit().putString("longs", ObjectSerializer.serialize(longs)).apply();

                        }
                        catch(Exception e){

                        e.printStackTrace();
                        }

                       finish();


                    }


                }
                catch(Exception e){
                    e.printStackTrace();
                }

            }
        });



    }

    public LocationListener locationListen() {

        if(flag==true) {


            return new LocationListener() {


                @Override
                public void onLocationChanged(Location location) {


                    if (locationMarker != null) {  // updates current Location
//                    Toast.makeText(MapsActivity.this, "here", Toast.LENGTH_SHORT).show();
                        locationMarker.remove();
                    }

                    if (flag) {


                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                locationMarker=new MarkerOptions().position(userLocation).title("Your Location");
                        locationMarker = mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
//                mMap.addMarker(locationMarker);

//                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));

//                        Log.i("Location Method", "Location Method");
//                        Log.i("flag", flag + "");


                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    }

                }




                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }

            };

        }
        else{

            // if we are not to look at the current location

//            Log.i("here","we got here");

            LatLng userLocation = new LatLng(globalLatLng.latitude,globalLatLng.longitude);
            String title=gLatLngString;
//                locationMarker=new MarkerOptions().position(userLocation).title("Your Location");
            locationMarker = mMap.addMarker(new MarkerOptions().position(userLocation).title(title));


            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());


            return null;
        }

        }











}