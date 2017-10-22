package com.example.isaacwang.wholesomeapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * Created by isaacwang on 10/21/17.
 */

public class Utilities {

    static final Bitmap getScaledAndRotatedBitmap(Bitmap original, int width, int height) {
        Matrix matrix = new Matrix();

        matrix.postRotate(90);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(original, width, height, true);

        return Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
    }
}
