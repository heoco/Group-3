package com.it.heoco.smartebook;

import android.app.Application;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;

/**
 * Created by heoco on 11/04/2017.
 */

public class MainApplication extends Application {
    public static MainApplication instance = null;
    private static final String TAG = MainApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        copyAssets();
    }

    public String getAppDirectory() {
        return MainApplication.instance.getExternalFilesDir(null).getAbsolutePath();
    }

    private void copyAssets() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                AssetManager assetManager = MainApplication.instance.getAssets();
                File tessFolder = new File(getAppDirectory());
                try {
                    AssetsHelper.copyAssets(TAG, assetManager, tessFolder);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }
        };
        new Thread(run).start();
    }
}
