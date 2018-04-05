package com.example.god.inventory.data;

/**
 * Created by GOD on 5/1/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class InventoryDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;
    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PRODUCT_TABLE =  "CREATE TABLE " + InventoryContract.ProductEntry.TABLE_NAME + " ("
                + InventoryContract.ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryContract.ProductEntry.COLUMN_EXAMPLE + " TEXT NOT NULL, "
                + InventoryContract.ProductEntry.COLUMN_SUPPLIER + " TEXT, "
                + InventoryContract.ProductEntry.COLUMN_PICTURE + " TEXT, "
                + InventoryContract.ProductEntry.COLUMN_QTY + " INTEGER, "
                + InventoryContract.ProductEntry.COLUMN_PRICE + " REAL NOT NULL DEFAULT 0.00, "
                + InventoryContract.ProductEntry.COLUMN_NAME + " TEXT);";

        db.execSQL(SQL_CREATE_PRODUCT_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}

