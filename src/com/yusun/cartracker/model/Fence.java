package com.yusun.cartracker.model;

public abstract class Fence extends Object{
	public static final String FENCE_STATE_ON = "ON";
	public static final String FENCE_STATE_OFF = "OFF";	
	public static final String FENCE_TYPE_IN = "IN";
	public static final String FENCE_TYPE_OUT = "OUT";
	public static final int FENCE_SHARP_CIRCLE = 0;
	public static final int FENCE_SHART_RECTANGLE = 1;
	
	private String state;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	private String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	protected int sharp;
	public int getSharp() {
		return sharp;
	}
	public abstract boolean inSharp(double lat, double lon);
	public abstract String toString();
}
