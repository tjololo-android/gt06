package com.yusun.cartracker.protocol;

import com.yusun.cartracker.model.Message;
import com.yusun.cartracker.model.MessageLogin;
import com.yusun.cartracker.protocol.abs.BaseProtocolEncoder;
import com.yusun.cartracker.util.Checksum;
import com.yusun.cartracker.util.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Gt06ProtocolEncoder extends BaseProtocolEncoder{
	Logger logger = new Logger(Gt06ProtocolEncoder.class);
	@Override
	public ByteBuf encodeContent(Object msg) {		
		logger.debug("encode");
		
		ByteBuf buf = Unpooled.buffer();			
		if(msg instanceof Message){
			logger.debug("encode+++");
			
			Message m = (Message)msg;
			
			ByteBuf content = m.encode();
	        buf.writeByte(0x78);
	        buf.writeByte(0x78);	        
	        
	        buf.writeByte(1 + content.readableBytes()+2+2);
	
	        buf.writeByte(m.id);        
	           
	        buf.writeBytes(content);
	
	        buf.writeShort(m.index);
	
	        buf.writeShort(Checksum.crc16(Checksum.CRC16_X25, buf.nioBuffer(2, buf.writerIndex() - 2)));
	
	        buf.writeByte('\r');
	        buf.writeByte('\n'); 
	        
	        logger.debug("encode---");
		}
        return buf;	
	}

}
