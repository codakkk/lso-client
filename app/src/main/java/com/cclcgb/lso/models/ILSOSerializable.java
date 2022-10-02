package com.cclcgb.lso.models;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;

public interface ILSOSerializable {

    void Serialize(LSOWriter writer);
    void Deserialize(LSOReader reader);
}
