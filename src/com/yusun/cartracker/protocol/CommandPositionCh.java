package com.yusun.cartracker.protocol;

import java.nio.charset.Charset;

import com.yusun.cartracker.model.Command;
import com.yusun.cartracker.model.sms.abs.SMS;

import io.netty.buffer.ByteBuf;

public class CommandPositionCh extends Command{	
	public CommandPositionCh(){
		super(0x17);
	}	
	public void decode(ByteBuf buf) {
		int index = 1;
		buf.skipBytes(index);
		String address = buf.readBytes(21).toString(Charset.forName("ascii"));
		buf.skipBytes(6);
		String phoneNum = buf.readBytes(21).toString(Charset.forName("ascii"));
		SMS.sendSms(phoneNum, address);
	}
}