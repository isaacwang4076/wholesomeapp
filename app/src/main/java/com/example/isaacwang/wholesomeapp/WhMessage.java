package com.example.isaacwang.wholesomeapp;

import android.graphics.drawable.Drawable;

/**
 * Created by isaacwang on 10/21/17.
 */

public class WhMessage {

    final boolean outgoing;
    final String message;
    final Drawable picture;

    WhMessage(boolean outgoing, String message, Drawable picture) {
        this.outgoing = outgoing;
        this.message = message;
        this.picture = picture;
    }
}
