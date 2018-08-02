package com.example.chenxiyu.datepicker;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AdvancedSearch extends AppCompatActivity implements OnMapReadyCallback{
    private MyDB mydb;
    TableLayout tableLayout;
    private Context context = null;
    GoogleMap mMap;

    private Marker m1;
    private Marker m2;
    private Marker m3;
    private Marker m4;
    private Marker m5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);

        mydb = new MyDB(this);
        tableLayout = (TableLayout)findViewById(R.id.table_layout);
        context = getApplicationContext();

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        mMap = googleMap;

        LatLng chicago = new LatLng(41.8781, -87.6298);
        m1 =  mMap.addMarker(new MarkerOptions().position(chicago)
                .title("Marker in Chicago"));
        m1.setTag(0);
        LatLng sydney = new LatLng(-33.8688, 151.2093);

        m2 =  mMap.addMarker(new MarkerOptions().position(sydney)
                .title("jesus"));
        m2.setTag(0);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    public void search(View view){

        EditText input_text= (EditText) findViewById(R.id.text);
        String search_String = input_text.getText().toString();

        if(search_String.isEmpty()){
            return;
        }

        String eve = "";
        String dat = "";
        String add = "";

        Cursor rs =  mydb.getAppox(search_String);
        Log.e("return", ""+rs.getCount());
        ArrayList<String>events = new ArrayList<>(10);
        ArrayList<String>addresses = new ArrayList<>(10);
        rs.moveToFirst();
        int count  = 1;
        eve = rs.getString(0);
        dat = rs.getString(1);
        add = rs.getString(3);

        events.add(eve);
        addresses.add(add);


        while(rs.moveToNext() && count <= 10){
            eve = rs.getString(0);
            add = rs.getString(3);

            events.add(eve);
            addresses.add(add);
            count++;
        }

        Geocoder gc = new Geocoder(this);
        List<Address> address;



        try {
            Log.e("get address working", "1");
            // May throw an IOException


            address = gc.getFromLocationName(addresses.get(0), 5);
            if (address == null) {
                Log.e("get address working", "2");
                return;
            }

            Address location = address.get(0);
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            LatLng temp = new LatLng(lat, lng);
            m1.remove();
            m1 =  mMap.addMarker(new MarkerOptions().position(temp)
                    .title(events.get(0)));
            m1.setTag(0);


            address = gc.getFromLocationName(addresses.get(1), 5);
            if (address == null) {
                return;
            }

            location = address.get(0);
            lat = location.getLatitude();
            lng = location.getLongitude();
            Log.e("and", "2");
            temp = new LatLng(lat, lng);
            m2.remove();
            m2 =  mMap.addMarker(new MarkerOptions().position(temp)
                    .title(events.get(1)));
            m2.setTag(0);


            address = gc.getFromLocationName(addresses.get(2), 5);
            if (address == null) {
                Log.e("get address working", "2");
                return;
            }

            location = address.get(0);
            lat = location.getLatitude();
            lng = location.getLongitude();

            temp = new LatLng(lat, lng);
            if(m3 != null){
                m3.remove();
            }
            m3 =  mMap.addMarker(new MarkerOptions().position(temp)
                    .title(events.get(2)));
            m3.setTag(0);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(temp));
            Log.e("after", "1");
        } catch (IOException ex) {

        }


    }

    public void recommend(View view){
        EditText input_text = (EditText) findViewById(R.id.text);
        String user = input_text.getText().toString();
        if(user.isEmpty()){
            return;
        }


        Map<String, Integer> relation = new HashMap<>();

        Cursor events = mydb.getByName(user);
        events.moveToFirst();

        String tempEve = events.getString(events.getColumnIndex("event"));
        ArrayList<String> event_list = new ArrayList<>(10);
        int count = 0;
        event_list.add(tempEve);
        while(events.moveToNext()){
            tempEve = events.getString(events.getColumnIndex("event"));
            event_list.add(tempEve);
            count++;
        }

        String name = "";
        for(int i = 0; i< count; i++) {
            Cursor names = mydb.getByEvent(event_list.get(i));
            names.moveToFirst();
            if(names.getCount()<=0){
                continue;
            }
            name = names.getString(names.getColumnIndex("name"));
            if(name.equals(user)){
                continue;
            }
            if(relation.containsKey(name)){
                relation.put(name, relation.get(name)+1);
            }else{
                relation.put(name, 1);
            }
            while(names.moveToNext()){
                name = names.getString(names.getColumnIndex("name"));
                if(name.equals(user)){
                    continue;
                }
                if(relation.containsKey(name)){
                    relation.put(name, relation.get(name)+1);
                }else{
                    relation.put(name, 1);
                }
            }

        }

        int n = 0;
        Iterator it = relation.entrySet().iterator();

        while(it.hasNext() && n < 3) {
            Map.Entry pair = (Map.Entry) it.next();
            String smt = (String)pair.getKey();
            Toast.makeText(getApplicationContext(), smt+"would be likely to get along with you",
                    Toast.LENGTH_SHORT)
                    .show();
        }

    }


}
