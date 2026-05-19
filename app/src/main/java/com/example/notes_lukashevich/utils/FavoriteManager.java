package com.example.notes_lukashevich.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class FavoriteManager {
    private static final String PREF_NAME = "favorites_pref";
    private static final String KEY_SHOW_ONLY_FAVORITES = "show_only_favorites";
    private static SharedPreferences prefs;

    public static void init(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isShowOnlyFavorites() {
        return prefs.getBoolean(KEY_SHOW_ONLY_FAVORITES, false);
    }

    public static void setShowOnlyFavorites(boolean show) {
        prefs.edit().putBoolean(KEY_SHOW_ONLY_FAVORITES, show).apply();
    }
}