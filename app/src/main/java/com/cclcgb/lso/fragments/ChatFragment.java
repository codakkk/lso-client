package com.cclcgb.lso.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cclcgb.lso.adapters.ChatMessagesAdapter;
import com.cclcgb.lso.api.APIManager;
import com.cclcgb.lso.api.LSOMessage;
import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.Tags;
import com.cclcgb.lso.api.messages.OnMatchMessage;
import com.cclcgb.lso.api.messages.SendMessage;
import com.cclcgb.lso.databinding.FragmentChatBinding;
import com.cclcgb.lso.models.ChatMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatFragment extends Fragment {
    FragmentChatBinding mBinding;
    ChatMessagesAdapter mAdapter;

    private int mRoomId;

    private boolean mIsMatched;
    private String mMatchedWithName;

    private final List<ChatMessage> mChatMessages = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentChatBinding.inflate(inflater, container, false);

        ChatFragmentArgs args = ChatFragmentArgs.fromBundle(getArguments());
        mRoomId = args.getRoomId();

        mAdapter = new ChatMessagesAdapter(requireContext(), mChatMessages);
        mBinding.chatRecyclerView.setAdapter(mAdapter);

        publishMessage("SERVER: Waiting for a match...", "-1");

        APIManager.addMessageReceivedListener(this::onMessageReceived);

        mBinding.sendButton.setOnClickListener((v) -> {
            String data = mBinding.inputMessage.getText().toString();
            if(data.length() == 0) {
                return;
            }

            LSOMessage sendMessage = LSOMessage.Create(Tags.SendMessageTag, new SendMessage(data));
            APIManager.send(sendMessage);

            mBinding.inputMessage.setText("");
        });
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        // String currentId = FirebaseAuth.getInstance().getUid();
        //database.getReference().child("presence").child(currentId).setValue("Online");
    }

    @Override
    public void onPause() {
        super.onPause();
        // String currentId = FirebaseAuth.getInstance().getUid();
        //database.getReference().child("presence").child(currentId).setValue("Offline");
    }

    public void publishMessage(String message, String senderId) {
        mChatMessages.add(new ChatMessage(message, senderId, new Date().getTime()));
        mAdapter.notifyItemInserted(mChatMessages.size() - 1);
    }

    private void onMessageReceived(LSOMessage message) {
        LSOReader reader = message.getReader();
        if(message.getTag() == Tags.OnMatchTag) {
            OnMatchMessage onMatchMessage = reader.readSerializable(new OnMatchMessage());
            mMatchedWithName = onMatchMessage.getName();
            mIsMatched = true;

            publishMessage("SERVER: Matched with " + mMatchedWithName, "-1");
        } else if(message.getTag() == Tags.ConfirmSentMessage) {
            SendMessage sendMessage = reader.readSerializable(new SendMessage());
            publishMessage(sendMessage.getMessage(), "0");
        } else if(message.getTag() == Tags.SendMessageTag) {
            SendMessage sendMessage = reader.readSerializable(new SendMessage());
            publishMessage(sendMessage.getMessage(), "1");
        }
    }
}
