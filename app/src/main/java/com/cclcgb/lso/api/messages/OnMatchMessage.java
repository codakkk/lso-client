package com.cclcgb.lso.api.messages;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;
import com.cclcgb.lso.models.ILSOSerializable;

public class OnMatchMessage implements ILSOSerializable {

    private int mRoomId;
    private int mClientId;
    private String mName;

    @Override
    public void Serialize(LSOWriter writer) {}

    @Override
    public void Deserialize(LSOReader reader) {
        try {
            mRoomId = reader.readInt();
            mClientId = reader.readInt();
            mName = reader.readString();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public String getName() {
        return mName;
    }

    public int getClientId() {
        return mClientId;
    }

    public int getRoomId() {
        return mRoomId;
    }
}
