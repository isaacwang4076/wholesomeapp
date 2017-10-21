package com.example.isaacwang.wholesomeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by isaacwang on 10/21/17.
 */

interface FeedItem {
    View getView(Context context, ViewGroup rootViewGroup);
}

class ConversationFeedItem implements FeedItem {

    private final String listener;

    ConversationFeedItem(String listener) {
        this.listener = listener;
    }

    public View getView(Context context, ViewGroup rootViewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.conversation_feed_item, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 30, 0, 0);
        v.setLayoutParams(params);

        ImageView imageView = (ImageView) v.findViewById(R.id.listenerImageView);
        imageView.setImageResource(R.drawable.prof_pic);

        TextView listenerTextView = (TextView) v.findViewById(R.id.listenerTextView);
        listenerTextView.setText(listener);

        return v;
    }
}