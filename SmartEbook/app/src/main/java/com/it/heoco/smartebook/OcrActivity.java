package com.it.heoco.smartebook;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by heoco on 11/04/2017.
 */

public class OcrActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String IMAGE_DIRECTORY_NAME = "Smart eBook";
    private static final int GALLERY_REQUEST = 1, CAM_REQUEST = 1313;
    private String tessData;
    private File photoFile;
    private String pathImage;
    private Bitmap bitmap;

    private String[] countryNames = {"English", "Vietnam"};
    private int flags[] = {R.drawable.flag_great_britain, R.drawable.flag_vietnam};
    private String[] tessDatas = {"eng", "vie"};

    ImageButton ibtnCrop, ibtnAccept;
    ImageView imgPreview;
    EditText edtResual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        // ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);

        // Spinner
        Spinner spin = (Spinner) findViewById(R.id.spinLanguage);
        spin.setOnItemSelectedListener(this);
        CustomLanguageAdapter customLanguageAdapter = new CustomLanguageAdapter(getApplicationContext(), flags, countryNames);
        spin.setAdapter(customLanguageAdapter);

        ibtnCrop = (ImageButton) findViewById(R.id.ibtnCrop);
        ibtnAccept = (ImageButton) findViewById(R.id.ibtnAccept);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        edtResual = (EditText) findViewById(R.id.edtResual);

        ibtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OcrManager ocrManager = new OcrManager(tessData);
                try {
                    edtResual.setText(ocrManager.startRecognize(bitmap));
                } catch (Exception ex) {
                    Toast.makeText(OcrActivity.this, "Chọn hình cần scan!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edtResual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_ocr, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.atnGallery: {
                startActivityForResult(FileIntent.photoIntent(this, null, true), GALLERY_REQUEST);
                return true;
            }
            case R.id.atnCamera: {
                try {
                    photoFile = FileIntent.createImageFile(IMAGE_DIRECTORY_NAME);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                startActivityForResult(FileIntent.photoIntent(this, photoFile, false), CAM_REQUEST);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tessData = tessDatas[position];
        Toast.makeText(getApplicationContext(), countryNames[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            pathImage = FileIntent.photoResult(this, photoFile, data);
            switch (requestCode) {
                case GALLERY_REQUEST: {
                    previewImage();
                    break;
                }
                case CAM_REQUEST: {
                    previewImage();
                    break;
                }
            }
            edtResual.setText("");
        }
    }

    private void previewImage() {
        try {
            bitmap = getBitmap(pathImage);
            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmap(String fileName) {
        File image = new File(fileName);
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image.getPath(), bounds);
        if ((bounds.outWidth == -1) || (bounds.outHeight == -1)) {
            return null;
        }
        int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight : bounds.outWidth;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = originalSize / 64;
        return BitmapFactory.decodeFile(image.getPath());
    }
}