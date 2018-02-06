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
        socket = new Socket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket.connect(new InetSocketAddress(host, port), 5 * 1000);
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    if (onConnectedListener != null)
                        onConnectedListener.run();
                    startReceiving();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void startReceiving() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataInputStream dis;
                try {
                    dis = new DataInputStream(socket.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                while (true) {
                    try {
                        String data = dis.readUTF();
                        if (onReceiveDataListener != null)
                            onReceiveDataListener.onReceiveData(data, socket.getRemoteSocketAddress());
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (onDisconnectListener != null)
                    onDisconnectListener.run();
            }
        }).start();
    }

    public void send(final String data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dataOutputStream.writeUTF(data);
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setOnReceiveDataListener(OnReceiveDataListener onReceiveDataListener) {
        this.onReceiveDataListener = onReceiveDataListener;
    }

    public void close() {
        try {
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOnDisconnectListener(Runnable onDisconnectListener) {
        this.onDisconnectListener = onDisconnectListener;
    }
}
