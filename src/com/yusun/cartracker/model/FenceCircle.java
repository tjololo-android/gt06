package com.yusun.cartracker.model;

import com.yusun.cartracker.helper.Utils;

public class FenceCircle extends Fence{
	public FenceCircle(int radius, double lat, double lon) {
		sharp = FENCE_SHARP_CIRCLE;
		this.radius = radius;
		this.lat = lat;
		this.lon = lon;
	}
	public int getRadius() {
		return radius;
	}
	public double getLat() {
		return lat;
	}
	public double getLon() {
		return lon;
	}
	private int radius;
	private double lat;
	private double lon;
	@Override
	public boolean inSharp(double lat, double lon) {
		return false;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getState());
		sb.append(","+getSharp());
		if(0 == getLat()){
			sb.append(","+ 0);
			sb.append(","+ 0);
		}else{
			sb.append(","+Utils.getLanString(getLat()));
			sb.append(","+Utils.getLonString(getLon()));
		}
		sb.append("," + getRadius()/100);
		if(null != getType()){
			sb.append("," + getType());
		}
		return sb.toString();
	}
}
