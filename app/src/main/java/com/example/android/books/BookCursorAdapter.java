package com.example.android.books;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.books.data.StoreContract.BookEntry;

public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTextView =  view.findViewById(R.id.name);
        TextView priceTextView =  view.findViewById(R.id.price);
        TextView quantityTextView =  view.findViewById(R.id.quantity);

        int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
        String t = String.valueOf(nameColumnIndex);
        Log.i("test",t);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);

        String bookName = cursor.getString(nameColumnIndex);
        int bookPrice = cursor.getInt(priceColumnIndex);
        int bookQuantity = cursor.getInt(quantityColumnIndex);

        nameTextView.setText(bookName);
        priceTextView.setText("price: " + Integer.toString(bookPrice) +"$");
        quantityTextView.setText("Quantity: " + Integer.toString(bookQuantity));
    }
}
