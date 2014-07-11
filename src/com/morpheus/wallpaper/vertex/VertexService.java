package com.morpheus.wallpaper.vertex;

import com.morpheus.wallpaper.vertex.R;
import com.morpheus.wallpaper.vertex.shape.Ball;
import com.morpheus.wallpaper.vertex.shape.Container;
import com.morpheus.wallpaper.vertex.shape.Number;
import com.morpheus.wallpaper.vertex.shape.NumberData;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class VertexService extends WallpaperService {

	public static final String LOG_TAG = "VertexService";
    /**
     * The frame rate we will attempt to achieve with the wallpaper
     */
    public static final int FRAME_RATE = 40;

	private float MIN(float x, float y) {
		return (x) < (y) ? (x) : (y);
	}

	@Override
	public Engine onCreateEngine() {
		return new VertexEngine();
	}



	public class VertexEngine extends Engine {

        private final Handler mHandler = new Handler();
		private long mTime;
		private float mScale;
		private int mNum_0_X, mNum_0_Y, mNum_3_X, mNum_3_Y;
		private int mHour, mMin, mHour2, mMin2;
		private Paint mPaint = new Paint();
		private Container mContainer;
		private Bitmap mBitmap;
		private boolean mbTimeChange;
		private boolean mbCanDraw = true;
		private boolean mbCanCalculate;
		private TimeReceiver timeRecevier = new TimeReceiver();

		public VertexEngine() {
//			SurfaceHolder holder = getSurfaceHolder();

			BitmapFactory.Options bitmapOption = new BitmapFactory.Options();
			bitmapOption.inTargetDensity = 320;
			bitmapOption.inPreferredConfig = Bitmap.Config.ARGB_8888;
			mBitmap = BitmapFactory.decodeResource(VertexService.this.getResources(), R.drawable.vertex_bg, bitmapOption);
		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			setTouchEventsEnabled(true);

			// register BroadcastReceiver an action(TIME_TICK)
			IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
			filter.addAction(Intent.ACTION_TIME_CHANGED);
			VertexService.this.registerReceiver(timeRecevier, filter);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
            mHandler.removeCallbacks(drawer);
			VertexService.this.unregisterReceiver(timeRecevier);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			if (visible) {
				mbCanDraw = true;
				postDraw();
			} else {
	            mHandler.removeCallbacks(drawer);
			}
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			Log.d(LOG_TAG, "onSurfaceChanged(w="+width+", h="+height+")");
			super.onSurfaceChanged(holder, format, width, height);
			init(width, height);

			mbCanDraw = true;
			postDraw();
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
            mHandler.removeCallbacks(drawer);
		}

		@Override
		public void onTouchEvent(MotionEvent event) {
			super.onTouchEvent(event);
			int x = (int) event.getX();
			int y = (int) event.getY();

			long now = System.currentTimeMillis();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Log.d(LOG_TAG, "onTouch_Down: x = "+x+", y = "+y);
				Ball.sShake = true;
				setCalculateFlag(true);
				calculate(x, y);
				break;
			case MotionEvent.ACTION_MOVE:
				if (Ball.sShake && now - mTime > 100) {
					mTime = now;
//					GLOG(LOG_TAG, LOGDBG, "onTouch_Move: x = %d, y = %d", x, y);
					setDrawFlag(false);
					setCalculateFlag(true);
					calculate(x, y);
				}
				break;
			case MotionEvent.ACTION_UP:
//			case MotionEvent.ACTION_CANCEL:
				Log.d(LOG_TAG, "onTouch_Up: x = "+x+", y = "+y);
				Ball.sShake = false;
//				cancelDelayedTasks();
//				asyncDelayed(new ActionUpWorkItem(this, mpContainer), 0);
				new ActionUpWorkItem(this, mContainer).start();
				break;
			default:
				break;
			}
		}

		
		private void init(int width, int height) {
			Log.d(LOG_TAG, "init(w="+width+", h="+height+")");
			mContainer = new Container();

			float hScale = (float)width / NumberData.SCREEN_WIDTH;
			float vScale = (float)height / NumberData.SCREEN_HEIGHT;
			int ballRadius = 4;
			mScale = MIN(hScale, vScale);
			if (mScale > 1.0f) {
				mScale *= 0.91f;
				ballRadius = 6;
			}

			Time now = new Time();
			now.setToNow();
			mHour = now.hour;
			mMin = now.minute;

			int num [] = {
					mHour / 10, mHour % 10,
					mMin / 10, mMin % 10
			};

			mNum_0_X = (int) (width / 2 - NumberData.NUMBER_WIDTH * mScale);
//			mNum_0_Y = (int) (790 - NumberData.NUMBER_HEIGHT * mScale - 35);
			mNum_0_Y = (int) (height / 2 - NumberData.NUMBER_HEIGHT * mScale);
			mNum_3_X = width / 2;
//			mNum_3_Y = 790 + 35;
			mNum_3_Y = height / 2;
			int offsetX [] = {mNum_0_X, mNum_3_X, mNum_0_X, mNum_3_X};
			int offsetY [] = {mNum_0_Y, mNum_0_Y, mNum_3_Y, mNum_3_Y};
			for (int i = 0; i < 4; ++i) {
				Number number = new Number(num[i], ballRadius);
				number.scale(mScale).transform(offsetX[i], offsetY[i]);
				mContainer.addNumber(number);
			}
			
			
			mPaint.setStyle(Paint.Style.FILL);
			mPaint.setAntiAlias(true);
			mPaint.setTextAlign(Paint.Align.CENTER);
		}
		
		public boolean doDraw(Canvas canvas) {
//			Log.d(LOG_TAG, "doDraw()");
			canvas.drawColor(Color.WHITE);
			canvas.drawBitmap(mBitmap, 0, 0, mPaint);

			return mContainer.draw(canvas, mPaint);
		}

		private void doTimeChange3() {
			int numFrom [] = {
				mHour / 10, mHour % 10,
				mMin / 10, mMin % 10
			};
			int numTo [] = {
				mHour2 / 10, mHour2 % 10,
				mMin2 / 10, mMin2 % 10
			};
			int offsetX [] = {mNum_0_X, mNum_3_X, mNum_0_X, mNum_3_X};
			int offsetY [] = {mNum_0_Y, mNum_0_Y, mNum_3_Y, mNum_3_Y};
		
			Log.d(LOG_TAG, "doTimeChange3(): "+mHour+":"+mMin+" To "+mHour2+":"+mMin2);
		
			for (int i = 0; i < 4; ++i) {
				if (numTo[i] != numFrom[i]) {
					Number number = mContainer.getNumber(i);
					number.startChange(numTo[i], mScale, offsetX[i], offsetY[i]);
				}
			}

            if (mbTimeChange) {
				mbTimeChange = false;
				mHour = mHour2;
				mMin = mMin2;

//				cancelDelayedTasks();
//				asyncDelayed(new ActionUpWorkItem(this, mContainer), 0);
				new ActionUpWorkItem(this, mContainer).start();
			}
		}

		public void setDrawFlag(boolean flag) {
//			android.AutoMutex lock(mpLock);
			mbCanDraw = flag;
		}

		public void calculate(int x, int y) {
//		    android.AutoMutex lock(mpLock);
		    if (mbCanCalculate) {
//		    	cancelDelayedTasks();
//		    	asyncDelayed(new CalculateThread(this, x, y, mContainer), 0);
		    	new CalculateThread(this, x, y, mContainer).start();
		    	mbCanCalculate = false;
		    }
		}
		public void setCalculateFlag(boolean flag) {
//			android.AutoMutex lock(mpLock);
			mbCanCalculate = flag;
		}
		
		private void drawFrame() {
			boolean bFinishDraw = false;

			final SurfaceHolder holder = getSurfaceHolder();

            Canvas canvas = null;
			try {
				canvas = holder.lockCanvas(null);
				if (canvas != null) {
//					Log.d(VertexService.LOG_TAG, "canvas<"+canvas.getWidth()+" x "+canvas.getHeight()+">");
//					if(canvas.getWidth() < canvas.getHeight()) // only support ORIENTATION_PORTRAIT mode
						bFinishDraw = doDraw(canvas);
				}
			} finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }

			if (!bFinishDraw)
				setDrawFlag(true);
            postDraw();
		}
		
        private final Runnable drawer = new Runnable() {
            public void run() {
                drawFrame();
            }
        };

        /**
         * Posts a draw event to the handler.
         */
        public void postDraw() {
            mHandler.removeCallbacks(drawer);
//            if (mVisible) {
            if (mbCanDraw) {
                mHandler.postDelayed(drawer, 1000 / FRAME_RATE);
                mbCanDraw = false;
            }
        }

		private class TimeReceiver extends BroadcastReceiver {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();

				Time t = new Time();
				t.setToNow();
				mHour2 = t.hour;
				mMin2 = t.minute;
				Log.d(LOG_TAG, "onReceive(): get an Action = "+action+": "+mHour2+":"+mMin2);

				Ball.sShake = false;

				mbTimeChange = true;
				doTimeChange3();
			}
		}
	}

}
