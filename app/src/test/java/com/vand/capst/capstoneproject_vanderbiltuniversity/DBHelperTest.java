package com.vand.capst.capstoneproject_vanderbiltuniversity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.content.ContentValues;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by Sudeep.Pandey on 1/24/2018.
 *
 * The following class consists of Robolectric test cases for SQLiteOpenHelper implementation.
 * The tests cover functionalities like database creation when SQLiteOpenHelper gets instantiated,
 * SQL query execution capabilities, database upgrade checks, getting readable and writable
 * databases.
 *
 */

@RunWith(RobolectricTestRunner.class)
public class DBHelperTest {

    private DBHelper helper;
    Context ctx = RuntimeEnvironment.application;

    @Before
    public void setUp() throws Exception{
        helper = new DBHelper(ctx);
    }

    @After
    public void tearDown(){
        helper.close();
    }

    //checks if the SQLiteOpenHelper works correctly with database when instantiated.
    @Test
    public void testInitialGetReadableDatabase() throws Exception {
        SQLiteDatabase database = helper.getReadableDatabase();
        assertNotNull(database);
        assertTrue(database.isOpen());
        assertTrue(database.getPath().contains("google_place_db"));
    }

    //checks SQLiteOpenHelper when re-instantiated.
    @Test
    public void testSubsequentGetReadableDatabase() throws Exception {
        helper.getReadableDatabase();
        helper.close();
        SQLiteDatabase database = helper.getReadableDatabase();

        assertNotNull(database);
        assertTrue(database.isOpen());
    }

    //checks onUpgrade() method if it is followed by database and table.
    @Test
    public void onUpgradeImpliesVersionChange() throws Exception{
        SQLiteDatabase db = helper.getReadableDatabase();
        helper.onUpgrade(db,1,2);
        Cursor cursor = db.rawQuery("Select * from location_table",null);
        assertTrue(cursor.getCount()==0);
    }

    //verifies that multiple calls to getReadableDatabase() points to same instance of the SQLiteDatabase.
    @Test
    public void testSameDBInstanceSubsequentGetReadableDatabase() throws Exception {
        SQLiteDatabase db1 = helper.getReadableDatabase();
        SQLiteDatabase db2 = helper.getReadableDatabase();

        assertTrue(db1==db2);
    }

  //verifies getWritableDatabase() opens the SQLiteDatabae.
   @Test
    public void testInitialGetWritableDatabase() throws Exception {
        SQLiteDatabase database = helper.getWritableDatabase();

        assertNotNull(database);
        assertTrue(database.isOpen());
    }

    //subsequent calls to getWritableDatabase()
    @Test
    public void testSubsequentGetWritableDatabase() throws Exception {
        helper.getWritableDatabase();
        helper.close();

        assertNotNull(helper.getWritableDatabase());
        assertTrue(helper.getWritableDatabase().isOpen());
    }

    //subsequent calls to getWritableDatabase() are associated with same instance of SQLiteDatabase.
    @Test
    public void testSameDBInstanceSubsequentGetWritableDatabase() throws Exception {
        SQLiteDatabase db1 = helper.getWritableDatabase();
        SQLiteDatabase db2 = helper.getWritableDatabase();

        assertTrue(db1==db2);
    }

    //closing a SQLiteOpenHelper should close the database.
    @Test
    public void testClose() throws Exception {
        SQLiteDatabase database = helper.getWritableDatabase();

        assertTrue(database.isOpen());
        helper.close();
        assertFalse(database.isOpen());
    }

    //multiple SQLiteOpenHelper
    @Test
    public void testCloseMultipleDbs() throws Exception {
        DBHelper helper2 = new DBHelper(ctx);
        SQLiteDatabase database1 = helper.getWritableDatabase();
        SQLiteDatabase database2 = helper2.getWritableDatabase();
        assertTrue(database1.isOpen());
        assertTrue(database2.isOpen());
        helper.close();
        assertFalse(database1.isOpen());

        assertTrue(database2.isOpen());
        helper2.close();
        assertFalse(database2.isOpen());
    }

    private void setupTable(SQLiteDatabase db, String table) {
        db.execSQL("CREATE TABLE " + table + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "testVal INTEGER DEFAULT 0" +
                ");");
    }
    private void insertData(SQLiteDatabase db, String table, int[] values) {
        for (int i : values) {
            ContentValues cv = new ContentValues();
            cv.put("testVal", i);
            db.insert(table, null, cv);
        }
    }

    private void verifyData(SQLiteDatabase db, String table, int expectedVals) {
        assertEquals(db.query(table, null, null, null,
                null, null, null).getCount(),(expectedVals));
    }

    //insert operations on database tables.
    @Test
    public void testMultipleDbsPreserveData() throws Exception {
        final String TABLE_NAME1 = "fart", TABLE_NAME2 = "fart2";
        SQLiteDatabase db1 = helper.getWritableDatabase();
        setupTable(db1, TABLE_NAME1);
        insertData(db1, TABLE_NAME1, new int[]{1, 2});
        DBHelper helper2 = new DBHelper(ctx);
        SQLiteDatabase db2 = helper2.getWritableDatabase();
        setupTable(db2, TABLE_NAME2);
        insertData(db2, TABLE_NAME2, new int[]{4, 5, 6});
        verifyData(db1, TABLE_NAME1, 2);
        verifyData(db2, TABLE_NAME2, 3);
    }
}
