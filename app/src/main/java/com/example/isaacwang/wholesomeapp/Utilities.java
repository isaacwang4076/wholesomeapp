package com.example.isaacwang.wholesomeapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.content.res.Resources;

import com.koushikdutta.async.Util;

/**
 * Created by isaacwang on 10/21/17.
 */

public class Utilities {

    static final String ANONYMOUS = "Anonymous";

    static final Bitmap getScaledAndRotatedBitmap(Bitmap original, int width, int height) {
        Matrix matrix = new Matrix();

        matrix.postRotate(90);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(original, width, height, true);

        return Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
    }

    static Bitmap getProfPic(Context context, String id) {
        return ((BitmapDrawable) context.getDrawable(Utilities.getProfResource(id))).getBitmap();
    }

    static int getProfResource(String id) {
        if (id.equals("Gates")) {
            return R.drawable.gates_profile_pic;
        }
        else if (id.equals("Eric")) {
            return R.drawable.eric_profile_pic;
        }
        else if (id.equals("Jeremy")) {
            return R.drawable.jeremy_profile_pic;
        }
        return R.drawable.prof_pic;
    }
}
