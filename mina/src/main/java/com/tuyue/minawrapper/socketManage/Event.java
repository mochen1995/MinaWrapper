package com.tuyue.minawrapper.socketManage;

/**
 * 项目名：MinaWrapper
 * 包名：com.tuyue.minawrapper.socketManage
 * 创建者：mmcc
 * 创建时间：2018/5/31 9:13
 * 描述：与服务器协议好的事件号
 */


public class Event {
    /**
     * 客户端发送给服务器头信息
     */
    public final static int EV_SEND_MESSAGE= 1;  //表发送消息
    public final static int EV_SEND_IMG= 2;  //表发送图片消息
    /**
     * 服务器回应给客户端的头信息 DataFlag -》 101 表需要内容  102 表需要图片
     */
    public final static short EV_RECIEVE_MESSAGE = (short)101;  //接收消息时

    public final static short EV_RECIEVE_IMG = (short)102;  //接收图片时
}
