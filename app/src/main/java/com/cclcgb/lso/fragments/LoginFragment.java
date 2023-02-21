package com.cclcgb.lso.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.cclcgb.lso.api.APIManager;
import com.cclcgb.lso.api.LSOMessage;
import com.cclcgb.lso.api.Tags;
import com.cclcgb.lso.api.messages.FirstConfigurationMessage;
import com.cclcgb.lso.api.messages.SignInMessage;
import com.cclcgb.lso.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    FragmentLoginBinding mBinding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentLoginBinding.inflate(inflater, container, false);

        mBinding.signUpText.setText(Html.fromHtml("<font color=#01e675>Registra</font> un nuovo account"));
        mBinding.signUpText.setOnClickListener((v) -> {
            NavDirections dir = LoginFragmentDirections.actionLoginFragmentToSignUpFragment();
            Navigation.findNavController(requireView()).navigate(dir);
        });

        APIManager.init();
        APIManager.enableDebug();
        APIManager.connect();

        mBinding.setupButton.setOnClickListener((view) -> {
            String username = mBinding.username.getText().toString();
            String password = mBinding.password.getText().toString();

            LSOMessage m = LSOMessage.Create(Tags.SignInRequestedTag, new SignInMessage(username, password));
            APIManager.send(m);
        });

        APIManager.addMessageReceivedListener((message -> {
            if(message.getTag() == Tags.SignInRejectedTag) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Login fallito")
                        .setMessage("Username o password errati.")
                        .setPositiveButton("Riprova", null)
                        .show();
            }
            else if(message.getTag() == Tags.SignInAcceptedTag) {
                NavDirections dir = LoginFragmentDirections.actionLoginFragmentToRoomsFragment();
                Navigation.findNavController(requireView()).navigate(dir);
            }
        }));

        return mBinding.getRoot();
    }
}
