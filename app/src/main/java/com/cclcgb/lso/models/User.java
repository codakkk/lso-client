package com.cclcgb.lso.models;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;

public class User implements ILSOSerializable {
    private int mId;
    private String mName;

    public User() {}

    public User(int id, String name) {
        this.mId = id;
        this.mName = name;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    @Override
    public void Serialize(LSOWriter writer) {
        writer.write(mId);
        writer.write(mName);
    }

    @Override
    public void Deserialize(LSOReader reader) {
        try {
            mId = reader.readInt();
            mName = reader.readString();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
