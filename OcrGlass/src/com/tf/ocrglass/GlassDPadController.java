package com.tf.ocrglass;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GlassDPadController extends
		GestureDetector.SimpleOnGestureListener {
	
	CameraFragment fragment;
	
	public GlassDPadController(CameraFragment fragment){
		this.fragment = fragment;
	}


	@Override
	public boolean onScroll(MotionEvent start, MotionEvent finish,
			float distanceX, float distanceY) {

		return false;
	}

	@Override
	public boolean onFling(MotionEvent start, MotionEvent finish,
			float velocityX, float velocityY) {

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		super.onLongPress(e);
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		Log.d("Event", "On Double Tap");
		fragment.onTap();
		return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		Log.d("Event", "On Single Tap");
		return false;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return super.onSingleTapUp(e); // To change body of overridden methods
										// use File | Settings | File Templates.
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		return super.onDoubleTapEvent(e); // To change body of overridden
											// methods use File | Settings |
											// File Templates.
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return super.onDown(e); // To change body of overridden methods use File
								// | Settings | File Templates.
	}

	@Override
	public void onShowPress(MotionEvent e) {
		super.onShowPress(e); // To change body of overridden methods use File |
								// Settings | File Templates.
	}
}