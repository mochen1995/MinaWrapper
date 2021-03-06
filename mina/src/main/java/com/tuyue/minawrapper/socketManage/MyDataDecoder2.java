package com.tuyue.minawrapper.socketManage;

import android.util.Log;

import com.google.gson.Gson;
import com.tuyue.minawrapper.socketManage.domain.MinaMsgHead2;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * 项目名：MinaWrapper
 * 包名：com.tuyue.minawrapper.socketManage
 * 创建者：mmcc
 * 创建时间：2018/5/31 8:53
 * 描述：TODO
 */


public class MyDataDecoder2 extends CumulativeProtocolDecoder {

    /**
     * 返回值含义:
     * 1、当内容刚好时，返回false，告知父类接收下一批内容
     * 2、内容不够时需要下一批发过来的内容，此时返回false，这样父类 CumulativeProtocolDecoder
     * 会将内容放进IoSession中，等下次来数据后就自动拼装再交给本类的doDecode
     * 3、当内容多时，返回true，因为需要再将本批数据进行读取，父类会将剩余的数据再次推送本类的doDecode方法
     */
    @Override
    protected boolean doDecode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        /**
         * 假定消息格式为：消息头（int类型：表示消息体的长度、short类型：表示事件号）+消息体
         */
        int remaining = in.remaining();
        Log.e("tag", "收到内容大小 = "+remaining);
        if (remaining < 264)//是用来当拆包时候剩余长度小于264的时候的保护，不加容易出错
        {
            return false;
        }
        if (in.remaining() > 1) {
            //以便后继的reset操作能恢复position位置
            in.mark();
            Log.e("tag", "进入获取数据");
            //前6字节是包头，一个int和一个short，我们先取一个int
//            int len = in.getShort();//先获取包体数据长度值
            byte[] headJson = new byte[264];
            in.get(headJson); //取四个字节,先获取包体数据长度值
            /*int testLen = CommonUtils.bytesToInt(headLen,0);
            Log.e("tag", "测试长度为："+testLen);*/
            String hjson = new String(headJson, "GBK").trim();
            Log.e("tag", hjson);
            MinaMsgHead2 msgHead = new Gson().fromJson(hjson, MinaMsgHead2.class);
            int len = msgHead.getSocket_header_msgLength();
           /* for (byte b : headLen) {
                Log.e("tag", " b = "+b);
                len+=b;
            }*/
            Log.e("tag", "应该接收包体长度1 = "+len);
            //比较消息长度和实际收到的长度是否相等，这里-2是因为我们的消息头有个short值还去取
            int shoudLen = in.remaining();
            Log.e("tag", "当前获得的包体长度 = "+shoudLen);
            if (len > shoudLen) {
                Log.e("tag", "出现断包");
                //出现断包，则重置恢复position位置到操作前,进入下一轮, 接收新数据，以拼凑成完整数据
                in.reset();
                return false;
            } else {
                Log.e("tag", "内容充足");
                //消息内容足够
                in.reset();//重置恢复position位置到操作前
                int sumLen = 264 + len;//总长 = 包头+包体
                byte[] packArr = new byte[sumLen];
                in.get(packArr, 0, sumLen);
                IoBuffer buffer = IoBuffer.allocate(sumLen);
                buffer.put(packArr);
                buffer.flip();
                out.write(buffer);
                //走到这里会调用DefaultHandler的messageReceived方法

                if (in.remaining() > 0) {//出现粘包，就让父类再调用一次，进行下一次解析
                    return true;
                }
            }
        }
        return false;//处理成功，让父类进行接收下个包
    }
}
