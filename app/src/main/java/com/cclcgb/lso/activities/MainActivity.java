package com.cclcgb.lso.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.cclcgb.lso.adapters.HomeAdapter;
import com.cclcgb.lso.R;
import com.cclcgb.lso.api.APIManager;
import com.cclcgb.lso.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        APIManager.init();
        APIManager.enableDebug();

        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.logout) {
                //auth.signOut();
                //startActivity(new Intent(MainActivity.this, PhoneNumberActivity.class));
                finish();
            }
            return false;
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if(auth.getCurrentUser()==null){
            startActivity(new Intent(MainActivity.this, PhoneNumberActivity.class));
            finish();
        }*/
    }
}