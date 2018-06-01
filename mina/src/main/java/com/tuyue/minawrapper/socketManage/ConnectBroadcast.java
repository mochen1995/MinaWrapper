package com.tuyue.minawrapper.socketManage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 项目名：TestModuleApp
 * 包名：com.tuyue.yinghuo.socketManage
 * 创建者：mmcc
 * 创建时间：2018/5/29 15:50
 * 描述：TODO
 */


public class ConnectBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SocketEntity socketEntity = new SocketEntity();
        if (!TextUtils.isEmpty(intent.getStringExtra(ConnectContacts.STATE_SUCCESS))) {
            socketEntity.status = ConnectContacts.STATE_SUCCESS;
            socketEntity.content = "连接成功！";
        } else if (!TextUtils.isEmpty(intent.getStringExtra(ConnectContacts.STATE_WAIT))) {
            socketEntity.status = ConnectContacts.STATE_WAIT;
            socketEntity.content = "正在连接服务器...";
        } else if (!TextUtils.isEmpty(intent.getStringExtra(ConnectContacts.STATE_CLOSE))) {
            socketEntity.status = ConnectContacts.STATE_CLOSE;
            socketEntity.content = "断开连接...";
        } else {
            socketEntity.status = ConnectContacts.STATE_MESSAGE;
            socketEntity.content = intent.getStringExtra(ConnectContacts.STATE_MESSAGE);
           /* Bundle extras = intent.getExtras();
            Bitmap b = extras.getParcelable("img");

            if (b!=null)
            {
                socketEntity.bitmap = b;
            }*/
        }
        EventBus.getDefault().post(socketEntity);
    }
}
