package com.cclcgb.lso.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cclcgb.lso.adapters.RoomsAdapter;
import com.cclcgb.lso.api.APIManager;
import com.cclcgb.lso.api.LSOMessage;
import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.Tags;
import com.cclcgb.lso.api.messages.CreateRoomMessage;
import com.cclcgb.lso.api.messages.RequestRoomsAccepted;
import com.cclcgb.lso.api.messages.RoomCreateAccepted;
import com.cclcgb.lso.databinding.FragmentRoomsBinding;
import com.cclcgb.lso.models.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private FragmentRoomsBinding mBinding;

    private static final List<Room> mRooms = new ArrayList<>();


    private RoomsAdapter mRoomsAdapter;

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
            builder.setTitle("Crea chat");

            final EditText input = new EditText(ctx);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("Crea", (dialog, which) -> {
                String name = input.getText().toString();

                LSOMessage message = LSOMessage.Create(Tags.RoomCreateRequested, new CreateRoomMessage(name));
                APIManager.send(message);
            });
            builder.setNegativeButton("Chiudi", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);
        // fetchRoomsPeriodically();

        return mBinding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void onMessageReceived(LSOMessage message) {
        short tag = message.getTag();
        LSOReader reader = message.getReader();

        if(tag == Tags.RoomCreateAccepted) {
            RoomCreateAccepted roomCreateAccepted = reader.readSerializable(new RoomCreateAccepted());
            Room room = roomCreateAccepted.getRoom();

            View view = getView();
            if(view != null) {
                NavDirections dir = RoomsFragmentDirections.actionRoomsFragmentToChatFragment(room);
                Navigation.findNavController(view).navigate(dir);
            }
        } else if(tag == Tags.RequestRoomsAccepted) {
            RequestRoomsAccepted requestRoomsAccepted = reader.readSerializable(new RequestRoomsAccepted());

            mRooms.clear();
            mRooms.addAll(requestRoomsAccepted.getRooms());

            mRoomsAdapter.notifyDataSetChanged();

            mBinding.swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void onRoomClicked(Room room) {
        View view = getView();
        if(view != null) {
            NavDirections dir = RoomsFragmentDirections.actionRoomsFragmentToChatFragment(room);
            Navigation.findNavController(view).navigate(dir);
        }
    }

    @Override
    public void onRefresh() {
        LSOMessage requestRooms = LSOMessage.CreateEmpty(Tags.RequestRooms);
        APIManager.send(requestRooms);
    }
}