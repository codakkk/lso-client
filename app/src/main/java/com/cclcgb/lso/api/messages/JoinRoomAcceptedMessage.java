package com.cclcgb.lso.api.messages;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;
import com.cclcgb.lso.models.ILSOSerializable;

import java.io.StreamCorruptedException;

public class JoinRoomAcceptedMessage implements ILSOSerializable {

    private int mRoomId;

    @Override
    public void Serialize(LSOWriter writer) {}

    @Override
    public void Deserialize(LSOReader reader) {
        try {
            mRoomId = reader.readInt();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        }
    }

    public int getRoomId() {
        return mRoomId;
    }
}
