package com.cclcgb.lso.api;

import android.os.StrictMode;
import android.util.Log;

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

    private static final String ServerIP = "172.28.175.188";
    private static final int Port = 5555;

    private static final APIManager mInstance = new APIManager();

    private Thread mThread;
    private Socket mSocket;

    private DataOutputStream mWriter;

    private final List<IOnMessageReceived> mCallbacks = new ArrayList<>();

    private final List<IOnConnectionClose> mOnConnectionCloseCallback = new ArrayList<>();

    private APIManager() {}

    public static void enableDebug() {
        addMessageReceivedListener((System.out::println));
    }

    public static void init() {
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

            if(mSocket.isConnected()) {
                Log.i("APIManager", "Connected to server");
            } else {
                Log.e("APIManager", "Cannot connected to server");
            }

            byte[] bytes = new byte[2048];
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

            MainActivity.Instance.runOnUiThread(() -> mInstance.mOnConnectionCloseCallback.forEach(IOnConnectionClose::onClose));

            mSocket.close();
        } catch(Exception e) {
            e.printStackTrace();
            Log.e("APIManager", "Error: " + e.getMessage());
        }
    }

    public static void connect() {
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
        MainActivity.Instance.runOnUiThread(() -> mInstance.mCallbacks.forEach((e) -> e.onMessageReceived(message)));
    }

    public static void addOnConnectionClosedListener(IOnConnectionClose c) {
        mInstance.mOnConnectionCloseCallback.add(c);
    }

    public static void removeOnConnectionClosedListener(IOnConnectionClose c) {
        mInstance.mOnConnectionCloseCallback.remove(c);
    }

}
