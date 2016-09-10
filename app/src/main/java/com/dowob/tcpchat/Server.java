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
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startListeningAsync() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Socket socket = serverSocket.accept();
                        handleNewClient(socket);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void handleNewClient(final Socket socket) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 通知大家有新人進來
                    String newClientMsg = String.format("[%s] 加入了聊天室", socket.getRemoteSocketAddress());
                    sendToAllClients(newClientMsg);
                    sendToAllClients(newClientMsg);

                    // 將新的 client 加入 client 列表
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    synchronized (clients) {
                        clients.put(socket, dataOutputStream);
                    }

                    // 接收來自該 client 的訊息
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    while (true) {
                        String data;
                        try {
                            data = dis.readUTF();
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                        if (onReceiveDataListener != null)
                            onReceiveDataListener.onReceiveData(data, socket.getRemoteSocketAddress());
                        // 把訊息傳給每個 clients
                        String msg = String.format("[%s] 說 : %s", socket.getRemoteSocketAddress(), data);
                        sendToAllClients(msg);
                    }
                    // 關閉接收資料的串流
                    dis.close();
                    // 從 clients 列表中移除
                    synchronized (clients) {
                        clients.remove(socket);
                    }
                    // 告訴大家有人離開了
                    String msg = String.format("[%s] 離開了", socket.getRemoteSocketAddress());
                    sendToAllClients(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendToAllClients(String data) throws IOException {
        synchronized (clients) {
            for (Socket clientSocket : clients.keySet()) {
                if (clientSocket.isClosed())
                    continue;
                DataOutputStream dos = clients.get(clientSocket);
                dos.writeUTF(data);
                dos.flush();
            }
        }
    }

    public void setOnReceiveDataListener(OnReceiveDataListener onReceiveDataListener) {
        this.onReceiveDataListener = onReceiveDataListener;
    }

    public void close() {
        if (serverSocket == null && serverSocket.isClosed())
            return;
        synchronized (clients) {
            for (Socket s : clients.keySet()) {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
