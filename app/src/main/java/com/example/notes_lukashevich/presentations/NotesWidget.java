package com.example.notes_lukashevich.presentations;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import com.example.notes_lukashevich.R;
import com.example.notes_lukashevich.datas.NotesContext;
import com.example.notes_lukashevich.domains.models.Note;
import java.util.ArrayList;

public class NotesWidget extends AppWidgetProvider {

    private static final String ACTION_NEXT_NOTE = "com.example.notes_lukashevich.NEXT_NOTE";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_NEXT_NOTE.equals(intent.getAction())) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
            if (appWidgetId != -1) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int currentIndex = getCurrentIndex(context, appWidgetId);
                ArrayList<Note> notes = NotesContext.AllNotes(context);
                if (!notes.isEmpty()) {
                    int nextIndex = (currentIndex + 1) % notes.size();
                    saveCurrentIndex(context, appWidgetId, nextIndex);
                    updateWidget(context, appWidgetManager, appWidgetId);
                }
            }
        }
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        ArrayList<Note> notes = NotesContext.AllNotes(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        if (notes.isEmpty()) {
            views.setTextViewText(R.id.tv, "Нет заметок\nДобавьте заметку в приложении");
        } else {
            int currentIndex = getCurrentIndex(context, appWidgetId);
            Note note = notes.get(currentIndex);
            views.setTextViewText(R.id.tv, note.text);
        }

        Intent intent = new Intent(context, NotesWidget.class);
        intent.setAction(ACTION_NEXT_NOTE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                appWidgetId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        views.setOnClickPendingIntent(R.id.tv, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private int getCurrentIndex(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE);
        return prefs.getInt("WidgetIndex_" + appWidgetId, 0);
    }

    private void saveCurrentIndex(Context context, int appWidgetId, int index) {
        SharedPreferences prefs = context.getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE);
        prefs.edit().putInt("WidgetIndex_" + appWidgetId, index).apply();
    }
}