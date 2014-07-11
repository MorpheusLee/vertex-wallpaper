package com.morpheus.wallpaper.vertex.shape;

import com.morpheus.wallpaper.vertex.action.BallAction;
import com.morpheus.wallpaper.vertex.action.BallShakeAction;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Ball {
	private static final int RADIUS = 6;
	private int mOrigX, mOrigY;
	private	int mX, mY;

	private int mRadius;	// We can support different radius.

	private BallAction mAction = null;
	private BallAction mShakeAction = null;

	private long mStartTime;

	public static boolean sShake;

	public Ball(int x, int y) {
		mOrigX = mX = x;
		mOrigY = mY = y;
		mRadius = RADIUS;
		mStartTime = 0;
	    mShakeAction = new BallShakeAction();
	}
	public Ball(int x, int y, int redius) {
		mOrigX = mX = x;
		mOrigY = mY = y;
		mRadius = redius;
		mStartTime = 0;
	    mShakeAction = new BallShakeAction();
	}

	public int getOrigX() {
		return mOrigX;
	}
	public int getOrigY() {
		return mOrigY;
	}
	public void setOrigXY(int x, int y) {
		mOrigX = x;
		mOrigY = y;
	}

	public int getX() {
		return mX;
	}
	public void setX(int x) {
		mX = x;
	}

	public int getY() {
		return mY;
	}
	public void setY(int y) {
		mY = y;
	}
	
	public int getRadius() {
		return mRadius;
	}

	public Ball transform(int xOffset,int yOffset) {
		mOrigX += xOffset;
		mOrigY += yOffset;
		mX += xOffset;
		mY += yOffset;
		return this;
    }
	public Ball scale(float scale) {
		mOrigX *= scale;
		mOrigY *= scale;
		mX *= scale;
		mY *= scale;
		return this;
	}

	public void startAction(BallAction action, long startTime) {
		if(mAction == null) {
			mAction = action;
			synchronized(mAction){
				mStartTime = startTime;
				mAction.startAction(mStartTime, mX, mY);
			}
		}
	}
	public void clearAction() {
		if(mAction != null){
			synchronized(mAction){
				mAction = null;
			}
		}
	}

	public boolean isActionReady() {
		return (mAction == null);
	}

	public boolean draw(Canvas canvas, Paint paint, long curTime) {
		boolean bEnd = true;
		if(mAction != null){
			synchronized(mAction){

			mX = mAction.getX(curTime);
			mY = mAction.getY(curTime);

			if(curTime - mStartTime >= mAction.getTotalTime())
				clearAction();
			else
				bEnd = false;

			}
		}
		else if(sShake){
			mX += mShakeAction.getX(0);
			mY += mShakeAction.getY(0);
		}

		paint.setColor(Color.BLACK);
		paint.setAlpha(128);
		canvas.drawCircle(mX+RADIUS, mY+RADIUS, mRadius, paint);

		paint.setColor(Color.WHITE);
		paint.setAlpha(255);
		canvas.drawCircle(mX, mY, mRadius, paint);

		return bEnd;
	}

}
