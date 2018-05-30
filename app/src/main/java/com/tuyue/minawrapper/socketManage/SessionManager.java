package com.tuyue.minawrapper.socketManage;

import org.apache.mina.core.session.IoSession;

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
