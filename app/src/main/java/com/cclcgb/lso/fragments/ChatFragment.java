package com.cclcgb.lso.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cclcgb.lso.adapters.ChatMessagesAdapter;
import com.cclcgb.lso.adapters.RoomsAdapter;
import com.cclcgb.lso.api.APIManager;
import com.cclcgb.lso.api.LSOMessage;
import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.Tags;
import com.cclcgb.lso.databinding.FragmentChatBinding;
import com.cclcgb.lso.databinding.FragmentRoomsBinding;
import com.cclcgb.lso.models.Message;
import com.cclcgb.lso.models.Room;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatFragment extends Fragment {
    FragmentChatBinding mBinding;
    ChatMessagesAdapter mAdapter;

    private List<Message> mMessages;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentChatBinding.inflate(inflater, container, false);

        mMessages = new ArrayList<>();
        mAdapter = new ChatMessagesAdapter(requireContext(), mMessages);

        mMessages.add(new Message("Waiting for another person...", "-1", new Date().getTime()));
        mAdapter.notifyItemInserted(0);

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

    private void onRoomClicked(Room room) {}
}
