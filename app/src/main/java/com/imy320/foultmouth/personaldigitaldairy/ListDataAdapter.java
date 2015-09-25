package com.imy320.foultmouth.personaldigitaldairy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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
        layoutHandler.DATE.setText(dataProvider.getEntryDate());
        layoutHandler.TIME.setText(dataProvider.getEntryTime());
        layoutHandler.DETAILS.setText(dataProvider.getEntryDetails());

        //Add onclick events to the edit and delete buttons of each of the row items
        ImageButton edit_Button = (ImageButton) row.findViewById(R.id.button_editItem);

        ImageButton delete_button = (ImageButton) row.findViewById(R.id.button_deleteItem);
        deleteButtonClick(position, delete_button,dataProvider.getEntryTitle());

        return row;
    }

    private void deleteButtonClick(final int position, ImageButton butt, String title)
    {
        final int pos = position;
        final String t = title;
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(appContext,
                        "Delete Button " + pos + " clicked",
                        Toast.LENGTH_LONG).show();
                //TODO: Get the title from the entry to be deleted.
                dbHelper.deleteEntry(t, sqLiteDatabase);
                list.remove(pos);
                updateView();
            }});
    }

    //Update view after deletion
    private void updateView()
    {
        this.notifyDataSetChanged();
    }

}
