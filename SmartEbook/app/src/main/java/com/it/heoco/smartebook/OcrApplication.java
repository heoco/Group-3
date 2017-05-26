package com.it.heoco.smartebook;

import android.app.Application;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by heoco on 11/04/2017.
 */

public class OcrApplication extends Application {
    public static OcrApplication instance = null;
    private static final String TAG = OcrApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        copyTessDataForTextRecognizor();
    }

    private String tessDataPath()
    {
        return OcrApplication.instance.getExternalFilesDir(null) + File.separator + "tessdata";
    }

    public String getTessDataParentDirectory() {
        return OcrApplication.instance.getExternalFilesDir(null).getAbsolutePath();
    }

    private void copyTessDataForTextRecognizor() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                AssetsHelper assetsHelper = new AssetsHelper(TAG);
                AssetManager assetManager = OcrApplication.instance.getAssets();
                File tessFolder = new File(tessDataPath());
                assetsHelper.copyAssets(assetManager, tessFolder);
            }
        };
        new Thread(run).start();
    }
}
