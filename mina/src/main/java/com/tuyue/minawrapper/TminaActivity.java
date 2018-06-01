package com.tuyue.minawrapper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tuyue.minawrapper.socketManage.ConnectContacts;
import com.tuyue.minawrapper.socketManage.ConnectService;
import com.tuyue.minawrapper.socketManage.Event;
import com.tuyue.minawrapper.socketManage.SessionManager;
import com.tuyue.minawrapper.socketManage.SocketEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class TminaActivity extends AppCompatActivity {
    private android.widget.EditText edit1;
    private android.widget.EditText edit2;
    private android.widget.Button btn1;
    private android.widget.TextView tv2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmina);
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
                Intent intent = new Intent(TminaActivity.this, ConnectService.class);
                startService(intent);
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"未连接".equals(tv2.getText())) {
                    Toast.makeText(TminaActivity.this, edit2.getText().toString(), Toast.LENGTH_SHORT).show();
                   //发内容 DataFlag : Event.EV_RECIEVE_MESSAGE->需要服务器返回内容信息，Event.EV_RECIEVE_IMG->需要服务器返回图片
                    String content = "{\"DataFlag\":"+ Event.EV_RECIEVE_IMG+", \"Data\":{\"username\":\"admin\", \"password\":\"123456789\"}}";
                    SessionManager.getmInstance().write(content.getBytes());
                    //发图片
                    SessionManager.getmInstance().writeFile("/mnt/sdcard/tencent/MicroMsg/WeiXin/wx_camera_1527070621921.jpg");
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        stopService(new Intent(TminaActivity.this, ConnectService.class));
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
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
            case ConnectContacts.STATE_RECONNECT:
                tv2.append("连接关闭，正在进行重新连接..\n");
                break;
            case ConnectContacts.STATE_MESSAGE:
                tv2.append("收到服务器内容：" + socket.content + "\n");
                if (socket.bitmap!=null) {   //收到图片时
                    Log.e("tag", "bitmap = "+socket.bitmap+" size = "+socket.bitmap.getHeight());
                }
                break;
        }
    }
}
