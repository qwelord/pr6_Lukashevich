package com.example.notes_lukashevich.presentations;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notes_lukashevich.R;
import com.example.notes_lukashevich.datas.RepoNotes;
import com.example.notes_lukashevich.domains.models.Note;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteActivity extends AppCompatActivity {

    Note note;
    EditText etTitle, etText;
    TextView tvDate;
    View bthSelectColor, bthBack, bthTrash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Date DateNow = new Date();
        SimpleDateFormat FormatForDateNow = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");

        bthSelectColor = findViewById(R.id.bth_select_color);
        bthBack = findViewById(R.id.bth_back);
        bthTrash = findViewById(R.id.bth_trash);
        etTitle = findViewById(R.id.et_title);
        etText = findViewById(R.id.et_text);
        tvDate = findViewById(R.id.tv_date);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            int Position = arguments.getInt("position");
            note = RepoNotes.Notes.get(Position);
            etTitle.setText(note.title);
            etText.setText(note.text);
        } else {
            bthTrash.setVisibility(View.GONE);
        }

        tvDate.setText("Отредактировано: " + FormatForDateNow.format(DateNow));

        bthSelectColor.setOnClickListener(v -> {
            Toast.makeText(this, "Выбор цвета недоступен", Toast.LENGTH_SHORT).show();
        });

        bthBack.setOnClickListener(v -> {
            String Title = etTitle.getText().toString();
            String Text = etText.getText().toString();

            if (Text.replace(" ", "").replace("\r", "").replace("\n", "").isEmpty()) {
                Toast.makeText(this, "Не чего сохранять", Toast.LENGTH_SHORT).show();
            } else {
                if (note == null) {
                    note = new Note();
                    RepoNotes.Notes.add(note);
                }
                note.title = Title;
                note.text = Text;
                note.date = FormatForDateNow.format(DateNow);
            }
            finish();
        });

        bthTrash.setOnClickListener(v -> {
            RepoNotes.Notes.remove(note);
            finish();
            Toast.makeText(this, "Заметка удалена", Toast.LENGTH_SHORT).show();
        });
    }
}