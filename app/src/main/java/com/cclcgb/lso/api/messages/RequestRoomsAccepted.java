package com.cclcgb.lso.api.messages;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;
import com.cclcgb.lso.models.ILSOSerializable;
import com.cclcgb.lso.models.Room;

import java.util.ArrayList;
import java.util.List;

public class RequestRoomsAccepted implements ILSOSerializable {
    private List<Room> mRooms;

    public List<Room> getRooms() { return mRooms; }

    @Override
    public void Serialize(LSOWriter writer) {}

    @Override
    public void Deserialize(LSOReader reader)
    {
        mRooms = new ArrayList<>();
        while(reader.getPosition() < reader.getLength())
        {
            mRooms.add(reader.readSerializable(new Room()));
        }
    }
}
