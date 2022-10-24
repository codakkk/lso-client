package com.cclcgb.lso.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.cclcgb.lso.api.messages.OnMatchMessage;
import com.cclcgb.lso.api.messages.SendMessage;
import com.cclcgb.lso.databinding.FragmentChatBinding;
import com.cclcgb.lso.models.ChatMessage;
import com.cclcgb.lso.models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatFragment extends Fragment {
    private FragmentChatBinding mBinding;
    private ChatMessagesAdapter mAdapter;

    private OnMatchMessage mMatch;

    private final List<ChatMessage> mChatMessages = new ArrayList<>();

    private CountDownTimer mTimer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentChatBinding.inflate(inflater, container, false);

        ChatFragmentArgs args = ChatFragmentArgs.fromBundle(getArguments());
        int mRoomId = args.getRoomId();
        String mRoomName = args.getRoomName();

        mBinding.roomName.setText(mRoomName);

        mAdapter = new ChatMessagesAdapter(requireContext(), mChatMessages);
        mBinding.chatRecyclerView.setAdapter(mAdapter);

        publishMessage("SERVER: Waiting for a match...", -1);

        APIManager.addMessageReceivedListener(this::onMessageReceived);

        mBinding.sendButton.setOnClickListener(this::onSendButton);

        mBinding.backButton.setOnClickListener(this::onBackButton);

        mBinding.newMatch.setOnClickListener(this::onRequestNewMatch);

        return mBinding.getRoot();
    }

    public void publishMessage(String message, int senderId) {
        mChatMessages.add(new ChatMessage(message, senderId));
        mAdapter.notifyItemInserted(mChatMessages.size() - 1);
        mBinding.chatRecyclerView.smoothScrollToPosition(mChatMessages.size() - 1);
    }

    private void onMessageReceived(LSOMessage message) {
        LSOReader reader = message.getReader();

        if(message.getTag() == Tags.OnMatchTag) {
            OnMatchMessage onMatchMessage = reader.readSerializable(new OnMatchMessage());

            setMatch(onMatchMessage);

            publishMessage("SERVER: Matched with " + mMatch.getUser().getName(), -1);
        } else if(message.getTag() == Tags.ConfirmSentMessage) {
            SendMessage sendMessage = reader.readSerializable(new SendMessage());
            publishMessage(sendMessage.getMessage(), 0);
        } else if(message.getTag() == Tags.MessageTag) {
            SendMessage sendMessage = reader.readSerializable(new SendMessage());
            publishMessage(sendMessage.getMessage(), 1);
        } else if(message.getTag() == Tags.LeaveChat) {
            publishMessage( mMatch.getUser().getName() + " left the chat.", -1);
            publishMessage("Waiting for new match...", -1);

            setMatch(null);
        } else if(message.getTag() == Tags.ChatTimeoutTag) {
            publishMessage("Chat timed out...", -1);
            publishMessage("Waiting for new match...", -1);

            setMatch(null);
        }
    }

    private void onRequestNewMatch(View view) {
        if(mMatch == null)
        {
            publishMessage("You're not matched yet.", -1);
            return;
        }

        setMatch(null);

        LSOMessage leaveChatMessage = LSOMessage.CreateEmpty(Tags.LeaveChat);
        APIManager.send(leaveChatMessage);

        publishMessage("You left the chat.", -1);
        publishMessage("Waiting for a new match...", -1);
    }

    private void onSendButton(View view) {
        String data = mBinding.inputMessage.getText().toString();
        if(data.length() == 0) {
            return;
        }

        if(mMatch == null) {
            publishMessage("Cannot send messages without a match.", -1);
            return;
        }

        LSOMessage sendMessage = LSOMessage.Create(Tags.MessageTag, new SendMessage(data));
        APIManager.send(sendMessage);

        mBinding.inputMessage.setText("");
    }

    private void onBackButton(View view) {
        if(view == null) {
            return;
        }

        LSOMessage leaveRoomMessage = LSOMessage.CreateEmpty(Tags.LeaveRoom);
        APIManager.send(leaveRoomMessage);

        NavDirections dir = ChatFragmentDirections.actionChatFragmentToRoomsFragment();
        Navigation.findNavController(view).navigate(dir);
    }

    @SuppressLint("DefaultLocale")
    private void setMatch(OnMatchMessage match) {
        mMatch = match;

        if(mTimer != null) {
            mTimer.cancel();
        }

        final String[] text = {"Match: waiting"};

        if(mMatch != null) {
            mTimer = new CountDownTimer(mMatch.getMaxChatTimeInSeconds() * 1000L, 1000) {

                @Override
                public void onTick(long l) {
                    text[0] = String.format("Match: %s (time: %02d)", mMatch.getUser().getName(), l / 1000);
                    mBinding.matchedWithName.setText(text[0]);
                }

                @Override
                public void onFinish() {

                }
            }.start();
            text[0] = String.format("Match: %s (time: %02d)", mMatch.getUser().getName(), mMatch.getMaxChatTimeInSeconds());
            mBinding.matchedWithName.setText(text[0]);
        } else {
            mBinding.matchedWithName.setText("Waiting for match...");
        }

    }
}
