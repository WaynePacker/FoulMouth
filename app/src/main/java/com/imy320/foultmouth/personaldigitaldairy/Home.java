package com.imy320.foultmouth.personaldigitaldairy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

///Openweather API e7f3bf94d17a443a1de6ec8945eb7761

public class Home extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;
    Cursor cursor;
    ListView listview;
    ImageButton addNew_Button;
    ListDataAdapter listDataAdapter;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    String mLatitude = "";
    String mLongitude = "";

    Weather weather = new Weather();
    TextView toolbar_weather;
    TextView toolbar_weather_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.overridePendingTransition(R.anim.slide_out,
                R.anim.slide_in);

        //set up all the toolbar displays and functions
        setupToolbar();


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


        //Add new item buttons
        addNew_Button = (ImageButton) findViewById(R.id.button_addNew);
        addNew_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewItem();
            }
        });

        //Adjust the scrolling settings when keyboard is active
        //Without this line the circle button scrolls up with the keyboard obstructing the view
        //of th input texts
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //Location Services
        buildGoogleApiClient();
        mGoogleApiClient.connect();

    }

    //location  functions
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    private class RequestWeatherUpdate extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params) {
            String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));
            //  System.out.println("/////////////////////// " + data );
            try {

                JSONWeatherParser weatherParser = new JSONWeatherParser();
                weather = weatherParser.getWeather(data);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather.temperature.getTemp() + " C";
        }

        @Override
        protected void onPostExecute(String result) {
            updateWeather();
        }
    }

    public void updateWeather()
    {

        String icon = "1";
        switch (weather.currentCondition.getIcon())
        {
            case "01n" :
            case "01d" : icon = "1";
                break;
            case "02n" :
            case "02d" : icon = "A";
                break;
            case "03n" :
            case "03d" : icon = "3";
                break;
            case "04n" :
            case "04d" : icon = "C";
                break;
            case "09d" :
            case "09n" : icon = "J";
               break;
            case "10n" :
            case "10d" : icon = "K";
                break;
            case "11n" :
            case "11d" : icon = "P";
                break;
            case "13n" :
            case "13d" : icon = "I";
                break;
            case "50d" :
            case "50n" : icon = "C";
                break;
        }

        toolbar_weather.setText(weather.temperature.getTemp() + "\u00b0" + " C" + System.lineSeparator() + weather.currentCondition.getDescr());
        toolbar_weather_icon.setText(icon);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitude = String.valueOf(mLastLocation.getLatitude());
            mLatitude = mLatitude.substring(0, 5);
            mLongitude = String.valueOf(mLastLocation.getLongitude());
            mLongitude = mLongitude.substring(0, 5);

            toolbar_weather.setText("");

            new RequestWeatherUpdate().execute("lat=" + mLatitude + "&lon=" + mLongitude);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        toolbar_weather.setText("connection suspended");
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        toolbar_weather.setText("connection failed");
    }


    public void setupToolbar()
    {
        toolbar_weather_icon = (TextView)(findViewById(R.id.toolbar_weather_icon));
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/artill_clean_icons.ttf");
        toolbar_weather_icon.setTypeface(face);

        toolbar_weather = (TextView) findViewById(R.id.toolbar_weather);
        //Set up the toolbar add new
        ImageButton toolbar_addNew_Button = (ImageButton) findViewById(R.id.toolbar_button_add);
        toolbar_addNew_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewItem();
            }
        });

        //Assign the dialog controls on the email button
        ImageButton emailButton = (ImageButton) findViewById(R.id.toolbar_button_email);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doEmailDialog();
            }
        });



        String monthName = (String)android.text.format.DateFormat.format("MMMM", new Date());
        String day = (String) android.text.format.DateFormat.format("dd", new Date());
        TextView toolbar_date = (TextView) findViewById(R.id.toolbar_Date);
        toolbar_date.setText(day + " " + monthName);

        String dayName = (String)android.text.format.DateFormat.format("EEEE", new Date());
        TextView toolbar_dayName = (TextView) findViewById(R.id.toolbar_dayName);
        toolbar_dayName.setText(dayName);

        String currentTime = (String)android.text.format.DateFormat.format("HH:mm", new Date());
        TextView toolbar_time = (TextView)findViewById(R.id.toolbar_Time);
        toolbar_time.setText(currentTime);

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
                new EmailHandler(cursor).sendMail(emailAdress);
                Toast.makeText(getBaseContext(), getString(R.string.sentEmail), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


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



}
