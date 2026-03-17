package com.example.notes_lukashevich.presentations;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.notes_lukashevich.R;
import com.example.notes_lukashevich.datas.RepoNotes;
import com.example.notes_lukashevich.domains.models.Note;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class NotesActivity extends AppCompatActivity {

    GridLayout itemsParent;
    View bthAddNotes;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        bthAddNotes = findViewById(R.id.bth_add_notes);
        itemsParent = findViewById(R.id.gl_notes);
        etSearch = findViewById(R.id.et_search);

        bthAddNotes.setOnClickListener(v -> {
            Intent intent = new Intent(this, NoteActivity.class);
            startActivity(intent);
        });

        etSearch.setOnKeyListener((v, keyCode, event) -> {
            String Search = etSearch.getText().toString();
            ArrayList<Note> FindNotes = RepoNotes.Notes.stream()
                    .filter(item -> item.text.contains(Search) || item.title.contains(Search))
                    .collect(Collectors.toCollection(ArrayList::new));
            LoadNotes(FindNotes);
            return false;
        });

        LoadNotes(RepoNotes.Notes);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadNotes(RepoNotes.Notes);
    }

    public void LoadNotes(ArrayList<Note> notes) {
        itemsParent.removeAllViews();
        for (int i = 0; i < notes.size(); i++) {
            View item_notes = LayoutInflater.from(this).inflate(R.layout.item_note, itemsParent, false);
            ((TextView) item_notes.findViewById(R.id.tv_title)).setText(notes.get(i).title);
            ((TextView) item_notes.findViewById(R.id.tv_text)).setText(notes.get(i).text);
            ((TextView) item_notes.findViewById(R.id.tv_date)).setText(notes.get(i).date);

            final int position = i;
            item_notes.setOnClickListener(v -> {
                Intent intent = new Intent(this, NoteActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            });
            itemsParent.addView(item_notes);
        }
    }
}