package com.example.cs125finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button pictureBtn = findViewById(R.id.pictureButton);
        Button importBtn = findViewById(R.id.importButton);

        pictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pictureButtonCallback();
            }
        });

        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                importButtonCallback();
            }
        });
    }

    private void pictureButtonCallback() {
        Log.println(Log.INFO, "INFO:", "Starting camera app");
    }

    private void importButtonCallback() {
        Log.println(Log.INFO, "INFO:", "Starting import");
    }
}
