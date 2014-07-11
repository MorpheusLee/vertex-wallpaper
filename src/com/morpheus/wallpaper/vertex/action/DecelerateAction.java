package com.morpheus.wallpaper.vertex.action;

public class DecelerateAction extends BallMoveAction {
	private float mPower;

	public DecelerateAction(int destX, int destY, int totalTime, float power) {
		super(destX, destY, totalTime);
		mPower = power;
	}

	public int getXInternal(long deltaTime) {
		float ratio = ((float)deltaTime) / mTotalTime;

		//return (1 - 2 * pow(ratio-1, 2)) * (mDestX - mStartX);
		if (0.001 < mPower && mPower < 0.999)
			return (int) (Math.pow(ratio, mPower) * (mDestX - mStartX));
		else
			return (int) (mStartX + (1 - Math.pow(1-ratio, 3)) * (mDestX - mStartX));
	}
	public int getYInternal(long deltaTime) {
		float ratio = ((float)deltaTime) / mTotalTime;

		//return (1 - 2 * pow(ratio-1, 2)) * (mDestX - mStartX);
		if (0.001 < mPower && mPower < 0.999)
			return (int) (Math.pow(ratio, mPower) * (mDestY - mStartY));
		else
			return (int) (mStartY + (1 - Math.pow(1-ratio, 3)) * (mDestY - mStartY));
	}
}
