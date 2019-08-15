package com.jamjan.drawing_application;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Utils {

    public static Bitmap mask(Bitmap b1, Bitmap b2) {
        Bitmap result = Bitmap.createBitmap(b1.getWidth(), b1.getHeight(), b1.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(b1, new Matrix(), null);
        canvas.drawBitmap(b2, 0, 0, null);
        return result;
    }
}
