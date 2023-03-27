package com.cclcgb.lso.api.messages;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;
import com.cclcgb.lso.models.ILSOSerializable;
import com.cclcgb.lso.models.Room;

public class JoinRoomAcceptRequestMessage implements ILSOSerializable {
    private int mUserId;
    private int mRoomId;

    public JoinRoomAcceptRequestMessage(int userId, int roomId) {
        mUserId = userId;
        mRoomId = roomId;
    }

    @Override
    public void Serialize(LSOWriter writer) {
        writer.write(mUserId);
        writer.write(mRoomId);
    }

    @Override
    public void Deserialize(LSOReader reader) {
    }
}