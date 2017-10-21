package com.example.isaacwang.wholesomeapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout feedLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.appBar);
        myToolbar.setTitle(getResources().getString(R.string.app_name));
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(myToolbar);

        feedLayout = (LinearLayout) findViewById(R.id.feedLayout);

        FeedItem testFeedItem1 = new ConversationFeedItem("Jason");
        FeedItem testFeedItem2 = new ConversationFeedItem("Alvin");
        FeedItem testFeedItem3 = new ConversationFeedItem("Rick");
        initializeFeed(Arrays.asList(testFeedItem1, testFeedItem2, testFeedItem3));

        Network.addToFeed(testFeedItem1);
        Network.addToFeed(testFeedItem2);
        Network.addToFeed(testFeedItem3);

        Network.feedDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<FeedItem> feedList = new ArrayList<FeedItem>();
                for (DataSnapshot feedSnapshot : dataSnapshot.getChildren()) {
                    FeedItem feedItem = feedSnapshot.getValue(ConversationFeedItem.class);
                    feedList.add(feedItem);
                }
                System.out.println("Feed list: " + feedList);
                initializeFeed(feedList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void initializeFeed(List<FeedItem> feedItemList) {
        for (FeedItem feedItem: feedItemList) {
            feedLayout.addView(feedItem.getView(this, feedLayout));
        }
    }

    public void startChat(View v) {
        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra("partner_name", "Jason");
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


}
