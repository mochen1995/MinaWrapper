package com.tuyue.minawrapper.socketManage;

import android.graphics.Bitmap;

/**
 * 项目名：TestModuleApp
 * 包名：com.tuyue.yinghuo.socketManage
 * 创建者：mmcc
 * 创建时间：2018/5/29 15:54
 * 描述：TODO
 */


public class SocketEntity {
    public String status;   //建立连接的状态 ，开始，连接中，断开，发消息等等
    public int dataFlag;   //表操作，登录，注册 ，或者 接收图片
    public String content;  //整体json
    public int code;    //待定
    public Bitmap bitmap;   //图片
}
