package com.example.isaacwang.wholesomeapp;

import android.app.Activity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
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

    public static Socket chatSocket;

    public static void wantToTalk() {
        try {
            chatSocket = IO.socket("http://10.0.2.2:3000");
            chatSocket.connect();
            chatSocket.emit("want_to_talk", "MY_ID");
        } catch(URISyntaxException e) {System.out.println("Failed to connect to socket");}
    }

    public static void downToListen(Emitter.Listener listener) {
        try {
            chatSocket = IO.socket("10.0.2.2:3000");
            chatSocket.connect();
            chatSocket.emit("down_to_listen", "MY_ID");

//            Emitter.Listener onNewTalker = new Emitter.Listener() {
//                @Override
//                public void call(final Object... args) {
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            JSONObject data = (JSONObject) args[0];
//                            String username;
//                            String message;
//                            try {
//                                username = data.getString("username");
//                                message = data.getString("message");
//                            } catch (JSONException e) {
//                                return;
//                            }
//
//                            // add the message to view
//                            addMessage(username, message);
//                        }
//                    });
//                }
//            };
            chatSocket.on("want_to_listen", listener);
        } catch(URISyntaxException e) {}
    }

}
