package com.tuyue.minawrapper.socketManage;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

/**
 * 项目名：TestModuleApp
 * 包名：com.tuyue.yinghuo
 * 创建者：mmcc
 * 创建时间：2018/5/29 14:19
 * 描述：开启长连接的服务
 */


public class ConnectService extends Service {
    private ConnectThred connectThred;
    private boolean isStart = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //使用子线程开启连接
        isStart = true;
        connectThred = new ConnectThred("mina", getApplicationContext());
        connectThred.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        connectThred.disConnection();
        connectThred = null;
        isStart = false;
    }

    /**
     * 负责调用connectmanager类来完成与服务器的连接
     */
    class ConnectThred extends HandlerThread {

        boolean isConnection;
        ConnectManager mManager;

        public ConnectThred(String name, Context context) {
            super(name);
            //创建配置文件类
            ConnectConfig config = new ConnectConfig.Builder(context)
                    .setIp(ConnectContacts.IP)
                    .setPort(ConnectContacts.PORT)
                    .setReadBufferSize(10240)
                    .setConnectionTimeout(10000)
                    .bulid();
            //创建连接的管理类
            mManager = new ConnectManager(config);
        }

        @Override
        protected void onLooperPrepared() {
            //利用循环请求连接
            while (isStart) {
                isConnection = mManager.connect();
                if (isConnection) {
                    //当请求成功的时候,跳出循环
                    break;
                }
                SocketEntity socketEntity = new SocketEntity();
                socketEntity.status = ConnectContacts.STATE_WAIT; //正在连接
                EventBus.getDefault().post(socketEntity);
             /*   Intent intent = new Intent(ConnectManager.BROADCAST_ACTION);
                intent.putExtra(ConnectContacts.STATE_WAIT,"正在连接服务器...");
                sendBroadcast(intent);*/
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {

                }
            }
        }

        /**
         * 断开连接
         */
        public void disConnection() {
            mManager.disConnect();
        }

    }
}
