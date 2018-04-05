package com.example.god.inventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class InventoryContract {

    public static final String CONTENT_AUTHORITY = "com.example.god.inventory";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PRODUCT = "product";
    private InventoryContract() {}
    public static final class ProductEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCT);
        public final static String TABLE_NAME = "product";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_EXAMPLE = "example";
        public final static String COLUMN_SUPPLIER = "supplier";
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_QTY = "qty";
        public final static String COLUMN_PICTURE = "picture";
        public final static String COLUMN_PRICE = "price";
    }
}

