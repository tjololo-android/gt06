package com.yusun.cartracker.protocol;

import java.nio.charset.Charset;

import com.yusun.cartracker.model.Command;
import com.yusun.cartracker.model.sms.abs.SMS;

import io.netty.buffer.ByteBuf;

public class CommandPositionEn extends Command{
	String address;
	String phoneNum;
	public CommandPositionEn(){
		super(0x97);
	}	
	public void decode(ByteBuf buf) {
		int len = buf.readableBytes();
		int m = len - 50;
		int index = 19;
		buf.skipBytes(index);
		address = buf.readBytes(m).toString(Charset.forName("utf-8")).trim();
		buf.skipBytes(2);
		phoneNum = buf.readBytes(21).toString(Charset.forName("ascii")).trim();		
	}
	@Override
	public void doCmd() {
		SMS.sendSms(phoneNum, address);
	}
}