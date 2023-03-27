package com.cclcgb.lso.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.os.Handler;
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
import com.cclcgb.lso.api.messages.RequestRoomsAccepted;
import com.cclcgb.lso.api.messages.RoomCreateAccepted;
import com.cclcgb.lso.databinding.FragmentRoomsBinding;
import com.cclcgb.lso.models.Room;

import com.cclcgb.lso.adapters.RoomsAdapter;

import java.util.ArrayList;
import java.util.List;

public class RoomsFragment extends Fragment {
    private FragmentRoomsBinding mBinding;
    private Handler mHandler;

    private static final List<Room> mRooms = new ArrayList<>();
    private static Room mCurrentRoom;

    private RoomsAdapter mRoomsAdapter;

    private ProgressDialog mDialog;

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

        // fetchRoomsPeriodically();

        return mBinding.getRoot();
    }

    private void onMessageReceived(LSOMessage message) {
        short tag = message.getTag();
        LSOReader reader = message.getReader();

        if(tag == Tags.RoomCreateAccepted) {
            RoomCreateAccepted roomCreateAccepted = reader.readSerializable(new RoomCreateAccepted());
            Room room = roomCreateAccepted.getRoom();
            mCurrentRoom = room;

            View view = getView();
            if(view != null) {
                NavDirections dir = RoomsFragmentDirections.actionRoomsFragmentToChatFragment(room.getId(), room.getName());
                Navigation.findNavController(view).navigate(dir);
            }
        } else if(tag == Tags.RequestRoomsAccepted) {
            RequestRoomsAccepted requestRoomsAccepted = reader.readSerializable(new RequestRoomsAccepted());

            mRooms.clear();
            mRooms.addAll(requestRoomsAccepted.getRooms());

            mRoomsAdapter.notifyItemRangeChanged(0, mRooms.size());
        }
    }

    private void onRoomClicked(Room room) {
        mCurrentRoom = room;

        View view = getView();
        if(view != null) {
            NavDirections dir = RoomsFragmentDirections.actionRoomsFragmentToChatFragment(room.getId(), room.getName());
            Navigation.findNavController(view).navigate(dir);
        }
    }

    private void fetchRoomsPeriodically() {

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10000);

                    LSOMessage requestRooms = LSOMessage.CreateEmpty(Tags.RequestRooms);
                    APIManager.send(requestRooms);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static List<Room> getRooms() {
        return mRooms;
    }
    public static Room getCurrentRoom() {
        return mCurrentRoom;
    }
}