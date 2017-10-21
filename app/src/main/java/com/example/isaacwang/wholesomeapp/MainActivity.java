package com.example.isaacwang.wholesomeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout feedLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        feedLayout = (LinearLayout) findViewById(R.id.feedLayout);

        FeedItem testFeedItem1 = new ConversationFeedItem("Jason");
        FeedItem testFeedItem2 = new ConversationFeedItem("Alvin");
        FeedItem testFeedItem3 = new ConversationFeedItem("Rick");
        initializeFeed(Arrays.asList(testFeedItem1, testFeedItem2, testFeedItem3));
        startActivity(new Intent(this, ChatActivity.class));
    }

    private void initializeFeed(List<FeedItem> feedItemList) {
        for (FeedItem feedItem: feedItemList) {
            feedLayout.addView(feedItem.getView(this, feedLayout));
        }
    }

    public void startChat(View v) {
        startActivity(new Intent(this, ChatActivity.class));
    }
}
