package com.cclcgb.lso.api.messages;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;
import com.cclcgb.lso.models.ILSOSerializable;

import java.io.StreamCorruptedException;

public class JoinRoomMessage implements ILSOSerializable {
    private int mId;

    public JoinRoomMessage(int id) {
        mId = id;
    }

    @Override
    public void Serialize(LSOWriter writer) {
        writer.write(mId);
    }

    @Override
    public void Deserialize(LSOReader reader) {
        try {
            mId = reader.readInt();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        }
    }
}
