package com.example.android.books;

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

import com.example.android.books.data.StoreContract.BookEntry;

public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        view.setTag(new ViewHolder(view,context));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.id = cursor.getLong(cursor.getColumnIndex(BookEntry._ID));

        TextView nameTextView =  view.findViewById(R.id.name);
        TextView priceTextView =  view.findViewById(R.id.price);
        TextView quantityTextView =  view.findViewById(R.id.quantity);

        int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);

        String bookName = cursor.getString(nameColumnIndex);
        int bookPrice = cursor.getInt(priceColumnIndex);
        int bookQuantity = cursor.getInt(quantityColumnIndex);

        nameTextView.setText(bookName);
        priceTextView.setText("price: " + Integer.toString(bookPrice) +"$");
        quantityTextView.setText(Integer.toString(bookQuantity));
    }

    private class ViewHolder implements View.OnClickListener {
        final View view;
        final Context context;
        final TextView nameTextView;
        final TextView priceTextView;
        final TextView quantityTextView;
        final Button btSale;
        Long id = -1l;

        public ViewHolder(View view, Context context)  {
            this.view = view;
            this.context = context;
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
            int qtdInteger = Integer.parseInt(quantityString);
            qtdInteger -= 1;
            if (qtdInteger>=0) {
                ContentValues values = new ContentValues();
                values.put(BookEntry.COLUMN_BOOK_QUANTITY, qtdInteger);
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                Uri uri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, viewHolder.id);
                int t = context.getContentResolver().update(uri, values, null, null);
                Toast.makeText(context.getApplicationContext(), context.getString(R.string.book_sold), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context.getApplicationContext(), context.getString(R.string.no_more_book), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
