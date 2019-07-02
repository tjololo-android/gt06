package com.yusun.cartracker.protocol;

import com.yusun.cartracker.helper.Logger;
import com.yusun.cartracker.model.Command;
import com.yusun.cartracker.model.CommandFactory;
import com.yusun.cartracker.protocol.abs.BaseProtocolDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public class Gt06ProtocolDecoder extends BaseProtocolDecoder{
	Logger logger = new Logger(Gt06ProtocolDecoder.class);
	Gt06CommandFactory CommandFactory = new Gt06CommandFactory();
	
    @Override
    protected Object decode(Channel channel, Object msg) throws Exception {
    	
    	//logger.info("receive data: "+ ByteBufUtil.hexDump((ByteBuf) msg));

        ByteBuf buf = (ByteBuf) msg;
        
        buf.markReaderIndex();

        int header = buf.readShort();

        if (header == 0x7878) {
           
        } else if (header == 0x7979) {
           
        }
        
        buf.skipBytes(1);
        int type = buf.readByte();  
        
        buf.resetReaderIndex();        
        Command cmd = CommandFactory.newCommand(type);
        if(null != cmd){
        	cmd.decode(buf);
        }else{
        	onReceiveCmd(type, buf);
        }
        return null;
    }
}
