package com.cclcgb.lso.api.messages;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;
import com.cclcgb.lso.models.ILSOSerializable;
import com.cclcgb.lso.models.User;

import java.io.StreamCorruptedException;

public class JoinRoomRequestedMessage implements ILSOSerializable {
    private User mUser;

    public JoinRoomRequestedMessage() {

    }

    @Override
    public void Serialize(LSOWriter writer) {

    }

    @Override
    public void Deserialize(LSOReader reader) {
        mUser = reader.readSerializable(new User());
    }

    public User getUser() {
        return mUser;
    }
}
