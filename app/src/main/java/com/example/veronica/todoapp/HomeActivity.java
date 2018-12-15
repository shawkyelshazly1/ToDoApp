package com.example.veronica.todoapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.veronica.todoapp.data.ToDoContract;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FloatingActionButton workButton = findViewById(R.id.work_button);
        FloatingActionButton schoolButton = findViewById(R.id.school_button);
        FloatingActionButton addButton = findViewById(R.id.add_button);
        TextView workCountView = findViewById(R.id.work_count);
        TextView schoolCountView = findViewById(R.id.school_count);

        String[] schoolProjection = {ToDoContract.itemsEntry._ID,
                ToDoContract.itemsEntry.COLUMN_CATEGORY};
        String schoolSelection = ToDoContract.itemsEntry.COLUMN_CATEGORY + "=?";
        String[] schoolSelectionArgs = {String.valueOf(ToDoContract.itemsEntry.SCHOOL_CATEGORY)};
        Cursor cursor = getContentResolver().query(ToDoContract.itemsEntry.CONTENT_URI, schoolProjection, schoolSelection, schoolSelectionArgs, null);
        int schoolCount = cursor.getCount();
        schoolCountView.setText(String.valueOf(schoolCount));


        String[] workProjection = {ToDoContract.itemsEntry._ID,
                ToDoContract.itemsEntry.COLUMN_CATEGORY};
        String workSelection = ToDoContract.itemsEntry.COLUMN_CATEGORY + "=?";
        String[] workSelectionArgs = {String.valueOf(ToDoContract.itemsEntry.WORK_CATEGORY)};
        Cursor workCursor = getContentResolver().query(ToDoContract.itemsEntry.CONTENT_URI, workProjection, workSelection, workSelectionArgs, null);
        int workCount = workCursor.getCount();
        workCountView.setText(String.valueOf(workCount));


        workButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getWorkItems = new Intent(HomeActivity.this, MainActivity.class);
                getWorkItems.putExtra("category", ToDoContract.itemsEntry.WORK_CATEGORY);
                startActivity(getWorkItems);
            }
        });

        schoolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getWorkItems = new Intent(HomeActivity.this, MainActivity.class);
                getWorkItems.putExtra("category", ToDoContract.itemsEntry.SCHOOL_CATEGORY);
                startActivity(getWorkItems);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addPageIntent = new Intent(HomeActivity.this, AddActivity.class);
                startActivity(addPageIntent);
            }
        });


    }


}
