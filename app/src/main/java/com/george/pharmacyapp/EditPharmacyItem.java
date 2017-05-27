package com.george.pharmacyapp;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.george.pharmacyapp.data.DatabaseUtil;
import com.george.pharmacyapp.data.PharmacyContract;
import com.george.pharmacyapp.data.PharmacyDBHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class EditPharmacyItem extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText mEditName, mEditQuantity, mEditPrice;
    private ImageView mImage;

    private final int PHARMACY_EDIT_LOADER = 0;
    private static final int PICK_IMAGE_REQUEST = 0;

    private Uri mCurrentPharmacyUri;

    private static final String LOG_TAG = EditPharmacyItem.class.getSimpleName();

    private Uri imageUri;

    private PharmacyDBHelper mdbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pharmacy_item);

        Intent intent = getIntent();
        mCurrentPharmacyUri = intent.getData();

        mEditName = (EditText) findViewById(R.id.editTextName);
        mEditQuantity = (EditText) findViewById(R.id.editTextQuantity);
        mEditPrice = (EditText) findViewById(R.id.editTextPrice);
        mImage = (ImageView) findViewById(R.id.image);

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery();
            }
        });
        Log.e(LOG_TAG,mCurrentPharmacyUri.toString());

        mdbHelper = new PharmacyDBHelper(this);

        getSupportLoaderManager().initLoader(PHARMACY_EDIT_LOADER, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }if(id == R.id.action_save){
            insertPet();
            DatabaseUtil.copyDatabaseToExtStg(this);
            Toast.makeText(getBaseContext(),"Copied to DB",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {

        // The projection contains all the columns since the activity displays all the product data
        String[] projection = {
                PharmacyContract.PharmacyEntry._ID,
                PharmacyContract.PharmacyEntry.COLUMN_NAME,
                PharmacyContract.PharmacyEntry.COLUMN_QUANTITY,
                PharmacyContract.PharmacyEntry.COLUMN_PRICE,
                PharmacyContract.PharmacyEntry.COLUMN_IMAGE};

        return new CursorLoader(this,   // Parent activity context
                mCurrentPharmacyUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(PharmacyContract.PharmacyEntry.COLUMN_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(PharmacyContract.PharmacyEntry.COLUMN_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(PharmacyContract.PharmacyEntry.COLUMN_PRICE);
            int imageColumnIndex = cursor.getColumnIndex(PharmacyContract.PharmacyEntry.COLUMN_IMAGE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            String image = cursor.getString(imageColumnIndex);
            String quantity = cursor.getString(quantityColumnIndex);
            String price = cursor.getString(priceColumnIndex);

            // Update the views on the screen with the values from the database
            mEditName.setText(name);
            mEditQuantity.setText(quantity);
            mEditPrice.setText(price);

            imageUri = Uri.parse(image);
            mImage.setImageBitmap(getBitmapFromUri(imageUri));

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void insertPet() {

        /*if(mCurrentPharmacyUri==null){
            // Read from input fields
            // Use trim to eliminate leading or trailing white space
            String nameString = mEditName.getText().toString().trim();
            String quantityString = mEditQuantity.getText().toString().trim();
            String priceString = mEditPrice.getText().toString().trim();
            String imageString =



            if (mCurrentPetUri == null &&
                    TextUtils.isEmpty(nameString) && TextUtils.isEmpty(breedString) &&
                    TextUtils.isEmpty(weightString) && mGender == PetEntry.GENDER_UNKNOWN) {return;}


            // Create a ContentValues object where column names are the keys,
            // and pet attributes from the editor are the values.
            ContentValues values = new ContentValues();
            values.put(PetEntry.COLUMN_PET_NAME, nameString);
            values.put(PetEntry.COLUMN_PET_BREED, breedString);
            values.put(PetEntry.COLUMN_PET_GENDER, mGender);

            int weight = 0;
            if (!TextUtils.isEmpty(weightString)) {
                weight = Integer.parseInt(weightString);
            }
            values.put(PetEntry.COLUMN_PET_WEIGHT, weight);

            // Insert a new row for pet in the database, returning the ID of that new row.
            Uri newUri = getContentResolver().insert(PetEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }else {*/


            // Otherwise this is an EXISTING pet, so update the pet with content URI: mCurrentPetUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentPetUri will already identify the correct row in the database that
            // we want to modify.
            String nameString = mEditName.getText().toString().trim();
            String quantityString = mEditQuantity.getText().toString().trim();
            String priceString = mEditPrice.getText().toString().trim();
            String imageString = imageUri.toString();;

            ContentValues values = new ContentValues();
            values.put(PharmacyContract.PharmacyEntry.COLUMN_NAME, nameString);
            values.put(PharmacyContract.PharmacyEntry.COLUMN_QUANTITY, quantityString);
            values.put(PharmacyContract.PharmacyEntry.COLUMN_PRICE, priceString);
            values.put(PharmacyContract.PharmacyEntry.COLUMN_IMAGE, imageString);



            Uri uri = getContentResolver().insert(PharmacyContract.PharmacyEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (uri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, "Failed",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, "Success",
                    Toast.LENGTH_SHORT).show();
        }


      /*  }*/

    }

    private void getImageFromGallery(){
        Intent intent;

        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }

        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code READ_REQUEST_CODE.
        // If the request code seen here doesn't match, it's the response to some other intent,
        // and the below code shouldn't run at all.

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.  Pull that uri using "resultData.getData()"

            if (data != null) {
                imageUri = data.getData();
                Log.i(LOG_TAG, "Uri: " + imageUri.toString());

                mImage.setImageBitmap(getBitmapFromUri(imageUri));
            }
        }
    }

    public Bitmap getBitmapFromUri(Uri uri) {

        if (uri == null || uri.toString().isEmpty())
            return null;

        // Get the dimensions of the View
        int targetW = mImage.getWidth();
        int targetH = mImage.getHeight();

        InputStream input = null;
        try {
            input = this.getContentResolver().openInputStream(uri);

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            input = this.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();
            return bitmap;

        } catch (FileNotFoundException fne) {
            Log.e(LOG_TAG, "Failed to load image.", fne);
            return null;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to load image.", e);
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException ioe) {

            }
        }
    }

}