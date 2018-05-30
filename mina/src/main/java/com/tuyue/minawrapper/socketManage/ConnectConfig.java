package com.tuyue.minawrapper.socketManage;

import android.content.Context;

/**
 * 项目名：TestModuleApp
 * 包名：com.tuyue.yinghuo
 * 创建者：mmcc
 * 创建时间：2018/5/29 14:20
 * 描述：连接的配置文件
 */


public class ConnectConfig {
    private Context mContext;
    private String ip;
    private int port;
    private int readBufferSize; //缓存大小
    private long connectionTimeout;//连接超时时间

    public Context getmContext() {
        return mContext;
    }



    public String getIp() {
        return ip;
    }



    public int getPort() {
        return port;
    }


    public int getReadBufferSize() {
        return readBufferSize;
    }



    public long getConnectionTimeout() {
        return connectionTimeout;
    }



    public static class Builder{
        private Context mContext;
        private String ip="";
        private int port=8888;
        private int readBufferSize=10240; //缓存大小
        private long connectionTimeout=10000;//连接超时时间


        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public Builder setConnectionTimeout(long connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder setmContext(Context mContext) {
            this.mContext = mContext;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setReadBufferSize(int readBufferSize) {
            this.readBufferSize = readBufferSize;
            return this;
        }

        public ConnectConfig bulid(){
            ConnectConfig connectConfig = new ConnectConfig();
            connectConfig.connectionTimeout = this.connectionTimeout;
            connectConfig.ip = this.ip;
            connectConfig.port = this.port;
            connectConfig.mContext = this.mContext;
            connectConfig.readBufferSize = this.readBufferSize;
            return  connectConfig;
        }
    }
}
