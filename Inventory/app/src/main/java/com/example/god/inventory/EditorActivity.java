package com.example.god.inventory;

/**
 * Created by GOD on 5/1/2017.
 */

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.god.inventory.data.InventoryContract;
import java.io.File;
import java.io.IOException;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = EditorActivity.class.getSimpleName();
    private EditText mSkuEditText;
    private EditText mNameEditText;
    private AutoCompleteTextView mSupplierAutocomplete;
    private EditText mQtyEditText;
    private ImageView mPicture;
    private Button mTakePicture;
    private EditText mPriceEditText;
    private Button mSaleButton;
    private Button mOrderButton;
    private Button mOrderMoreButton;

    private Uri mStockUri;
    private String mPicturePath;
    private boolean mStockHasChanged = false;
    private static final int EXISTING_STOCK_LOADER = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mSkuEditText = (EditText) findViewById(R.id.sku_edit);
        mNameEditText = (EditText) findViewById(R.id.name_edit);
        mSupplierAutocomplete = (AutoCompleteTextView) findViewById(R.id.supplier_autocomplete);
        mQtyEditText = (EditText) findViewById(R.id.qty_edit);
        mPriceEditText = (EditText) findViewById(R.id.price_edit);
        mPicture = (ImageView) findViewById(R.id.picture);
        mTakePicture = (Button) findViewById(R.id.take_picture);
        mSaleButton = (Button) findViewById(R.id.sale_button);
        mOrderButton = (Button) findViewById(R.id.order_button);
        mOrderMoreButton = (Button) findViewById(R.id.order_more_button);

        mStockUri = getIntent().getData();
        if (mStockUri == null) {
            setTitle(getString(R.string.new_product));
            invalidateOptionsMenu();
            mOrderMoreButton.setVisibility(View.GONE);
        } else {
            getLoaderManager().initLoader(EXISTING_STOCK_LOADER, null, this);
            setTitle(getString(R.string.edit_product));
        }

        mTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStockHasChanged = true;
                dispatchTakePictureIntent();
            }
        });

        mOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStockHasChanged = true;
                int qty;
                String qtyString = mQtyEditText.getText().toString();
                if (TextUtils.isEmpty(qtyString)) {
                    qty = 0;
                } else {
                    qty = Integer.parseInt(mQtyEditText.getText().toString());
                }
                mQtyEditText.setText("" + ++qty);
            }
        });

        mSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStockHasChanged = true;
                int qty;
                String qtyString = mQtyEditText.getText().toString();
                if (TextUtils.isEmpty(qtyString)) {
                    qty = 0;
                } else {
                    qty = Integer.parseInt(mQtyEditText.getText().toString());
                }
                if (qty <= 0) {
                    return;
                }
                mQtyEditText.setText("" + --qty);
            }
        });

        mOrderMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderMoreEmail();
            }
        });

        mSkuEditText.setOnTouchListener(mTouchListener);
        mNameEditText.setOnTouchListener(mTouchListener);
        mSupplierAutocomplete.setOnTouchListener(mTouchListener);
        mQtyEditText.setOnTouchListener(mTouchListener);
    }
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mStockHasChanged = true;
            return false;
        }
    };
    private void orderMoreEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","salesteam@example.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Order more " + mSkuEditText.getText());
        String body = "Please send us more of the below product\n";
        body += "SKU: " + mSkuEditText.getText() + "\n";
        body += "Product name: " + mNameEditText.getText() + "\n";
        body += "Quantity: ";
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                try {
                    saveStock();
                    finish();
                } catch (IllegalArgumentException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_delete:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.delete_product)
                        .setMessage(R.string.do_you_really_want_to_delete)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                deleteStock();
                                finish();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
    private void dispatchTakePictureIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), REQUEST_IMAGE_CAPTURE);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    private void saveStock() {
        String skuString = mSkuEditText.getText().toString().trim();
        String nameString = mNameEditText.getText().toString().trim();
        String supplierString = mSupplierAutocomplete.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        int qtyInt = 0;
        if (!TextUtils.isEmpty(mQtyEditText.getText())) {
            qtyInt = Integer.parseInt(mQtyEditText.getText().toString());
        }
        if (mStockUri == null && TextUtils.isEmpty(skuString) && TextUtils.isEmpty(nameString) && TextUtils.isEmpty(supplierString)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(InventoryContract.ProductEntry.COLUMN_EXAMPLE, skuString);
        values.put(InventoryContract.ProductEntry.COLUMN_NAME, nameString);
        values.put(InventoryContract.ProductEntry.COLUMN_SUPPLIER, supplierString);
        values.put(InventoryContract.ProductEntry.COLUMN_QTY, qtyInt);
        values.put(InventoryContract.ProductEntry.COLUMN_PICTURE, mPicturePath);
        values.put(InventoryContract.ProductEntry.COLUMN_PRICE, priceString);

        if (mStockUri == null) {
            // New Stock
            Uri newUri = getContentResolver().insert(InventoryContract.ProductEntry.CONTENT_URI, values);
            if (newUri != null) {
                Toast.makeText(this, R.string.new_stock_saved, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.error_saving_product, Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mStockUri, values, null, null);
            Log.e(LOG_TAG, "" + rowsAffected);
            if (rowsAffected == 1) {
                Toast.makeText(this, R.string.stock_updated, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.error_updating_product, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deleteStock() {
        if (mStockUri == null) {
            return;
        }
        int r = getContentResolver().delete(mStockUri, null, null);
        if (r == 1) {
            Toast.makeText(this, R.string.deleting_product_successful, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.unable_to_delete_product, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                InventoryContract.ProductEntry._ID,
                InventoryContract.ProductEntry.COLUMN_NAME,
                InventoryContract.ProductEntry.COLUMN_EXAMPLE,
                InventoryContract.ProductEntry.COLUMN_QTY,
                InventoryContract.ProductEntry.COLUMN_SUPPLIER,
                InventoryContract.ProductEntry.COLUMN_PICTURE,
                InventoryContract.ProductEntry.COLUMN_PRICE
        };
        return new CursorLoader(this, mStockUri, projection, null, null, null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, getString(R.string.cursor_loading_finished));
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        cursor.moveToFirst();
        int examColumnIndex = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_EXAMPLE);
        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_NAME);
        int supplierColumnIndex = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_SUPPLIER);
        int qtyColumnIndex = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_QTY);
        int pictureColumnIndex = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_PICTURE);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_PRICE);
        String sku = cursor.getString(examColumnIndex);
        String name = cursor.getString(nameColumnIndex);
        String supplier = cursor.getString(supplierColumnIndex);
        double price = cursor.getDouble(priceColumnIndex);
        int qty = cursor.getInt(qtyColumnIndex);
        mPicturePath = cursor.getString(pictureColumnIndex);
        if (!TextUtils.isEmpty(mPicturePath)) {
            mPicture.setImageURI(Uri.parse(new File(mPicturePath).toString()));
        }
        mSkuEditText.setText(sku);
        mNameEditText.setText(name);
        mSupplierAutocomplete.setText(supplier);
        mQtyEditText.setText(Integer.toString(qty));
        mPriceEditText.setText(Double.toString(price));
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSkuEditText.setText("");
        mNameEditText.setText("");
        mSupplierAutocomplete.setText("");
        mQtyEditText.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                mPicture.setImageBitmap(bitmap);
                mPicturePath = getRealPathFromURI(selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.could_not_load_image, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] project = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, project, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}
