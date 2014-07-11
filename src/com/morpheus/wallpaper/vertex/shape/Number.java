package com.morpheus.wallpaper.vertex.shape;

import java.util.ArrayList;

import com.morpheus.wallpaper.vertex.action.BallMoveAction;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Number {
	private ArrayList<Ball> mBalls = new ArrayList<Ball>();
	private ArrayList<Line> mLines = new ArrayList<Line>();

	private void addBall(Ball ball) {
		mBalls.add(ball);
	}
	private void addLine(Line line) {
		mLines.add(line);
	}

	public int mNum;
	public int mX, mY;
	public int mWidth, mHeight;
	private int mBallRadius;

	public Number(int num, int ballR) {
		mNum = num;
		mX = mY = 0;
		mWidth = NumberData.NUMBER_WIDTH;
		mHeight = NumberData.NUMBER_HEIGHT;
		mBallRadius = ballR;
		assert(0 <= num && num < 10);

		for (int[] pPoint : NumberData.point[num]) {
			int x = pPoint[0];
			int y = pPoint[1];
			addBall(new Ball(x, y, mBallRadius));
		}

//		S_Line* pLine = line[num];
		for (int[] pLine : NumberData.line[num]) {
			Ball ball1 = mBalls.get(pLine[0]);
			Ball ball2 = mBalls.get(pLine[1]);
			addLine(new Line(ball1, ball2));
		}
	}

	public ArrayList<Ball> getBalls() {
		return mBalls;
	}

	public ArrayList<Line> getLines() {
        return mLines;
    }

	public Number transform(int x, int y) {
		for (int i = 0; i < mBalls.size(); ++i)
			mBalls.get(i).transform(x, y);

		mX += x;
		mY += y;
		return this;
	}
	public Number scale(float scale) {
		for (int i = 0; i < mBalls.size(); ++i)
			mBalls.get(i).scale(scale);

		mWidth *= scale;
		mHeight *= scale;
		return this;
	}
	public Number trans_scale(int x, int y, float scale) {
		for (Ball ball : mBalls) {
			//GLOG(LOG_TAG, LOGER, "Ball_%d: (%d, %d)", i, mBalls[i]->getX(), mBalls[i]->getY());
			ball.transform(x, y).scale(scale);
			//GLOG(LOG_TAG, LOGER, "Move To: (%d, %d)", mBalls[i]->getX(), mBalls[i]->getY());
		}

		mX = (int) (x * scale);
		mY = (int) (y * scale);
		mWidth *= scale;
		mHeight *= scale;
		return this;
	}
	public Number setToCenterX() {
		for (Ball ball : mBalls) {
			ball.setX(mX + mWidth / 2);
		}
		return this;
	}

	// No longer delete this Number Object
	// We must change inner
	public void startChange(int numTo, float scale, int transX, int transY) {
		// 1. delete all Lines
		if (!mLines.isEmpty()) {
//			for (size_t i = 0; i < mLines.size(); ++i)
//				delete mLines[i];
			mLines.clear();
		}

		// 2. start all Balls action
		int[][] pPoint = NumberData.point[numTo];
		if (NumberData.pointSize[mNum] > NumberData.pointSize[numTo]) {
			int b = 0;
//			long now = SystemClock.elapsedRealtime();
			long now = System.currentTimeMillis();
			for (; b < NumberData.pointSize[numTo]; ++b) {
				Ball pBall = mBalls.get(b);

				int x = (int) (pPoint[b][0] * scale + transX);
				int y = (int) (pPoint[b][1] * scale + transY);

				pBall.clearAction();
				pBall.setOrigXY(x, y);
				BallMoveAction action = new BallMoveAction(x, y, 300);
				pBall.startAction(action, now);
			}
			ArrayList<Ball> collection = new ArrayList<Ball>();
			for (int d = b; d < NumberData.pointSize[mNum]; ++d) {
//				delete mBalls[d];
//				mBalls.remove(d);
				collection.add(mBalls.get(d));
			}
//			mBalls.removeAt(b, NumberData.pointSize[mNum] - NumberData.pointSize[numTo]);
			mBalls.removeAll(collection);

		} else {
			int b = 0;
//			long now = SystemClock.elapsedRealtime();
			long now = System.currentTimeMillis();
			for (; b < NumberData.pointSize[mNum]; ++b) {
				Ball pBall = mBalls.get(b);

				int x = (int) (pPoint[b][0] * scale + transX);
				int y = (int) (pPoint[b][1] * scale + transY);

				pBall.clearAction();
				pBall.setOrigXY(x, y);
				BallMoveAction action = new BallMoveAction(x, y, 300);
				pBall.startAction(action, now);
			}
			for (int a = b; a < NumberData.pointSize[numTo]; ++a) {
				int numCenterX = mX + mWidth / 2;
				int numCenterY = mY + mHeight / 2;
				int x = (int) (pPoint[a][0] * scale + transX);
				int y = (int) (pPoint[a][1] * scale + transY);

				Ball pBall = new Ball(numCenterX, numCenterY, mBallRadius);
				pBall.setOrigXY(x, y);
				BallMoveAction action = new BallMoveAction(x, y, 300);
				pBall.startAction(action, now);
				mBalls.add(pBall);
			}
		}

		// 3. add new Lines
		mNum = numTo;
		for (int[] pLine : NumberData.line[mNum]) {
			Ball ball1 = mBalls.get(pLine[0]);
			Ball ball2 = mBalls.get(pLine[1]);
			addLine(new Line(ball1, ball2));
		}
	}

	public boolean draw(Canvas canvas, Paint paint) {
		boolean bEnd = true;

//		long curTime = SystemClock.elapsedRealtime();
		long curTime = System.currentTimeMillis();
		for (Ball ball : mBalls) {
			if (!ball.draw(canvas, paint, curTime))
				bEnd = false;		// Even One Ball doesnot finish drawing,
		}

		for (Line line : mLines)
			line.draw(canvas, paint);

		return bEnd;
	}

}
