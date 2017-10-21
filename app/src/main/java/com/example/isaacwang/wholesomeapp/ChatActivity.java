package com.example.isaacwang.wholesomeapp;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    
    private static final int TINY_GAP = 15;
    
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

        Message m1 = new Message(true, "hey man this is me", null);
        Message m2 = new Message(true, "how you been", null);
        Message m3 = new Message(false, "not bad", getDrawable(R.drawable.prof_pic));
        Message m4 = new Message(false, "mostly j chillin haha. Been pretty caught up by school work esketiiiiiit. you know, same old same old this is a long message.", getDrawable(R.drawable.prof_pic));
        Message m5 = new Message(true, "aight good to know", null);

        initializeMessages(Arrays.asList(m1, m2, m3, m4, m5));
    }

    private void initializeMessages(List<Message> messages) {
        for (Message msg: messages) {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);
        return true;
    }
}
