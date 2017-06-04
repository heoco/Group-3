package com.it.heoco.smartebook.activities;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.it.heoco.smartebook.FileIntent;
import com.it.heoco.smartebook.OcrManager;
import com.it.heoco.smartebook.R;
import com.it.heoco.smartebook.lists.SpinnerLangItem;
import com.it.heoco.smartebook.lists.TitleLanguageAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by heoco on 11/04/2017.
 */

public class OcrActivity extends AppCompatActivity {
    private static final String IMAGE_DIRECTORY_NAME = MainActivity.APP_DIRECTORY;
    private static final int GALLERY_REQUEST = 1, CAM_REQUEST = 1313;
    private String _tessData;
    private String[] _tessDatas;
    private File _photoFile;
    private String _pathImage;
    private Bitmap _bitmap;

    LinearLayout tabImage, tabText;
    ImageButton ibtnCrop, ibtnAccept;
    ImageView imgPreview;
    EditText edtResual;

    // action bar
    private ActionBar actionBar;

    // Title language Spinner data
    private ArrayList<SpinnerLangItem> langSpinners;

    // Spinner
    Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        // View
        tabImage = (LinearLayout) findViewById(R.id.tabImage);
        tabText = (LinearLayout) findViewById(R.id.tabText);
        ibtnCrop = (ImageButton) findViewById(R.id.ibtnCrop);
        ibtnAccept = (ImageButton) findViewById(R.id.ibtnAccept);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        edtResual = (EditText) findViewById(R.id.edtResual);
        mSpinner = (Spinner) findViewById(R.id.spinLanguage);

        // Toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            actionBar = getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
        }

        // Spinner
        langSpinners = getLangSpinner();
        final TitleLanguageAdapter titleLanguageAdapter = new TitleLanguageAdapter(getApplicationContext(), langSpinners);
        mSpinner.setAdapter(titleLanguageAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _tessData = _tessDatas[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ibtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OcrManager ocrManager = new OcrManager(_tessData);
                try {
                    edtResual.setText(ocrManager.startRecognize(_bitmap));
                    tabImage.setVisibility(View.INVISIBLE);
                    tabText.setVisibility(View.VISIBLE);
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

    private ArrayList<SpinnerLangItem> getLangSpinner() {
        _tessDatas = new String[] {"eng", "vie"};
        int[] flags = { R.drawable.flag_great_britain, R.drawable.flag_vietnam };
        String[] countryNames = { "English", "VietNam" };

        ArrayList<SpinnerLangItem> spinnerLangItems = new ArrayList<SpinnerLangItem>();
        for (int i = 0; i < flags.length; i++) {
            SpinnerLangItem spinnerLangItem = new SpinnerLangItem(flags[i], countryNames[i]);
            spinnerLangItems.add(spinnerLangItem);
        }
        return spinnerLangItems;
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
                    _photoFile = FileIntent.createImageFile(IMAGE_DIRECTORY_NAME);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                startActivityForResult(FileIntent.photoIntent(this, _photoFile, false), CAM_REQUEST);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            _pathImage = FileIntent.photoResult(this, _photoFile, data);
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
            _bitmap = getBitmap(_pathImage);
            imgPreview.setImageBitmap(_bitmap);
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