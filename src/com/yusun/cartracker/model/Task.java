package com.yusun.cartracker.model;

public abstract class Task implements Runnable{	
	protected int ID;
	public Task(int id){
		ID = id;
	}
	public int getId(){
		return ID;
	}
	public abstract void onComplete(int result);
}