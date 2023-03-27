package com.cclcgb.lso.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;

import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

public class Room implements ILSOSerializable, Parcelable {
    private int mId;
    private int mCount;
    private int mMaxCount;

    private String mName;

    private User mOwner;

    private final List<User> mUsers = new ArrayList<>();

    public Room() {}

    protected Room(Parcel in) {
        mId = in.readInt();
        mCount = in.readInt();
        mMaxCount = in.readInt();
        mName = in.readString();
        mOwner = in.readParcelable(User.class.getClassLoader());
        in.readParcelableList(mUsers, User.class.getClassLoader());
    }

    public static final Creator<Room> CREATOR = new Creator<>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeInt(mCount);
        parcel.writeInt(mMaxCount);
        parcel.writeString(mName);
        parcel.writeParcelable(mOwner, 0);
        parcel.writeParcelableList(mUsers, 0);
    }
}
