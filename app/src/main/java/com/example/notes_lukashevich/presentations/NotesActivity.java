package com.example.notes_lukashevich.presentations;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.notes_lukashevich.R;
import com.example.notes_lukashevich.datas.DbContext;
import com.example.notes_lukashevich.datas.NotesContext;
import com.example.notes_lukashevich.domains.models.Note;
import com.example.notes_lukashevich.utils.FavoriteManager;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class NotesActivity extends AppCompatActivity {

    GridLayout itemsParent;
    View bthAddNotes;
    EditText etSearch;
    ImageView ivFilterFavorite;
    DbContext dbContext;
    boolean showOnlyFavorites = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_notes);

        bthAddNotes = findViewById(R.id.bth_add_notes);
        itemsParent = findViewById(R.id.gl_notes);
        etSearch = findViewById(R.id.et_search);
        ivFilterFavorite = findViewById(R.id.iv_filter_favorite);

        FavoriteManager.init(this);
        showOnlyFavorites = FavoriteManager.isShowOnlyFavorites();
        updateFilterIcon();

        ivFilterFavorite.setOnClickListener(v -> {
            showOnlyFavorites = !showOnlyFavorites;
            FavoriteManager.setShowOnlyFavorites(showOnlyFavorites);
            updateFilterIcon();
            loadFilteredNotes();
        });

        bthAddNotes.setOnClickListener(v -> {
            Intent intent = new Intent(this, NoteActivity.class);
            startActivity(intent);
        });

        etSearch.setOnKeyListener(SearchListener);

        dbContext = new DbContext(this);
        loadFilteredNotes();
    }

    private void updateFilterIcon() {
        if (showOnlyFavorites) {
            ivFilterFavorite.setImageResource(R.drawable.ic_star_filled);
            ivFilterFavorite.setColorFilter(Color.YELLOW);
        } else {
            ivFilterFavorite.setImageResource(R.drawable.ic_star_outline);
            ivFilterFavorite.setColorFilter(Color.GRAY);
        }
    }

    private void loadFilteredNotes() {
        ArrayList<Note> allNotes = NotesContext.AllNotes();
        if (showOnlyFavorites) {
            allNotes = allNotes.stream().filter(n -> n.isFavorite == 1).collect(Collectors.toCollection(ArrayList::new));
        }
        LoadNotes(allNotes);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFilteredNotes();
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

            ImageView ivFavorite = item_notes.findViewById(R.id.iv_favorite);
            if (currentNote.isFavorite == 1) {
                ivFavorite.setImageResource(R.drawable.ic_star_filled);
                ivFavorite.setColorFilter(Color.YELLOW);
            } else {
                ivFavorite.setImageResource(R.drawable.ic_star_outline);
                ivFavorite.setColorFilter(Color.GRAY);
            }

            ivFavorite.setOnClickListener(v -> {
                int newState = (currentNote.isFavorite == 1) ? 0 : 1;
                NotesContext.toggleFavorite(currentNote.id, newState);
                currentNote.isFavorite = newState;
                loadFilteredNotes();
                Toast.makeText(this, newState == 1 ? "Добавлено в избранное" : "Удалено из избранного", Toast.LENGTH_SHORT).show();
            });

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

        if (showOnlyFavorites) {
            findNotes = findNotes.stream().filter(n -> n.isFavorite == 1).collect(Collectors.toCollection(ArrayList::new));
        }
        LoadNotes(findNotes);
        return false;
    };
}