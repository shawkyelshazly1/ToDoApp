package com.example.veronica.todoapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.veronica.todoapp.MainActivity;

public class ItemsProvider extends ContentProvider {
    public static final String LOG_TAG = ItemsProvider.class.getSimpleName();
    public static final int ITEMS = 100;
    public static final int ITEMS_ID = 101;
    public static final int ITEMS_CATEGORY = 102;

    public static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(ToDoContract.CONTENT_AUTHORITY, ToDoContract.ITEMS_PATH, ITEMS);
        mUriMatcher.addURI(ToDoContract.CONTENT_AUTHORITY, ToDoContract.ITEMS_PATH + ToDoContract.itemsEntry.COLUMN_CATEGORY, ITEMS_CATEGORY);
        mUriMatcher.addURI(ToDoContract.CONTENT_AUTHORITY, ToDoContract.ITEMS_PATH + "/#", ITEMS_ID);
    }

    ItemsDbHelper itemsDbHelper;

    @Override
    public boolean onCreate() {
        itemsDbHelper = new ItemsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = itemsDbHelper.getReadableDatabase();
        Cursor cursor = null;
        int match = mUriMatcher.match(uri);
        switch (match) {
            case ITEMS_CATEGORY:
                selection = ToDoContract.itemsEntry.COLUMN_CATEGORY + "=?";
                selectionArgs = new String[]{String.valueOf(MainActivity.category)};
                cursor = database.query(ToDoContract.itemsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ITEMS:
                cursor = database.query(ToDoContract.itemsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ITEMS_ID:
                selection = ToDoContract.itemsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(ToDoContract.itemsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot Query Unknown URI " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);


        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return insertItem(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for" + uri);

        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = itemsDbHelper.getWritableDatabase();
        int rowsDeleted;
        int match = mUriMatcher.match(uri);
        switch (match) {
            case ITEMS_CATEGORY:
                selection = ToDoContract.itemsEntry.COLUMN_CATEGORY + "=?";
                selectionArgs = new String[]{String.valueOf(MainActivity.category)};
                rowsDeleted = database.delete(ToDoContract.itemsEntry.TABLE_NAME, selection, selectionArgs);
            case ITEMS:
                rowsDeleted = database.delete(ToDoContract.itemsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEMS_ID:
                selection = ToDoContract.itemsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(ToDoContract.itemsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for Uri" + uri);

        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case ITEMS_ID:
                selection = ToDoContract.itemsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for uri" + uri);

        }
    }

    private Uri insertItem(Uri uri, ContentValues values) {
        long ig;
        String title = values.getAsString(ToDoContract.itemsEntry.COLUMN_NAME);
        if (title == "") {
            throw new IllegalArgumentException("Item Requires Title");
        }

        String desc = values.getAsString(ToDoContract.itemsEntry.COLUMN_DESCRIPTION);
        if (desc == "") {
            throw new IllegalArgumentException("Item Requires A Description");
        }

        int category = values.getAsInteger(ToDoContract.itemsEntry.COLUMN_CATEGORY);
        SQLiteDatabase database = itemsDbHelper.getWritableDatabase();
        long id = database.insert(ToDoContract.itemsEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "failed to insert row for" + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated;
        if (values.containsKey(ToDoContract.itemsEntry.COLUMN_NAME)) {
            String title = values.getAsString(ToDoContract.itemsEntry.COLUMN_NAME);
            if (title == "") {
                throw new IllegalArgumentException("Item Requires Title");
            }
        }

        if (values.containsKey(ToDoContract.itemsEntry.COLUMN_DESCRIPTION)) {
            String desc = values.getAsString(ToDoContract.itemsEntry.COLUMN_DESCRIPTION);
            if (desc == "") {
                throw new IllegalArgumentException("Item Requires A Description");
            }
        }

        int category = values.getAsInteger(ToDoContract.itemsEntry.COLUMN_CATEGORY);
        SQLiteDatabase database = itemsDbHelper.getWritableDatabase();
        rowsUpdated = database.update(ToDoContract.itemsEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
