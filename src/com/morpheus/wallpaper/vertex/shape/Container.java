package com.morpheus.wallpaper.vertex.shape;

import java.util.ArrayList;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Container {
	private ArrayList<Number> mNumber = new ArrayList<Number>(4);

	public Number getNumber(int i) {
		if (i < mNumber.size())
			return mNumber.get(i);
		else
			return null;
	}

	public int getNumberCount() {
		return mNumber.size();
	}

	public void addNumber(Number num) {
		mNumber.add(num);
	}

	public void replaceNumberAt(Number num, int index) {
		if (index < mNumber.size()) {
//			delete mNumber[index];
			mNumber.set(index, num);
		}
	}

	public boolean draw(Canvas canvas, Paint paint) {

		boolean bEnd = true;

		for (Number number : mNumber) {
			if (!number.draw(canvas, paint))
				bEnd = false;		// Even One Ball doesnot finish drawing,
		}

		return bEnd;
	}
}
