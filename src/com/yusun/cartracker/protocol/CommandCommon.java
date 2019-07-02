package com.yusun.cartracker.protocol;

import com.yusun.cartracker.model.CMessage;
import com.yusun.cartracker.model.Command;

import io.netty.buffer.ByteBuf;

public class CommandCommon extends Command{	
	public CommandCommon(){
		super(0x80);
	}	
	public void decode(ByteBuf buf) {
		String command = "DW";
		CMessage msg = new MessageLogin();	//NG
		msg.sendToTarget();
	}
}