package com.yusun.cartracker.model;

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
}
