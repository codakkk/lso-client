package com.cclcgb.lso.api;

import com.cclcgb.lso.models.ILSOSerializable;

import java.nio.charset.StandardCharsets;

public class LSOWriter {

    private int mPosition;
    private MessageBuffer mBuffer;

    private LSOWriter() {}

    // Number of bytes currently written
    public int getLength() {
        return mBuffer.getCount();
    }

    public static LSOWriter Create() {
        return Create(16);
    }

    public static LSOWriter Create(int initialLength) {
        LSOWriter writer = new LSOWriter();
        writer.mPosition = 0;
        writer.mBuffer = MessageBuffer.Create(initialLength);

        return writer;
    }

    public void Write(byte value) {
        mBuffer.ensureSize(mPosition + 1);

        mBuffer.getBuffer()[mPosition++] = value;

        mBuffer.setCount(Math.max(getLength(), mPosition));
    }

    public void Write(boolean value) {
        Write((byte)(value ? 1 : 0));
    }

    public void Write(short value) {
        mBuffer.ensureSize(mPosition + 2);

        BufferHelpers.WriteBytes(mBuffer.getBuffer(), mPosition, value);

        mPosition += 2;

        mBuffer.setCount(Math.max(getLength(), mPosition));
    }

    public void Write(int value) {
        mBuffer.ensureSize(mPosition + 4);

        BufferHelpers.WriteBytes(mBuffer.getBuffer(), mPosition, value);

        mPosition += 4;

        mBuffer.setCount(Math.max(getLength(), mPosition));
    }

    public void Write(String value) {
        final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

        // 4 is int-size for string length
        mBuffer.ensureSize(mPosition + 4 + bytes.length);

        byte[] byteBuffer = mBuffer.getBuffer();
        BufferHelpers.WriteBytes(byteBuffer, mPosition, bytes.length);

        for(int i = 0; i < bytes.length; ++i) {
            byteBuffer[mPosition + 4 + i] = bytes[i];
        }

        mPosition += 4 + bytes.length;

        mBuffer.setCount(Math.max(getLength(), mPosition));
    }

    public <T extends ILSOSerializable> void Write(T serializable) {
        serializable.Serialize(this);
    }

    public MessageBuffer toBuffer() {
        return mBuffer.clone();
    }
}
