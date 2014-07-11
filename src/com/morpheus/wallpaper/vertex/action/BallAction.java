package com.morpheus.wallpaper.vertex.action;

public abstract class BallAction {

	protected int mTotalTime;
	protected long mStartTime;

	public abstract int getXInternal(long deltaTime);
	public abstract int getYInternal(long deltaTime);

	public BallAction(int totalTime) {
		mTotalTime = totalTime;
		mStartTime = 0;
	}

	public int getTotalTime() {
		return mTotalTime;
	}

	public int getX(long curTime) {
		long deltaTime = curTime - mStartTime;
		if(deltaTime > mTotalTime)
			deltaTime = mTotalTime;

		return getXInternal(deltaTime);
	}
	public int getY(long curTime) {
		long deltaTime = curTime - mStartTime;
		if(deltaTime > mTotalTime)
			deltaTime = mTotalTime;

		return getYInternal(deltaTime);
	}

	public abstract void startAction(long startTime, int startX, int startY);
}
