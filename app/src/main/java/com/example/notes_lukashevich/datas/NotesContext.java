package com.example.notes_lukashevich.datas;

import android.content.ContentValues;
import android.database.Cursor;
import com.example.notes_lukashevich.domains.models.Note;
import java.util.ArrayList;

public class NotesContext {

    public static ArrayList<Note> AllNotes() {
        ArrayList<Note> allNotes = new ArrayList<>();
        Cursor cursor = DbContext.sqLiteDatabase.query("Notes", null, null, null, null, null, null);

        if (cursor.moveToFirst() == false) {
            cursor.close();
            return allNotes;
        }

        do {
            Note note = new Note();
            note.id = cursor.getInt(0);
            note.title = cursor.getString(1);
            note.text = cursor.getString(2);
            note.date = cursor.getString(3);
            note.color = cursor.getString(4);
            allNotes.add(note);
        } while (cursor.moveToNext());

        cursor.close();
        return allNotes;
    }

    public static void Save(Note note, boolean update) {
        ContentValues CV = new ContentValues();
        CV.put("Title", note.title);
        CV.put("Text", note.text);
        CV.put("Date", note.date);
        CV.put("Color", note.color);

        if (update == false) {
            DbContext.sqLiteDatabase.insert("Notes", null, CV);
        } else {
            DbContext.sqLiteDatabase.update("Notes", CV, "Id = ?", new String[] {String.valueOf(note.id)});
        }
    }

    public static Note GetNoteById(int id) {
        Cursor cursor = DbContext.sqLiteDatabase.query("Notes", null, "Id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst() == false) {
            cursor.close();
            return null;
        }
        Note note = new Note();
        note.id = cursor.getInt(0);
        note.title = cursor.getString(1);
        note.text = cursor.getString(2);
        note.date = cursor.getString(3);
        note.color = cursor.getString(4);
        cursor.close();
        return note;
    }

    public static void Delete(Note note) {
        DbContext.sqLiteDatabase.delete("Notes", "Id = ?", new String[] {String.valueOf(note.id)});
    }
}