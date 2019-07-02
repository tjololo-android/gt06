package com.yusun.cartracker.model;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.helper.SerialIndex;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class CMessage extends Object{
	public String Imei;
	public int id;
	public int index;
	protected CMessage(int id){
		this.id = id;
		index = SerialIndex.get();
		Imei = Hardware.instance().getIMEI();
	}
	public int getId(){
		return id;
	}
	
	public ByteBuf encode(){
		ByteBuf buf = Unpooled.buffer();
		return buf;
	}
	public void sendToTarget(){
		AppContext.instance().getClient().sendMessage(this);
	}
}
