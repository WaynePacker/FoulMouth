package com.imy320.foultmouth.personaldigitaldairy;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AddNewItem extends AppCompatActivity {

    TextView datePicker;
    int year_pick, month_pick, day_pick;
    static final int DIALOG_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);


        final Calendar cal = Calendar.getInstance();
        year_pick = cal.get(Calendar.YEAR);
        month_pick = cal.get(Calendar.MONTH);
        day_pick = cal.get(Calendar.DAY_OF_MONTH);
        datePicker.setText(day_pick + "/" + month_pick + "/" +year_pick);
        showDialogOnPick();
    }

    ////Show the dialog for picking the date
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void showDialogOnPick()
    {
        //test the button
        datePicker = (TextView) findViewById(R.id.datePicker);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
                Toast.makeText(AddNewItem.this, "hallo", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID)
            return new DatePickerDialog(this, dPickerListener, year_pick, month_pick, day_pick);

        return null;
    }

    private DatePickerDialog.OnDateSetListener dPickerListener = new DatePickerDialog.OnDateSetListener()
                                            {

                                                @Override
                                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                                                {
                                                    year_pick = year;
                                                    month_pick = monthOfYear + 1;
                                                    day_pick = dayOfMonth;
                                                    datePicker.setText(day_pick + "/" + month_pick + "/" +year_pick);
                                                }
                                            };

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
