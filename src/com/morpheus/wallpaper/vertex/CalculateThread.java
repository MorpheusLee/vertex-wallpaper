/**
 * 
 */
package com.morpheus.wallpaper.vertex;

import java.util.ArrayList;
import com.morpheus.wallpaper.vertex.VertexService.VertexEngine;
import com.morpheus.wallpaper.vertex.action.DecelerateAction;
import com.morpheus.wallpaper.vertex.shape.Ball;
import com.morpheus.wallpaper.vertex.shape.Container;
import com.morpheus.wallpaper.vertex.shape.Number;

/**
 * @author lee
 *
 */
public class CalculateThread extends Thread {

	private static final int TOUCH_RADIUS = 150;
	private VertexEngine mpVertexEngine;
	private int mTouchX, mTouchY;
	private Container mpContainer;


	public CalculateThread(VertexEngine pVE, int x, int y, Container pContainer) {
		super();
		mpVertexEngine = pVE;
		mTouchX = x;
		mTouchY = y;
		mpContainer = pContainer;
	}

	public void run() {
//		long time1 = SystemClock::elapsedRealtime();
//		GLOG(LOG_TAG, LOGER, "run CalculateWorkItem() time1=%ld", time1);

		for (int n = 0; n < mpContainer.getNumberCount(); ++n) {
			Number pNumber = mpContainer.getNumber(n);
			ArrayList<Ball> refBallVec = pNumber.getBalls();

			for (int b = 0; b < refBallVec.size(); ++b) {
				Ball pBall = refBallVec.get(b);

				int startX = pBall.getX();
				int startY = pBall.getY();
				double destanceToTouch = Math.hypot(startX - mTouchX,
						startY - mTouchY);
				if (destanceToTouch < TOUCH_RADIUS) {
//					Log.d(LOG_TAG, LOGER, "Ball_%d Yes!", i);
					//int destX = startX + (startX - mTouchX)*2;
					//int destY = startY + (startY - mTouchY)*2;
					int destX = (int) (startX + (startX-mTouchX) * TOUCH_RADIUS / destanceToTouch);
					int destY = (int) (startY + (startY-mTouchY) * TOUCH_RADIUS / destanceToTouch);

					pBall.clearAction();
					//BallMoveAction action = new BallMoveAction(destX, destY, 200);
					//BallMoveActionExt action = new BallMoveActionExt(destX, destY, 300);
					DecelerateAction action = new DecelerateAction(destX, destY, 400, 0);
//					pBall.startAction(action, SystemClock.elapsedRealtime());
					pBall.startAction(action, System.currentTimeMillis());
				}
			}
		}
//		long time2 = SystemClock::elapsedRealtime();
//		GLOG(LOG_TAG, LOGER, "time2=%ld, cost %dms", time2, time2-time1);

		mpVertexEngine.setDrawFlag(true);
//		mpVertexEngine.draw();
		mpVertexEngine.postDraw();
	}

}
