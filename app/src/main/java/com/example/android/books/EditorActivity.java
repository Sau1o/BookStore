package com.example.android.books;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.books.data.StoreContract.BookEntry;

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EXISTING_BOOK_LOADER = 0;

    private Uri mCurrentBookUri;

    private EditText mNameEditText;

    private EditText mPriceEditText;

    private TextView mQuantityText;

    private EditText mSupplierNameEditText;

    private EditText mSupplierPhoneEditText;

    private Button mPlusButton;

    private Button mMinusButton;

    private Button mCallButton;

    private boolean mBookHasChanged = false;

    int quantity = 0;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mBookHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();

        if(mCurrentBookUri == null){
            setTitle(getString(R.string.editor_activity_title_new_book));
            invalidateOptionsMenu();
        }else{
            setTitle(getString(R.string.editor_activity_title_edit_book));
            getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }

        mNameEditText = findViewById(R.id.edit_book_name);
        mPriceEditText = findViewById(R.id.edit_book_price);
        mQuantityText = findViewById(R.id.edit_book_quantity);
        mSupplierNameEditText = findViewById(R.id.edit_book_suppplier_name);
        mSupplierPhoneEditText = findViewById(R.id.edit_book_supplier_phone);
        mPlusButton = findViewById(R.id.button_plus);
        mMinusButton = findViewById(R.id.button_minus);
        mCallButton = findViewById(R.id.callButton);

        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityText.setOnTouchListener(mTouchListener);
        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mSupplierPhoneEditText.setOnTouchListener(mTouchListener);

        mPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity += 1;
                mQuantityText.setText(String.valueOf(quantity));
            }
        });

        mMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity>=1){
                    quantity-=1;
                    mQuantityText.setText(String.valueOf(quantity));
                }else {
                    Toast.makeText(getBaseContext(), getString(R.string.info_value_quantity), Toast.LENGTH_LONG).show();
                }
            }
        });

        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mSupplierPhoneEditText.getText().toString();
                Uri uri = Uri.parse("tel:"+phone);
                Intent intent = new Intent(Intent.ACTION_DIAL,uri);
                startActivity(intent);
            }
        });

    }

    private boolean saveBook(){

        boolean testName = false;
        boolean testPrice = false;
        boolean testSupplierPhone = false;
        boolean testSupplierName = false;

        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityText.getText().toString().trim();
        String supplierNameString = mSupplierNameEditText.getText().toString().trim();
        String supplierPhoneString = mSupplierPhoneEditText.getText().toString().trim();
        if (mCurrentBookUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString) &&
                TextUtils.isEmpty(quantityString) && TextUtils.isEmpty(supplierNameString) &&
                TextUtils.isEmpty(supplierPhoneString)) {
            return false;
        }

        ContentValues values = new ContentValues();
        if (!TextUtils.isEmpty(nameString)) {
            values.put(BookEntry.COLUMN_BOOK_NAME, nameString);
            testName = true;
        }
        int price = 0;
        if(!TextUtils.isEmpty(priceString)){
            price = Integer.parseInt(priceString);
            testPrice = true;
        }
        values.put(BookEntry.COLUMN_BOOK_PRICE, price);

        int quantity = 0;
        if(!TextUtils.isEmpty(quantityString)){
            quantity = Integer.parseInt(quantityString);
        }
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, quantity);

        if(!TextUtils.isEmpty(supplierNameString)) {
            values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, supplierNameString);
            testSupplierName = true;
        }

        int phone = 0;
        if(!TextUtils.isEmpty(supplierPhoneString)){
            phone = Integer.parseInt(supplierPhoneString);
            testSupplierPhone = true;
        }
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE, phone);

        boolean campoVazio = testName && testPrice && testSupplierName && testSupplierPhone;

        if (!campoVazio){
            return campoVazio;
        }else {

            if (mCurrentBookUri == null) {

                Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);

                if (newUri == null) {
                    Toast.makeText(this, getString(R.string.editor_insert_book_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.editor_insert_book_successful),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);

                if (rowsAffected == 0) {
                    Toast.makeText(this, getString(R.string.editor_update_book_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.editor_update_book_successful),
                            Toast.LENGTH_SHORT).show();
                }
            }
            return campoVazio;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentBookUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                boolean isEmpty = saveBook();
                if (!isEmpty){
                   Toast.makeText(this,getText(R.string.information),Toast.LENGTH_LONG).show();
                }else {
                    finish();
                }
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mBookHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mBookHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
                BookEntry.COLUMN_BOOK_SUPPLIER_PHONE};

        return new CursorLoader(this,
                mCurrentBookUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE);

            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            int supplierPhone = cursor.getInt(supplierPhoneColumnIndex);

            mNameEditText.setText(name);
            mPriceEditText.setText(Integer.toString(price));
            mQuantityText.setText(Integer.toString(quantity));
            mSupplierNameEditText.setText(supplierName);
            mSupplierPhoneEditText.setText(Integer.toString(supplierPhone));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityText.setText("");
        mSupplierNameEditText.setText("");
        mSupplierPhoneEditText.setText("");
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteBook() {

        if (mCurrentBookUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}
