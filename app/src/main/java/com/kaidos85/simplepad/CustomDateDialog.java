package com.kaidos85.simplepad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Aidos on 07.06.2016.
 */
public class CustomDateDialog extends DialogFragment implements DialogInterface.OnClickListener{

    final String LOG_TAG="SimplePad";
    private View view = null;
    TimePicker timepic;
    DatePicker datepic;
    TextView dateText;

    @Override
    public Dialog onCreateDialog(Bundle SavedInstanse){
        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
        timepic = (TimePicker)view.findViewById(R.id.timePicker);
        datepic = (DatePicker)view.findViewById(R.id.datePicker);
        dateText = (TextView)getActivity().findViewById(R.id.DateText);
        timepic.setIs24HourView(true);
        setDate();

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return(builder.setTitle("Change date").setView(view)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, null).create());

    }

    @Override
    public void onClick(DialogInterface dialog, int which){
        int year = datepic.getYear();
        int month = datepic.getMonth();
        int day = datepic.getDayOfMonth();
        int hour = timepic.getCurrentHour();
        int minute = timepic.getCurrentMinute();
        String selectedTime = String.format("%d-%02d-%02d %02d:%02d",
                year,
                month,
                day,
                hour,
                minute);
        dateText.setText(selectedTime);
    }

    @Override
    public void onDismiss(DialogInterface unused) {
        super.onDismiss(unused);
    }
    @Override
    public void onCancel(DialogInterface unused) {
        super.onCancel(unused);
    }

    private void setDate(){
        Date date = strToDate(dateText.getText().toString());
        Log.d(LOG_TAG, date.toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        datepic.updateDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) +1,
                calendar.get(Calendar.DAY_OF_MONTH));
        timepic.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timepic.setCurrentMinute(calendar.get(Calendar.MINUTE));
    }

    public Date strToDate(String date)
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateT = null;
        try {
            dateT = format.parse(date);
        } catch (ParseException e) {
            Log.d(LOG_TAG, e.getMessage());
        }
        return dateT;
    }
}
