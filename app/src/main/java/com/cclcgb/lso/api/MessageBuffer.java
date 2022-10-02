package com.cclcgb.lso.api;

import androidx.annotation.NonNull;

public class MessageBuffer implements Cloneable {

    private byte[] mBuffer;

    // Number of bytes in the array
    private int mCount;

    // Number of bytes of data start in the buffer
    private int mOffset;

    private MessageBuffer() {}

    public byte[] getBuffer() {
        return mBuffer;
    }

    public void ensureSize(int space) {
        if(space <= mBuffer.length) return;

        byte[] newBuffer = new byte[space];

        System.arraycopy(mBuffer, 0, newBuffer, 0, mBuffer.length);

        mBuffer = newBuffer;
    }

    public static MessageBuffer Create(int minCapacity) {
        MessageBuffer buffer = new MessageBuffer();
        buffer.mBuffer = new byte[minCapacity];
        buffer.mOffset = 0;
        buffer.mCount = 0;

        return buffer;
    }

    public static MessageBuffer Create(int size, byte[] bytes) {
        MessageBuffer buffer = new MessageBuffer();
        buffer.mBuffer = new byte[size];

        System.arraycopy(bytes, 0, buffer.mBuffer, 0, size);

        buffer.mOffset = 0;
        buffer.mCount = bytes.length;

        return buffer;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int newCount) {
        mCount = newCount;
    }

    public int getOffset() {
        return mOffset;
    }

    public void setOffset(int newOffset) {
        mOffset = newOffset;
    }


    @NonNull
    @Override
    public MessageBuffer clone() {
        MessageBuffer buffer = new MessageBuffer();
        buffer.mBuffer = mBuffer;

        buffer.mOffset = mOffset;
        buffer.mCount = mCount;
        return buffer;
    }
}
