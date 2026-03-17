package com.example.notes_lukashevich.presentations;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.notes_lukashevich.R;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        ImageView btnBack = findViewById(R.id.bth_back);
        btnBack.setOnClickListener(v -> finish());
    }
}