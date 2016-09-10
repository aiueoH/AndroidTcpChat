package com.dowob.tcpchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private OnReceiveDataListener onReceiveDataListener;
    private Runnable onDisconnectListener;

    public void connectAsync(final String host, final int port, final Runnable onConnectedListener) {
    }

    private void startReceiving() {
    }

    public void send(String data) {
    }

    public void setOnReceiveDataListener(OnReceiveDataListener onReceiveDataListener) {
        this.onReceiveDataListener = onReceiveDataListener;
    }

    public void close() {
    }

    public void setOnDisconnectListener(Runnable onDisconnectListener) {
        this.onDisconnectListener = onDisconnectListener;
    }
}
