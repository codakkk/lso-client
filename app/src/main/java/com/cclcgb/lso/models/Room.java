package com.cclcgb.lso.models;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;

import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

public class Room implements ILSOSerializable {
    private int mId;
    private int mCount;
    private int mMaxCount;

    private String mName;

    private User mOwner;

    private final List<User> mUsers = new ArrayList<>();

    public Room() {}

    public int getId() {
        return mId;
    }

    public void setCount(int count) {
        this.mCount = count;
    }

    public int getCount() {
        return mCount;
    }

    public String getName() {
        return mName;
    }

    public int getMaxCount() {
        return mMaxCount;
    }

    public User getOwner() {
        return mOwner;
    }

    public List<User> getUsers() {
        return mUsers;
    }

    @Override
    public void Serialize(LSOWriter writer) { }

    @Override
    public void Deserialize(LSOReader reader) {
        try {
            mId = reader.readInt();
            mCount = reader.readInt();
            mMaxCount = reader.readInt();
            mName = reader.readString();
            mOwner = reader.readSerializable(new User());

            mUsers.clear();
            while(reader.getPosition() < reader.getLength()) {
                mUsers.add(reader.readSerializable(new User()));
            }
        } catch(StreamCorruptedException e) {
            e.printStackTrace();
        }
    }
}
