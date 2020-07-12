package com.vand.capst.capstoneproject_vanderbiltuniversity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper
extends SQLiteOpenHelper

    {
        private static final String DATABASE_NAME =
                "google_place_db";

        /**
         * Database version number, which is updated with each schema
         * change.
         */
        private static int DATABASE_VERSION = 1;

        /**
         * SQL statement used to create the table.
         */
        final String SQL_CREATE_TABLE =
                "CREATE TABLE "
                        + LocationContract.LocationEntry.TABLE_NAME + " ("
                        + LocationContract.LocationEntry._ID + " INTEGER PRIMARY KEY, "
                        + LocationContract.LocationEntry.COLUMN_INFO1 + " TEXT NOT NULL, "
                        + LocationContract.LocationEntry.COLUMN_INFO2 + " TEXT NOT NULL "
                        + " );";

        /**
         * Constructor - initialize database name and version, but don't
         * actually construct the database (which is done in the
         * onCreate() hook method). It places the database in the
         * application's cache directory, which will be automatically
         * cleaned up by Android if the device runs low on storage space.
         *
         * @param context Any context
         */
    public DBHelper(Context context) {
        super(context,DATABASE_NAME,
                //context.getCacheDir()
                        //+ File.separator
                        //+ DATABASE_NAME,
                null,
                DATABASE_VERSION);
    }

        /**
         * Hook method called when the database is created.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            // Create the table.
        db.execSQL(SQL_CREATE_TABLE);

    }

        /**
         * Hook method called when the database is upgraded.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db,
        int oldVersion,
        int newVersion) {
        // Delete the existing tables.
        db.execSQL("DROP TABLE IF EXISTS "
                + LocationContract.LocationEntry.TABLE_NAME);
        // Create the new tables.
        onCreate(db);
        }
    }

