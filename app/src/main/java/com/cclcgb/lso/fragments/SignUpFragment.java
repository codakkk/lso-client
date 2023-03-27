package com.cclcgb.lso.fragments;

import android.app.ProgressDialog;
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
import com.cclcgb.lso.api.messages.SignUpMessage;
import com.cclcgb.lso.databinding.FragmentSignupBinding;

public class SignUpFragment extends Fragment {
    FragmentSignupBinding mBinding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSignupBinding.inflate(inflater, container, false);

        mBinding.orLoginText.setText(Html.fromHtml("Esegui il <font color=#01e675>Login</font>"));
        mBinding.orLoginText.setOnClickListener((v) -> {
            NavDirections dir = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment();
            Navigation.findNavController(requireView()).navigate(dir);
        });

        mBinding.setupButton.setOnClickListener((view) -> {
            String username = mBinding.username.getText().toString();
            String password = mBinding.password.getText().toString();

            LSOMessage m = LSOMessage.Create(Tags.SignUpRequestedTag, new SignUpMessage(username, password));
            APIManager.send(m);
        });

        APIManager.addMessageReceivedListener((message -> {
            if(message.getTag() == Tags.SignUpAcceptedTag)
            {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Account creato")
                        .setMessage("Account creato con successo.")
                        .setPositiveButton("Vai al Login", (dialog, which) -> {
                            NavDirections dir = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment();
                            Navigation.findNavController(requireView()).navigate(dir);
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else if(message.getTag() == Tags.SignUpRejectedTag)
            {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Account non creato")
                        .setMessage("Account non creato.\nUsername già in uso")
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }));

        return mBinding.getRoot();
    }
}
