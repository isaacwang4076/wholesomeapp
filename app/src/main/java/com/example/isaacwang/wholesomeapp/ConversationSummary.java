package com.example.isaacwang.wholesomeapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by isaacwang on 10/21/17.
 */

public class ConversationSummary {

    private static final String ANONYMOUS = "Anonymous";

    private final Drawable otherPhoto;
    private final String otherName;
    private final String lastMessage;

    ConversationSummary(Drawable otherPhoto, String otherName, String lastMessage) {
        this.otherPhoto = otherPhoto;
        if (otherName != null) {
            this.otherName = otherName;
        } else {
            this.otherName = ANONYMOUS;
        }
        this.lastMessage = lastMessage;
    }

    View getView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.conversation_summary, null);
        TextView name = (TextView) v.findViewById(R.id.nameTextView);
        TextView lastMsg = (TextView) v.findViewById(R.id.messageTextView);
        ImageView pic = (ImageView) v.findViewById(R.id.convImageView);

        name.setText(this.otherName);
        lastMsg.setText(this.lastMessage);
        pic.setImageDrawable(otherPhoto);

        return v;
    }
}
