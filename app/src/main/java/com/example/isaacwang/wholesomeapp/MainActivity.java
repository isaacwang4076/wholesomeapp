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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean listening  = false;
    private LinearLayout feedLayout;
    private TextView peopleHelpedTextView;
    private Button listenButton;
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
        peopleHelpedTextView = (TextView) findViewById(R.id.peopleHelpedTextView);

        // attach FeedDatabase listener
        final Context context = this;
        Network.feedDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                peopleHelpedTextView.setText(feedLayout.getChildCount() + " people were heard today.");
                peopleHelpedTextView.setVisibility(View.VISIBLE);

                FeedItem feedItem = null;
                if ((Long) dataSnapshot.child("type").getValue() == ConversationFeedItem.TYPE) {
                    feedItem = dataSnapshot.getValue(ConversationFeedItem.class);
                }
                else if ((Long) dataSnapshot.child("type").getValue() == StoryFeedItem.TYPE) {
                    feedItem = dataSnapshot.getValue(StoryFeedItem.class);
                }
                feedLayout.addView(feedItem.getView(context), 0);

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        listenButton = (Button) findViewById(R.id.listenButton);
        if (listening) {
            listenButton.setText("Stop listening");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_conversations:
                goToConversations();
                return true;
            case R.id.menu_item_story:
                goToStoryActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToConversations() {
        Intent i = new Intent(this, ConversationsActivity.class);
        startActivity(i);
    }

    private void goToStoryActivity() {
        Intent i = new Intent(this, StoryActivity.class);
        startActivity(i);
    }

    public void startChat(View v) {
        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra("is_talker", true);
        startActivity(i);

    }


    /******************************************************************************************************/
    /*************************************** REGISTERING AS LISTENER **************************************/
    /******************************************************************************************************/

    public void listenButtonOnClick(View v) {
        if (listening) {
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
                        Bitmap photo = iv.getDrawable() != null ? ((BitmapDrawable) iv.getDrawable()).getBitmap() : null;

                        // add the listener
                        addListener(name, photo);

                        // show a toast
                        Toast.makeText(getApplicationContext(), "You are now a listener!", Toast.LENGTH_LONG).show();
                        listening = true;
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
                        listening = false;
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}});

        Dialog dialog = builder.create();
        dialog.show();
    }



    // TODO: Eric - this user with this name and this photo has registered as a listener
    private void addListener(final String name, Bitmap photo) {
        Emitter.Listener onNewTalker = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                System.out.println("received want_to_talk");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("name: " + args[0]);
                        showTalkRequestDialog((String) args[0], name);
                    }
                });
            }
        };

        Network.downToListen(onNewTalker);
        listenButton.setText("Stop listening");
    }

    // TODO: Eric - this user has unregistered as a listener
    private void removeListener() {
        Network.stopDownToListen();
        listenButton.setText("I want to listen");
    }

    /******************************************************************************************************/
    /************************************* RESPONDING TO TALK REQUEST *************************************/
    /******************************************************************************************************/

    private void showTalkRequestDialog(final String talkerId, final String listenerId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.talk_request_dialog, null))
                .setPositiveButton("Yes, I'm free", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Network.confirmTalk(talkerId, listenerId); // emit confirm_talk with talkerId and listenerId
                        Network.stopDownToListen(); // rm listener so no more alerts on future want_to_talk's
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

    // only for listener
    private void joinConversation(String talkerId) {
        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra("partner_name", "Anonymous");
        startActivity(i);
    }

    private void declineConversation(String talkerId) {
        // nothing happens lel
    }

}