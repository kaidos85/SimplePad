package com.kaidos85.simplepad;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.kaidos85.simplepad.dal.RepositoryNote;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    private final String LOG_TAG = "SimplePad";

    EditText title;
    EditText desc;
    TextView date;
    TextView location;
    RepositoryNote rep;
    NoteItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        rep = new RepositoryNote(this);

        title = (EditText)findViewById(R.id.TitleText);
        desc = (EditText)findViewById(R.id.DescText);
        date = (TextView)findViewById(R.id.DateText);
        location = (TextView)findViewById(R.id.LocationText);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("Id");
        Log.d(LOG_TAG, id);
        loadData(id);

        Button setDate = (Button)findViewById(R.id.setDate);
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CustomDateDialog().show(getSupportFragmentManager(), "date");
            }
        });

        final Button setLocation = (Button)findViewById(R.id.setLocation);
        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocation();
            }
        });

        final Button clearLocation = (Button)findViewById(R.id.clearLocation);
        clearLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);

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
            case R.id.saveButton:
                Save(false);
                this.finish();
                return true;
            case R.id.deleteButton:
                if(item.Id != 0){
                    contextDelete(item);
                }
                else
                    this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item_menu);
        }
    }

    private void loadData(String id){
        if(id.equals("0"))
            item = new NoteItem();
        else
            item = rep.getNoteItem(id);
        title.setText(item.Title);
        desc.setText(item.Description);
        date.setText(item.NoteDate);
        location.setText(item.Location);
    }

    private void Save(boolean delete){
        if(!delete) {
            item.Title = title.getText().toString();
            item.Description = desc.getText().toString();
            item.NoteDate = date.getText().toString();
            item.Location = location.getText().toString();
            rep.insertOrUpdate(item);
        }
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
    }

    private void Cancel(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
    }

    private void contextDelete(final NoteItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete item");
        builder.setMessage("Do you delete this item?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rep.Delete(item);
                Save(true);
                finish();
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

    private void setLocation(){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("location", location.getText());
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                Bundle bund = data.getExtras();
                String str = bund.getString("Location");
                Log.d(LOG_TAG, "OK!!!");
                Log.d(LOG_TAG, str);
                location.setText(str);
            }
            if(resultCode == Activity.RESULT_CANCELED){
                Log.d(LOG_TAG, "Cancel!!!");
            }
        }

    }
}
