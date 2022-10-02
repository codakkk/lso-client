package com.cclcgb.lso.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cclcgb.lso.databinding.FragmentWaitingInRoomBinding;
import com.cclcgb.lso.models.Room;

public class WaitingInRoomFragment extends Fragment {
    private FragmentWaitingInRoomBinding mBinding;
    private Room mRoom;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentWaitingInRoomBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }
}