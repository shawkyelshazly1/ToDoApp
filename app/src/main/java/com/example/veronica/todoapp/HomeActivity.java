package com.example.veronica.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.veronica.todoapp.data.ToDoContract;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FloatingActionButton workButton = findViewById(R.id.work_button);
        FloatingActionButton schoolButton = findViewById(R.id.school_button);
        FloatingActionButton addButton = findViewById(R.id.add_button);

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
