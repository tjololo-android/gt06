package com.yusun.cartracker.model;

public class FenceRectangle extends Fence{
	public FenceRectangle() {
		fenceSharp = FENCE_SHARP_RECTANGLE;
	}
	public FenceRectangle(Point leftTop, Point leftBottom, Point RightTop, Point RightBottom) {
		fenceSharp = FENCE_SHARP_RECTANGLE;	
	}	
	private Point leftTop;
	private Point leftBottom;
	private Point RightTop;
	private Point RightBottom;
}
