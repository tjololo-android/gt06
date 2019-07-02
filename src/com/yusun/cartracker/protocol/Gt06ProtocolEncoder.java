package com.yusun.cartracker.protocol;

import com.yusun.cartracker.helper.Checksum;
import com.yusun.cartracker.helper.Logger;
import com.yusun.cartracker.model.CMessage;
import com.yusun.cartracker.protocol.abs.BaseProtocolEncoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Gt06ProtocolEncoder extends BaseProtocolEncoder{
	Logger logger = new Logger(Gt06ProtocolEncoder.class);
	@Override
	public ByteBuf encodeContent(Object msg) {		
		logger.debug("encode");
		
		ByteBuf buf = Unpooled.buffer();			
		if(msg instanceof CMessage){
			logger.debug("encode+++");
			
			CMessage m = (CMessage)msg;
			
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
