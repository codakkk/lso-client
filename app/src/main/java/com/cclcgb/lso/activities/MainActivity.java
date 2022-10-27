package com.cclcgb.lso.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cclcgb.lso.R;
import com.cclcgb.lso.api.APIManager;
import com.cclcgb.lso.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public static final Integer RecordAudioRequestCode = 1;

    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        APIManager.init(this);
        APIManager.enableDebug();

        mBinding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.logout) {
                //auth.signOut();
                //startActivity(new Intent(MainActivity.this, PhoneNumberActivity.class));
                finish();
            }
            return false;
        });

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if(auth.getCurrentUser()==null){
            startActivity(new Intent(MainActivity.this, PhoneNumberActivity.class));
            finish();
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }
}