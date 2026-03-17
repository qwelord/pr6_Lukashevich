package com.example.notes_lukashevich.datas;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.notes_lukashevich.domains.models.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class RepoNotes {
    public static ArrayList<Note> Notes = new ArrayList<>();
    private static final String PREF_NAME = "notes_prefs";
    private static final String KEY_NOTES = "notes_list";

    public static void saveNotes(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Notes);
        editor.putString(KEY_NOTES, json);
        editor.apply();
    }

    public static void loadNotes(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_NOTES, null);
        Type type = new TypeToken<ArrayList<Note>>() {}.getType();
        Notes = gson.fromJson(json, type);
        if (Notes == null) Notes = new ArrayList<>();
    }
}