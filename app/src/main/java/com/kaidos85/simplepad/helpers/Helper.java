package com.kaidos85.simplepad.helpers;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AiDoS on 19.01.2016.
 */
public class Helper {

    public Helper()
    {

    }

    public static String dbToDate(String _date)
    {
        Date dateT = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        DateFormat format2 = new SimpleDateFormat("dd MMMM yyyy г.");
        try {
            dateT = format.parse(_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format2.format(dateT);
    }

    public static Date strToDate(String date)
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateT = new Date();
        try {
            dateT = format.parse(date);
        } catch (ParseException e) {
            Log.d("stringTODB ERROR", e.getMessage());
        }
        return dateT;
    }

    public static  LatLng strToLatLan(String loc_str){
        String[] loc_array = loc_str.split(",");
        LatLng loc = new LatLng(Double.parseDouble(loc_array[0]),
                Double.parseDouble(loc_array[1]));
        return loc;
    }

    public static String latLanToStr(LatLng loc){
        String lStr = loc.toString().substring(10);
        lStr = lStr.substring(0, lStr.length()-1);
        return  lStr;
    }

}
