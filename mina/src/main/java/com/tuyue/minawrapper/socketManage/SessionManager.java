package com.tuyue.minawrapper.socketManage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.tuyue.minawrapper.socketManage.domain.MinaMsgHead;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 项目名：TestModuleApp
 * 包名：com.tuyue.yinghuo
 * 创建者：mmcc
 * 创建时间：2018/5/29 14:20
 * 描述：session管理类,通过ioSession与服务器通信
 */


public class SessionManager {
    private static  SessionManager mInstance = null;

    private IoSession ioSession;//最终与服务器 通信的对象

    public static SessionManager getmInstance(){
        if (mInstance == null) {
            synchronized (SessionManager.class) {
                if (mInstance == null) {
                    mInstance = new SessionManager();
                }
            }
        }
        return mInstance;
    }
    private SessionManager(){

    }

    public void setIoSession(IoSession ioSession) {
        this.ioSession = ioSession;
    }

    public IoSession getIoSession(){
        return ioSession;
    }
    /**
     * 将对象写到服务器
     */
    public void writeToServer(Object msg) {
        if (ioSession != null) {
            ioSession.write(msg);
        }
    }

    //写入byte数组 1内容 2内容长度 ，3内容类型  1消息 2图片
    public void write(byte[] data,int flag){
        byte[] sendHeader = new byte[264];
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("socket_client_type",2);
            jsonObject.put("socket_header_msgLength",data.length);
            jsonObject.put("socket_header_msgFlag",flag);
            jsonObject.put("socket_headerFileName",null);
            String header = jsonObject.toString();
//        String header = "{\"socket_client_type\": 2,\"socket_header_msgLength\":"+data.length+",\"socket_header_msgFlag\":"+flag+",\"socket_headerFileName\":" + fileName +"}";
            Log.e("tag", "header = "+header);
            byte[] headerBytes = header.getBytes();
            for (int i = 0; i < headerBytes.length; i++) {
                sendHeader[i] = headerBytes[i];
            }
            if (ioSession!=null)
            {
                ioSession.write(IoBuffer.wrap(sendHeader));
                ioSession.write(IoBuffer.wrap(data));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
       /* if (type==1&&data.length!=4)
        {
            byte[] b = new byte[4];
            for (int i = 0; i < data.length; i++) {
                b[i] = data[i];
            }
            if (ioSession != null){
                ioSession.write(IoBuffer.wrap(b));//关键，传递数组的关键
            }
        }else if (type==2)
        {
            if (ioSession != null){
                ioSession.write(IoBuffer.wrap(data));//关键，传递数组的关键
            }
        }*/
    }
    public void writeToServer(IoBuffer buffer){
        if(ioSession!=null){
            buffer.flip();
            ioSession.write(buffer);
        }
    }
    public void writeFile(String path){
        File file = new File(path);
        if (file.exists())
        {
            Log.e("tag", "path = "+file.getAbsolutePath());
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] sendHeader = new byte[264];

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("socket_client_type",2);
                jsonObject.put("socket_header_msgLength",baos.size());
                jsonObject.put("socket_header_msgFlag",1);
                jsonObject.put("socket_headerFileName",file.getName());
                String header = jsonObject.toString();
//                String header = "{\"socket_client_type\": 2,\"socket_header_msgLength\":"+baos.size()+",\"socket_header_msgFlag\":"+1+",\"socket_headerFileName\":" + file.getName() +"}";
                Log.e("tag", "header = "+header);
                byte[] headerBytes = header.getBytes();
                for (int i = 0; i < headerBytes.length; i++) {
                    sendHeader[i] = headerBytes[i];
                }
                if (ioSession != null){
                    ioSession.write(IoBuffer.wrap(sendHeader));
                    ioSession.write(IoBuffer.wrap(baos.toByteArray()));//关键，传递数组的关键
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else {
            Log.e("tag", "文件不存在");
        }

    }
    public void writeMsg(MinaMsgHead head,byte[] content){
        /**
         * 假定消息格式为：消息头（一个short类型：表示事件号、一个int类型：表示消息体的长度）+消息体
         */
      /*  MinaMsgHead msgHead = new MinaMsgHead();
        msgHead.event = Event.EV_C_S_TEST;
        msgHead.bodyLen = 0;//因为消息体是空的所以填0，根据消息体的长度而变*/

        Log.e("tag", "发送的头："+head);
        Log.e("tag", "发送的内容长度："+content.length);
        //创建一个缓冲，缓冲大小为:消息头长度(6位)+消息体长度
        IoBuffer buffer = IoBuffer.allocate(8+head.bodyLen);
        //把消息头put进去
        buffer.putInt(head.bodyLen);
        buffer.putInt(head.event);
        //把消息体put进去
        buffer.put(content);
        //发送
        writeToServer(buffer);
    }

    /**
     * 关闭连接
     */
    public void closeSession() {
        if (ioSession != null) {
            ioSession.closeOnFlush();
        }
    }

    public void removeSession() {
        ioSession = null;
    }
}
