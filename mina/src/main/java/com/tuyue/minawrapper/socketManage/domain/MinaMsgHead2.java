package com.tuyue.minawrapper.socketManage.domain;

/**
 * 项目名：MinaWrapper
 * 包名：com.tuyue.minawrapper.socketManage.domain
 * 创建者：mmcc
 * 创建时间：2018/5/31 9:13
 * 描述： mina消息头（6位)
 */


public class MinaMsgHead2 {

    /**
     * socket_client_type : 1
     * socket_headerFileName :
     * socket_header_msgFlag : 102
     * socket_header_msgLength : 75
     */

    private int socket_client_type;
    private String socket_headerFileName;
    private int socket_header_msgFlag;
    private int socket_header_msgLength;

    public int getSocket_client_type() {
        return socket_client_type;
    }

    public void setSocket_client_type(int socket_client_type) {
        this.socket_client_type = socket_client_type;
    }

    public String getSocket_headerFileName() {
        return socket_headerFileName;
    }

    public void setSocket_headerFileName(String socket_headerFileName) {
        this.socket_headerFileName = socket_headerFileName;
    }

    public int getSocket_header_msgFlag() {
        return socket_header_msgFlag;
    }

    public void setSocket_header_msgFlag(int socket_header_msgFlag) {
        this.socket_header_msgFlag = socket_header_msgFlag;
    }

    public int getSocket_header_msgLength() {
        return socket_header_msgLength;
    }

    public void setSocket_header_msgLength(int socket_header_msgLength) {
        this.socket_header_msgLength = socket_header_msgLength;
    }

    @Override
    public String toString() {
        return "MinaMsgHead2{" +
                "socket_client_type=" + socket_client_type +
                ", socket_headerFileName='" + socket_headerFileName + '\'' +
                ", socket_header_msgFlag=" + socket_header_msgFlag +
                ", socket_header_msgLength=" + socket_header_msgLength +
                '}';
    }
}
