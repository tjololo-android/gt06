package com.yusun.cartracker.protocol;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.helper.Logger;
import com.yusun.cartracker.model.CMessage;
import com.yusun.cartracker.position.Position;

import io.netty.buffer.ByteBuf;

public class MessageRequestPosition extends CMessage{
	Logger logger = new Logger(MessageRequestPosition.class);

	public MessageRequestPosition(Position pos, String phoneNum) {
		super(0x2a);
		gps = new GPS(pos);
		this.phoneNum = phoneNum; 
		Alarm_Index = Hardware.instance().getAlarmIndex();
		language = Hardware.instance().getLanguage();
	}	
	
	GPS gps;
	String phoneNum;
	public int Alarm_Index;
	public int language;
	
	@Override
	public ByteBuf encode() {
		ByteBuf buf = super.encode();
		buf.writeBytes(gps.encode());
		byte[] data = new byte[21];
		System.arraycopy(phoneNum.getBytes(), 0, data, 0, phoneNum.getBytes().length);
		buf.writeBytes(data);		
		buf.writeByte(Alarm_Index);
		buf.writeByte(language);
		return buf;
	}
}
