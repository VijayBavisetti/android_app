package com.vand.capst.capstoneproject_vanderbiltuniversity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private boolean mBound=false;
    private WebService ws;
    private Context ctx = this;
    BroadcastReceiver mReceiver;
    protected final String TAG =
            getClass().getSimpleName();
    private double latitude;
    private double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * Get UI elements initialized with spinners displaying options and buttons.
         */
        initializeViews();

        /**
         * set up button which when clicked uses bound service that makes Google API Web Service call.
         */
        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mBound) {
                    setLatitude();
                    setLongitude();
                    String requestUrl="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius=1000&type="+getInterest()+"&key=AIzaSyDo7-hsZ6-c5YaxfB8R906UFjkOE20K3yA";
                    button.setEnabled(false);
                    button.setText("thank you..");
                    ws.execute(requestUrl,getInterest());
                }
            }
        });

        final Button resultButton = findViewById(R.id.button2);
        resultButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /**
                 * After Web Service call has been made, the result in persisted in SQLite database via Content Provider. This is
                 * implemented via Bound Service. Down below represents querying the same data from database and then passing via
                 * intent to second activity which displays RecyclerView.
                 */
                Button viewButton = findViewById(R.id.button2);
                viewButton.setEnabled(false);
                List<String> collect = new ArrayList<String>();
                SQLiteDatabase dbase = new DBHelper(MainActivity.this).getWritableDatabase();
                Cursor resultSet = dbase.rawQuery("Select place_name from location_table",null);
                while(resultSet.moveToNext()){
                    collect.add(resultSet.getString(0));
                }
                dbase.close();
                String[] responseArray = new String[collect.size()];
                collect.toArray(responseArray);
                Intent intent = new Intent(MainActivity.this,ResultView.class);
                intent.putExtra("responseArray",responseArray);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart(){

        super.onStart();
        // Bind to Service in the same process
        Intent intent = new Intent(this, WebService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Button button = findViewById(R.id.button);
        findViewById(R.id.button2).setEnabled(false);
        button.setEnabled(true);
        button.setText("Find");
        IntentFilter intentFilter = new IntentFilter(
                "capstone.project.action.PERSIST_COMPLETE");

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                Button mybutton = findViewById(R.id.button2);
                mybutton.setEnabled(true);

            }
        };
        //registering broadcast receiver
        this.registerReceiver(this.mReceiver, intentFilter);
    }

    @Override
    public void onPause(){
        super.onPause();
        //unregister broadcast receiver
        this.unregisterReceiver(this.mReceiver);
    }
    @Override
    protected void onStop() {
        super.onStop();
        //unbinding from the bound service
        unbindService(mConnection);
        mBound = false;
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder b) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            WebService.LocalBinder binder = (WebService.LocalBinder) b;
            ws = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    private void initializeViews(){

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.popular_points, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.interests, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        findViewById(R.id.button2).setEnabled(false);
    }

    private String getSite(){

        /**
         * retrieve user selected value representing the site like 'statue of liberty', 'empire state', etc.
         */
        Spinner mySpinner=(Spinner) findViewById(R.id.spinner);
        String text = mySpinner.getSelectedItem().toString();
        return text;
    }
    private String getInterest(){

        /**
         * retrieve user selected value representing user interest around the site like 'hospital', 'library', etc.
         */
        Spinner mySpinner=(Spinner) findViewById(R.id.spinner2);
        String text = mySpinner.getSelectedItem().toString();
        return text;
    }

    /**
     * depending upon user selection of site and interest, latitude and longitude values need to be assigned. These values
     * become part of the Google API web service URL to be called.
     */
    private void setLatitude(){

        if (getSite().equals("Statue of Liberty")){
            latitude= 40.689249;
        }else if (getSite().equals("Empire State")){
            latitude=40.748817;
        }else if(getSite().equals("Brooklyn Bridge")){
            latitude=40.757715;
        }else if(getSite().equals("Times Square")){
            latitude=40.758896;
        }else if(getSite().equals("Rockefeller Center")){
            latitude=40.75874;
        }else if(getSite().equals("World Trade Center")){
            latitude=40.711801;
        }else if(getSite().equals("Madison Square Garden")){
            latitude=40.750298;
        }
        else if(getSite().equals("Roosevelt Island")) {
            latitude = 38.895073;
        }
    }

    //to get specific longitude based on user selection of the site of interest.
    private void setLongitude(){

        if(getSite().equals("Statue of Liberty")){
            longitude=-74.044500;
        }else if(getSite().equals("Empire State")){
            longitude=-73.985428;
        }else if(getSite().equals("Brooklyn Bridge")){
            longitude=-73.98152829999998;
        }else if(getSite().equals("Times Square")){
            longitude=-73.985130;
        }else if(getSite().equals("Rockefeller Center")){
            longitude=-73.978674;
        }else if(getSite().equals("World Trade Center")){
            longitude=-74.013120;
        }else if(getSite().equals("Madison Square Garden")){
            longitude=-73.993324;
        }else if(getSite().equals("Roosevelt Island")){
            longitude= -77.061859;
        }
    }
}
