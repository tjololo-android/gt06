package com.yusun.cartracker.protocol;

import java.nio.charset.Charset;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.model.Command;

import io.netty.buffer.ByteBuf;

public class CommandCommon extends Command{	
	public CommandCommon(){
		super(0x80);
	}	
	public void decode(ByteBuf buf) {
		buf.skipBytes(2);
		int len = buf.readUnsignedByte();
		buf.skipBytes(1);		
		int flag = buf.readInt();		
		String content = buf.readBytes(len-17).toString(Charset.forName("ascii")).trim();
		AppContext.instance().getSmsCmdManager().addGprsReq(content, flag);
	}
	@Override
	public void doCmd() {
	
		
	}
}