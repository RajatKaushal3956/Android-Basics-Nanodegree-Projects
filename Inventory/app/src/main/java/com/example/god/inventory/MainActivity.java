package com.example.god.inventory;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.god.inventory.data.InventoryContract;
import static com.example.god.inventory.data.InventoryProvider.LOG_TAG;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int URL_LOADER = 0;
    SimpleCursorAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        String[] columns = {
                InventoryContract.ProductEntry.COLUMN_EXAMPLE,
                InventoryContract.ProductEntry.COLUMN_NAME,
                InventoryContract.ProductEntry.COLUMN_SUPPLIER,
                InventoryContract.ProductEntry.COLUMN_QTY,
                InventoryContract.ProductEntry.COLUMN_PICTURE,
                InventoryContract.ProductEntry.COLUMN_PRICE
        };
        int[] views = {
                R.id.example,
                R.id.name_text,
                R.id.supplier_text,
                R.id.qty_text,
                R.id.picture,
                R.id.price_text
        };
        mAdapter = new SimpleCursorAdapter(this, R.layout.list_item, null, columns, views, 0) {
            @Override
            public void bindView(View view, Context context, final Cursor cursor) {
                super.bindView(view, context, cursor);
                Button button = (Button) view.findViewById(R.id.sale_button);
                button.setTag(cursor.getPosition());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(LOG_TAG, getString(R.string.position_colon) + view.getTag());
                        sellStock(cursor, (int) view.getTag());
                    }
                });
            }
        };
        ListView productListView = (ListView) findViewById(R.id.list);
        productListView.setAdapter(mAdapter);
        View emptyView = findViewById(R.id.empty_view);
        productListView.setEmptyView(emptyView);
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), EditorActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(MainActivity.this, view, "stock_image");
                intent.setData(ContentUris.withAppendedId(InventoryContract.ProductEntry.CONTENT_URI, id));
               startActivity(intent,options.toBundle());
            }
        });
        getLoaderManager().initLoader(URL_LOADER, null, this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_dummy:
                insertDummy();
                return true;
            case R.id.delete_all:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.delete_product)
                        .setMessage(R.string.do_you_really_want_to_delete)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                getContentResolver().delete(InventoryContract.ProductEntry.CONTENT_URI, null, null);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    /**
     * Sell stock from item id
     * @param cursor Cursor of the item to sell
     */
    private void sellStock(Cursor cursor, int position) {
        cursor.moveToPosition(position);
        ContentValues values = new ContentValues();
        int idIndex = cursor.getColumnIndex(InventoryContract.ProductEntry._ID);
        int qtyIndex = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_QTY);
        int qty = cursor.getInt(qtyIndex);
        int id = cursor.getInt(idIndex);

        if (--qty <= -1) {
            Toast.makeText(this, "Not enough stock", Toast.LENGTH_SHORT).show();
            return;
        }
        values.put(InventoryContract.ProductEntry.COLUMN_QTY, qty);
        Uri stockUrl = ContentUris.withAppendedId(InventoryContract.ProductEntry.CONTENT_URI, id);
        int rowsAffected = getContentResolver().update(stockUrl, values, null, null);
        Toast.makeText(this, rowsAffected + getString(R.string.item_has_been_sold), Toast.LENGTH_SHORT).show();
    }

    private void insertDummy() {
        ContentValues values = new ContentValues();
        values.put(InventoryContract.ProductEntry.COLUMN_EXAMPLE, "ABC1234");
        values.put(InventoryContract.ProductEntry.COLUMN_NAME, "Porridge Oats");
        values.put(InventoryContract.ProductEntry.COLUMN_SUPPLIER, "Quakers");
        values.put(InventoryContract.ProductEntry.COLUMN_QTY, 12);
        values.put(InventoryContract.ProductEntry.COLUMN_PRICE, 5.5);
        getContentResolver().insert(InventoryContract.ProductEntry.CONTENT_URI, values);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        /*
     * Takes action based on the ID of the Loader that's being created
     */
        switch (loaderID) {
            case URL_LOADER:
                String[] projection = {
                        InventoryContract.ProductEntry._ID,
                        InventoryContract.ProductEntry.COLUMN_EXAMPLE,
                        InventoryContract.ProductEntry.COLUMN_NAME,
                        InventoryContract.ProductEntry.COLUMN_SUPPLIER,
                        InventoryContract.ProductEntry.COLUMN_QTY,
                        InventoryContract.ProductEntry.COLUMN_PICTURE,
                        InventoryContract.ProductEntry.COLUMN_PRICE
                };
                return new CursorLoader(
                        this,
                        InventoryContract.ProductEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null
                );
            default:
                return null;
        }
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
