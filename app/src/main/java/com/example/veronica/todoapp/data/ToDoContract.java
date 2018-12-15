package com.example.veronica.todoapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class ToDoContract {
    public static final String CONTENT_AUTHORITY = "com.example.veronica.todoapp";
    public static final String ITEMS_PATH = "items";
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private ToDoContract() {

    }

    public static abstract class itemsEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, ITEMS_PATH);
        public static final String TABLE_NAME = "items";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_CATEGORY = "category";
        public static final int SCHOOL_CATEGORY = 0;
        public static final int WORK_CATEGORY = 1;


    }

}
