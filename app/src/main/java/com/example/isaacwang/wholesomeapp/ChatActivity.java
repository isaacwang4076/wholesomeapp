package com.example.isaacwang.wholesomeapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    
    private static final int TINY_GAP = 15;
    private Message chatBotGreeting;


    LinearLayout messagesLayout;

    LayoutInflater inflater;
    LinearLayout.LayoutParams incomingMessageParams;
    LinearLayout.LayoutParams outgoingMessageParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.appBar);
        myToolbar.setTitle(getIntent().getStringExtra("partner_name"));
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        messagesLayout = (LinearLayout) findViewById(R.id.messagesLayout);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        incomingMessageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        incomingMessageParams.setMargins(0, TINY_GAP, 0, 0);

        outgoingMessageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        outgoingMessageParams.gravity = Gravity.RIGHT;
        outgoingMessageParams.setMargins(0, TINY_GAP, TINY_GAP, 0);

//        Message m1 = new Message(true, "hey man this is me", null);
////        Message m2 = new Message(true, "how you been", null);
//        Message m3 = new Message(false, "not bad", getDrawable(R.drawable.prof_pic));
////        Message m4 = new Message(false, "mostly j chillin haha. Been pretty caught up by school work esketiiiiiit. you know, same old same old this is a long message.", getDrawable(R.drawable.prof_pic));
////        Message m5 = new Message(true, "aight good to know", null);
//
        chatBotGreeting = new Message(false, "Hey there! I'm Gates, Toomi's chat bot. " +
                "We're looking for a 'real' person for you to talk to. Don't worry, you'll have complete anonymity. " +
                "In the meantime, feel free to tell me what's on your mind!",
                getDrawable(R.drawable.cutebot));

       initializeMessages(new ArrayList<Message>());
//
//        showTalkRequestDialog("Yams", ((BitmapDrawable) getDrawable(R.drawable.prof_pic)).getBitmap());
    }

    private void initializeMessages(List<Message> priorMessages) {
        if (priorMessages.isEmpty()) {
            addMessage(chatBotGreeting);
        }
        for (Message msg: priorMessages) {
            addMessage(msg);
        }
    }

    private void addMessage(Message msg) {
        View messageContainer;

        if (msg.outgoing) {
            messageContainer = inflater.inflate(R.layout.outgoing_message_container, null);
            messageContainer.setLayoutParams(outgoingMessageParams);
        } else {
            messageContainer = inflater.inflate(R.layout.incoming_message_container, null);
            messageContainer.setLayoutParams(incomingMessageParams);

            ImageView iv = (ImageView) messageContainer.findViewById(R.id.chatterImageView);
            iv.setImageDrawable(msg.picture);
        }

        View messageView = messageContainer.findViewById(R.id.messageView);
        TextView messageTextView = (TextView) messageView.findViewById(R.id.messageTextView);
        messageTextView.setText(msg.message);

        messagesLayout.addView(messageContainer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);
        return true;
    }

    private void showTalkRequestDialog(String name, Bitmap photo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(R.layout.talk_confirmation_dialog, null);

        TextView tv = (TextView) dialogView.findViewById(R.id.confirmationTextView);
        tv.setText(name + " is free to listen.");

        ImageView iv = (ImageView) dialogView.findViewById(R.id.confirmationImageView);
        iv.setImageBitmap(photo);

        builder.setView(dialogView)
                .setPositiveButton("Start conversation", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmConversation();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelConversation();
                    }});

        Dialog dialog = builder.create();

        dialog.show();
    }

    public void sendMessage(View v) {
        EditText et = (EditText) findViewById(R.id.message);
        String message = et.getText().toString();
        et.setText("");

        // TODO: send this message
    }

    private void confirmConversation() {}
    private void cancelConversation() {}
}
