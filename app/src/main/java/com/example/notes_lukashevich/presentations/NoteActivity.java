package com.example.notes_lukashevich.presentations;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.notes_lukashevich.R;
import com.example.notes_lukashevich.datas.NotesContext;
import com.example.notes_lukashevich.domains.models.Note;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {

    Note note;
    EditText etTitle, etText;
    TextView tvDate;
    View bthSelectColor, bthBack, bthTrash;
    boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_note);

        bthSelectColor = findViewById(R.id.bth_select_color);
        bthBack = findViewById(R.id.bth_back);
        bthTrash = findViewById(R.id.bth_trash);
        etTitle = findViewById(R.id.et_title);
        etText = findViewById(R.id.et_text);
        tvDate = findViewById(R.id.tv_date);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            int id = arguments.getInt("Id");
            note = NotesContext.GetNoteById(id);
            if (note != null) {
                etTitle.setText(note.title);
                etText.setText(note.text);
                isUpdate = true;
                if (note.color != null) {
                    updateColorButton(note.color);
                }
            }
        } else {
            bthTrash.setVisibility(View.GONE);
        }

        updateDateLabel();

        bthSelectColor.setOnClickListener(v -> showColorPickerDialog());

        bthBack.setOnClickListener(v -> {
            saveNote();
            finish();
        });

        bthTrash.setOnClickListener(v -> {
            if (note != null) {
                NotesContext.Delete(note);
                Toast.makeText(this, "Удалено", Toast.LENGTH_SHORT).show();
            }
            finish();
        });
    }

    private void saveNote() {
        String title = etTitle.getText().toString();
        String text = etText.getText().toString();

        if (!text.trim().isEmpty()) {
            if (note == null) {
                note = new Note();
            }
            note.title = title;
            note.text = text;
            note.date = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy", Locale.getDefault()).format(new Date());

            NotesContext.Save(note, isUpdate);
        }
    }

    private void showColorPickerDialog() {
        final String[] colorNames = {"Синий", "Красный", "Зеленый", "Желтый", "Фиолетовый", "Оранжевый"};
        final String[] colorCodes = {"#302071F9", "#FFCDD2", "#C8E6C9", "#FFF9C4", "#E1BEE7", "#FFE0B2"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите цвет");
        builder.setItems(colorNames, (dialog, which) -> {
            if (note == null) {
                note = new Note();
            }
            note.color = colorCodes[which];
            updateColorButton(note.color);
            saveNote();
            isUpdate = true;
        });
        builder.show();
    }

    private void updateColorButton(String colorHex) {
        if (colorHex != null && !colorHex.isEmpty()) {
            int color = Color.parseColor(colorHex);
            bthSelectColor.setBackgroundTintList(ColorStateList.valueOf(color));
        }
    }

    private void updateDateLabel() {
        String current = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy", Locale.getDefault()).format(new Date());
        tvDate.setText("Отредактировано: " + current);
    }
}