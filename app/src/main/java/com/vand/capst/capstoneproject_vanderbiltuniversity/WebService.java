package com.vand.capst.capstoneproject_vanderbiltuniversity;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.ArrayList;

public class WebService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private RequestQueue mRequestQueue;
    private final IBinder binder = new LocalBinder();

    protected final String TAG =
            getClass().getSimpleName();

    ContentValues[] cvsArray;

    //Handler class as inner class. This is needed to run bound service on worker thread.
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
    }
    public WebService(){}

    @Override
    public void onCreate(){
        /**
         * Create worker thread to start bound service task.
         */
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
        //Log.d(TAG, "onCreate(): Bound Service initiated");
    }

    @Override
    public IBinder onBind(Intent intent) {
        //Log.d(TAG, "onBind(): Bound to service and IBinder returned");
        return binder;
    }

    public class LocalBinder extends Binder {
        WebService getService() {
            // Return this instance of LocalService so clients can call public methods
            return WebService.this;
        }
    }

    public void execute(final String URL, final String placetype){
        //Log.d(TAG, "execute(): service started the web service call with URL: "+URL+" placetype: "+placetype+".");
        /**
         * on the worker thread - initiate Google Place API Web Service call to fetch data.
         */
        mServiceHandler.post(new Runnable(){
           public void run() {
                Cache cache = new DiskBasedCache(getCacheDir(),1024*1024);
                Network network = new BasicNetwork(new HurlStack());
                mRequestQueue=new RequestQueue(cache,network);
                mRequestQueue.start();
               //Log.d(TAG, "execute(): RequestQueue Started");

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,new Response.Listener<JSONObject>(){
                    public void onResponse(JSONObject response){
                     //handle response on UI thread..
                        //Log.d(TAG, "execute(): Response being handled on UI thread");
                        List<String> placenames = new ArrayList<String>();
                        JSONArray resultArray=null;
                        try {
                            resultArray = response.getJSONArray("results");
                        } catch (JSONException e) {}
                        //Log.d(TAG, "execute(): JSONArray size: "+resultArray.length());
                        for(int i=0;i<resultArray.length();i++){
                            JSONObject obj=null;
                            try {
                                obj = resultArray.getJSONObject(i);
                                String name = obj.getString("name");
                                placenames.add(name);
                            } catch (JSONException e) {}
                        }

                    //bulk insert using content provider
                        cvsArray =
                                new ContentValues[placenames.size()];

                        // Index counter.
                        int i = 0;

                        // Insert all the characters into the ContentValues array.
                        for (String name : placenames) {
                            ContentValues cvs = new ContentValues();
                            cvs.put(LocationContract.LocationEntry.COLUMN_INFO1,
                                    name);
                            cvs.put(LocationContract.LocationEntry.COLUMN_INFO2,
                                    placetype);
                            cvsArray[i++] = cvs;

                        }
                        //Log.d(TAG, "execute(): ContentValues achieved with size: "+cvsArray.length);
                        /**
                         * truncate the table to erase old web service call data, insert new data from fresh web service call, and
                         * send broadcast - all in background thread via AsyncTask.
                         */
                        new TruncateTask().execute();
                        //Log.d(TAG, "execute(): AsyncTask just got called");

                    }
                },new Response.ErrorListener(){
                    public void onErrorResponse(VolleyError error){
                     //handle error on UI thread..
                    }
                });

                jsonObjectRequest.setTag("MyTag");
                mRequestQueue.add(jsonObjectRequest);

                //Handler UI_Handler = new Handler(Looper.getMainLooper());
                //UI_Handler.post(new Runnable(){
                  // public void run(){
                   //}
                //});
           }
        });
    }
    public void onDestroy(){
        //cancel pending web service request..
        if(mRequestQueue != null){
            mRequestQueue.cancelAll("MyTag");
        }
    }

    private class TruncateTask extends AsyncTask<Void,Void,Void> {


        protected Void doInBackground(Void... params){

            SQLiteDatabase db = new DBHelper(WebService.this).getWritableDatabase();
            db.execSQL("DELETE from "+LocationContract.LocationEntry.TABLE_NAME);
            //Log.d(TAG, "AsyncTask.doInBackground(): table truncated..");
            db.close();
            WebService.this.getContentResolver().bulkInsert(LocationContract.LocationEntry.CONTENT_URI,cvsArray);
            //Log.d(TAG, "AsyncTask.doInBackground(): Bulk Insert achieved");

            Intent intent = new Intent();
            intent.setAction("capstone.project.action.PERSIST_COMPLETE");
            sendBroadcast(intent);
            //Log.d(TAG, "AsyncTask.doInBackground(): Broadcast sent with intent..");
            return null;

        }
    }
}
