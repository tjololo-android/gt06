package com.yusun.cartracker.model;

import com.yusun.cartracker.helper.Utils;

public class FenceRectangle extends Fence{	
	public FenceRectangle(double leftTopLan, double leftTopLon, double rightBottomLan, double rightBottomLon) {
		sharp = FENCE_SHART_RECTANGLE;
		this.lan1 = leftTopLan;
		this.lon1 = leftTopLon;
		this.lan2 = rightBottomLan;
		this.lon2 = rightBottomLon;
	}	
	public double getLan1() {
		return lan1;
	}
	public double getLon1() {
		return lon1;
	}
	public double getLan2() {
		return lan2;
	}
	public double getLon2() {
		return lon2;
	}
	private double lan1;
	private double lon1;
	private double lan2;
	private double lon2;
	@Override
	public boolean inSharp(double lat, double lon) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getState());
		sb.append(","+getSharp());	
		sb.append(","+Utils.getLanString(getLan1()));
		sb.append(","+Utils.getLonString(getLon1()));
		sb.append(","+Utils.getLanString(getLan2()));
		sb.append(","+Utils.getLonString(getLon2()));
		if(null != getType()){
			sb.append("," + getType());
		}
		return sb.toString();
	}
}
