package com.imy320.foultmouth.personaldigitaldairy;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.util.Calendar;

public class AddNewItem extends  FragmentActivity
{

    Context context = this;
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

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
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            //Call the activity whom opened this fragment and set its date to the user input, add 1 to month of year
            //to get the correct month(starts at 0 for Jan)
            ((AddNewItem) getActivity()).updateDate(dayOfMonth, monthOfYear + 1, year);
        }

    }


    //Variable saveButton
    ImageButton save_Button;
    //Variable DatePicker
    TextView datePicker;
    //Variable TimePicker
    TextView timePicker;
    //variable to see wether user is editing an existing item or adding a new one
    Boolean editing = false;

    private int curr_year, curr_month, curr_day, curr_hour, curr_minute;

    private String item_Text = "";
    private String item_Titel = "";

    private Bundle oldData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if(extrasBundle == null){
            //Kill activity here if no data is present to populate fields
            finish();
        }


        //Assign the saveButton
        save_Button = (ImageButton) findViewById(R.id.button_item_Save);
        //Assign the DatePicker
        datePicker = (TextView) findViewById(R.id.item_new_DatePicker);
        //Assign the TimePicker
        timePicker =(TextView) findViewById(R.id.item_new_TimePicker);

        //set up the toolbar and its actions
        setupToolbar();

        //Adjust the scrolling settings when keyboard is active
        //Without this line the circle button scrolls up with the keyboard obstructing the view
        //of th input texts
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        editing = extrasBundle.getBoolean("editing");

        if(editing)
        {
            oldData = extrasBundle;
            editExisting();
        }
        else
        {
            addNew();
        }

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

        //Assign the save operation to the onclick of the save button
        save_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               doSaveItem();
            }
        });

    }


    public void addNew()
    {
        //Assign the calender to access the current date and time values
        final Calendar cal = Calendar.getInstance();

        //Set the date value to the current date
        curr_year = cal.get(Calendar.YEAR);
        curr_month = cal.get(Calendar.MONTH)+1;
        curr_day = cal.get(Calendar.DAY_OF_MONTH);
        updateDate(curr_day, curr_month, curr_year);

        //Set the time value to the current time
        curr_hour = cal.get(Calendar.HOUR_OF_DAY);
        curr_minute = cal.get(Calendar.MINUTE);
        updateTime(curr_hour, curr_minute);
    }

    public void editExisting()
    {
        //get out all the data from the bundle
        String title = oldData.getString("title");
        String date = oldData.getString("date");
        String time = oldData.getString("time");
        String details = oldData.getString("details");

        //Update the title
        EditText titleText = (EditText) findViewById(R.id.item_new_Title);
        titleText.setText(title);

        //Get the hour and minute from the time
        String[] ti = time.split(":");
        updateTime(Integer.parseInt(ti[0]), Integer.parseInt(ti[1]));

        //Get the day, month and year from the date
        String[] dte = date.split("-");
        updateDate(Integer.parseInt(dte[0]), Integer.parseInt(dte[1]), Integer.parseInt(dte[2]));

        //Update the details
        EditText detailsText = (EditText) findViewById(R.id.item_new_Text);
        detailsText.setText(details);
    }
    //function to set up the various toolbar options
    public void setupToolbar()
    {
        ImageButton exit_button = (ImageButton) findViewById(R.id.toolbar_button_cancel);
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Add New Item cancel, no data was saved",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

    //Save the content of the item
    public void doSaveItem()
    {
        //Get the text of the item
        EditText itemDetails = (EditText) findViewById(R.id.item_new_Text);
        item_Text = itemDetails.getText().toString();

        //Get the title of the item
        EditText itemTitle = (EditText) findViewById(R.id.item_new_Title);
        item_Titel = itemTitle.getText().toString();

        DataContainer data = new DataContainer(curr_day, curr_month, curr_year, curr_hour, curr_minute, item_Titel, item_Text);

        String date = data.dateToString();
        String time = data.timeToString();

        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();

        if(editing)
        {
            //get all the old data
            String old_title = oldData.getString("title");
            String old_date = oldData.getString("date");
            String old_time = oldData.getString("time");
            String old_details = oldData.getString("details");

            dbHelper.updateEntry(old_title,data.title,old_date,date,old_time,time,old_details,data.bodyText,sqLiteDatabase);
            Toast.makeText(getBaseContext(), "Entry updated", Toast.LENGTH_LONG).show();
        }
        else {
            dbHelper.insertEntry(data.title, date, time, data.bodyText, sqLiteDatabase);
            Toast.makeText(getBaseContext(), "Entry Added", Toast.LENGTH_LONG).show();
        }
        dbHelper.close();

        finish();

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
        String zero = "";

        if(minute < 10)
            zero = "0";
        timePicker.setText(hour + ":"+zero + minute);

        curr_hour = hour;
        curr_minute = minute;
    }

    //Update the date TextView in the view
    public void updateDate(int day, int month, int year)
    {
        datePicker.setText(day + "/" + month + "/" + year);

        curr_day = day;
        curr_month = month;
        curr_year = year;
    }

 /*   @Override
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
    }*/
}
