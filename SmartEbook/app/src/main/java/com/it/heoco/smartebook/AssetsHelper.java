package com.it.heoco.smartebook;

import android.annotation.TargetApi;
import android.content.res.AssetManager;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by heoco on 11/04/2017.
 */

public class AssetsHelper {
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static boolean copyAssets(String LOG_TAG, AssetManager assetManager,
                                     File targetFolder) throws Exception {
        Log.i(LOG_TAG, "Copying files from assets to folder " + targetFolder);
        return copyAssets(assetManager, "", targetFolder);
    }

    public static boolean copyAssets(AssetManager assetManager, String path,
                                     File targetFolder) throws Exception {
        String sources[] = assetManager.list(path);
        if (sources.length == 0) { // its not a folder, so its a file:
            copyAssetFileToFolder(assetManager, path, targetFolder);
        } else { // its a folder:
            if (path.startsWith("images") || path.startsWith("sounds") || path.startsWith("webkit")) {
                return false;
            }
            File targetDir = new File(targetFolder, path);
            targetDir.mkdirs();
            for (String source : sources) {
                String fullSourcePath = path.equals("") ? source : (path + File.separator + source);
                copyAssets(assetManager, fullSourcePath, targetFolder);
            }
        }
        return true;
    }

    private static void copyAssetFileToFolder(AssetManager assetManager,
                                              String fullAssetPath, File targetBasePath) throws IOException {
        InputStream in = assetManager.open(fullAssetPath);
        OutputStream out = new FileOutputStream(new File(targetBasePath, fullAssetPath));
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
