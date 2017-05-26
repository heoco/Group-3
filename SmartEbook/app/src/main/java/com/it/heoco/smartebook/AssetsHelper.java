package com.it.heoco.smartebook;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by heoco on 26/05/2017.
 */

public class AssetsHelper {
    private String TAG;

    public AssetsHelper(String TAG) {
        this.TAG = TAG;
    }

    public void copyAssets(AssetManager assetManager, File targetFolder) {
        Log.i(TAG, "Copying files from assets to folder " + targetFolder);
        if (!targetFolder.exists()) {
            targetFolder.mkdir();
        }

        try {
            String sources[] = assetManager.list(targetFolder.getName());
            if (sources == null || sources.length == 0) {
                throw new IOException();
            }
            for (String source : sources) {
                String fullSourcePath = targetFolder + File.separator + source;
                File targetDir = new File(targetFolder, source);
                if (!targetDir.exists()) {
                    targetDir.mkdir();
                }
                InputStream in = assetManager.open(fullSourcePath);
                OutputStream out = new FileOutputStream(targetDir.getPath());
                copyAssetFileToFolder(in, out);
            }
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }

    private static void copyAssetFileToFolder(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
        in.close();
        out.flush();
        out.close();
    }
}
