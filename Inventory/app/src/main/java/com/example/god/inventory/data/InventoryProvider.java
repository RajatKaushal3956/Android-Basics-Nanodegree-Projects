package com.example.god.inventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.god.inventory.R;

import org.w3c.dom.Text;

public class InventoryProvider extends ContentProvider {
    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();

    private InventoryDbHelper mDbHelper;

    public static final int PRODUCT = 100;
    public static final int PRODUCT_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_PRODUCT, PRODUCT);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_PRODUCT + "/#", PRODUCT_ID);
    }
    @Override
    public boolean onCreate() {
        mDbHelper = new InventoryDbHelper(getContext());
        return true;
    }
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                cursor = database.query(InventoryContract.ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PRODUCT_ID:
                selection = InventoryContract.ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(InventoryContract.ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
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
                Uri newUri = insertStock(uri, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                return newUri;
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }
    private Uri insertStock(Uri uri, ContentValues values) {
        String sku = values.getAsString(InventoryContract.ProductEntry.COLUMN_EXAMPLE);
        String name = values.getAsString(InventoryContract.ProductEntry.COLUMN_NAME);
        String supplier = values.getAsString(InventoryContract.ProductEntry.COLUMN_SUPPLIER);
        String picture = values.getAsString(InventoryContract.ProductEntry.COLUMN_PICTURE);
        int qty = values.getAsInteger(InventoryContract.ProductEntry.COLUMN_QTY);
        if (sku == null || name == null || supplier == null) {
            throw new IllegalArgumentException("A stock entry requires SKU, Name, Supplier");
        }
        if (TextUtils.isEmpty(sku)
                || TextUtils.isEmpty(name)
                || TextUtils.isEmpty(supplier)
                || qty == 0) {
            throw new IllegalArgumentException("A stock entry requires valid SKU, Name, Supplier, Qty");
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(InventoryContract.ProductEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, getContext().getString(R.string.failed_to_insert_row) + uri);
            return null;
        }
        Log.e(LOG_TAG, getContext().getString(R.string.successfully_inserted_row_for) + uri);
        return ContentUris.withAppendedId(uri, id);
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case PRODUCT:
                rowsUpdated = database.delete(InventoryContract.ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = InventoryContract.ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = database.delete(InventoryContract.ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                return updateStock(uri, contentValues, selection, selectionArgs);
            case PRODUCT_ID:
                selection = InventoryContract.ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                int rowsUpdated = updateStock(uri, contentValues, selection, selectionArgs);
                if (rowsUpdated != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsUpdated;
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int updateStock(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String example = values.getAsString(InventoryContract.ProductEntry.COLUMN_QTY);
        String name = values.getAsString(InventoryContract.ProductEntry.COLUMN_NAME);
        String supplier = values.getAsString(InventoryContract.ProductEntry.COLUMN_SUPPLIER);
        if (values.containsKey(InventoryContract.ProductEntry.COLUMN_QTY) && example == null) {
            throw new IllegalArgumentException("A stock entry requires a valid QTY");
        }
        if (values.containsKey(InventoryContract.ProductEntry.COLUMN_NAME) && name == null) {
            throw new IllegalArgumentException("A stock entry requires a valid Name");
        }
        if (values.containsKey(InventoryContract.ProductEntry.COLUMN_SUPPLIER) && supplier == null) {
            throw new IllegalArgumentException("A stock entry requires a valid Supplier");
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rows = database.update(InventoryContract.ProductEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rows == -1) {
            Log.e(LOG_TAG, getContext().getString(R.string.failed_to_update) + uri);
            return 0;
        }
        return rows;
    }
}

