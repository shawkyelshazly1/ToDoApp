package com.example.veronica.todoapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.veronica.todoapp.data.ToDoContract.itemsEntry;

public class ItemsDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "todoItems.db";
    public static final int DB_VERSION = 1;
    public static final String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " +
            itemsEntry.TABLE_NAME + "(" +
            itemsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            itemsEntry.COLUMN_NAME + " TEXT NOT NULL, " +
            itemsEntry.COLUMN_DESCRIPTION + " TEXT, " +
            itemsEntry.COLUMN_CATEGORY + " INTEGER NOT NULL DEFAULT 0);";

    public ItemsDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
