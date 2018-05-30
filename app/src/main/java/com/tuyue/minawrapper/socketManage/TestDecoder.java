package com.tuyue.minawrapper.socketManage;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.nio.charset.Charset;

/**
 * 项目名：TestModuleApp
 * 包名：com.tuyue.yinghuo.socketManage
 * 创建者：mmcc
 * 创建时间：2018/5/29 17:39
 * 描述：TODO
 */


public class TestDecoder implements ProtocolDecoder {

    private final static Charset charset = Charset.forName("GBK");
    // 可变的IoBuffer数据缓冲区
    private IoBuffer buff = IoBuffer.allocate(100).setAutoExpand(true);
    @Override
    public void decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        // 如果有消息
//        Log.e("tag", "ioBuffer.hasRemaining() = "+ioBuffer.hasRemaining());
        while (ioBuffer.hasRemaining()) {
            // 判断消息是否是结束符，不同平台的结束符也不一样；
            // windows换行符（\r\n）就认为是一个完整消息的结束符了； UNIX 是\n；MAC 是\r
            byte b = ioBuffer.get();
//            Log.e("tag", "b = "+b);
            if (b == '\n') {
                buff.flip();
                byte[] bytes = new byte[buff.limit()];
                buff.get(bytes);
                String message = new String(bytes, charset);

                buff = IoBuffer.allocate(100).setAutoExpand(true);

                // 如果结束了，就写入转码后的数据
                protocolDecoderOutput.write(message);
                //log.info("message: " + message);
            } else {
                buff.put(b);
            }
        }
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
