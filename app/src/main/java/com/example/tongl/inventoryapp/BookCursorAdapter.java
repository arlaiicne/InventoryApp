package com.example.tongl.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tongl.inventoryapp.data.BookContract.BookEntry;

/**
 * {@link BookCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of book data as its data source. This adapter knows
 * how to create list items for each row of book data in the {@link Cursor}.
 */
public class BookCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link BookCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the book data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current book can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        final int index = cursor.getColumnIndex(BookEntry._ID);

        // Find the columns of book attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE);
        final int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);
        final int id = cursor.getInt(index);

        // Read the book attributes from the Cursor for the current book
        String productName = cursor.getString(nameColumnIndex);
        String price = cursor.getString(priceColumnIndex);
        final String quantity = cursor.getString(quantityColumnIndex);

        // Update the TextViews with the attributes for the current book
        nameTextView.setText(productName);
        priceTextView.setText(price);
        quantityTextView.setText(quantity);
        Button saleButton = (Button) view.findViewById(R.id.sale_button);

        // Setup OnClickListener for the sale button to decrease quantity by 1

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int updatedQuantity = Integer.parseInt(quantity);
                if (updatedQuantity > 0) {
                    updatedQuantity = updatedQuantity - 1;
                    ContentValues values = new ContentValues();
                    values.put(BookEntry.COLUMN_QUANTITY, updatedQuantity);
                    Uri newUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);
                    context.getContentResolver().update(newUri, values, null, null);
                    quantityTextView.setText(Integer.toString(updatedQuantity));
                } else {
                    Toast.makeText(context, R.string.below_zero,
                            Toast.LENGTH_SHORT).show();


                }
            }
        });

    }
}

