package com.imy320.foultmouth.personaldigitaldairy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Home extends AppCompatActivity
{

    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;
    Cursor cursor;
    ListView listview;
    ImageButton addNew_Button;
    ListDataAdapter listDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //instantiate the ListView in the main activity
        listview = (ListView) findViewById(R.id.listview);
        dbHelper = new DBHelper(getApplicationContext());
        sqLiteDatabase = dbHelper.getReadableDatabase();
        cursor = dbHelper.getInformation(sqLiteDatabase);
        listDataAdapter = new ListDataAdapter(getApplicationContext(), R.layout.custom_row, sqLiteDatabase, dbHelper);
        listview.setAdapter(listDataAdapter);




        if(cursor.moveToFirst())
        {
            do {
                String title, date, time, details;
                title = cursor.getString(0);
                date = cursor.getString(1);
                time = cursor.getString(2);
                details = cursor.getString(3);

                DataProvider dataProvider = new DataProvider(title, date, time, details);
                listDataAdapter.add(dataProvider);

            } while (cursor.moveToNext());
        }

        addNew_Button = (ImageButton) findViewById(R.id.button_addNew);
        addNew_Button.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewItem();
            }
        });

        //Adjust the scrolling settings when keyboard is active
        //Without this line the circle button scrolls up with the keyboard obstructing the view
        //of th input texts
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //Create the button image on the toolbar
        ImageButton addButton = (ImageButton) findViewById(R.id.toolbar_button_right);
        addButton.setImageResource(R.drawable.ic_add);
        addButton.setScaleType(ImageView.ScaleType.FIT_XY);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewItem();
            }
        });



        //Assign the dialog controls on the email button
        ImageButton emailButton = (ImageButton) findViewById(R.id.toolbar_button_email);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                doEmailDialog();
            }
        });

    }

    private String emailAdress = "";
    public void doEmailDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Send to email");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected;
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                emailAdress = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

        //TODO: send all the items to the input email adress
    }

    public void createNewItem()
    {
        Intent i = new Intent(Home.this,AddNewItem.class );
        Bundle b = new Bundle();
        b.putBoolean("editing",false);
        i.putExtras(b);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    class CustomRowAdapter extends BaseAdapter
//    {
//        Context context;
//        ArrayList<DataItem> data;
//        private LayoutInflater inflater = null;

//        public CustomRowAdapter(Context context, ArrayList<DataItem> data)
//       {
//            this.context = context;
//            this.data = data;
//            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        }

//        @Override
//        public int getCount()
//        {
//            return data.size();
//        }

//        @Override
//        public Object getItem(int position)
//        {
//            return data.get(position);
//        }

//        @Override
//        public long getItemId(int position)
//        {
//            return position;
//        }

//        @Override
//        public View getView(int position, View convertView, ViewGroup parent)
//        {
//            //TODO : Use exctraction of date to populate correct fields in custom_row.xml
//            View vi = convertView;
//            if (vi == null)
//                vi = inflater.inflate(R.layout.custom_row, null);

            //Populate a single row item
            //Day
//            TextView textView = (TextView) vi.findViewById(R.id.item_day);
//            textView.setText(data.get(position).date);
//            //Month
//            textView = (TextView) vi.findViewById(R.id.item_month);
//            textView.setText(data.get(position).date);
//            //Year
//            textView = (TextView) vi.findViewById(R.id.item_year);
//            textView.setText(data.get(position).date);
//            //Title
//            textView = (TextView) vi.findViewById(R.id.item_title);
//            textView.setText(data.get(position).title);


//            //Add onclick events to the edit and delete buttons of each of the row items
//            ImageButton edit_Button = (ImageButton)vi.findViewById(R.id.button_editItem);
//            ImageButton delete_button = (ImageButton)vi.findViewById(R.id.button_deleteItem);
//            editButtonClick(position, edit_Button);
//            deleteButtonClick(position,delete_button);

//            return vi;
//        }

//        private void editButtonClick(int position, ImageButton editBut)
 //       {
//            final int pos = position;
 //           editBut.setOnClickListener(new View.OnClickListener() {
//
 //               @Override
//                public void onClick(View arg0) {
//
//                    /** TODO : On the click of this button, a new activity should be started to edit the content of the button
//                     * The content can be acquired by using data[position]
//                     */
//
//
//                    Toast.makeText(getApplicationContext(),
//                            "Edit Button " + pos + " clicked",
//                            Toast.LENGTH_LONG).show();
//                }});
//        }
//    }
}
