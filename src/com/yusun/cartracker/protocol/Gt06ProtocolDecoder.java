package com.yusun.cartracker.protocol;

import com.yusun.cartracker.protocol.abs.BaseProtocolDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public class Gt06ProtocolDecoder extends BaseProtocolDecoder{
    @Override
    protected Object decode(Channel channel, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;

        int header = buf.readShort();

        if (header == 0x7878) {
           
        } else if (header == 0x7979) {
           
        }
        
        int len = buf.readByte();
        int type = buf.readByte();      
        
        onReceiveCmd(type, null);	//TODO

        return null;
    }
}
