package com.vand.capst.capstoneproject_vanderbiltuniversity;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by Sudeep.Pandey on 1/27/2018.
 * The following class tests if SQLiteOpenHelper gets instantiated successfully with google_place_db database in place.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class DBHelperTest {

    DBHelper helper;
    private Context instrumentationCtx;

    @Before
    //Get the context
    public void setup() {
        instrumentationCtx = InstrumentationRegistry.getContext();
    }

    @After
    //Close SQLiteOpenHelper after each test.
    public void tearDown(){
        helper.close();

    }

    @Test
    //DBHelper should be associated with "google_place_db" database.
    public void testSQLiteOpenHelperInitiation() throws Exception{
        helper = new DBHelper(instrumentationCtx);
        assertEquals(helper.getDatabaseName(),"google_place_db");
    }

}
