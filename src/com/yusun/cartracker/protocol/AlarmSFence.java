package com.yusun.cartracker.protocol;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.api.Settings;
import com.yusun.cartracker.model.CMessage;
import com.yusun.cartracker.position.Position;

import io.netty.buffer.ByteBuf;

public class AlarmSFence extends CMessage{
	public AlarmSFence(int id, Position pos) {
		super(id);
		gps = new GPS(pos);
		lbs = new LBS();
		deviceStatus = new DeviceStatus();
		voltage = Hardware.instance().getBATTERY();
		signal = Hardware.instance().getSIGNAL();
		language = Settings.instance().getLanguage();
		Alarm_Index = Hardware.instance().getAlarmIndex();
	}
	public GPS gps;
	public LBS lbs;
	public DeviceStatus deviceStatus;
	public int voltage;
	public int signal;
	public int Alarm_Index;
	public int language;
	
	@Override
	public ByteBuf encode() {
		ByteBuf buf = super.encode();	
		buf.writeBytes(gps.encode());
		buf.writeBytes(lbs.encode());
		buf.writeByte(deviceStatus.encode());
		buf.writeByte(voltage);
		buf.writeByte(signal);
		buf.writeByte(Alarm_Index);
		buf.writeByte(language);
		return buf;
	}
}
