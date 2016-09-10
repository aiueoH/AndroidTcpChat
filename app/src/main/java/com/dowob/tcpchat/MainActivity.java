package com.dowob.tcpchat;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.SocketAddress;

public class MainActivity extends AppCompatActivity {
    private Server server;
    private Client client;

    // Server setting
    private LinearLayout serverSettingLayout;
    private EditText serverPortEditText;
    private Button serverStartListeningButton;
    // Server console
    private LinearLayout serverConsoleLayout;
    private TextView serverConsoleTextView;
    private Button closeServerButton;
    private ScrollView serverConsoleScrollView;
    // Client setting
    private LinearLayout clientSettingLayout;
    private EditText connectHostEditText;
    private EditText connectPortEditText;
    private Button connectButton;
    // Client console
    private LinearLayout clientChatLayout;
    private TextView clientConsoleTextView;
    private EditText messageEditText;
    private Button sendButton;
    private Button disconnectButton;
    private ScrollView clientConsoleScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ///////////////////////////////////////////////////////////////////////////////////////////
        // server setting
        ///////////////////////////////////////////////////////////////////////////////////////////
        serverSettingLayout = (LinearLayout) findViewById(R.id.linearLayour_server_setting);
        serverPortEditText = (EditText) findViewById(R.id.editText_server_port);
        serverStartListeningButton = (Button) findViewById(R.id.button_start_listening);
        serverStartListeningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////////
        // server console
        ///////////////////////////////////////////////////////////////////////////////////////////
        serverConsoleScrollView = (ScrollView) findViewById(R.id.scrollView_server_console);
        serverConsoleLayout = (LinearLayout) findViewById(R.id.linearLayout_server_console);
        serverConsoleTextView = (TextView) findViewById(R.id.textView_server);
        closeServerButton = (Button) findViewById(R.id.button_close_server);
        closeServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////////
        // client setting
        ///////////////////////////////////////////////////////////////////////////////////////////
        clientSettingLayout = (LinearLayout) findViewById(R.id.client_setting);
        connectHostEditText = (EditText) findViewById(R.id.editText_connect_host);
        connectPortEditText = (EditText) findViewById(R.id.editText_connect_port);
        connectButton = (Button) findViewById(R.id.button_connect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////////
        // client chat
        ///////////////////////////////////////////////////////////////////////////////////////////
        clientChatLayout = (LinearLayout) findViewById(R.id.client_chat);
        clientConsoleScrollView = (ScrollView) findViewById(R.id.scrollView_client_console);
        clientConsoleTextView = (TextView) findViewById(R.id.textView_client);
        messageEditText = (EditText) findViewById(R.id.editText_client);
        sendButton = (Button) findViewById(R.id.button_send_client);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        disconnectButton = (Button) findViewById(R.id.button_disconnect);
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void appendServerConsole(String s) {
        serverConsoleTextView.append(s + "\n\n");
        serverConsoleScrollView.fullScroll(View.FOCUS_DOWN);
    }

    private void appendClientConsole(String s) {
        clientConsoleTextView.append(s + "\n\n");
        clientConsoleScrollView.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (server != null)
            server.close();
        if (client != null)
            client.close();
    }

    public String getWifiIp() {
        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return Formatter.formatIpAddress(ip);
    }
}
