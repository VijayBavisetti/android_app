package com.vand.capst.capstoneproject_vanderbiltuniversity;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


public class LocationContract {

    /**
     * This ContentProvider's unique identifier.
     */
    public static final String CONTENT_AUTHORITY =
    "vand.capst.myprovider";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which
     * apps will use to contact the content provider.
     */
    public static final Uri BASE_CONTENT_URI =
            Uri.parse("content://"
                    + CONTENT_AUTHORITY);

    public static final String PATH_LOCATION =
            LocationEntry.TABLE_NAME;

    /**
     * Inner class that defines the table contents of the data
     * table.
     */
    public static final class LocationEntry
            implements BaseColumns {
        /**
         * Use BASE_CONTENT_URI to create the unique URI for Acronym
         * Table that apps will use to contact the content provider.
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon()
                        .appendPath(PATH_LOCATION).build();

        /**
         * When the Cursor returned for a given URI by the
         * ContentProvider contains 0..x items.
         */
        public static final String CONTENT_ITEMS_TYPE =
                "caps.android.cursor.dir/"
                        + CONTENT_AUTHORITY
                        + "/"
                        + PATH_LOCATION;

        /**
         * When the Cursor returned for a given URI by the
         * ContentProvider contains 1 item.
         */
        public static final String CONTENT_ITEM_TYPE =
                "caps.android.cursor.item/"
                        + CONTENT_AUTHORITY
                        + "/"
                        + PATH_LOCATION;

        /**
         * Columns to display.
         */
        public static final String sColumnsToDisplay [] =
                new String[] {
                        LocationContract.LocationEntry._ID,
                        LocationContract.LocationEntry.COLUMN_INFO1,
                        LocationContract.LocationEntry.COLUMN_INFO2
                };

        /**
         * Name of the database table.
         */
        public static final String TABLE_NAME =
                "location_table";

        /**
         * Columns to store data.
         */
        public static final String COLUMN_INFO1 = "place_name";
        public static final String COLUMN_INFO2 = "place_type";

        /**
         * Return a Uri that points to the row containing a given id.
         *
         * @param id row id
         * @return Uri URI for the specified row id
         */
        public static Uri buildUri(Long id) {
            return ContentUris.withAppendedId(CONTENT_URI,
                    id);
        }
    }
}
