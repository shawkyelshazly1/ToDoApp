package com.example.veronica.todoapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.veronica.todoapp.data.ToDoContract.itemsEntry;

public class CustomCursorAdapter extends CursorAdapter {
    public CustomCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_layout, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView titleView = view.findViewById(R.id.item_title);
        TextView descView = view.findViewById(R.id.item_desc);

        String title = cursor.getString(cursor.getColumnIndex(itemsEntry.COLUMN_NAME));
        String desc = cursor.getString(cursor.getColumnIndex(itemsEntry.COLUMN_DESCRIPTION));

        titleView.setText(title);
        descView.setText(desc);
    }
}
