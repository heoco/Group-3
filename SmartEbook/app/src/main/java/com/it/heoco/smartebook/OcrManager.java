package com.it.heoco.smartebook;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;


/**
 * Created by heoco on 11/04/2017.
 */

public class OcrManager {
    private String tessData;
    private TessBaseAPI tessBaseAPI = null;

    public OcrManager(String tessData) {
        this.tessData = tessData + ".traineddata";
        this.tessBaseAPI = new TessBaseAPI();
        String dataPath = OcrApplication.instance.getTessDataParentDirectory();
        this.tessBaseAPI.init(dataPath, tessData);
    }

    public String startRecognize(Bitmap bitmap) {
        this.tessBaseAPI.setImage(bitmap);
        return this.tessBaseAPI.getUTF8Text();
    }
}
