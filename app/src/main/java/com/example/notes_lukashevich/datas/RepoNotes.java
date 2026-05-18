package com.example.notes_lukashevich.datas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.example.notes_lukashevich.domains.models.Note;
import java.util.ArrayList;

public class RepoNotes {
    public static ArrayList<Note> Notes = new ArrayList<>();

    public static void saveNotes(Context context) {
        if (DbContext.sqLiteDatabase == null) {
            new DbContext(context);
        }

        DbContext.sqLiteDatabase.delete("Notes", null, null);

        for (Note note : Notes) {
            ContentValues values = new ContentValues();
            values.put("Id", note.id);
            values.put("Title", note.title);
            values.put("Text", note.text);
            values.put("Date", note.date);
            values.put("Color", note.color);
            DbContext.sqLiteDatabase.insert("Notes", null, values);
        }
    }

    public static void loadNotes(Context context) {
        if (DbContext.sqLiteDatabase == null) {
            new DbContext(context);
        }

        Notes.clear();
        Cursor cursor = DbContext.sqLiteDatabase.query("Notes", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.id = cursor.getInt(cursor.getColumnIndexOrThrow("Id"));
                note.title = cursor.getString(cursor.getColumnIndexOrThrow("Title"));
                note.text = cursor.getString(cursor.getColumnIndexOrThrow("Text"));
                note.date = cursor.getString(cursor.getColumnIndexOrThrow("Date"));
                note.color = cursor.getString(cursor.getColumnIndexOrThrow("Color"));
                Notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}