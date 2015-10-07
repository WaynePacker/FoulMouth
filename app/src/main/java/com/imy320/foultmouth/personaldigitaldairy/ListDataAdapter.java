package com.imy320.foultmouth.personaldigitaldairy;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListDataAdapter extends ArrayAdapter
{


    static class LayoutHandler
    {
        TextView TITLE;
        TextView DATE;
        TextView TIME;
        TextView DETAILS;
    }

    List list = new ArrayList();
    Context appContext;
    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;

    public ListDataAdapter(Context context, int resource, SQLiteDatabase sqLiteDatabase,DBHelper dbHelper )
    {
        super(context, resource);
        appContext = context;
        this.sqLiteDatabase = sqLiteDatabase;
        this.dbHelper = dbHelper;

    }

    @Override
    public void add(Object object)
    {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        LayoutHandler layoutHandler;

        if(row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.custom_row, parent, false);
            layoutHandler = new LayoutHandler();
            layoutHandler.TITLE = (TextView)row.findViewById(R.id.item_title);
            layoutHandler.DATE = (TextView)row.findViewById(R.id.item_date);
            layoutHandler.TIME = (TextView)row.findViewById(R.id.item_time);
            layoutHandler.DETAILS = (TextView)row.findViewById(R.id.item_details);

            row.setTag(layoutHandler);
        }
        else
        {
            layoutHandler = (LayoutHandler)row.getTag();
        }
        DataProvider dataProvider = (DataProvider)this.getItem(position);
        layoutHandler.TITLE.setText(dataProvider.getEntryTitle());
        layoutHandler.DATE.setText(extractDate(dataProvider.getEntryDate()));
        layoutHandler.TIME.setText(dataProvider.getEntryTime());
        layoutHandler.DETAILS.setText(dataProvider.getEntryDetails());

        //Add onclick events to the edit and delete buttons of each of the row items
        ImageButton edit_Button = (ImageButton) row.findViewById(R.id.button_editItem);
        editButtonClick(position, edit_Button, dataProvider.getEntryTitle(),
                dataProvider.getEntryDate(),
                dataProvider.getEntryTime(),
                dataProvider.getEntryDetails());

        ImageButton delete_button = (ImageButton) row.findViewById(R.id.button_deleteItem);
        deleteButtonClick(position, delete_button,dataProvider.getEntryTitle(), row);


        return row;
    }

    private String extractDate(String date)
    {
        char separator = '-';
        int day = Integer.parseInt(date.substring(0,date.indexOf(separator)));
        int month = Integer.parseInt(date.substring(date.indexOf(separator)+1, date.lastIndexOf(separator)));

        String montName = "";

        switch (month)
        {
            case 1 :  montName = "Jan"; break;
            case 2 :  montName = "Feb"; break;
            case 3 :  montName = "Mar"; break;
            case 4 :  montName = "Apr"; break;
            case 5 :  montName = "May"; break;
            case 6 :  montName = "Jun"; break;
            case 7 :  montName = "Jul"; break;
            case 8 :  montName = "Aug"; break;
            case 9 :  montName = "Sep"; break;
            case 10 :  montName = "Oct"; break;
            case 11 :  montName = "Nov"; break;
            case 12 :  montName = "Dec"; break;
        }

        return  day + " " + montName;

    }

    private void deleteButtonClick(final int position, ImageButton butt, String title, final View child)
    {
        final int pos = position;
        final String t = title;
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dbHelper.deleteEntry(t, sqLiteDatabase);

                list.remove(pos);
                //child.animate().setDuration(1000).alpha(0);
                updateView();
            }
        });
    }

    private void editButtonClick(final int position, ImageButton butt, String title, String date, String time, String details)
    {
        final int pos = position;
        final String t = title;
        final String d = date;
        final String ti = time;
        final String dtls = details;

        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intentBundle = new Intent(appContext, AddNewItem.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("editing", true);
                bundle.putString("title", t);
                bundle.putString("date",d);
                bundle.putString("time",ti);
                bundle.putString("details", dtls);

                intentBundle.putExtras(bundle);
                intentBundle.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                appContext.startActivity(intentBundle);

            }
        });
    }

    //Update view after deletion
    private void updateView()
    {
        this.notifyDataSetChanged();
    }

}
