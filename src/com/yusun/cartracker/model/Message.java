package com.yusun.cartracker.model;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.helper.SerialIndex;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Message extends Object{
	public String Imei;
	public int id;
	public int index;
	protected Message(int id){
		this.id = id;
		index = SerialIndex.get();
		Imei = Hardware.getImei();
	}
	
	public ByteBuf encode(){
		ByteBuf buf = Unpooled.buffer();
		return buf;
	}
	public Object decode(Byte Buf){
		return null;
	}
}
