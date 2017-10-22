package com.example.isaacwang.wholesomeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Set;
import java.util.UUID;

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
        params.setMargins(0, 0, 0, 30);
        v.setLayoutParams(params);

        ImageView imageView = (ImageView) v.findViewById(R.id.listenerImageView);
//        imageView.setImageResource(R.drawable.prof_pic);
        imageView.setImageResource(Utilities.getProfResource(listener));

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
    public int likes;
    public String ID = UUID.randomUUID().toString();

    StoryFeedItem(String name, String story, int likes) {
        if (name == null) {
            this.name = Utilities.ANONYMOUS;
        } else {
            this.name = name;
        }
        this.story = story;
        this.likes = likes;
    }

    StoryFeedItem() {
        this.name = Utilities.ANONYMOUS;
        this.story = "lel";
        this.likes = 0;
    }

    public View getView(final Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.story_feed_item, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 30);
        v.setLayoutParams(params);

        TextView posterNameTextView = (TextView) v.findViewById(R.id.posterNameTextView);
        posterNameTextView.setText(name);

        TextView storyTextView = (TextView) v.findViewById(R.id.storyTextView);
        storyTextView.setText(story);

        final TextView numLikes = (TextView) v.findViewById(R.id.numLikes);
        numLikes.setText(String.valueOf(likes));

        final Button likeButton = (Button) v.findViewById(R.id.likeButton);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeButton.setBackground(context.getDrawable(R.drawable.like_full));
                Network.bumpItUpBois(ID);
                numLikes.setText(String.valueOf(Integer.valueOf(numLikes.getText().toString()) + 1));
            }
        });


        return v;
    }
}