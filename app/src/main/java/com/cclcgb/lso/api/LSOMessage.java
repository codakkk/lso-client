package com.cclcgb.lso.api;

import com.cclcgb.lso.models.ILSOSerializable;

import java.io.DataInputStream;

public class LSOMessage {

    private short mSize;
    private short mTag;

    private MessageBuffer mBuffer;

    private LSOReader mReader;

    private LSOMessage() {}

    public LSOMessage(short size, byte tag) {
        mSize = size;
        mTag = tag;
    }

    public short getSize() {
        return mSize;
    }

    public short getTag() {
        return mTag;
    }

    public LSOReader getReader() {
        return mReader;
    }

    public static LSOMessage Create(MessageBuffer buffer)
    {
        LSOMessage message = new LSOMessage();

        message.mBuffer = buffer.clone();

        int headerSize = 2;
        message.mBuffer.setOffset(buffer.getOffset() + headerSize);
        message.mBuffer.setCount(buffer.getCount() - headerSize);

        message.mTag = BufferHelpers.ReadShort(buffer.getBuffer(), buffer.getOffset());
        return message;
    }

    public static <T extends ILSOSerializable> LSOMessage Create(short tag, T serializable)
    {
        LSOMessage message = new LSOMessage();

        LSOWriter writer = LSOWriter.Create();
        writer.Write(serializable);

        message.mBuffer = writer.toBuffer();

        message.mTag = tag;
        return message;
    }

    // Used for messages without data but Tag
    public static LSOMessage CreateEmpty(short tag)
    {
        LSOMessage message = new LSOMessage();

        message.mTag = tag;
        message.mBuffer = MessageBuffer.Create(0);

        return message;
    }

    @Override
    public String toString() {
        return "LSOMessage{" +
                "mSize=" + mSize +
                ", mTag=" + mTag +
                '}';
    }

    public int getDataLength() {
        return mBuffer.getCount();
    }

    public MessageBuffer toBuffer() {
        // 2 because short is 2 bytes long
        int totalLength = getDataLength() + 2;

        MessageBuffer buffer = MessageBuffer.Create(totalLength);
        buffer.setCount(totalLength);

        BufferHelpers.WriteBytes(buffer.getBuffer(), 0, mTag);

        // Same as totalLength + 2
        System.arraycopy(mBuffer.getBuffer(), 0, buffer.getBuffer(), 2, mBuffer.getCount());
        return buffer;
    }
}
