package com.example.android.books;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.books.data.StoreContract.BookEntry;

public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        view.setTag(new ViewHolder(view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.id = cursor.getInt(cursor.getColumnIndex(BookEntry._ID));

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
        quantityTextView.setText("Quantity:" + Integer.toString(bookQuantity));
    }

    private class ViewHolder implements View.OnClickListener {
        final View view;

        final TextView nameTextView;
        final TextView priceTextView;
        final TextView quantityTextView;
        final Button btSale;
        long id = -1;

        public ViewHolder(View view) {
            this.view = view;
            nameTextView = view.findViewById(R.id.name);
            priceTextView = view.findViewById(R.id.price);
            quantityTextView = view.findViewById(R.id.quantity);
            btSale = view.findViewById(R.id.sale_button);
            btSale.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (id == -1) return;
            String quantityString = quantityTextView.getText().toString();
            String[] quantity = quantityString.split(":");
            String qtd = quantity[1];
            Log.i("test",qtd);
            int qtdInteger = Integer.parseInt(qtd);
            qtdInteger -= 1;
            ContentValues values = new ContentValues();
            values.put(BookEntry.COLUMN_BOOK_NAME, qtdInteger);
            Log.i("test",Integer.toString(qtdInteger));
        }
    }

}
