package com.cclcgb.lso.api.messages;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;
import com.cclcgb.lso.models.ILSOSerializable;
import com.cclcgb.lso.models.User;

public class OnMatchMessage implements ILSOSerializable {
    private User mUser;
    private long mStartTimestamp;
    private long mEndTimestamp;
    private int mMaxChatTimeInSeconds;

    @Override
    public void Serialize(LSOWriter writer) {}

    @Override
    public void Deserialize(LSOReader reader) {
        try {
            mUser = reader.readSerializable(new User());
            mStartTimestamp = reader.readLong();
            mEndTimestamp = reader.readLong();
            mMaxChatTimeInSeconds = reader.readInt();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public User getUser() {
        return mUser;
    }

    public long getStartTimestamp() {
        return mStartTimestamp;
    }
    public long getEndTimestamp() {
        return mEndTimestamp;
    }

    public int getMaxChatTimeInSeconds() {
        return mMaxChatTimeInSeconds;
    }
}
