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

    public String getTessDataParentDirectory() {
        return OcrApplication.instance.getExternalFilesDir(null).getAbsolutePath();
    }

    private void copyTessDataForTextRecognizor() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                AssetManager assetManager = OcrApplication.instance.getAssets();
                File tessFolder = new File(getTessDataParentDirectory());
                copyAssets(assetManager, "tessdata", tessFolder);
            }
        };
        new Thread(run).start();
    }

    private boolean copyAssets(AssetManager assetManager, String path, File targetFolder) throws Exception {
        Log.i(TAG, "Copying " + path + " to " + targetFolder);
        String sources[] = assetManager.list(path);
        if (sources.length == 0) {
            copyAssetFileToFolder(assetManager, path, targetFolder);
        } else {
            if (path.startsWith("images") || path.startsWith("sounds")
                    || path.startsWith("webkit")) {
                Log.i(TAG, " > Skipping " + path);
                return false;
            }

            File targetDir = new File(targetFolder, path);
            if (!targetDir.exists()) {
                targetDir.mkdir();
            }
            InputStream in = null;
            OutputStream out = null;
            for (String source : sources) {
                String fullSourcePath = path.equals("") ? source : (path
                        + File.separator + source);
                copyAssets(assetManager, fullSourcePath, targetFolder);
            }
        }
        return true;
    }

    private static void copyAssetFileToFolder(AssetManager assetManager, String fullAssetPath, File targetBasePath) throws IOException {
        InputStream in = assetManager.open(fullAssetPath);
        OutputStream out = new FileOutputStream(new File(targetBasePath,
                fullAssetPath));
        byte[] buffer = new byte[16 * 1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();
        out.flush();
        out.close();
    }
}
