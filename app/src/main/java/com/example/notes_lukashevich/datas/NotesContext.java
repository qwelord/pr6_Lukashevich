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
            note.isFavorite = cursor.getInt(5);
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
        CV.put("IsFavorite", note.isFavorite);

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
        note.isFavorite = cursor.getInt(5);
        cursor.close();
        return note;
    }

    public static void Delete(Note note) {
        DbContext.sqLiteDatabase.delete("Notes", "Id = ?", new String[] {String.valueOf(note.id)});
        DbContext.sqLiteDatabase.delete("NoteVersions", "NoteId = ?", new String[] {String.valueOf(note.id)});
    }

    public static void toggleFavorite(int noteId, int isFavorite) {
        ContentValues cv = new ContentValues();
        cv.put("IsFavorite", isFavorite);
        DbContext.sqLiteDatabase.update("Notes", cv, "Id = ?", new String[] {String.valueOf(noteId)});
    }

    public static void saveVersion(Note note) {
        ContentValues cv = new ContentValues();
        cv.put("NoteId", note.id);
        cv.put("Title", note.title);
        cv.put("Text", note.text);
        cv.put("Date", note.date);
        DbContext.sqLiteDatabase.insert("NoteVersions", null, cv);
    }

    public static ArrayList<Note> getVersions(int noteId) {
        ArrayList<Note> versions = new ArrayList<>();
        Cursor cursor = DbContext.sqLiteDatabase.query("NoteVersions", null, "NoteId = ?", new String[]{String.valueOf(noteId)}, null, null, "Date DESC");
        if (cursor.moveToFirst()) {
            do {
                Note v = new Note();
                v.id = cursor.getInt(0);
                v.title = cursor.getString(2);
                v.text = cursor.getString(3);
                v.date = cursor.getString(4);
                versions.add(v);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return versions;
    }

    public static Note getVersionById(int versionId) {
        Cursor cursor = DbContext.sqLiteDatabase.query("NoteVersions", null, "Id = ?", new String[]{String.valueOf(versionId)}, null, null, null);
        if (cursor.moveToFirst() == false) {
            cursor.close();
            return null;
        }
        Note version = new Note();
        version.id = cursor.getInt(0);
        version.title = cursor.getString(2);
        version.text = cursor.getString(3);
        version.date = cursor.getString(4);
        cursor.close();
        return version;
    }

    public static void restoreVersion(int noteId, Note version) {
        Note current = GetNoteById(noteId);
        if (current != null) {
            saveVersion(current);
            current.title = version.title;
            current.text = version.text;
            current.date = version.date;
            Save(current, true);
        }
    }
}