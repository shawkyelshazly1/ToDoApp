package com.example.veronica.todoapp;

import android.content.ContentValues;
import android.content.Intent;
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

public class AddActivity extends AppCompatActivity {
    EditText titleField;
    EditText descField;
    ItemsDbHelper itemsDbHelper;
    Spinner mCategorySpinner;
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




        itemsDbHelper = new ItemsDbHelper(this);

        setupSpinner();
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
        Uri uri = getContentResolver().insert(ToDoContract.itemsEntry.CONTENT_URI, values);

        if (uri != null) {
            Toast.makeText(this, R.string.new_item_toast, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.failed_add_toast, Toast.LENGTH_SHORT).show();

        }
        Intent goHome = new Intent(this, MainActivity.class);
        goHome.putExtra("category",mCategory);
        startActivity(goHome);

    }

}
