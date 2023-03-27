package com.cclcgb.lso.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;

public class User implements ILSOSerializable, Parcelable {
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private int mId;
    private String mName;

    public User() {}

    public User(int id, String name) {
        this.mId = id;
        this.mName = name;
    }

    protected User(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
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

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mName);
    }
}
