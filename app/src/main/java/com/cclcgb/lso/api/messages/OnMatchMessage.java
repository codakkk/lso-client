package com.cclcgb.lso.api.messages;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;
import com.cclcgb.lso.models.ILSOSerializable;
import com.cclcgb.lso.models.User;

public class OnMatchMessage implements ILSOSerializable {

    private int mRoomId;
    private User mUser;

    @Override
    public void Serialize(LSOWriter writer) {}

    @Override
    public void Deserialize(LSOReader reader) {
        try {
            mRoomId = reader.readInt();
            mUser = reader.readSerializable(new User());
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public int getRoomId() {
        return mRoomId;
    }

    public User getUser() {
        return mUser;
    }
}
