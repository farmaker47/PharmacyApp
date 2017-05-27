package com.george.pharmacyapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.george.pharmacyapp.data.PharmacyContract;
import com.george.pharmacyapp.data.PharmacyDBHelper;

import static com.george.pharmacyapp.data.PharmacyContract.CONTENT_AUTHORITY;
import static com.george.pharmacyapp.data.PharmacyContract.PATH_PRODUCTS;

/**
 * Created by farmaker1 on 27/05/2017.
 */

public class PharmacyProvider extends ContentProvider {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = PharmacyProvider.class.getSimpleName();

    /**
     * Initialize the provider and the database helper object.
     */
    private PharmacyDBHelper mDbHelper;

    /**
     * URI matcher code for the content URI for the pets table
     */
    private static final int PRODUCT = 100;

    /**
     * URI matcher code for the content URI for a single pet in the pets table
     */
    private static final int PRODUCT_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_PRODUCTS, PRODUCT);

        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_PRODUCTS + "/#", PRODUCT_ID);

    }


    @Override
    public boolean onCreate() {
        mDbHelper = new PharmacyDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        String name = values.getAsString(PharmacyContract.PharmacyEntry.COLUMN_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Product requires a name");
        }

        String price = values.getAsString(PharmacyContract.PharmacyEntry.COLUMN_PRICE);
        if (price == null) {
            throw new IllegalArgumentException("Product requires price");
        }

        String quantity = values.getAsString(PharmacyContract.PharmacyEntry.COLUMN_QUANTITY);
        if (quantity == null) {
            throw new IllegalArgumentException("Product requires a quantity");
        }

        String image = values.getAsString(PharmacyContract.PharmacyEntry.COLUMN_IMAGE);
        if (image == null) {
            throw new IllegalArgumentException("Product requires an image");
        }

        // Insert the new pet with the given values
        long id = database.insert(PharmacyContract.PharmacyEntry.TABLE_NAME, null, values);

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        //notify all listeners that uri has changed
        getContext().getContentResolver().notifyChange(uri, null);
        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);


    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
