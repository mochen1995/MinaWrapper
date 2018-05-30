package com.tuyue.minawrapper.socketManage;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.textline.LineDelimiter;

import java.nio.charset.Charset;

/**
 * 项目名：TestModuleApp
 * 包名：com.tuyue.yinghuo.socketManage
 * 创建者：mmcc
 * 创建时间：2018/5/29 17:57
 * 描述：TODO
 */


public class TestEnCoder implements ProtocolEncoder {
    private final static Charset charset = Charset.forName("GBK");
    @Override
    public void encode(IoSession ioSession, Object o, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {
        IoBuffer buff = IoBuffer.allocate(100).setAutoExpand(true);
        buff.putString(o.toString(), charset.newEncoder());
        // put 当前系统默认换行符
        buff.putString(LineDelimiter.DEFAULT.getValue(), charset.newEncoder());
        // 为下一次读取数据做准备
        buff.flip();

        protocolEncoderOutput.write(buff);
    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
