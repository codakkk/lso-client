package com.cclcgb.lso.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cclcgb.lso.api.APIManager;
import com.cclcgb.lso.api.LSOMessage;
import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.Tags;
import com.cclcgb.lso.api.messages.JoinRoomAcceptedMessage;
import com.cclcgb.lso.api.messages.JoinRoomMessage;
import com.cclcgb.lso.api.messages.JoinRoomRefusedMessage;
import com.cclcgb.lso.databinding.FragmentRoomsBinding;
import com.cclcgb.lso.models.Room;

import com.cclcgb.lso.adapters.RoomsAdapter;

import java.util.ArrayList;
import java.util.List;

public class RoomsFragment extends Fragment {
    FragmentRoomsBinding mBinding;

    private final List<Room> mRooms = new ArrayList<>();
    RoomsAdapter mRoomsAdapter;

    ProgressDialog dialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentRoomsBinding.inflate(inflater, container, false);


        mBinding.userRecyclerView.showShimmerAdapter();

        /*mRooms.add(new Room(0, 2, "Politica"));
        mRooms.add(new Room(1, 1, "Meloni"));*/

        mRoomsAdapter = new RoomsAdapter(getContext(), mRooms, this::onRoomClicked);
        mBinding.userRecyclerView.setAdapter(mRoomsAdapter);

        LSOMessage requestRooms = LSOMessage.CreateEmpty(Tags.RequestRoomsTag);
        APIManager.send(requestRooms);

        APIManager.addMessageReceivedListener(this::onMessageReceived);

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

    private void onMessageReceived(LSOMessage message) {
        short tag = message.getTag();
        LSOReader reader = message.getReader();

        if(tag == Tags.RoomTag) {
            while(reader.getPosition() < reader.getLength())
            {
                Room room = new Room();
                room.Deserialize(reader);

                mRooms.add(room);
            }

            mRoomsAdapter.notifyItemRangeChanged(0, mRooms.size()-1);
        } else if(tag == Tags.JoinRoomAcceptedTag) {
            onJoinRoomAccepted(reader.readSerializable(new JoinRoomAcceptedMessage()));
        } else if(tag == Tags.JoinRoomRefusedTag) {
            onJoinRoomRefused(reader.readSerializable(new JoinRoomRefusedMessage()));
        }
    }

    private void onRoomClicked(Room room) {
        JoinRoomMessage joinRoomMessage = new JoinRoomMessage(room.getId());
        LSOMessage message = LSOMessage.Create(Tags.JoinRoomTag, joinRoomMessage);
        APIManager.send(message);

        dialog = new ProgressDialog(requireActivity());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
    }

    private void onJoinRoomAccepted(JoinRoomAcceptedMessage message) {
        System.out.println("Accepted in room " + message.getRoomId());
        NavDirections dir = RoomsFragmentDirections.actionRoomsFragmentToChatFragment(message.getRoomId());
        Navigation.findNavController(requireView()).navigate(dir);
    }

    private void onJoinRoomRefused(JoinRoomRefusedMessage message) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Rejected")
                .setMessage("Room is full")
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        System.out.println("Join room refused " + message.getRoomId());
    }
}