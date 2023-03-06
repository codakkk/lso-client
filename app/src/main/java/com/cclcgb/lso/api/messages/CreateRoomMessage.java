package com.cclcgb.lso.api.messages;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;
import com.cclcgb.lso.models.ILSOSerializable;

import java.io.StreamCorruptedException;

public class CreateRoomMessage implements ILSOSerializable {
    private String name;

    public CreateRoomMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void Serialize(LSOWriter writer) {
        writer.write(name);
    }

    @Override
    public void Deserialize(LSOReader reader) {
        try {
            name = reader.readString();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        }
    }
}
