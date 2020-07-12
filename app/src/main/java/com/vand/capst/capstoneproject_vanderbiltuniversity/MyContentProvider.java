package com.vand.capst.capstoneproject_vanderbiltuniversity;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class MyContentProvider extends ContentProvider {

    /**
     * Debugging tag used by the Android logger.
     */
    protected final static String TAG =
            MyContentProvider.class.getSimpleName();

    /**
     * Use DBHelper to manage database creation and version
     * management.
     */
    private DBHelper persistHelper;

    /**
     * Context for the Content Provider.
     */
    private Context mContext;

    @Override
    public boolean onCreate() {

        mContext = getContext();

        persistHelper =
                new DBHelper(mContext);
        return true;
    }

    /**
     * The code that is returned when a URI for more than 1 items is
     * matched against the given components.  Must be positive.
     */
    public static final int LOCATIONS = 100;

    /**
     * The code that is returned when a URI for exactly 1 item is
     * matched against the given components.  Must be positive.
     */
    public static final int LOCATION = 101;

    /**
     * The URI Matcher used by this content provider.
     */
    private static final UriMatcher sUriMatcher =
            buildUriMatcher();

    /**
     * Helper method to match each URI to the ACRONYM integers
     * constant defined above.
     *
     * @return UriMatcher
     */
    protected static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code
        // to return when a match is found.  The code passed into the
        // constructor represents the code to return for the rootURI.
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher =
                new UriMatcher(UriMatcher.NO_MATCH);

        // For each type of URI that is added, a corresponding code is
        // created.
        matcher.addURI(LocationContract.CONTENT_AUTHORITY,
                LocationContract.PATH_LOCATION,
                LOCATIONS);
        matcher.addURI(LocationContract.CONTENT_AUTHORITY,
                LocationContract.PATH_LOCATION
                        + "/#",
                LOCATION);
        return matcher;
    }

    /**
     * Method called to handle type requests from client applications.
     * It returns the MIME type of the data associated with each
     * URI.
     */
    @Override
    public String getType(Uri uri) {
        // Match the id returned by UriMatcher to return appropriate
        // MIME_TYPE.
        switch (sUriMatcher.match(uri)) {
            case LOCATIONS:
                return LocationContract.LocationEntry.CONTENT_ITEMS_TYPE;
            case LOCATION:
                return LocationContract.LocationEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: "
                        + uri);
        }
    }

    /**
     * Method called to handle insert requests from client apps.
     */

    @Override
    public Uri insert(Uri uri,
                      ContentValues cvs) {

        throw new UnsupportedOperationException("Not yet implemented");

    }

    /**
     * Method that handles bulk insert requests.
     */
    @Override
    public int bulkInsert( Uri uri,
                          ContentValues[] cvsArray) {

        // Try to match against the path in a url.  It returns the
        // code for the matched node (added using addURI), or -1 if
        // there is no matched node.  If there's a match bulk insert
        // new rows.
        switch (sUriMatcher.match(uri)) {
            case LOCATIONS:
                int returnCount = bulkInsertLocations(uri,
                        cvsArray);

                if (returnCount > 0)
                    // Notifies registered observers that row(s) were
                    // inserted.
                    mContext.getContentResolver().notifyChange(uri,
                            null);
                return returnCount;
            default:
                throw new UnsupportedOperationException();
        }
    }

    /**
     * Method that handles bulk insert requests.
     */
    private int bulkInsertLocations(Uri uri,
                                     ContentValues[] cvsArray) {
        // Create and/or open a database that will be used for reading
        // and writing. Once opened successfully, the database is
        // cached, so you can call this method every time you need to
        // write to the database.
        final SQLiteDatabase db =
                persistHelper.getWritableDatabase();

        int returnCount = 0;

        // Begins a transaction in EXCLUSIVE mode.
        db.beginTransaction();
        try {
            for (ContentValues cvs : cvsArray) {
                final long id =
                        db.insert(LocationContract.LocationEntry.TABLE_NAME,
                                null,
                                cvs);
                if (id != -1)
                    returnCount++;
            }

            // Marks the current transaction as successful.
            db.setTransactionSuccessful();
        } finally {
            // End a transaction.
            db.endTransaction();
            db.close();
        }
        return returnCount;
    }

    //we won't implement this method since we won't need it.
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //we won't implement this method since we won't need it.
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //we won't implement this method since we won't need it.
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
