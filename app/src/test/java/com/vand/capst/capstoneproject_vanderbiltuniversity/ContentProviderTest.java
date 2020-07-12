package com.vand.capst.capstoneproject_vanderbiltuniversity;

import android.content.ContentValues;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.net.Uri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Sudeep.Pandey on 1/26/2018.
 *The class below contains Robolectric test cases for MyContentProvider. Test cases cover key functionalities of the content provider
 * like insert(), bulkInsert(), query(), update(), delete().
 *
 */

@RunWith(RobolectricTestRunner.class)
public class ContentProviderTest {

    MyContentProvider provider;

    private static final String AUTHORITY = "vand.capst.myprovider";
    @Before
    public void setUp(){

        provider = Robolectric.setupContentProvider(MyContentProvider.class);
    }

    //checks if content provider got instantiated correctly by verifying the accuracy of the database name.
    @Test
    public void onCreateLifecycleStageTest() throws Exception{
        provider.onCreate();
        assertNotNull(provider);
        assertEquals(new DBHelper(provider.getContext()).getDatabaseName(),"google_place_db");
    }

    //checks if getType() method of the content provider works correctly.
    @Test
    public void getTypeWorksCorrectly() throws Exception{

        String str = provider.getType(Uri.parse("content://vand.capst.myprovider/location_table/1"));
        assertEquals(str,"caps.android.cursor.item/vand.capst.myprovider/location_table");
    }

    //insert() method of the content provider wasn't implemented. So it should throw exception.
    @Test(expected = UnsupportedOperationException.class)
    public void insertThrowsException() throws Exception{

        Uri uri = LocationContract.LocationEntry.CONTENT_URI;
        provider.insert(uri,new ContentValues());
    }

    //checks if bulkInsert() works correctly by supplying ContentValues array of size 2.
    @Test
    public void bulkInsertWorksCorrectly() throws Exception{

        ContentValues[] cvsArray = new ContentValues[2];
        ContentValues cVals = new ContentValues();
        cVals.put(LocationContract.LocationEntry.COLUMN_INFO1,"Cranberry");
        cVals.put(LocationContract.LocationEntry.COLUMN_INFO2,"City");

        ContentValues cVals2 = new ContentValues();
        cVals2.put(LocationContract.LocationEntry.COLUMN_INFO1,"demoplace");
        cVals2.put(LocationContract.LocationEntry.COLUMN_INFO2,"demotype");

        cvsArray[0]=cVals;
        cvsArray[1]=cVals2;

        Uri uri = LocationContract.LocationEntry.CONTENT_URI;
        int check = provider.bulkInsert(uri,cvsArray);
        assertEquals(check,2);
    }

    //delete() should work correctly by throwing Exception.
    @Test(expected = UnsupportedOperationException.class)
    public void deleteWorksCorrectly() throws Exception{

        int del = provider.delete(Uri.parse("content://vand.capst.myprovider/location_table/1"),"place_name",new String[]{"test"});
    }

    //update() method of contentprovider wasn't implemented. So it should throw Exception.
    @Test(expected = UnsupportedOperationException.class)
    public void updateWorksCorrectly() throws Exception{

        Uri uri = LocationContract.LocationEntry.CONTENT_URI;
        provider.update(uri,new ContentValues(),"place_name",new String[]{"Cranberry"});
    }

    //query() method of content provider should throw Exception as it wasn't implemented.
    @Test(expected = UnsupportedOperationException.class)
    public void queryThrowsException() throws Exception{

        Uri uri = LocationContract.LocationEntry.CONTENT_URI;
        provider.query(uri,null,"place_name",new String[]{"Cranberry"},"DESC");
    }
}
