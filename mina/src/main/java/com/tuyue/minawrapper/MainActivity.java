package com.tuyue.minawrapper;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tuyue.minawrapper.socketManage.ConnectBroadcast;
import com.tuyue.minawrapper.socketManage.ConnectContacts;
import com.tuyue.minawrapper.socketManage.ConnectManager;
import com.tuyue.minawrapper.socketManage.ConnectService;
import com.tuyue.minawrapper.socketManage.SessionManager;
import com.tuyue.minawrapper.socketManage.SocketEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {
    private android.widget.EditText edit1;
    private android.widget.EditText edit2;
    private android.widget.Button btn1;
    private android.widget.TextView tv2;
    private ConnectBroadcast redceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        this.tv2 = (TextView) findViewById(R.id.tv2);
        this.btn1 = (Button) findViewById(R.id.btn1);
        this.edit2 = (EditText) findViewById(R.id.edit2);
        this.edit1 = (EditText) findViewById(R.id.edit1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //连接服务器
                String ip = edit1.getText().toString();
                int port = Integer.parseInt(edit2.getText().toString());
                ConnectContacts.IP = ip;
                ConnectContacts.PORT = port;
                //开启连接服务
                Intent intent = new Intent(MainActivity.this, ConnectService.class);
                startService(intent);
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"未连接".equals(tv2.getText())) {
                    Toast.makeText(MainActivity.this, edit2.getText().toString(), Toast.LENGTH_SHORT).show();
//                        String encode = URLEncoder.encode(edit2.getText().toString(), "utf-8");
                    SessionManager.getmInstance().writeToServer(edit2.getText().toString());
                }
            }
        });

        //注册广播接收消息
        IntentFilter intentFilter = new IntentFilter(ConnectManager.BROADCAST_ACTION);
        redceiver = new ConnectBroadcast();
        registerReceiver(redceiver, intentFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        stopService(new Intent(MainActivity.this, ConnectService.class));
        unregisterReceiver(redceiver);
    }
    @Subscribe
    public void NetInfoGet(SocketEntity socket){
        switch (socket.status) {
            case ConnectContacts.STATE_WAIT:
                tv2.append("正在连接服务器...\n");
                break;
            case ConnectContacts.STATE_SUCCESS:
                tv2.append("连接成功，点击发送数据给服务器\n");
                break;
            case ConnectContacts.STATE_CLOSE:
                tv2.append("断开连接...\n");
                break;
            case ConnectContacts.STATE_MESSAGE:
                tv2.append("收到服务器内容：" + socket.content + "\n");
                break;
        }
    }
}
