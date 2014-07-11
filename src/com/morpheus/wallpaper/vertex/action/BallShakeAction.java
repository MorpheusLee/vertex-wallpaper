package com.morpheus.wallpaper.vertex.action;

import java.util.Random;

public class BallShakeAction extends BallAction {

	private static Random random = new Random();

	public BallShakeAction() {
		super(0);
	}

	@Override
	public int getXInternal(long deltaTime) {
		return rand();
	}

	@Override
	public int getYInternal(long deltaTime) {
		return rand();
	}

	@Override
	public void startAction(long startTime, int startX, int startY) {
		mStartTime = startTime;
	}

	private static int rand() {
		int offset = random.nextInt(2);

		if (random.nextBoolean())
			offset = -offset;

		return offset;
	}
}
