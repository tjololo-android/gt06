package com.yusun.cartracker.model;

import java.util.Arrays;
import java.util.Date;

import com.yusun.cartracker.position.Position;

import io.netty.buffer.ByteBuf;

public class MessagePosition extends Message{

	public MessagePosition(int id) {
		super(id);
	}
	
	public Position position;
	public int mcc;
	public int mnc;
	public int lac;
	public int cellId;
	
	@Override
	public ByteBuf encode() {
		ByteBuf buf = super.encode();
		Date d = position.getTime();
		buf.writeByte(d.getYear());
		buf.writeByte(d.getMonth());
		buf.writeByte(d.getDay());
		buf.writeByte(d.getHours());
		buf.writeByte(d.getMinutes());
		buf.writeByte(d.getSeconds());
		
		buf.writeByte(position.getSatellites());
		
		byte[] latitude = new byte[4];
		buf.writeBytes(latitude);
		
		byte[] longitude = new byte[4];
		buf.writeBytes(longitude);
		buf.writeByte((int)position.getSpeed());
		buf.writeShort((int)position.getCourse());
		buf.writeShort(mcc);
		buf.writeByte(mnc);
		buf.writeShort(lac);
		byte[] CellID = new byte[]{				   
			(byte) (cellId & 0xFF),
	        (byte) ((cellId >> 8) & 0xFF),      
	        (byte) ((cellId >> 16) & 0xFF)			    
		};
		buf.writeBytes(CellID);
		return buf;
	}
}
