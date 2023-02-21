package com.cclcgb.lso.api.messages;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;
import com.cclcgb.lso.models.ILSOSerializable;

import java.io.StreamCorruptedException;

public class SignInMessage implements ILSOSerializable {
    private String mUsername;
    private String mPassword;

    public SignInMessage(String username, String password) {
        mUsername = username;
        mPassword = password;
    }

    @Override
    public void Serialize(LSOWriter writer) {
        writer.write(mUsername);
        writer.write(mPassword);
    }

    @Override
    public void Deserialize(LSOReader reader) {
        try {
            mUsername = reader.readString();
            mPassword = reader.readString();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        }
    }
}
