package com.cclcgb.lso.api.messages;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;
import com.cclcgb.lso.models.ILSOSerializable;
import com.cclcgb.lso.models.Room;

import java.io.StreamCorruptedException;

public class RoomCreateAccepted implements ILSOSerializable {
    private Room mRoom;

    public Room getRoom() { return mRoom; }

    @Override
    public void Serialize(LSOWriter writer) {}

    @Override
    public void Deserialize(LSOReader reader) {
        mRoom = reader.readSerializable(new Room());
    }
}
