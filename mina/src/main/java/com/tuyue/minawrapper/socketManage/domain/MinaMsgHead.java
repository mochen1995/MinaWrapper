package com.tuyue.minawrapper.socketManage.domain;

/**
 * 项目名：MinaWrapper
 * 包名：com.tuyue.minawrapper.socketManage.domain
 * 创建者：mmcc
 * 创建时间：2018/5/31 9:13
 * 描述： mina消息头（6位)
 */


public class MinaMsgHead {
    public int event;//消息事件号 2位
    public int bodyLen;//消息内容长度 4位

    @Override
    public String toString() {
        return "MinaMsgHead{" +
                "event=" + event +
                ", bodyLen=" + bodyLen +
                '}';
    }
}
