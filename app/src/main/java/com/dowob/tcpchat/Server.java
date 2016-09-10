package com.dowob.tcpchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class Server {
    private ServerSocket serverSocket;
    private Hashtable<Socket, DataOutputStream> clients = new Hashtable<>();
    private OnReceiveDataListener onReceiveDataListener;

    public void bind(int port) {
    }

    public void startListeningAsync() {
    }

    private void handleNewClient(final Socket socket) {
    }

    private void sendToAllClients(String data) throws IOException {
    }

    public void setOnReceiveDataListener(OnReceiveDataListener onReceiveDataListener) {
        this.onReceiveDataListener = onReceiveDataListener;
    }

    public void close() {
    }

}
