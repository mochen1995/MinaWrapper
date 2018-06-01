package com.tuyue.minawrapper.socketManage;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * 项目名：TestModuleApp
 * 包名：com.tuyue.yinghuo.socketManage
 * 创建者：mmcc
 * 创建时间：2018/5/29 17:27
 * 描述：TODO
 */


public class MyTextLineCodecFactory implements ProtocolCodecFactory {

    private final MyDataEncoder encoder;
    private final MyDataDecoder decoder;

    public MyTextLineCodecFactory() {
        // 使用自定义编码/解码类
        encoder = new MyDataEncoder();
        decoder = new MyDataDecoder();

    }
    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }
    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }
}
