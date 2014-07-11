package com.morpheus.wallpaper.vertex;

import java.util.ArrayList;
import android.os.SystemClock;
import android.util.Log;

import com.morpheus.wallpaper.vertex.VertexService.VertexEngine;
import com.morpheus.wallpaper.vertex.action.DecelerateAction;
import com.morpheus.wallpaper.vertex.shape.Ball;
import com.morpheus.wallpaper.vertex.shape.Container;
import com.morpheus.wallpaper.vertex.shape.Number;

public class ActionUpWorkItem extends Thread {

	public static final String LOG_TAG = "ActionUpWorkItem";
	private VertexEngine mpVertexEngine;
	private Container mpContainer;

	public ActionUpWorkItem(VertexEngine pVE, Container pContainer) {
		super();
		mpVertexEngine = pVE;
		mpContainer = pContainer;
	}

	// overrides java.lang.Thread.run
	public void run() {

//		long now = SystemClock.elapsedRealtime();
		long now = System.currentTimeMillis();
//		Log.d(LOG_TAG, "run ActionUpWorkItem() now="+now);
		for (int n = 0; n < mpContainer.getNumberCount(); ++n) {
			Number pNumber = mpContainer.getNumber(n);
			ArrayList<Ball> refBallVec = pNumber.getBalls();

			for (int b = 0; b < refBallVec.size(); ++b) {
				Ball pBall = refBallVec.get(b);

				pBall.clearAction();
				//BallMoveActionExt action = new BallMoveActionExt(pBall.getOrigX(),pBall.getOrigY(), 600);
				DecelerateAction action = new DecelerateAction(pBall.getOrigX(), pBall.getOrigY(), 350, 0);
				//DecelerateActionExt action = new DecelerateActionExt(pBall.getOrigX(), pBall.getOrigY(), 1000);
				pBall.startAction(action, now);
			}
		}
//		long time2 = SystemClock::elapsedRealtime();
//		GLOG(LOG_TAG, LOGER, "time2=%ld, cost %ldms", time2, time2-time1);

		mpVertexEngine.setDrawFlag(true);
//		mpVertexEngine.draw();
		mpVertexEngine.postDraw();
	}
}
