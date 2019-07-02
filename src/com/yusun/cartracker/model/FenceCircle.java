package com.yusun.cartracker.model;

public class FenceCircle extends Fence{
	public FenceCircle() {
		fenceSharp = FENCE_SHARP_CIRCLE;
	}
	public FenceCircle(Point center, int radius) {
		fenceSharp = FENCE_SHARP_CIRCLE;
		this.radius = radius;
		this.center = center;
	}
	private int radius;
	private Point center;
}
