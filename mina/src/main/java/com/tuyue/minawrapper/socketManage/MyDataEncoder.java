package com.tuyue.minawrapper.socketManage;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * 项目名：MinaWrapper
 * 包名：com.tuyue.minawrapper.socketManage
 * 创建者：mmcc
 * 创建时间：2018/5/31 8:56
 * 描述：编码器将数据直接发出去(不做处理)
 */


public class MyDataEncoder extends ProtocolEncoderAdapter{
    @Override
    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput out) throws Exception {
        IoBuffer value = (IoBuffer) message;
        out.write(value);
        out.flush();
    }
}
