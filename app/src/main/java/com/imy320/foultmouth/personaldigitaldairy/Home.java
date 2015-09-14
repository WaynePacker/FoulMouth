package com.imy320.foultmouth.personaldigitaldairy;

import android.content.Context;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //instantiate the ListView in the main activity
        listview = (ListView) findViewById(R.id.listview);

        //TODO : create a database and file structure to save all the content of the items.




        /** TODO : load all the items' date and title into a array
         * so that the rows can be populated in the main activity view.
         */
        //Test population for row
        DataItem itemOne = new DataItem("17/09/2015","Do this thing");
        DataItem itemTwo = new DataItem("17/09/2015","Some test to write");
        DataItem itemThree = new DataItem("17/09/2015","Bitches Be cray cray");

        //TODO : once data is loaded into an array of DataItem[] then replace in the statement below
        listview.setAdapter(new CustomRowAdapter(this, new DataItem[]{itemOne, itemTwo, itemThree}));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
    class DataItem
    {
        /**TODO : reference body text and image and recoring items in this class
         so that it can be passed to the edit activity*/

        public String date;//TODO : Restructure this date from string to a more complex structure where day, month, year can be extracted individually
        public String title = "title";

        public DataItem(String date, String title)
        {
            this.date = date;
            this.title = title;
        }

    }

    class CustomRowAdapter extends BaseAdapter
    {

        Context context;
        DataItem[] data;
        private LayoutInflater inflater = null;

        public CustomRowAdapter(Context context, DataItem[] data) {

            this.context = context;
            this.data = data;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {

            return data.length;
        }

        @Override
        public Object getItem(int position) {

            return data[position];
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //TODO : Use exctraction of date to populate correct fields in custom_row.xml
            View vi = convertView;
            if (vi == null)
                vi = inflater.inflate(R.layout.custom_row, null);

            //Populate a single row item
            //Day
            TextView textView = (TextView) vi.findViewById(R.id.item_day);
            textView.setText(data[position].date);
            //Month
            textView = (TextView) vi.findViewById(R.id.item_month);
            textView.setText(data[position].date);
            //Year
            textView = (TextView) vi.findViewById(R.id.item_year);
            textView.setText(data[position].date);
            //Title
            textView = (TextView) vi.findViewById(R.id.item_title);
            textView.setText(data[position].title);


            //Add onclick events to the edit and delete buttons of each of the row items
            ImageButton edit_Button = (ImageButton)vi.findViewById(R.id.button_editItem);
            ImageButton delete_button = (ImageButton)vi.findViewById(R.id.button_deleteItem);
            editButtonClick(position, edit_Button);
            deleteButtonClick(position,delete_button);

            return vi;
        }

        private void editButtonClick(int position, ImageButton editBut)
        {
            final int pos = position;
            editBut.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    /** TODO : On the click of this button, a new activity should be started to edit the content of the button
                     * The content can be acquired by using data[position]
                     */


                    Toast.makeText(getApplicationContext(),
                            "Edit Button " + pos + " clicked",
                            Toast.LENGTH_LONG).show();
                }});
        }

        private void deleteButtonClick(int position, ImageButton editBut)
        {
            final int pos = position;
            editBut.setOnClickListener(new View.OnClickListener() {
                /** TODO : On the click of this button, the content of this item should be deleted off of the device and its entry in the database
                 * The content can be acquired by using data[position]
                 */
                @Override
                public void onClick(View arg0) {
                    Toast.makeText(getApplicationContext(),
                            "Delete Button " + pos + " clicked",
                            Toast.LENGTH_LONG).show();
                }});
        }



    }
}
