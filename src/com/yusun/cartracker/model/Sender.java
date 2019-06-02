package com.yusun.cartracker.model;

import com.yusun.cartracker.position.Position;

public abstract class Sender{
	abstract public void send(Position pos);
	public interface Listener{
		void onComplete(boolean sucess, Position pos);
	}
}
