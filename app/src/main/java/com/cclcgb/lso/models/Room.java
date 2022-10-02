package com.cclcgb.lso.models;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;

public class Room implements ILSOSerializable {
    private int mId;
    private int mCount;
    private int mMaxCount;

    private String mName;

    public Room() {}

    public Room(int id, int count, String name) {
        this.mId = id;
        this.mCount = count;
        this.mName = name;
    }

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

    @Override
    public void Serialize(LSOWriter writer) {

    }

    @Override
    public void Deserialize(LSOReader reader) {
        mId = reader.readInt();
        mCount = reader.readInt();
        mMaxCount = reader.readInt();
    }
}
