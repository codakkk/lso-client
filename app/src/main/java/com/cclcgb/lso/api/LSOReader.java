package com.cclcgb.lso.api;

import com.cclcgb.lso.models.ILSOSerializable;

import java.io.StreamCorruptedException;

public class LSOReader {

    private int mPosition;
    private MessageBuffer mBuffer;

    private LSOReader() {}

    public static LSOReader Create(MessageBuffer buffer) {
        LSOReader reader = new LSOReader();

        reader.mBuffer = buffer;
        reader.mPosition = 0;
        return reader;
    }

    public int getLength() {
        return mBuffer.getCount();
    }
    public int getPosition() { return mPosition; }

    public boolean readBoolean() throws StreamCorruptedException {
        if(mPosition >= mBuffer.getCount()) {
            throw new StreamCorruptedException("Failed to read boolean.");
        }
        return mBuffer.getBuffer()[mBuffer.getOffset() + mPosition++] == 1;
    }

    public byte readByte() throws StreamCorruptedException {
        if(mPosition >= mBuffer.getCount()) {
            throw new StreamCorruptedException("Failed to read byte.");
        }
        return mBuffer.getBuffer()[mBuffer.getOffset() + mPosition++];
    }

    public short readShort() throws StreamCorruptedException {
        if(mPosition + 2 > getLength()) {
            throw new StreamCorruptedException("Failed to read short.");
        }

        short v = BufferHelpers.ReadShort(mBuffer.getBuffer(), mBuffer.getOffset() + mPosition);
        mPosition += 2;
        return v;
    }

    public int readInt() throws StreamCorruptedException {
        if(mPosition + 4 > getLength()) {
            throw new StreamCorruptedException("Failed to read int.");
        }

        int v = BufferHelpers.ReadInt(mBuffer.getBuffer(), mBuffer.getOffset() + mPosition);
        mPosition += 4;
        return v;
    }


    public String readString() throws StreamCorruptedException {
        // First, read string length

        int length = BufferHelpers.ReadInt(mBuffer.getBuffer(), mBuffer.getOffset() + mPosition);

        if(mPosition + 4 + length > getLength()) {
            throw new StreamCorruptedException("Failed to read string.");
        }

        String v = new String(mBuffer.getBuffer(), mBuffer.getOffset() + mPosition + 4, length);

        mPosition += 4 + length;

        return v;
    }

    public <T extends ILSOSerializable> T readSerializable(T serializable) {
        if(serializable == null) {
            return null;
        }
        serializable.Deserialize(this);
        return serializable;
    }
}
