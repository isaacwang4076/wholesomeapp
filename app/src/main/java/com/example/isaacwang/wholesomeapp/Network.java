package com.example.isaacwang.wholesomeapp;

import android.app.Activity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by ericzhang on 10/21/17.
 */

public class Network {
    public static final String serverURL = "http://8606eebb.ngrok.io";
    public static final DatabaseReference rootDatabase = FirebaseDatabase.getInstance().getReference();
    //public static final DatabaseReference feedDatabase = rootDatabase.child("FeedDatabase");
    public static final DatabaseReference feedDatabase = rootDatabase.child("FeedDatabaseIsaac");



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

    public static void bumpItUpBois(final String postId) {
        feedDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot feedSnapshot : dataSnapshot.getChildren()) {
                    if (feedSnapshot.hasChild("ID") && feedSnapshot.child("ID").getValue() == postId) {
                        feedSnapshot.child("likes").getRef().runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData) {
                                Long value = mutableData.getValue(Long.class);
                                if (value == null) {
                                    mutableData.setValue(1);
                                }
                                else {
                                    mutableData.setValue(value + 1);
                                }

                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static Socket chatSocket;
    static {
        try {
            chatSocket = IO.socket(serverURL);
            chatSocket.connect();
        } catch(URISyntaxException e) {System.out.println("Failed to connect to socket");}
    }

    public static void wantToTalk() {
        if (chatSocket != null) {
            chatSocket.emit("want_to_talk", "MY_ID");
        }
    }

    public static void downToListen(Emitter.Listener listener) {
        if (chatSocket != null) {
            chatSocket.emit("down_to_listen", "MY_ID");
            chatSocket.on("want_to_talk", listener);
            System.out.println("READY TO LISTEN");
        }
    }

    public static void stopDownToListen() {
        if (chatSocket != null) {
            chatSocket.off("down_to_listen");
        }
    }

}
