package com.cclcgb.lso.api.messages;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;
import com.cclcgb.lso.models.ILSOSerializable;

public class JoinRoomRefuseRequestMessage implements ILSOSerializable {
    private final int mUserId;

    public JoinRoomRefuseRequestMessage(int userId) {
        mUserId = userId;
    }

    @Override
    public void Serialize(LSOWriter writer) {
        writer.write(mUserId);
    }

    @Override
    public void Deserialize(LSOReader reader) {
    }
}