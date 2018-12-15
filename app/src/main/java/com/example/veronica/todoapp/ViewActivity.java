package com.example.veronica.todoapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.veronica.todoapp.data.ToDoContract;

public class ViewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    TextView titleView;
    TextView descView;
    private Uri mCurrentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        titleView = findViewById(R.id.title_view);
        descView = findViewById(R.id.desc_view);

        Intent fromHome = getIntent();
        mCurrentUri = fromHome.getData();

        if (mCurrentUri != null) {
            getLoaderManager().initLoader(1, null, this);
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ToDoContract.itemsEntry._ID,
                ToDoContract.itemsEntry.COLUMN_NAME,
                ToDoContract.itemsEntry.COLUMN_DESCRIPTION};

        return new CursorLoader(this, mCurrentUri, projection, null, null, null);

    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(ToDoContract.itemsEntry.COLUMN_NAME));
            String desc = cursor.getString(cursor.getColumnIndex(ToDoContract.itemsEntry.COLUMN_DESCRIPTION));
            titleView.setText(name);
            if (TextUtils.isEmpty(desc)) {
                descView.setText("No Description");

            } else {
                descView.setText(desc);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_item:
                deleteItem();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteItem() {
        int rowsDeleted;
        if (mCurrentUri != null) {
            rowsDeleted = getContentResolver().delete(mCurrentUri, null, null);
            if (rowsDeleted != 0) {
                Toast.makeText(this, R.string.item_deleted_msg, Toast.LENGTH_SHORT).show();
            }
        }
        Intent goHome = new Intent(this, MainActivity.class);
        startActivity(goHome);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        titleView.setText("");
        descView.setText("");

    }
}
