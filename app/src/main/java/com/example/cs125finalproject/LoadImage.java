package com.example.cs125finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.InputType;
import android.util.Patterns;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoadImage extends AppCompatActivity {

    private Button loadImageBtn, processImageBtn;
    private ImageView inputImage;
    private EditText outputText;
    private Bitmap image = null;

    private int RESULT = 27;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image);
        loadImageBtn = findViewById(R.id.loadImageBtn);
        processImageBtn = findViewById(R.id.processImageBtn);
        inputImage = findViewById(R.id.inputImage);
        outputText = findViewById(R.id.outputText);

        outputText.setFocusable(true);
        outputText.setFocusableInTouchMode(true);
        outputText.setInputType(InputType.TYPE_NULL);

        loadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImage();
            }
        });

        processImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image == null) {
                    //Alert the user
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoadImage.this);
                    builder.setTitle("Error");
                    builder.setMessage("Please load an image before processing");
                    builder.setCancelable(true);
                    builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // Process the image
                    TextRecognizer textRecognizer = MainActivity.textRecognizer;
                    if (!textRecognizer.isOperational()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoadImage.this);
                        builder.setTitle("Error");
                        builder.setMessage("Please wait for Vision API to load");
                        builder.setCancelable(true);
                        builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        Frame frame = new Frame.Builder().setBitmap(image).build();
                        SparseArray items = textRecognizer.detect(frame);
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < items.size(); i++) {
                            TextBlock textBlock = (TextBlock) items.valueAt(i);
                            stringBuilder.append(textBlock.getValue());
                            System.out.println("Found text block = " + textBlock.getValue());
                            stringBuilder.append("\n");
                        }
                        outputText.setText("", TextView.BufferType.NORMAL);
//                        outputText.setText(stringBuilder.toString(), TextView.BufferType.NORMAL);
                        List<String> links = parseText(stringBuilder.toString());
                        for (String link : links) {
                            outputText.setText(outputText.getText() + "\n" + Html.fromHtml(link, Html.FROM_HTML_MODE_COMPACT), TextView.BufferType.NORMAL);
                        }
                    }
                }
            }
        });
    }

    private void loadImage() {
        Intent imageIntent = new Intent();
        imageIntent.setType("image/*");
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(imageIntent, "Select Picture"), RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                InputStream stream = getContentResolver().openInputStream(selectedImage);
                image = BitmapFactory.decodeStream(stream);
                inputImage.setImageBitmap(image);
            } catch (Exception ex) {
                System.err.println("Failed to load image");
                ex.printStackTrace();
            }
        }
    }

    private List<String> parseText(String text) {
        if (text == null || text.length() == 0) {
            return null;
        }

        List<String> links = new ArrayList<>();
        String urlRegex = "\\b(?:(?:https?|ftp|file)://|www\\.|ftp\\.)[-A-Z0-9+&@#/%=~_|$?!:,.]*[A-Z0-9+&@#/%=~_|$]\n";
//        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Pattern pattern = Patterns.WEB_URL;
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            String link = new String();
            link = text.substring(urlMatcher.start(0), urlMatcher.end(0));
            System.out.println("Found link: " + link);
            links.add("<a href='" + link + "'>" + link + "</a>");
        }
        return links;
    }
}
