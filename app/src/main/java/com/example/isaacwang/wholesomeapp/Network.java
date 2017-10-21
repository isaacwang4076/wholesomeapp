package com.example.isaacwang.wholesomeapp;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by ericzhang on 10/21/17.
 */

public class Network {
    public static DatabaseReference rootDatabase = FirebaseDatabase.getInstance().getReference();
    public static DatabaseReference feedDatabase = rootDatabase.child("FeedDatabase");

    // temp, will move to activity controller
    public static void getFeed() {
        feedDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<FeedItem> feedList = new ArrayList<FeedItem>();
                for (DataSnapshot feedSnapshot : dataSnapshot.getChildren()) {
                    FeedItem feedItem = feedSnapshot.getValue(ConversationFeedItem.class);
                    feedList.add(feedItem);
                }
                System.out.println("Feed list: " + feedList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public static void addToFeed(FeedItem feedItem) {
        System.out.println("FEED ITEM: " + feedItem);
        feedDatabase.push().setValue(feedItem);
    }
}
