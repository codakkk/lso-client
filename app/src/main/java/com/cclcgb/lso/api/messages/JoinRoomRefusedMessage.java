package com.cclcgb.lso.api.messages;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;
import com.cclcgb.lso.models.ILSOSerializable;

import java.io.StreamCorruptedException;

public class JoinRoomRefusedMessage implements ILSOSerializable {

    private int mRoomId;
    private String mRoomName;

    @Override
    public void Serialize(LSOWriter writer) {}

    @Override
    public void Deserialize(LSOReader reader) {
        try {
            mRoomId = reader.readInt();
            mRoomName = reader.readString();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        }
    }

    public int getRoomId() {
        return mRoomId;
    }
    public String getRoomName() { return mRoomName; }
}
