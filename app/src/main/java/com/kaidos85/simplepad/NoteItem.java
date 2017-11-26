package com.kaidos85.simplepad;

import android.provider.ContactsContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Aidos on 02.06.2016.
 */
public class NoteItem implements Item {
    public int Id;
    public String Title;
    public String Description;
    public String NoteDate;
    public String Location;

    public NoteItem(){
        this.Id = 0;
        this.NoteDate = dbToDate(new Date());
    }

    public NoteItem(int _id, String _title, String _desc, String _date)
    {
        Id = _id;
        Title = _title;
        Description = _desc;
        NoteDate = _date;
    }

    public NoteItem(int _id, String _title, String _desc, String _date, String _location)
    {
        Id = _id;
        Title = _title;
        Description = _desc;
        NoteDate = _date;
        Location = _location;
    }

    @Override
    public boolean isSection(){
        return false;
    }

    public String dbToDate(Date _date)
    {
        Date dateT = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(dateT);
    }

}
