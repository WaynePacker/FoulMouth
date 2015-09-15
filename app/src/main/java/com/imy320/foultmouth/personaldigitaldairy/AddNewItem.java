package com.imy320.foultmouth.personaldigitaldairy;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.text.DateFormat;
import java.util.Calendar;

public class AddNewItem extends  FragmentActivity {

    TextView datePicker;
    TextView timePicker;
    private int curr_year, curr_month, curr_day, curr_hour, curr_minute;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);


        //Assign the DatePicker
        datePicker = (TextView) findViewById(R.id.datePicker);
        //Assign the TimePicker
        timePicker =(TextView) findViewById(R.id.timePicker);

        //Assign the calender to access the current date and time values
        final Calendar cal = Calendar.getInstance();

        //Set the date value to the current date
        curr_year = cal.get(Calendar.YEAR);
        curr_month = cal.get(Calendar.MONTH);
        curr_day = cal.get(Calendar.DAY_OF_MONTH);
        updateDate(curr_day, curr_month, curr_year);

        //Set the time value to the current time
        curr_hour = cal.get(Calendar.HOUR_OF_DAY);
        curr_minute = cal.get(Calendar.MINUTE);
        updateTime(curr_hour, curr_minute);


        //Assign the datePicker its click listener
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDatePicker(v);
            }
        });

        //Assign the timePicker its click listener
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTimePicker(v);
            }
        });


    }

    //Display the Date Picker dialog
    public void doDatePicker(View view)
    {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //Display the Time Picker dialog
    public void doTimePicker(View view)
    {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    //Update the time TextView  in the view
    public void updateTime(int hour, int minute)
    {
        timePicker.setText(hour + ":" + minute);
    }

    //Update the date TextView in the view
    public void updateDate(int day, int month, int year)
    {
        datePicker.setText(day + "/" + month + "/" + year);
    }


    //Class to show the interface for picking a time
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener
    {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            //Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            //Create a new instance of TimePickerDialog and return it
            return  new TimePickerDialog(getActivity(), this, hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {
            //Call the activity whom opened this fragment and set its time to the user input
            ((AddNewItem) getActivity()).updateTime(hourOfDay,minute);

        }
    }

    //Class to show the interface for picking a date
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
    {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //Call the activity whom opened this fragment and set its date to the user input, add 1 to month of year
            //to get the correct month(starts at 0 for Jan)
            ((AddNewItem) getActivity()).updateDate(dayOfMonth, monthOfYear + 1, year);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_item, menu);
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
