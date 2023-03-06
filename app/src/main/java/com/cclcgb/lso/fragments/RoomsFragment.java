package com.cclcgb.lso.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cclcgb.lso.api.APIManager;
import com.cclcgb.lso.api.LSOMessage;
import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.Tags;
import com.cclcgb.lso.api.messages.CreateRoomMessage;
import com.cclcgb.lso.api.messages.JoinRoomAcceptedMessage;
import com.cclcgb.lso.api.messages.JoinRoomMessage;
import com.cclcgb.lso.api.messages.JoinRoomRefusedMessage;
import com.cclcgb.lso.api.messages.RequestRoomsAccepted;
import com.cclcgb.lso.api.messages.RoomCreateAccepted;
import com.cclcgb.lso.databinding.FragmentRoomsBinding;
import com.cclcgb.lso.models.Room;

import com.cclcgb.lso.adapters.RoomsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RoomsFragment extends Fragment {
    FragmentRoomsBinding mBinding;

    private static final List<Room> mRooms = new ArrayList<>();
    RoomsAdapter mRoomsAdapter;

    ProgressDialog dialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentRoomsBinding.inflate(inflater, container, false);

        mRoomsAdapter = new RoomsAdapter(getContext(), mRooms, this::onRoomClicked);
        mBinding.roomsRecyclerView.setAdapter(mRoomsAdapter);

        LSOMessage requestRooms = LSOMessage.CreateEmpty(Tags.RequestRooms);
        APIManager.send(requestRooms);

        APIManager.addMessageReceivedListener(this::onMessageReceived);

        mBinding.addChat.setOnClickListener((v) -> {
            Context ctx = requireContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle("Crea stanza");

            final EditText input = new EditText(ctx);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);

            builder.setPositiveButton("Crea", (dialog, which) -> {
                String name = input.getText().toString();

                LSOMessage message = LSOMessage.Create(Tags.RoomCreateRequested, new CreateRoomMessage(name));
                APIManager.send(message);
            });
            builder.setNegativeButton("Chiudi", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        return mBinding.getRoot();
    }

    private void onMessageReceived(LSOMessage message) {
        short tag = message.getTag();
        LSOReader reader = message.getReader();

        if(tag == Tags.RoomCreateAccepted) {
            RoomCreateAccepted roomCreateAccepted = reader.readSerializable(new RoomCreateAccepted());
            Room room = roomCreateAccepted.getRoom();

            View view = getView();
            if(view != null) {
                NavDirections dir = RoomsFragmentDirections.actionRoomsFragmentToChatFragment(room.getId(), room.getName());
                Navigation.findNavController(view).navigate(dir);
            }
        } else if(tag == Tags.RequestRoomsAccepted) {
            RequestRoomsAccepted requestRoomsAccepted = reader.readSerializable(new RequestRoomsAccepted());

            mRooms.clear();
            mRooms.addAll(requestRoomsAccepted.getRooms());

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
        Room room = message.getRoom();
        System.out.println("Accepted in room " + room.getId());
        View view = getView();
        if(view != null) {
            NavDirections dir = RoomsFragmentDirections.actionRoomsFragmentToChatFragment(room.getId(), room.getName());
            Navigation.findNavController(view).navigate(dir);
        }
    }

    private void onJoinRoomRefused(JoinRoomRefusedMessage message) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Rejected")
                .setMessage("Room " + message.getRoomName() + "is full")
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        System.out.println("Join room refused " + message.getRoomId());
    }
}