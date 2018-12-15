package com.example.veronica.todoapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.veronica.todoapp.data.ToDoContract.itemsEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static int category = 0;
    //Declaring global Objects from the Views
    FloatingActionButton addItemButton;
    ListView itemsListView;
    CustomCursorAdapter mCustomCursorAdapter;
    View emptyView;
    Intent gotHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initializing the EmptyView
        emptyView = findViewById(R.id.empty_view);
        //Initializing the FloatingActionButton
        addItemButton = findViewById(R.id.add_item_button);

        //Initializing the ListView
        itemsListView = findViewById(R.id.main_list_view);

        //Setting the EmptyView to the ListView
        itemsListView.setEmptyView(emptyView);

        //Initializing the CustomCursorAdapter
        mCustomCursorAdapter = new CustomCursorAdapter(this, null);

        gotHere = getIntent();
        if (gotHere.hasExtra("category")) {
            category = gotHere.getExtras().getInt("category");

        }

        if (category == itemsEntry.SCHOOL_CATEGORY) {
            setTitle("School ToDo List");
        } else if (category == itemsEntry.WORK_CATEGORY) {
            setTitle("Work ToDo List");
        }

        //Setting the Adapter to the ListView
        itemsListView.setAdapter(mCustomCursorAdapter);
        getLoaderManager().initLoader(1, null, this);


        //Setting Intent on FloatingActionButton Press
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addPageIntent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(addPageIntent);
            }
        });

        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent viewItem = new Intent(MainActivity.this, ViewActivity.class);
                Uri mCurrentUri = ContentUris.withAppendedId(itemsEntry.CONTENT_URI, id);
                viewItem.setData(mCurrentUri);
                startActivity(viewItem);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_menu_item:
                deleteAllItems();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                itemsEntry._ID,
                itemsEntry.COLUMN_NAME,
                itemsEntry.COLUMN_DESCRIPTION,
                itemsEntry.COLUMN_CATEGORY};
        String selection = itemsEntry.COLUMN_CATEGORY + "=?";
        String[] selectionArgs = {String.valueOf(category)};

        return new CursorLoader(this, itemsEntry.CONTENT_URI, projection, selection, selectionArgs, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCustomCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCustomCursorAdapter.swapCursor(null);
    }

    private void deleteAllItems() {
        int rowsDeleted;
        String selection = itemsEntry.COLUMN_CATEGORY + "=?";
        String[] selectionArgs = {String.valueOf(category)};
        rowsDeleted = getContentResolver().delete(itemsEntry.CONTENT_URI, selection, selectionArgs);
        if (rowsDeleted == 0) {
            Toast.makeText(this, R.string.delete_items_failed_msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.delete_items_successful_msg, Toast.LENGTH_SHORT).show();

        }

    }
}
