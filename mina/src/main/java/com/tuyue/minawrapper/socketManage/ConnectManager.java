package com.tuyue.minawrapper.socketManage;

import android.content.Context;
import android.util.Log;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.greenrobot.eventbus.EventBus;

import java.io.FileOutputStream;
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
    public static final String BROADCAST_ACTION = "com.tuyue.yimei";

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
        mAddress = new InetSocketAddress(mConfig.getIp(), mConfig.getPort());
        //创建连接对象
        mConnection = new NioSocketConnector();
        //设置连接地址
        mConnection.setDefaultRemoteAddress(mAddress);
        mConnection.getSessionConfig().setReadBufferSize(mConfig.getReadBufferSize());
        //设置过滤
        mConnection.getFilterChain().addFirst("reconnection",new MyIoFilterAdapter());
        mConnection.getFilterChain().addLast("logger", new LoggingFilter());
//        mConnection.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ByteArrayCodecFactory()));
        //中文，数字，字母
        mConnection.getFilterChain().addLast("mycodec",new ProtocolCodecFilter(new MyTextLineCodecFactory()));
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

            SocketEntity socketEntity = new SocketEntity();
            socketEntity.status = ConnectContacts.STATE_CLOSE; //断开连接
            EventBus.getDefault().post(socketEntity);
           /* if (context != null) {
                //将接收到的消息利用广播发送出去
                Intent intent = new Intent(BROADCAST_ACTION);
                intent.putExtra(ConnectContacts.STATE_CLOSE, "断开连接");
                context.sendBroadcast(intent);
            }*/
        }

        /**
         * 连接成功时回调的方法
         *
         * @param session
         * @throws Exception
         */

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            //当与服务器连接成功时,将我们的session保存到我们的sesscionmanager类中,从而可以发送消息到服务器
            SessionManager.getmInstance().setIoSession(session);

            SocketEntity socketEntity = new SocketEntity();
            socketEntity.status = ConnectContacts.STATE_SUCCESS; //连接成功
            EventBus.getDefault().post(socketEntity);
           /* if (context != null) {
                //将接收到的消息利用广播发送出去
                Intent intent = new Intent(BROADCAST_ACTION);
                intent.putExtra(ConnectContacts.STATE_SUCCESS, "连接成功");
                context.sendBroadcast(intent);
            }*/
        }

        /**
         * 接收到消息时回调的方法
         *
         * @param session
         * @param message
         * @throws Exception
         */
        private FileOutputStream oos = null;

        @Override
        public void messageSent(IoSession session, Object message) throws Exception {
            super.messageSent(session, message);

        }

        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {

            IoBuffer ioBuffer = (IoBuffer) message;
            HandlerEvent.getInstance().handle(ioBuffer);

            /*if (oos==null)
            {
                oos = new FileOutputStream("/mnt/sdcard/YimeiPark/test.jpg",true);
            }*/
          /*  byte[] b = (byte[]) message;
            Log.e("tag", "内容大小："+b.length);*/
//            oos.write(b);

           /* byte[] head = new byte[4];
            byte[] content = new byte[b.length - 4];
            for (int i = 0; i < b.length; i++) {
                if (i < 4) {
                    Log.e("tag", "b" + i + ": " + b[i]);
                    if (b[i]!=0)
                    {
                        head[i] = b[i];
                        Log.e("tag", "头信息1:" + new String(head, Charset.forName("gbk")));
                    }
                } else {
                    content[i-4] = b[i];
                    Log.e("tag", "内容信息1：" + new String(content, Charset.forName("gbk")));
                }
            }
            Log.e("tag", "头2：" + new String(head, Charset.forName("gbk")));
            Log.e("tag", "内容信息2：" + new String(content, Charset.forName("gbk")));*/
         /*   if (context != null) {
                //将接收到的消息利用广播发送出去
                Intent intent = new Intent(BROADCAST_ACTION);
                intent.putExtra(ConnectContacts.STATE_MESSAGE, new String(b, Charset.forName("gbk")));
                //接收图片
//                Bundle bundle = new Bundle();
//                bundle.putByteArray("img",b);
//                intent.putExtras(bundle);

          *//*      Bitmap bitmap1 = BitmapFactory.decodeFile("/mnt/sdcard/tencent/MicroMsg/WeiXin/wx_camera_1527593649900.jpg");
                Bundle bundle = new Bundle();
                bundle.putParcelable("img",bitmap1);
                intent.putExtras(bundle);*//*


                context.sendBroadcast(intent);
            }*/
        }
    }

    /**
     * 与服务器连接的方法
     *
     * @return
     */
    public boolean connect() {
        try {
            ConnectFuture future = mConnection.connect();
            future.awaitUninterruptibly();
            mSessioin = future.getSession();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return mSessioin == null ? false : true;
    }

    /**
     * 断开连接的方法
     */
    public void disConnect() {
        mConnection.dispose();
        mConnection = null;
        mSessioin = null;
        mAddress = null;
        mContext = null;
    }


    //编码
    public class ByteArrayEncoder extends ProtocolEncoderAdapter {

        @Override
        public void encode(IoSession session, Object message,
                           ProtocolEncoderOutput out) {
            out.write(message);
            out.flush();
        }
    }

    //解码
    public class ByteArrayDecoder extends ProtocolDecoderAdapter {
        @Override
        public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
                throws Exception {
            int limit = in.limit();
            byte[] bytes = new byte[limit];
            in.get(bytes);
            out.write(bytes);
        }
    }

    //工厂
    public class ByteArrayCodecFactory implements ProtocolCodecFactory {
        private ByteArrayDecoder decoder;
        private ByteArrayEncoder encoder;

        public ByteArrayCodecFactory() {
            encoder = new ByteArrayEncoder();
            decoder = new ByteArrayDecoder();
        }

        @Override
        public ProtocolDecoder getDecoder(IoSession session) throws Exception {
            return decoder;
        }

        @Override
        public ProtocolEncoder getEncoder(IoSession session) throws Exception {
            return encoder;
        }
    }

    private  class MyIoFilterAdapter extends IoFilterAdapter {
        @Override
        public void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception {
            Log.e("tag", "连接关闭，每隔5秒进行重新连接");
            SocketEntity socketEntity = new SocketEntity();
            socketEntity.status = ConnectContacts.STATE_RECONNECT; //正在重连
            EventBus.getDefault().post(socketEntity);
            for(;;){
                if(mConnection==null){
                    break;
                }
                if(connect()){
                    socketEntity.status = ConnectContacts.STATE_SUCCESS; //正在重连
                    EventBus.getDefault().post(socketEntity);
                    Log.e("tag", "断线重连[" + mConnection.getDefaultRemoteAddress().getHostName() + ":" +
                            mConnection.getDefaultRemoteAddress().getPort() + "]成功");
                    break;
                }else {

                }
                Thread.sleep(5000);
            }
        }

    }
}
