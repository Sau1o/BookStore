package com.example.android.bookstore.data;

import android.provider.BaseColumns;

public final class StoreContract {

    private StoreContract(){

    }

    public static final class BookEntry implements BaseColumns{

        public final static String TABLE_NAME = "books";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_BOOK_NAME = "product_name";
        public final static String COLUMN_BOOK_PRICE = "price";
        public final static String COLUMN_BOOK_QUANTITY = "quantity";
        public final static String COLUMN_BOOK_SUPPLIER_NAME = "supplier_name";
        public final static String COLUMN_BOOK_SUPPLIER_PHONE = "supllier_phone";


    }
}
