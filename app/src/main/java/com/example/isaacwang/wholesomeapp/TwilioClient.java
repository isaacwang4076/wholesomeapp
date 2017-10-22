package com.example.isaacwang.wholesomeapp;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.twilio.chat.*;
import com.twilio.chat.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gateszeng on 10/21/17.
 */

public class TwilioClient {
    private final static String SERVER_TOKEN_URL = "https://88596722.ngrok.io/token";
    private final static String DEFAULT_CHANNEL_NAME = "general";
    private final static String TAG = "TwilioChat";

    private static Context mContext;
    private static ChatClient mChatClient;
    private static Channel mCurrentChannel;
    private static ArrayList<Message> mMessages = new ArrayList<>();

    private static final TwilioClient ourInstance = new TwilioClient();

    public static TwilioClient getInstance(Context context) {
        mContext = context;
        return ourInstance;
    }

    private TwilioClient() {
    }

    public static void retrieveAccessTokenfromServer() {
        String deviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

        String tokenURL = SERVER_TOKEN_URL + "?device=" + deviceId;

        Ion.with(mContext)
                .load(tokenURL)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            String identity = result.get("identity").getAsString();
                            String accessToken = result.get("token").getAsString();

                            ChatClient.Properties.Builder builder = new ChatClient.Properties.Builder();
                            ChatClient.Properties props = builder.createProperties();
                            ChatClient.create(mContext,accessToken,props,mChatClientCallback);

                        } else {
                            Toast.makeText(mContext,
                                    R.string.error_retrieving_access_token, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }

    private static CallbackListener<ChatClient> mChatClientCallback =
            new CallbackListener<ChatClient>() {
                @Override
                public void onSuccess(ChatClient chatClient) {
                    mChatClient = chatClient;
                    loadChannels();
                    Log.d(TAG, "Success creating Twilio Chat Client");
                }

                @Override
                public void onError(ErrorInfo errorInfo) {
                    Log.e(TAG,"Error creating Twilio Chat Client: " + errorInfo.getMessage());
                }
            };


    private static void loadChannels() {
        mChatClient.getChannels().getChannel(DEFAULT_CHANNEL_NAME, new CallbackListener<Channel>() {
            @Override
            public void onSuccess(Channel channel) {
                if (channel != null) {
                    joinChannel(channel);
                } else {
                    mChatClient.getChannels().createChannel(DEFAULT_CHANNEL_NAME,
                            Channel.ChannelType.PUBLIC, new CallbackListener<Channel>() {
                                @Override
                                public void onSuccess(Channel channel) {
                                    if (channel != null) {
                                        joinChannel(channel);
                                    }
                                }

                                @Override
                                public void onError(ErrorInfo errorInfo) {
                                    Log.e(TAG,"Error creating channel: " + errorInfo.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                Log.e(TAG,"Error retrieving channel: " + errorInfo.getMessage());
            }

        });

    }

    private static void joinChannel(final Channel channel) {
        Log.d(TAG, "Joining Channel: " + channel.getUniqueName());
        channel.join(new StatusListener() {
            @Override
            public void onSuccess() {
                mCurrentChannel = channel;
                Log.d(TAG, "Joined default channel");
                mCurrentChannel.getMessages().getLastMessages(10, new CallbackListener<List<Message>>() {
                    @Override
                    public void onSuccess(List<Message> messages) {
                        if (messages.isEmpty()) {
                            ((ChatActivity)mContext).scheduleChatBot();
                        }
                        for (Message tMessage : messages) {
                            mMessages.add(tMessage);
                            Log.d(TAG, tMessage.getMessageBody());
                            ((ChatActivity)mContext).addMessage(toMessage(tMessage));
                        }
                    }
                });

                mCurrentChannel.addListener(mDefaultChannelListener);
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                Log.e(TAG,"Error joining channel: " + errorInfo.getMessage());
            }
        });
    }

    private static com.example.isaacwang.wholesomeapp.Message toMessage(Message tMessage) {
        com.example.isaacwang.wholesomeapp.Message wMessage;
        wMessage = new com.example.isaacwang.wholesomeapp.Message(false,
                tMessage.getMessageBody(), mContext.getDrawable(R.drawable.prof_pic));

        return wMessage;
    }

    private static ChannelListener mDefaultChannelListener = new ChannelListener() {

        @Override
        public void onMessageAdded(final com.twilio.chat.Message message) {
            Log.d(TAG, "Message added");

            ((Activity)mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // need to modify user interface elements on the UI thread
                    mMessages.add(message);
                    ((ChatActivity)mContext).addMessage(toMessage(message));
                }
            });

        }

        @Override
        public void onMessageUpdated(com.twilio.chat.Message message, Message.UpdateReason var2) {
            Log.d(TAG, "Message updated: " + message.getMessageBody() + ". due to: " + var2.getValue());
        }

        @Override
        public void onMessageDeleted(com.twilio.chat.Message message) {
            Log.d(TAG, "Message deleted");
        }

        @Override
        public void onMemberAdded(Member member) {
            Log.d(TAG, "Member added: " + member.getIdentity());
        }

        @Override
        public void onMemberUpdated(Member member, com.twilio.chat.Member.UpdateReason var2) {
            Log.d(TAG, "Member updated: " + member.getIdentity() + ". due to: " + var2.getValue());
        }

        @Override
        public void onMemberDeleted(Member member) {
            Log.d(TAG, "Member deleted: " + member.getIdentity());
        }

        @Override
        public void onTypingStarted(Member member) {
            Log.d(TAG, "Started Typing: " + member.getIdentity());
        }

        @Override
        public void onTypingEnded(Member member) {
            Log.d(TAG, "Ended Typing: " + member.getIdentity());
        }

        @Override
        public void onSynchronizationChanged(Channel channel) {

        }
    };
}

