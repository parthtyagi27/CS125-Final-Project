package com.example.cs125finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.text.TextRecognizer;

public class MainActivity extends AppCompatActivity {
    public static TextRecognizer textRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button pictureBtn = findViewById(R.id.pictureButton);
        Button importBtn = findViewById(R.id.importButton);

        textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

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
//        Log.println(Log.INFO, "INFO:", "Starting camera app");
//        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
//        if (!textRecognizer.isOperational()) {
//            System.out.println("Failed to init OCR module");
//            return;
//        }
//        IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
//        boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;
//
//        if (hasLowStorage) {
//            System.out.println("Failed to init OCR module due to low storage");
//            return;
//        }
//
//        CameraSource cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
//                .setFacing(CameraSource.CAMERA_FACING_BACK)
//                .setRequestedPreviewSize(1280, 1024)
//                .setRequestedFps(15.0f)
//                .build();
        Intent intent = new Intent(this, Camera.class);
        startActivity(intent);
    }

    private void importButtonCallback() {
        Log.println(Log.INFO, "INFO:", "Starting import");
        startActivity(new Intent(this, LoadImage.class));
    }
}
