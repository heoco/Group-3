package com.it.heoco.smartebook;

import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fabAdd, fabDocuments, fabOcr;
    CoordinatorLayout moreLayout;
    boolean fabClick;
    private static final int DCM_REQUEST = 1,
            OCR_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        //actionBar.setLogo(R.drawable...);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        fabDocuments = (FloatingActionButton) findViewById(R.id.fabDocuments);
        fabOcr = (FloatingActionButton) findViewById(R.id.fabOcr);
        moreLayout = (CoordinatorLayout) findViewById(R.id.moreLayout);

        fabClick = true;
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreLayout.setVisibility(fabClick ? View.VISIBLE : View.GONE);
                fabClick = !fabClick;
            }
        });

        fabDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fabOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), OcrActivity.class);
                startActivity(intent);
            }
        });
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case DCM_REQUEST: {

                finish();
                break;
            }
            case OCR_REQUEST: {
                path = getIntent().getStringExtra("text");
                finish();
                break;
            }
        }
    }*/
}