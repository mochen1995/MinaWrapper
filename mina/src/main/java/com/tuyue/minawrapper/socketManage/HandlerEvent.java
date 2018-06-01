package com.tuyue.minawrapper.socketManage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.tuyue.minawrapper.socketManage.domain.CommonUtils;
import com.tuyue.minawrapper.socketManage.domain.MinaMsgHead;

import org.apache.mina.core.buffer.IoBuffer;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

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


public class HandlerEvent {
    private static HandlerEvent handlerEvent;
    public static HandlerEvent getInstance() {
        if (handlerEvent == null) {
            handlerEvent = new HandlerEvent();
        }
        return handlerEvent;
    }
    public void handle(IoBuffer buf) throws IOException, InterruptedException, UnsupportedEncodingException, SQLException {
        //解析包头
        MinaMsgHead msgHead = new MinaMsgHead();
//        msgHead.bodyLen = buf.getInt();//包体长度
        byte[] headLen = new byte[4];
        buf.get(headLen); //取四个字节,先获取包体数据长度值
        int len = CommonUtils.bytesToInt(headLen, 0);
        /*int len = 0;
        for (byte b : headLen) {
            Log.e("tag", " b = "+b);
            len+=b;
        }*/
        msgHead.bodyLen = len;
        byte[] headEvLen = new byte[4];
        buf.get(headEvLen); //取四个字节,先获取包体数据长度值
        int len2 = CommonUtils.bytesToInt(headEvLen,0);
        /*int len2 = 0;
        for (byte b : headEvLen) {
            Log.e("tag", " b = "+b);
            len2+=b;
        }*/
        msgHead.event = len2;//事件号

        Log.e("tag", "消息头："+msgHead);
        switch (msgHead.event){//根据事件号解析消息体内容
            case Event.EV_RECIEVE_MESSAGE:   //当前事件是消息
                byte[] by = new byte[msgHead.bodyLen];
                buf.get(by, 0, by.length);
                String json = new String(by, "GBK").trim();
                //接下来根据业务处理....
                Log.e("tag", "消息内容："+json);
                //{"DataFlag":101, "Data":null, "Code":0 }
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int dataFlag = jsonObject.optInt("DataFlag");
                    SocketEntity socketEntity = new SocketEntity();
                    socketEntity.dataFlag = dataFlag;
                    socketEntity.content = json;
                    socketEntity.status = ConnectContacts.STATE_MESSAGE; //属于消息
                    EventBus.getDefault().post(socketEntity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Event.EV_RECIEVE_IMG:
                byte[] imgb = new byte[msgHead.bodyLen];
                buf.get(imgb, 0, imgb.length);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imgb, 0, imgb.length);
                SocketEntity socketEntity = new SocketEntity();
                socketEntity.dataFlag =  Event.EV_RECIEVE_IMG;
                socketEntity.status = ConnectContacts.STATE_MESSAGE; //属于消息
                socketEntity.bitmap = bitmap;
                EventBus.getDefault().post(socketEntity);
                break;
        }
    }
}
