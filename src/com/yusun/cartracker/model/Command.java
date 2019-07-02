package com.yusun.cartracker.model;

import io.netty.buffer.ByteBuf;

public abstract class Command{	
	protected int ID;
	public Command(int id){
		ID = id;
	}
	public int getId(){
		return ID;
	}	
	public abstract void decode(ByteBuf buf);
}