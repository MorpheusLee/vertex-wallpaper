package com.morpheus.wallpaper.vertex.shape;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Line {
	private Ball mBall1;
	private Ball mBall2;
	private int ballOffset;

	public Line(Ball b1, Ball b2) {
		mBall1 = b1;
		mBall2 = b2;
		ballOffset = mBall1.getRadius();
	}

	public void draw(Canvas canvas, Paint paint) {
		paint.setColor(Color.BLACK);
		paint.setAlpha(128);
		canvas.drawLine(mBall1.getX()+ballOffset, mBall1.getY()+ballOffset,
				mBall2.getX()+ballOffset, mBall2.getY()+ballOffset, paint);

		paint.setColor(Color.WHITE);
		paint.setAlpha(255);
		canvas.drawLine(mBall1.getX(), mBall1.getY(),
				mBall2.getX(), mBall2.getY(), paint);
	}

}
