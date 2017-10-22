package com.example.isaacwang.wholesomeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

public class ConversationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.appBar);
        myToolbar.setTitle(getResources().getString(R.string.app_name));
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout conversationList = (LinearLayout) findViewById(R.id.conversation_list);

        ConversationSummary convSum = new ConversationSummary(getDrawable(R.drawable.prof_pic), "Gates", "this was the last message");
        ConversationSummary convSum2 = new ConversationSummary(getDrawable(R.drawable.prof_pic), "Gates2", "this was the last message");
        ConversationSummary convSum3 = new ConversationSummary(getDrawable(R.drawable.prof_pic), "Gates3", "this was the last message");

        conversationList.addView(convSum.getView(this));
        conversationList.addView(convSum2.getView(this));
        conversationList.addView(convSum3.getView(this));
    }


}
