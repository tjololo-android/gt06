package com.yusun.cartracker.protocol;

import java.nio.charset.Charset;

import com.yusun.cartracker.model.Command;
import com.yusun.cartracker.model.sms.abs.SMS;
import com.yusun.cartracker.model.sms.abs.MSG;

import io.netty.buffer.ByteBuf;

public class CommandPositionCh extends Command{
	String address;
	String phoneNum;
	public CommandPositionCh(){
		super(0x17);
	}	
	public void decode(ByteBuf buf) {
		int len = buf.readableBytes();
		int m = len - 49;
		int index = 18;
		buf.skipBytes(index);
		address = buf.readBytes(m).toString(Charset.forName("unicode")).trim();
		buf.skipBytes(2);
		phoneNum = buf.readBytes(21).toString(Charset.forName("ascii")).trim();		
	}
	@Override
	public void doCmd() {
		SMS.sendMsg(phoneNum, address);
	}
}