package com.example.veronica.todoapp;

import android.app.LoaderManager;
import android.content.ContentValues;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.veronica.todoapp.data.ItemsDbHelper;
import com.example.veronica.todoapp.data.ToDoContract;

public class AddActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    EditText titleField;
    EditText descField;
    ItemsDbHelper itemsDbHelper;
    Spinner mCategorySpinner;
    Uri mCurrentUri;
    private int mCategory;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_item:
                addItem();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        titleField = findViewById(R.id.todo_item_title);
        descField = findViewById(R.id.todo_item_desc);
        mCategorySpinner = findViewById(R.id.spinner_category);
        titleField.setText("");
        descField.setText("");
        setupSpinner();
        Intent editIntent = getIntent();
        mCurrentUri = editIntent.getData();
        if (editIntent.hasExtra("category")) {
            int category = editIntent.getExtras().getInt("category");
            switch (category) {
                case ToDoContract.itemsEntry.SCHOOL_CATEGORY:
                    mCategory = ToDoContract.itemsEntry.SCHOOL_CATEGORY;
                    break;
                case ToDoContract.itemsEntry.WORK_CATEGORY:
                    mCategory = ToDoContract.itemsEntry.WORK_CATEGORY;
                    break;
            }
        }

        if (mCurrentUri != null) {
            setTitle("Edit Item");
            getLoaderManager().initLoader(1, null, this);
        }

        itemsDbHelper = new ItemsDbHelper(this);


    }

    private void setupSpinner() {
        ArrayAdapter categoryArrayAdapter = ArrayAdapter.createFromResource(this, R.array.array_category_options, android.R.layout.simple_spinner_item);

        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mCategorySpinner.setAdapter(categoryArrayAdapter);

        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = (String) adapterView.getItemAtPosition(i);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.school_category))) {
                        mCategory = 0;
                    } else if (selection.equals(getString(R.string.work_category))) {
                        mCategory = 1;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mCategory = 0;
            }
        });


    }

    private void addItem() {

        String title = titleField.getText().toString().trim();
        String desc = descField.getText().toString();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, R.string.title_empty_error_toast, Toast.LENGTH_SHORT).show();

            return;

        }
        ContentValues values = new ContentValues();
        values.put(ToDoContract.itemsEntry.COLUMN_NAME, title);
        values.put(ToDoContract.itemsEntry.COLUMN_DESCRIPTION, desc);
        values.put(ToDoContract.itemsEntry.COLUMN_CATEGORY, mCategory);
        if (mCurrentUri == null) {

            Uri uri = getContentResolver().insert(ToDoContract.itemsEntry.CONTENT_URI, values);

            if (uri != null) {
                Toast.makeText(this, R.string.new_item_toast, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.failed_add_toast, Toast.LENGTH_SHORT).show();

            }

            Intent goHome = new Intent(this, MainActivity.class);
            goHome.putExtra("category", mCategory);
            startActivity(goHome);

        } else {
            int rowsUpdated = getContentResolver().update(mCurrentUri, values, null, null);
            if (rowsUpdated != 0) {
                Toast.makeText(this, "Item Was Updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update item", Toast.LENGTH_SHORT).show();

            }
            Intent goViewItem = new Intent(this, ViewActivity.class);
            goViewItem.setData(mCurrentUri);
            startActivity(goViewItem);
        }


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ToDoContract.itemsEntry._ID,
                ToDoContract.itemsEntry.COLUMN_NAME,
                ToDoContract.itemsEntry.COLUMN_DESCRIPTION,
                ToDoContract.itemsEntry.COLUMN_CATEGORY};

        return new CursorLoader(this, mCurrentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(ToDoContract.itemsEntry.COLUMN_NAME));
            String desc = cursor.getString(cursor.getColumnIndex(ToDoContract.itemsEntry.COLUMN_DESCRIPTION));
            int category = cursor.getInt(cursor.getColumnIndex(ToDoContract.itemsEntry.COLUMN_CATEGORY));

            titleField.setText(name);
            descField.setText(desc);

            switch (category) {
                case ToDoContract.itemsEntry.SCHOOL_CATEGORY:
                    mCategorySpinner.setSelection(ToDoContract.itemsEntry.SCHOOL_CATEGORY);
                    break;
                case ToDoContract.itemsEntry.WORK_CATEGORY:
                    mCategorySpinner.setSelection(ToDoContract.itemsEntry.WORK_CATEGORY);
                    break;
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        titleField.setText("");
        descField.setText("");

    }
}
