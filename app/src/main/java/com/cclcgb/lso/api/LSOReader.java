package com.cclcgb.lso.api;

import java.io.DataInputStream;

public class LSOReader {

    private final DataInputStream mBuffer;

    public LSOReader(DataInputStream stream) {
        mBuffer = stream;
    }

    public byte readByte() {
        try {
            return mBuffer.readByte();
        } catch(Exception ignored) {
        }
        return 0;
    }

    public short readShort() {
        try {
            return mBuffer.readShort();
        } catch(Exception ignored) {
        }
        return 0;
    }

    public int readInt() {
        try {
            return mBuffer.readInt();
        } catch(Exception ignored) {
        }
        return 0;
    }
}
