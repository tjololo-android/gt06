package com.yusun.cartracker.model;

public class FenceRectangle extends Fence{	
	public FenceRectangle(double leftTopLan, double leftTopLon, double rightBottomLan, double rightBottomLon) {
		sharp = FENCE_SHART_RECTANGLE;
		this.leftTopLan = leftTopLan;
		this.leftTopLon = leftTopLon;
		this.rightBottomLan = rightBottomLan;
		this.rightBottomLon = rightBottomLon;
	}	
	public double getLeftTopLan() {
		return leftTopLan;
	}
	public double getLeftTopLon() {
		return leftTopLon;
	}
	public double getRightBottomLan() {
		return rightBottomLan;
	}
	public double getRightBottomLon() {
		return rightBottomLon;
	}
	private double leftTopLan;
	private double leftTopLon;
	private double rightBottomLan;
	private double rightBottomLon;
	@Override
	public boolean inSharp(double lat, double lon) {
		// TODO Auto-generated method stub
		return false;
	}
}
