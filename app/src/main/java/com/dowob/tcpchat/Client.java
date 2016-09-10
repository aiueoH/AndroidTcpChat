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
                    socket.connect(new InetSocketAddress(host, port));
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
                // 打開接收資料的串流
                DataInputStream dis;
                try {
                    dis = new DataInputStream(socket.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                // 開始不斷接收資料
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
                // 關閉串流
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 通知斷線
                if (onDisconnectListener != null)
                    onDisconnectListener.run();
            }
        }).start();
    }

    public void send(String data) {
        try {
            dataOutputStream.writeUTF(data);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
