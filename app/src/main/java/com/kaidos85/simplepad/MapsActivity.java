package com.kaidos85.simplepad;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kaidos85.simplepad.helpers.Helper;

public class MapsActivity extends AppCompatActivity {

    private final String LOG_TAG = "SimplePad";
    SupportMapFragment mapFragment;
    GoogleMap map;
    LatLng loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String loc_str = bundle.getString("location");
        Log.d(LOG_TAG, loc_str);
        if(!loc_str.equals("")){
            loc = Helper.strToLatLan(loc_str);
        }


        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                map.setOnMapClickListener(mapclick);
                map.setOnMarkerClickListener(markerClick);
                setMarker(loc);
            }
        });
    }

    GoogleMap.OnMapClickListener mapclick = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            map.clear();
            setMarker(latLng);
        }
    };

    private void setMarker(LatLng _loc){
        if(_loc != null){
            Marker myMarker = map.addMarker(new MarkerOptions()
                    .position(_loc)
                    .title("Selected")
                    .snippet("Selected place")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            loc = _loc;
        }
    }

    GoogleMap.OnMarkerClickListener markerClick = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Log.d(LOG_TAG, marker.getPosition().toString());
            marker.showInfoWindow();
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maps_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item_menu) {
        switch (item_menu.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                Cancel();
                this.finish();
                return true;
            case R.id.maps_menu_save:
                Save();
                this.finish();
                return true;
            case R.id.maps_menu_cancel:
                Cancel();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item_menu);
        }
    }

    private void Save(){
        Intent returnIntent = new Intent();
        String lStr = Helper.latLanToStr(loc);
        returnIntent.putExtra("Location", lStr);
        setResult(Activity.RESULT_OK, returnIntent);
    }

    private void Cancel(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
    }

}
