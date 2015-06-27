package com.infjay.mice;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;


public class ConfMapActivity extends FragmentActivity {
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_map);

        mMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.conf_map)).getMap();
/*
        float latitudeE6 = (Float.valueOf(lat));
        float longitudeE6 = (Float.valueOf(lon));
        System.out.println("lat:" + latitudeE6 + "lon:" + longitudeE6);
        LatLng LOC = new LatLng(latitudeE6, longitudeE6);
        mMap.addMarker(new MarkerOptions().position(LOC).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(result.getCompanyName()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LOC, 16));

        return;
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_conf_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
