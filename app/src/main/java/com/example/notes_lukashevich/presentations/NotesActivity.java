package com.example.notes_lukashevich.presentations;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.notes_lukashevich.R;
import android.widget.Button;

public class NotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        findViewById(R.id.bth_add_notes).setOnClickListener(v -> {
            Intent intent = new Intent(NotesActivity.this, NoteActivity.class);
            startActivity(intent);
        });
    }
}