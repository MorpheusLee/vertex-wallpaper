package com.morpheus.wallpaper.vertex.action;

public class BallMoveAction extends BallAction {

	protected int mDestX;	// Destination position
	protected int mDestY;
	protected int mStartX;	// Start position
	protected int mStartY;

	public BallMoveAction(int destX, int destY, int totalTime) {
		super(totalTime);

		mDestX = destX;
		mDestY = destY;
		mStartX = 0;
		mStartY = 0;
	}

	@Override
	public int getXInternal(long deltaTime) {
	    float ratio = deltaTime / mTotalTime;

	    return (int) (mStartX + (mDestX - mStartX) * ratio);
	}

	@Override
	public int getYInternal(long deltaTime) {
		float ratio = ((float)deltaTime) / mTotalTime;

		return (int) (mStartY + (mDestY - mStartY) * ratio);
	}

	@Override
	public void startAction(long startTime, int startX, int startY) {
		mStartTime = startTime;
	    mStartX = startX;
	    mStartY = startY;
	}

}
