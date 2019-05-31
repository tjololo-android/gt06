package com.yusun.cartracker.protocol;

import com.yusun.cartracker.model.Message;
import com.yusun.cartracker.model.MessageLogin;
import com.yusun.cartracker.protocol.abs.BaseProtocolEncoder;
import com.yusun.cartracker.util.Checksum;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Gt06ProtocolEncoder extends BaseProtocolEncoder{
	@Override
	public ByteBuf encodeContent(Object msg) {
		ByteBuf buf = Unpooled.buffer();			
		if(msg instanceof Message){
			
			ByteBuf content = ((MessageLogin) msg).encode();
	        buf.writeByte(0x78);
	        buf.writeByte(0x78);	        
	        
	        buf.writeByte(1 + content.readableBytes()+2+2); // message length
	
	        buf.writeByte(((Message) msg).id); // message type        
	           
	        buf.writeBytes(content); // command
	
	        buf.writeShort(((Message) msg).index); // message index
	
	        buf.writeShort(Checksum.crc16(Checksum.CRC16_X25, buf.nioBuffer(2, buf.writerIndex() - 2)));
	
	        buf.writeByte('\r');
	        buf.writeByte('\n');        
		}
        return buf;	
	}

}
