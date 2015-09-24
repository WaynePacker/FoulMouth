package com.imy320.foultmouth.personaldigitaldairy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

    public ListDataAdapter(Context context, int resource)
    {
        super(context, resource);
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

        return row;
    }
}
