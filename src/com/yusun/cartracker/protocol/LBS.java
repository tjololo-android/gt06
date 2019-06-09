package com.yusun.cartracker.protocol;

import com.yusun.cartracker.api.Hardware;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class LBS{
	public String mcc;
	public String mnc;
	public int lac;
	public int cellId;
	public int rssi;
	public int preTime;
	
	LBS(){
		mcc = Hardware.instance().getMCC();
		mnc = Hardware.instance().getMNC();
		lac = Hardware.instance().getLAC();
		cellId = Hardware.instance().getCID();
		rssi = Hardware.instance().getSIGNAL();
		preTime = Hardware.instance().getPreTime();
	}

	public ByteBuf encode() {
		ByteBuf buf = Unpooled.buffer();	
		buf.writeByte(9);
		buf.writeShort(Integer.parseInt(mcc));
		buf.writeByte(Integer.parseInt(mnc));
		buf.writeShort(lac);
		buf.writeMedium(cellId);				
		return buf;
	}
}
