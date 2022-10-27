package com.cclcgb.lso.services;

import java.util.List;

public interface ISpeechListener {
    void onBegin();
    void onError();
    void onFinish(List<String> results);
}
