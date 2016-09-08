package com.dowob.tcpchat;

import java.net.SocketAddress;

public interface OnReceiveDataListener {
    void onReceiveData(String data, SocketAddress remoteSocketAddress);
}
