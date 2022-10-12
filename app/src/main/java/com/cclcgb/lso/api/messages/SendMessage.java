package com.cclcgb.lso.api.messages;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;
import com.cclcgb.lso.models.ILSOSerializable;

public class SendMessage implements ILSOSerializable {
    private String mMessage;

    public SendMessage() {}

    public SendMessage(String message) {
        mMessage = message;
    }

    @Override
    public void Serialize(LSOWriter writer) {
        writer.write(mMessage);
    }

    @Override
    public void Deserialize(LSOReader reader) {
        try {
            mMessage = reader.readString();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String getMessage() {
        return mMessage;
    }
}
