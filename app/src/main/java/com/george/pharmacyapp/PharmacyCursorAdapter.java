package com.george.pharmacyapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.george.pharmacyapp.data.PharmacyContract;

/**
 * Created by farmaker1 on 27/05/2017.
 */

public class PharmacyCursorAdapter extends CursorAdapter {

    public PharmacyCursorAdapter(Context context,Cursor cursor){
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        View listItemView = view;

        TextView name = (TextView)listItemView.findViewById(R.id.name);
        TextView quantity = (TextView)listItemView.findViewById(R.id.quantity);
        TextView price = (TextView)listItemView.findViewById(R.id.price);

        String nameProduct = cursor.getString(cursor.getColumnIndexOrThrow(PharmacyContract.PharmacyEntry.COLUMN_NAME));
        String quantityProduct = cursor.getString(cursor.getColumnIndexOrThrow(PharmacyContract.PharmacyEntry.COLUMN_QUANTITY));
        String priceProduct = cursor.getString(cursor.getColumnIndexOrThrow(PharmacyContract.PharmacyEntry.COLUMN_PRICE));

        name.setText(nameProduct);
        quantity.setText(quantityProduct);
        price.setText(priceProduct);


    }
}
