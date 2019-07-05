package com.yusun.cartracker.protocol;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.api.Settings;
import com.yusun.cartracker.helper.Logger;
import com.yusun.cartracker.model.CMessage;

import io.netty.buffer.ByteBuf;

public class MessageRequestLbs extends CMessage{
	Logger logger = new Logger(MessageRequestLbs.class);

	public MessageRequestLbs(String phoneNum) {
		super(0x17);
		lbs = new LBS();
		this.phoneNum = phoneNum; 
		Alarm_Index = Hardware.instance().getAlarmIndex();
		language = Settings.instance().getLanguage();
	}	
	
	LBS lbs;
	String phoneNum;
	public int Alarm_Index;
	public int language;
	
	@Override
	public ByteBuf encode() {
		ByteBuf buf = super.encode();
		buf.writeBytes(lbs.encodeBasic());
		byte[] data = new byte[21];
		System.arraycopy(phoneNum.getBytes(), 0, data, 0, phoneNum.getBytes().length);
		buf.writeBytes(data);		
		buf.writeByte(Alarm_Index);
		buf.writeByte(language);
		return buf;
	}
}
