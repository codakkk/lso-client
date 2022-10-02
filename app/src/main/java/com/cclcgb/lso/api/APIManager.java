package com.cclcgb.lso.api;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class APIManager implements Runnable{

    private static final String ServerIP = "10.0.2.2";
    private static final int Port = 4444;

    private static final APIManager mInstance = new APIManager();

    private String mName;

    private Thread mThread;
    private Socket mSocket;

    private DataOutputStream mWriter;
    private DataInputStream mReader;

    private final List<IOnMessageReceived> mCallbacks = new ArrayList<>();

    private APIManager() {
    }

    public static void enableDebug() {
        addMessageReceivedListener((System.out::println));
    }

    public static void init() {

    }

    @Override
    public void run() {
        try {
            mSocket = new Socket(InetAddress.getByName(ServerIP), Port);
            mWriter = new DataOutputStream(mSocket.getOutputStream());
            mReader = new DataInputStream(new BufferedInputStream(mSocket.getInputStream()));


            // First thing to do: send name

            /*JoinRequestMessage joinRequestMessage = new JoinRequestMessage(mName);
            LSOMessage m = LSOMessage.Create(Tags.JoinRequest, joinRequestMessage);
            send(m);*/

            while(mInstance.mSocket.isConnected()) {
                byte[] bytes = new byte[1024];
                int size = mSocket.getInputStream().read(bytes);

                MessageBuffer messageBuffer = MessageBuffer.Create(size, bytes);
                LSOMessage message = LSOMessage.Create(messageBuffer);

                mCallbacks.forEach((e) -> e.onMessageReceived(message));
            }

            mSocket.close();
        } catch(Exception e) {
            System.out.println(e);
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
}
