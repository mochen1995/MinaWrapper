package com.tuyue.minawrapper.socketManage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;
import com.tuyue.minawrapper.socketManage.domain.MinaMsgHead2;

import org.apache.mina.core.buffer.IoBuffer;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

/**
 * 项目名：MinaWrapper
 * 包名：com.tuyue.minawrapper.socketManage
 * 创建者：mmcc
 * 创建时间：2018/5/31 9:12
 * 描述：TODO
 */


public class HandlerEvent2 {
    private static HandlerEvent2 handlerEvent;

    public static HandlerEvent2 getInstance() {
        if (handlerEvent == null) {
            handlerEvent = new HandlerEvent2();
        }
        return handlerEvent;
    }

    public void handle(IoBuffer buf) throws IOException, InterruptedException, UnsupportedEncodingException, SQLException {
        //解析包头
        byte[] headJson = new byte[264];
        buf.get(headJson);
        String hjson = new String(headJson, "GBK").trim();
        //接下来根据业务处理....
        MinaMsgHead2 msgHead = new Gson().fromJson(hjson, MinaMsgHead2.class);
        Log.e("tag", "消息头：" + msgHead);
        if (msgHead.getSocket_header_msgFlag() != 1)  //消息
        {
            byte[] by = new byte[msgHead.getSocket_header_msgLength()];
            buf.get(by, 0, by.length);
            String json = new String(by, "GBK").trim();
            Log.e("tag", "消息内容：" + json);
            //接下来根据业务处理....
            SocketEntity socketEntity = new SocketEntity();
            socketEntity.dataFlag = msgHead.getSocket_header_msgFlag();
            socketEntity.content = json;
            socketEntity.status = ConnectContacts.STATE_MESSAGE; //属于消息
            EventBus.getDefault().post(socketEntity);

        } else {   //图片
            byte[] imgb = new byte[msgHead.getSocket_header_msgLength()];
            buf.get(imgb, 0, imgb.length);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgb, 0, imgb.length);
            SocketEntity socketEntity = new SocketEntity();
            socketEntity.dataFlag = msgHead.getSocket_header_msgFlag();  //图片是1
            socketEntity.status = ConnectContacts.STATE_MESSAGE; //属于消息
            socketEntity.bitmap = bitmap;
            EventBus.getDefault().post(socketEntity);

        }
    }
}
