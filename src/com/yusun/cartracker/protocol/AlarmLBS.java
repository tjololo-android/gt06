package com.yusun.cartracker.protocol;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.CMessage;

import io.netty.buffer.ByteBuf;

public class AlarmLBS extends CMessage{
	public AlarmLBS() {
		super(0x19);
		lbs = new LBS();
		deviceStatus = new DeviceStatus();
		Alarm_Index = Hardware.instance().getAlarmIndex();
	}

	public LBS lbs;
	public DeviceStatus deviceStatus;
	public int voltage;
	public int signal;
	public int Alarm_Index;
	public int language;
	
	@Override
	public ByteBuf encode() {
		ByteBuf buf = super.encode();		
		buf.writeBytes(lbs.encode());
		buf.writeByte(deviceStatus.encode());
		buf.writeByte(voltage);
		buf.writeByte(signal);
		buf.writeByte(Alarm_Index);
		buf.writeByte(language);
		return buf;
	}
}
