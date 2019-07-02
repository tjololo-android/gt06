package com.yusun.cartracker.model;

public abstract class Fence{
	public static final int FENCE_TYPE_ALL = 0;
	public static final int FENCE_TYPE_IN = 1;
	public static final int FENCE_TYPE_OUT = 2;
	public static final int FENCE_SHARP_CIRCLE = 2;
	public static final int FENCE_SHARP_RECTANGLE = 2;
	
	private boolean switcher;
	public boolean isSwitcher() {
		return switcher;
	}
	public void setSwitcher(boolean switcher) {
		this.switcher = switcher;
	}
	private int fenceType;
	protected int fenceSharp;
	public int getFenceType() {
		return fenceType;
	}
	public void setFenceType(int fenceType) {
		this.fenceType = fenceType;
	}
	public int getFenceSharp() {
		return fenceSharp;
	}
}
