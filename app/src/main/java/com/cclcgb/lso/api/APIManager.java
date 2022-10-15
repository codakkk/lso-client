package com.cclcgb.lso.api;

import android.os.StrictMode;

import com.cclcgb.lso.activities.MainActivity;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class APIManager implements Runnable{

    private static final String ServerIP = "10.0.2.2";
    private static final int Port = 4444;

    private static final APIManager mInstance = new APIManager();

    private String mName;

    private Thread mThread;
    private Socket mSocket;

    private DataOutputStream mWriter;

    private MainActivity mActivity;

    private final List<IOnMessageReceived> mCallbacks = new ArrayList<>();

    private final List<IOnConnectionClose> mOnConnectionCloseCallback = new ArrayList<>();

    private APIManager() {
    }

    public static void enableDebug() {
        addMessageReceivedListener((System.out::println));
    }

    public static void init(MainActivity mainActivity) {
        mInstance.mActivity = mainActivity;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        addMessageReceivedListener(mInstance::onMessageReceived);
    }

    private void onMessageReceived(LSOMessage message) {
        System.out.println("Message received with tag " + message.getTag());
    }

    @Override
    public void run() {
        try {
            mSocket = new Socket(InetAddress.getByName(ServerIP), Port);
            mWriter = new DataOutputStream(mSocket.getOutputStream());

            byte[] bytes = new byte[1024];
            while(mInstance.mSocket.isConnected()) {
                int size = mSocket.getInputStream().read(bytes);

                if(size < 0) {
                    break;
                }

                MessageBuffer messageBuffer = MessageBuffer.Create(size, bytes);
                LSOMessage message = LSOMessage.Create(messageBuffer);

                dispatchOnMessageReceived(message);

                Arrays.fill(bytes, (byte) 0);
            }

            mInstance.mActivity.runOnUiThread(() -> mInstance.mOnConnectionCloseCallback.forEach(IOnConnectionClose::onClose));

            mSocket.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // Connects to server with @name
    public static void connect(String name) {
        if(name == null || name.length() < 3) {
            return;
        }

        mInstance.mName = name;
        mInstance.mThread = new Thread(mInstance);
        mInstance.mThread.start();
    }

    public static void send(LSOMessage message) {
        try {
            MessageBuffer messageBuffer = message.toBuffer();

            mInstance.mWriter.write(messageBuffer.getBuffer(), 0, messageBuffer.getCount());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addMessageReceivedListener(IOnMessageReceived onMessageReceived) {
        mInstance.mCallbacks.add(onMessageReceived);
    }

    public static void removeMessageReceivedListener(IOnMessageReceived onMessageReceived) {
        mInstance.mCallbacks.remove(onMessageReceived);
    }

    private static void dispatchOnMessageReceived(LSOMessage message) {
        mInstance.mActivity.runOnUiThread(() -> mInstance.mCallbacks.forEach((e) -> e.onMessageReceived(message)));
    }

    public static void addOnConnectionClosedListener(IOnConnectionClose c) {
        mInstance.mOnConnectionCloseCallback.add(c);
    }

    public static void removeOnConnectionClosedListener(IOnConnectionClose c) {
        mInstance.mOnConnectionCloseCallback.remove(c);
    }

}
