package com.cclcgb.lso.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cclcgb.lso.api.APIManager;
import com.cclcgb.lso.api.LSOMessage;
import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.Tags;
import com.cclcgb.lso.databinding.FragmentRoomsBinding;
import com.cclcgb.lso.models.Message;
import com.cclcgb.lso.models.Room;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import com.cclcgb.lso.adapters.RoomsAdapter;
import com.cclcgb.lso.R;

import java.util.ArrayList;
import java.util.List;

public class RoomsFragment extends Fragment {
    FragmentRoomsBinding mBinding;

    private final List<Room> mRooms = new ArrayList<>();
    RoomsAdapter mRoomsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentRoomsBinding.inflate(inflater, container, false);


        mBinding.userRecyclerView.showShimmerAdapter();

        /*mRooms.add(new Room(0, 2, "Politica"));
        mRooms.add(new Room(1, 1, "Meloni"));*/

        mRoomsAdapter = new RoomsAdapter(getContext(), mRooms);
        mBinding.userRecyclerView.setAdapter(mRoomsAdapter);

        /*LSOMessage requestRooms = LSOMessage.CreateEmpty(Tags.RequestRoomsTag);
        APIManager.send(requestRooms);*/

        APIManager.addMessageReceivedListener((message -> {
            if(message.getTag() == Tags.RequestRoomsTag) {
                LSOReader reader = message.getReader();
                Room room = new Room();
                room.Deserialize(reader);

                mRooms.add(room);
                mRoomsAdapter.notifyDataSetChanged();
            }
        }));
        /*database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    User user = snapshot1.getValue(User.class);
                    if(!user.getUid().equals(FirebaseAuth.getInstance().getUid())){
                        users.add(user);
                    }

                }
                userRecyclerView.hideShimmerAdapter();
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

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
}