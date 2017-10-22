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
    View getView(Context context);
}

class ConversationFeedItem implements FeedItem {

    static final int TYPE = 1;

    public final String listener;
    public final int type = TYPE;

    ConversationFeedItem(String listener) {
        this.listener = listener;
    }
    ConversationFeedItem() {this.listener = "";}

    public View getView(Context context) {
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

    public int getType() {
        return TYPE;
    }
}

class StoryFeedItem implements FeedItem {
    static final int TYPE = 2;

    public final String name;
    public final String story;
    public final int type = TYPE;

    StoryFeedItem(String name, String story) {
        if (name == null) {
            this.name = Utilities.ANONYMOUS;
        } else {
            this.name = name;
        }
        this.story = story;
    }

    public View getView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.story_feed_item, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 30, 0, 0);
        v.setLayoutParams(params);

        TextView posterNameTextView = (TextView) v.findViewById(R.id.posterNameTextView);
        posterNameTextView.setText(name);

        TextView storyTextView = (TextView) v.findViewById(R.id.storyTextView);
        storyTextView.setText(story);

        return v;
    }
}