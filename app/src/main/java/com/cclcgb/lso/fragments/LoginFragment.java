package com.cclcgb.lso.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.cclcgb.lso.api.APIManager;
import com.cclcgb.lso.api.LSOMessage;
import com.cclcgb.lso.api.Tags;
import com.cclcgb.lso.api.messages.FirstConfigurationMessage;
import com.cclcgb.lso.databinding.FragmentSetupProfileBinding;

public class LoginFragment extends Fragment {
    FragmentSetupProfileBinding mBinding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSetupProfileBinding.inflate(inflater, container, false);

        mBinding.setupButton.setOnClickListener((view) -> {
            APIManager.connect(mBinding.nameBox.getText().toString());
        });

        APIManager.addMessageReceivedListener((message -> {
            if(message.getTag() == Tags.JoinRequestAccepted) {
                FirstConfigurationMessage firstConfigurationMessage = new FirstConfigurationMessage(mBinding.nameBox.getText().toString());
                LSOMessage m = LSOMessage.Create(Tags.SendFirstConfiguration, firstConfigurationMessage);
                APIManager.send(m);
            }
            else if(message.getTag() == Tags.FirstConfigurationAccepted) {
                getActivity().runOnUiThread(() -> {
                    NavDirections dir = LoginFragmentDirections.actionLoginFragmentToRoomsFragment();
                    Navigation.findNavController(requireView()).navigate(dir);
                });
            }
        }));

        return mBinding.getRoot();
    }
}
