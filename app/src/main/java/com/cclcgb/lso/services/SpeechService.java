package com.cclcgb.lso.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class SpeechService implements RecognitionListener {

    private final SpeechRecognizer mSpeechRecognizer;

    private final Intent mIntent;

    private ISpeechListener mSpeechListener;

    public SpeechService(Context context) {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        mSpeechRecognizer.setRecognitionListener(this);

        mIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ITALY);
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.ITALY);
        mIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, Locale.ITALY);
    }

    public void startListening() {
        mSpeechRecognizer.startListening(mIntent);
    }

    public void stopListening() {
        mSpeechRecognizer.stopListening();
    }

    public void dispose() {
        mSpeechRecognizer.destroy();
    }

    public void setSpeechListener(ISpeechListener listener) {
        mSpeechListener = listener;
    }

    public void removeSpeechListener() {
        mSpeechListener = null;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {
        mSpeechListener.onBegin();
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        mSpeechListener.onFinish(Collections.emptyList());
    }

    @Override
    public void onError(int error) {
        mSpeechListener.onError();
        Log.e("SpeechService", "Speech service error: " + error);
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        mSpeechListener.onFinish(data);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}
