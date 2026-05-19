package com.example.notes_lukashevich.presentations;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.notes_lukashevich.R;
import com.example.notes_lukashevich.datas.DbContext;
import com.example.notes_lukashevich.datas.NotesContext;
import com.example.notes_lukashevich.domains.models.Note;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class NotesActivity extends AppCompatActivity {

    GridLayout itemsParent;
    View bthAddNotes;
    EditText etSearch;
    DbContext dbContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_notes);

        bthAddNotes = findViewById(R.id.bth_add_notes);
        itemsParent = findViewById(R.id.gl_notes);
        etSearch = findViewById(R.id.et_search);

        bthAddNotes.setOnClickListener(v -> {
            Intent intent = new Intent(this, NoteActivity.class);
            startActivity(intent);
        });

        etSearch.setOnKeyListener(SearchListener);

        dbContext = new DbContext(this);
        LoadNotes(NotesContext.AllNotes());
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadNotes(NotesContext.AllNotes());
    }

    public void LoadNotes(ArrayList<Note> notes) {
        itemsParent.removeAllViews();
        for (int i = 0; i < notes.size(); i++) {
            View item_notes = LayoutInflater.from(this).inflate(R.layout.item_note, itemsParent, false);

            Note currentNote = notes.get(i);

            if (currentNote.color != null && item_notes instanceof CardView) {
                ((CardView) item_notes).setCardBackgroundColor(Color.parseColor(currentNote.color));
            }

            ((TextView) item_notes.findViewById(R.id.tv_title)).setText(currentNote.title);
            ((TextView) item_notes.findViewById(R.id.tv_text)).setText(currentNote.text);
            ((TextView) item_notes.findViewById(R.id.tv_date)).setText(currentNote.date);

            final int realPosition = i;

            item_notes.setOnClickListener(v -> {
                Intent intent = new Intent(this, NoteActivity.class);
                intent.putExtra("Id", currentNote.id);
                startActivity(intent);
            });
            itemsParent.addView(item_notes);
        }
    }

    View.OnKeyListener SearchListener = (v, keyCode, event) -> {
        String search = etSearch.getText().toString();

        ArrayList<Note> findNotes = NotesContext.AllNotes().stream()
                .filter(item -> (item.text != null && item.text.contains(search)) ||
                        (item.title != null && item.title.contains(search)))
                .collect(Collectors.toCollection(ArrayList::new));

        LoadNotes(findNotes);
        return false;
    };
}