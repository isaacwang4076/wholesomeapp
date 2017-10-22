package com.example.isaacwang.wholesomeapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final boolean TEMP_LISTENING = true;
    private LinearLayout feedLayout;
    private Dialog listenerDialog;

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

        Button listenButton = (Button) findViewById(R.id.listenButton);
        if (TEMP_LISTENING) {
            listenButton.setText("Stop listening");
        }

        showTalkRequestDialog("lmao");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
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


    /******************************************************************************************************/
    /*************************************** REGISTERING AS LISTENER **************************************/
    /******************************************************************************************************/

    public void listenButtonOnClick(View v) {
        if (TEMP_LISTENING) {
            showStopListeningDialog();
        } else {
            showStartListeningDialog();
        }
    }

    private void showStartListeningDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.listen_dialog, null))
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // get the info entered
                        EditText et = (EditText) listenerDialog.findViewById(R.id.nameEditText);
                        String name = et.getText().toString();

                        ImageView iv = (ImageView) listenerDialog.findViewById(R.id.photoImageView);
                        Bitmap photo = ((BitmapDrawable) iv.getDrawable()).getBitmap();

                        // add the listener
                        addListener(name, photo);

                        // show a toast
                        Toast.makeText(getApplicationContext(), "You are now a listener!", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}});

        // show the dialog
        Dialog dialog = builder.create();
        dialog.show();

        listenerDialog = dialog;
    }

    /**
     * On-Click method to browse for photos.
     */
    public void addPhoto(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    /**
     * After browsing for photos, set the image view to the rotated bitmap.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            try {
                Bitmap originalBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                Bitmap rotatedBitmap = Utilities.getScaledAndRotatedBitmap(originalBitmap, 450, 450);

                ImageView iv = (ImageView) listenerDialog.findViewById(R.id.photoImageView);
                iv.setImageBitmap(rotatedBitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void showStopListeningDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.stop_listen_dialog, null))
                .setPositiveButton("Stop listening", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // add the listener
                        removeListener();

                        // show a toast
                        Toast.makeText(getApplicationContext(), "You are no longer a listener.", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}});

        Dialog dialog = builder.create();
        dialog.show();
    }



    // TODO: Eric - this user with this name and this photo has registered as a listener
    private void addListener(String name, Bitmap photo) {}

    // TODO: Eric - this user has unregistered as a listener
    private void removeListener() {}

    /******************************************************************************************************/
    /************************************* RESPONDING TO TALK REQUEST *************************************/
    /******************************************************************************************************/

    private void showTalkRequestDialog(final String talkerId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.talk_request_dialog, null))
                .setPositiveButton("Yes, I'm free", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        joinConversation(talkerId);
                    }
                })
                .setNegativeButton("No, not now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        declineConversation(talkerId);
                    }});

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void joinConversation(String talkerId) {}

    private void declineConversation(String talkerId) {}

}