package com.cclcgb.lso.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.cclcgb.lso.adapters.ChatMessagesAdapter;
import com.cclcgb.lso.api.APIManager;
import com.cclcgb.lso.api.LSOMessage;
import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.Tags;
import com.cclcgb.lso.api.messages.Message;
import com.cclcgb.lso.databinding.FragmentChatBinding;
import com.cclcgb.lso.models.ChatMessage;

import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private FragmentChatBinding mBinding;
    private ChatMessagesAdapter mAdapter;

    private final List<ChatMessage> mChatMessages = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentChatBinding.inflate(inflater, container, false);

        ChatFragmentArgs args = ChatFragmentArgs.fromBundle(getArguments());
        String mRoomName = args.getRoomName();

        mBinding.roomName.setText(mRoomName);

        mAdapter = new ChatMessagesAdapter(requireContext(), mChatMessages);
        mBinding.chatRecyclerView.setAdapter(mAdapter);

        APIManager.addMessageReceivedListener(this::onMessageReceived);

        mBinding.sendButton.setOnClickListener(this::onSendButton);

        mBinding.backButton.setOnClickListener(this::onBackButton);

        return mBinding.getRoot();
    }

    public void publishMessage(String message, int senderId) {
        mChatMessages.add(new ChatMessage(message, senderId));
        mAdapter.notifyItemInserted(mChatMessages.size() - 1);
        mBinding.chatRecyclerView.smoothScrollToPosition(mChatMessages.size() - 1);
    }

    private void onMessageReceived(LSOMessage message) {
        LSOReader reader = message.getReader();

        if(message.getTag() == Tags.MessageTag) {
            Message receivedMessage = reader.readSerializable(new Message());
            publishMessage(receivedMessage.getMessage(), APIManager.getUserId() == receivedMessage.getUser().getId() ? 0 : 1);
        } else if(message.getTag() == Tags.LeaveRoom) {
             try {
                publishMessage(reader.readString() + " left the room.", -1);
             } catch (StreamCorruptedException e) {
                 e.printStackTrace();
             }
        }
    }


    private void onSendButton(View view) {
        String data = mBinding.inputMessage.getText().toString();
        if(data.length() == 0) {
            return;
        }

        LSOMessage sendMessage = LSOMessage.Create(Tags.MessageTag, new Message(data));
        APIManager.send(sendMessage);

        mBinding.inputMessage.setText("");
    }

    private void onBackButton(View view) {
        if(view == null) {
            return;
        }

        LSOMessage leaveRoomMessage = LSOMessage.CreateEmpty(Tags.LeaveRoomRequested);
        APIManager.send(leaveRoomMessage);

        NavDirections dir = ChatFragmentDirections.actionChatFragmentToRoomsFragment();
        Navigation.findNavController(view).navigate(dir);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
