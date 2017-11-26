package com.kaidos85.simplepad;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kaidos85.simplepad.dal.RepositoryNote;
import com.kaidos85.simplepad.helpers.Helper;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity{

    private final String LOG_TAG = "SimplePad";
    ListView view;
    RepositoryNote rep;
    ArrayList<Item> items;
    ArrayAdapter<Item> adapter;

    SupportMapFragment mapFragment;
    GoogleMap map;
    String sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditActivity(new NoteItem());
            }
        });

        TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);

        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("tag1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Notes");
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Map");
        tabs.addTab(spec);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                map.setOnMarkerClickListener(markerClick);
                map.setOnInfoWindowLongClickListener(infoMarker);
                map.setInfoWindowAdapter(markerAdapter);
                setMarker();
            }
        });

        SharedPreferences settingsActivity = getSharedPreferences("Sorting", MODE_PRIVATE);
        sort = settingsActivity.getString("Sort", RepositoryNote.orderByDate);

        view = (ListView)findViewById(R.id.listview1);
        registerForContextMenu(view);

        rep = new RepositoryNote(this);
        LoadItems();
        view.setOnItemClickListener(itemClick);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                Log.d(LOG_TAG, "OK!!!");
                LoadItems();
            }
            if(resultCode == Activity.RESULT_CANCELED){
                Log.d(LOG_TAG, "Cancel!!!");
            }
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        NoteItem note = (NoteItem)view.getItemAtPosition(info.position);
        switch (item.getItemId()) {
            case R.id.context_edit:
                EditActivity(note);
                return true;
            case R.id.context_delete:
                contextDelete(note);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort) {
            new SortDialog().show(getFragmentManager(), "Sort");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id)
        {
            // получаем выбранный пункт
            NoteItem item = (NoteItem)parent.getItemAtPosition(position);
            EditActivity(item);
        }
    };

    private void EditActivity(NoteItem item){
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("Id", String.valueOf(item.Id));
        startActivityForResult(intent, 1);
    }

    private void LoadItems(){

        items = rep.getNoteItems(sort);
        adapter = new MyAdapter(this, items);
        view.setAdapter(adapter);
        if(map != null)
            setMarker();
    }

    private void contextDelete(final NoteItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete item");
        builder.setMessage("Do you delete this item?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rep.Delete(item);
                LoadItems();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(LOG_TAG, "Cancel delete in dailog");
            }
        });
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }

    private void setMarker(){
        map.clear();
        for(int i=0; i < items.size();i++){
            NoteItem item = (NoteItem)items.get(i);
            if(!item.Location.equals("")){
                String text = String.format("Date: %s.%n%s%n", item.NoteDate, item.Description);
                Marker myMarker = map.addMarker(new MarkerOptions()
                        .position(Helper.strToLatLan(item.Location))
                        .title(item.Title)
                        .snippet(text)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }
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

    GoogleMap.OnInfoWindowLongClickListener infoMarker = new GoogleMap.OnInfoWindowLongClickListener() {
        @Override
        public void onInfoWindowLongClick(Marker marker) {
            NoteItem note = rep.getNoteItemByTitle(marker.getTitle());
            Log.d(LOG_TAG, String.valueOf(note.Id));
            EditActivity(note);
        }
    };

    GoogleMap.InfoWindowAdapter markerAdapter = new GoogleMap.InfoWindowAdapter() {
        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View v = getLayoutInflater().inflate(R.layout.custom_marker, null);
            TextView vTitle = (TextView)v.findViewById(R.id.markerTitle);
            TextView vSnip = (TextView)v.findViewById(R.id.markerText);
            vTitle.setText(marker.getTitle());
            vSnip.setText(marker.getSnippet());
            return v;
        }
    };

    public void SetSort(String _sort){
        sort = _sort;
        LoadItems();
        SharedPreferences settingsActivity = getSharedPreferences("Sorting", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settingsActivity.edit();
        prefEditor.putString("Sort", sort);
        prefEditor.commit();
    }
}
