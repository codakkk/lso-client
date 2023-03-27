package com.cclcgb.lso.fragments;

import android.graphics.Color;
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
import com.cclcgb.lso.api.messages.JoinRoomAcceptRequestMessage;
import com.cclcgb.lso.api.messages.JoinRoomRefuseRequestMessage;
import com.cclcgb.lso.api.messages.JoinRoomRequestMessage;
import com.cclcgb.lso.api.messages.JoinRoomNotifyAcceptedMessage;
import com.cclcgb.lso.api.messages.JoinRoomRequestedMessage;
import com.cclcgb.lso.api.messages.LeaveRoomMessage;
import com.cclcgb.lso.api.messages.MessageReceivedMessage;
import com.cclcgb.lso.api.messages.SendMessageMessage;
import com.cclcgb.lso.databinding.FragmentChatBinding;
import com.cclcgb.lso.models.ChatMessage;
import com.cclcgb.lso.models.ChatMessageJoinRequestState;
import com.cclcgb.lso.models.ChatMessageType;
import com.cclcgb.lso.models.Room;
import com.cclcgb.lso.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatFragment extends Fragment implements ChatMessagesAdapter.IChatMessageJoinRequestCallback {
    private static final String mTag = "ChatFragment";

    private FragmentChatBinding mBinding;
    private ChatMessagesAdapter mAdapter;
    private Room mRoom;
    private boolean mIsRoomClosed = false;
    private boolean mIsOwner = false;

    private final List<ChatMessage> mChatMessages = new ArrayList<>();
    private final Map<User, Color> mColors = new HashMap<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentChatBinding.inflate(inflater, container, false);

        assert getArguments() != null;
        ChatFragmentArgs args = ChatFragmentArgs.fromBundle(getArguments());

        mAdapter = new ChatMessagesAdapter(requireContext(), mChatMessages, this);
        mBinding.chatRecyclerView.setAdapter(mAdapter);

        APIManager.addMessageReceivedListener(this::onMessageReceived);

        mRoom = RoomsFragment.getCurrentRoom();

        mBinding.roomName.setText(mRoom.getName());
        mBinding.sendButton.setOnClickListener(this::onSendButton);
        mBinding.backButton.setOnClickListener(this::onBackButton);

        if(mRoom.getOwner().getId() != APIManager.getUser().getId()) {
            publishMessage(ChatMessageType.Server, mRoom.getOwner(), "Waiting to be accepted by " + mRoom.getOwner().getName());

            JoinRoomRequestMessage joinRoomMessage = new JoinRoomRequestMessage(mRoom.getId());
            LSOMessage message = LSOMessage.Create(Tags.JoinRoomRequest, joinRoomMessage);
            APIManager.send(message);

            mBinding.sendButton.setVisibility(View.INVISIBLE);
            mBinding.inputMessage.setVisibility(View.INVISIBLE);
        }
        else {
            mIsOwner = true;
        }

        return mBinding.getRoot();
    }

    public void publishMessage(ChatMessageType type, User user, String message) {
        int color = 0xFFFFFFFF;

        if(mColors.containsKey(user)) {
            color = Objects.requireNonNull(mColors.get(user)).toArgb();
        }

        mChatMessages.add(new ChatMessage(type, user, message, color));
        mAdapter.notifyItemInserted(mChatMessages.size() - 1);
        mBinding.chatRecyclerView.smoothScrollToPosition(mChatMessages.size() - 1);
    }

    private void onMessageReceived(LSOMessage message) {
        LSOReader reader = message.getReader();

        if(message.getTag() == Tags.ReceiveMessage) {
            MessageReceivedMessage receivedMessage = reader.readSerializable(new MessageReceivedMessage());

            final boolean isOwn = receivedMessage.getUser().getId() == APIManager.getUser().getId();

            publishMessage(isOwn ? ChatMessageType.Sent : ChatMessageType.Received, receivedMessage.getUser(), receivedMessage.getMessage());
        } else if(message.getTag() == Tags.LeaveRoom) {
            LeaveRoomMessage leaveRoomMessage = reader.readSerializable(new LeaveRoomMessage());
            publishMessage(ChatMessageType.Server, leaveRoomMessage.getUser(), leaveRoomMessage.getUser().getName() + " è uscito dalla chat.");
        } else if(message.getTag() == Tags.JoinRoomNotifyAccepted) {
            JoinRoomNotifyAcceptedMessage joinRoomNotifyAcceptedMessage = reader.readSerializable(new JoinRoomNotifyAcceptedMessage());

            User user = joinRoomNotifyAcceptedMessage.getUser();

            if(user.getId() == APIManager.getUser().getId()) {
                publishMessage(ChatMessageType.Server, mRoom.getOwner(), String.format("%s ti ha accettato.", mRoom.getOwner().getName()));

                mBinding.sendButton.setVisibility(View.VISIBLE);
                mBinding.inputMessage.setVisibility(View.VISIBLE);
            } else {
                publishMessage(ChatMessageType.Server, user, String.format("%s è entrato nella chat.", user.getName()));
            }
        } else if(message.getTag() == Tags.RoomClosed) {
            publishMessage(ChatMessageType.Server, mRoom.getOwner(), mRoom.getOwner().getName() + " ha chiuso la stanza.");
            mIsRoomClosed = true;
            mBinding.sendButton.setVisibility(View.INVISIBLE);
            mBinding.inputMessage.setVisibility(View.INVISIBLE);
        } else if(message.getTag() == Tags.JoinRoomRequested) {
            JoinRoomRequestedMessage joinRoomRequestMessage = reader.readSerializable(new JoinRoomRequestedMessage());

            publishMessage(ChatMessageType.JoinRequest, joinRoomRequestMessage.getUser(), "");
        } else if(message.getTag() == Tags.JoinRoomRefused) {
            publishMessage(ChatMessageType.Server, null, "Il proprietario non ha accettato la tua richiesta.");
        }
    }


    private void onSendButton(View view) {
        String data = mBinding.inputMessage.getText().toString();
        if(data.length() == 0) {
            return;
        }

        LSOMessage sendMessage = LSOMessage.Create(Tags.SendMessage, new SendMessageMessage(data));
        APIManager.send(sendMessage);

        mBinding.inputMessage.setText("");
    }

    private void onBackButton(View view) {
        if(view == null) {
            return;
        }

        if(mRoom != null && !mIsRoomClosed) {
            LSOMessage leaveRoomMessage = LSOMessage.CreateEmpty(Tags.LeaveRoomRequested);
            APIManager.send(leaveRoomMessage);
        }

        NavDirections dir = ChatFragmentDirections.actionChatFragmentToRoomsFragment();
        Navigation.findNavController(view).navigate(dir);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mRoom != null && !mIsRoomClosed) {
            LSOMessage leaveRoomMessage = LSOMessage.CreateEmpty(Tags.LeaveRoomRequested);
            APIManager.send(leaveRoomMessage);
        }
    }

    @Override
    public void onAccept(int position, ChatMessage chatMessage) {
        chatMessage.setRequestState(ChatMessageJoinRequestState.Accepted);
        mAdapter.notifyItemChanged(position);

        LSOMessage joinAccepted = LSOMessage.Create(Tags.JoinRoomAccepted, new JoinRoomAcceptRequestMessage(chatMessage.getUser().getId(), mRoom.getId()));
        APIManager.send(joinAccepted);
    }

    @Override
    public void onReject(int position, ChatMessage chatMessage) {
        chatMessage.setRequestState(ChatMessageJoinRequestState.Rejected);
        mAdapter.notifyItemChanged(position);

        LSOMessage joinRefused = LSOMessage.Create(Tags.JoinRoomRefused, new JoinRoomRefuseRequestMessage(chatMessage.getUser().getId()));
        APIManager.send(joinRefused);
    }
}
