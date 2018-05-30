package com.tuyue.minawrapper.socketManage;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;

/**
 * 项目名：TestModuleApp
 * 包名：com.tuyue.yinghuo
 * 创建者：mmcc
 * 创建时间：2018/5/29 14:19
 * 描述：连接的管理类
 */


public class ConnectManager {
    public static final String BROADCAST_ACTION="com.tuyue.yimei";

    private ConnectConfig mConfig;//配置文件
    private WeakReference<Context> mContext;
    private NioSocketConnector mConnection;
    private IoSession mSessioin;
    private InetSocketAddress mAddress;

    public ConnectManager(ConnectConfig mConfig) {
        this.mConfig = mConfig;
        this.mContext = new WeakReference<Context>(mConfig.getmContext());
        init();
    }

    private void init() {
        mAddress = new InetSocketAddress(mConfig.getIp(),mConfig.getPort());
        //创建连接对象
        mConnection = new NioSocketConnector();
        //设置连接地址
        mConnection.setDefaultRemoteAddress(mAddress);
        mConnection.getSessionConfig().setReadBufferSize(mConfig.getReadBufferSize());
        //设置过滤
        mConnection.getFilterChain().addLast("logger",new LoggingFilter());
        mConnection.getFilterChain().addLast("codec",new ProtocolCodecFilter(new MyTextLineCodecFactory()));
//        mConnection.getFilterChain().addLast("codec",new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("utf-8"))));
//        mConnection.getFilterChain().addLast("codec",new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        //设置连接监听
        mConnection.setHandler(new DefaultHandler(mContext.get()));
    }

    private static class DefaultHandler extends IoHandlerAdapter {
        private Context context;

        public DefaultHandler(Context context) {
            this.context = context;
        }

        @Override
        public void sessionClosed(IoSession session) throws Exception {
            super.sessionClosed(session);
            if (context != null) {
                //将接收到的消息利用广播发送出去
                Intent intent = new Intent(BROADCAST_ACTION);
                intent.putExtra(ConnectContacts.STATE_CLOSE,"断开连接");
                context.sendBroadcast(intent);
            }
        }

        /**
         * 连接成功时回调的方法
         * @param session
         * @throws Exception
         */

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            //当与服务器连接成功时,将我们的session保存到我们的sesscionmanager类中,从而可以发送消息到服务器
            SessionManager.getmInstance().setIoSession(session);

            if (context != null) {
                //将接收到的消息利用广播发送出去
                Intent intent = new Intent(BROADCAST_ACTION);
                intent.putExtra(ConnectContacts.STATE_SUCCESS,"连接成功");
                context.sendBroadcast(intent);
            }
        }
        /**
         * 接收到消息时回调的方法
         * @param session
         * @param message
         * @throws Exception
         */
        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            Log.e("tag", "收到消息："+message);
            if (context != null) {
                //将接收到的消息利用广播发送出去
                Intent intent = new Intent(BROADCAST_ACTION);
                intent.putExtra(ConnectContacts.STATE_MESSAGE,message.toString());
                context.sendBroadcast(intent);
            }
        }
    }

    /**
     * 与服务器连接的方法
     * @return
     */
    public boolean connect(){
        try{
            ConnectFuture future =mConnection.connect();
            future.awaitUninterruptibly();
            mSessioin = future.getSession();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return mSessioin==null?false:true;
    }

    /**
     * 断开连接的方法
     */
    public void disConnect(){
        mConnection.dispose();
        mConnection=null;
        mSessioin=null;
        mAddress=null;
        mContext=null;
    }
}
