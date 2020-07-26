package com.example.memorableplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> locationList= new ArrayList<String>(); // an array List of Locations
    static ArrayList<LatLng> locations= new ArrayList<LatLng>();
    static ArrayAdapter arrayAdapter;
    ListView listView;
    boolean declaredLocation= false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences= this.getSharedPreferences("com.example.memorableplaces", Context.MODE_PRIVATE);

        ArrayList<String> lats= new ArrayList<>();
        ArrayList<String> longs= new ArrayList<>();

        locationList.clear();
        locations.clear();
        lats.clear();
        longs.clear();
        try{

            locationList= (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places", ObjectSerializer.serialize(new ArrayList<String>())));
            lats= (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lats", ObjectSerializer.serialize(new ArrayList<String>())));
            longs= (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("longs", ObjectSerializer.serialize(new ArrayList<String>())));
        }
        catch (Exception e){

            e.printStackTrace();
        }

        if(locationList.size()>0 && lats.size()>0 && longs.size()>0){
            if(locationList.size()==lats.size() && locationList.size()== longs.size()){

                for(int i=0;i<lats.size();i++){
                    locations.add(new LatLng(Double.parseDouble(lats.get(i)), Double.parseDouble(longs.get(i))));
                }
            }

        }
        else{
            // first time the app has been opened
        }




        listView=findViewById(R.id.listView);

        arrayAdapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1,locationList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override


            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) { // for removal
                Toast.makeText(MainActivity.this, locationList.get(i), Toast.LENGTH_SHORT).show();

//                locationList.remove(i);
//                locations.remove(i);

                return true;
            }

        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                MapsActivity.flag=false;
//                MapsActivity.locationListener=null;
//                MapsActivity.findDefindedLocation(i);
                MapsActivity.globalLatLng= locations.get(i);
                MapsActivity.gLatLngString=locationList.get(i);
                Intent buttonIntent= new Intent(getApplicationContext(), MapsActivity.class);

                buttonIntent.putExtra("ArrayLocation", i);

                startActivity(buttonIntent);

            }
        });




        try{ // hides the action bar
            this.getSupportActionBar().hide();
        }
        catch(NullPointerException e){

        }




    }


    public void addLocation(View view){


        MapsActivity.flag=true;

        Intent buttonIntent= new Intent(getApplicationContext(), MapsActivity.class);


        startActivity(buttonIntent);

        declaredLocation=true;

    }




}