package com.cclcgb.lso.api.messages;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;
import com.cclcgb.lso.models.ILSOSerializable;

public class FirstConfigurationMessage implements ILSOSerializable {

    private String mName;

    public FirstConfigurationMessage(String name) {
        mName = name;
    }

    @Override
    public void Serialize(LSOWriter writer) {
        writer.write(mName);
    }

    @Override
    public void Deserialize(LSOReader reader) {

    }
}
