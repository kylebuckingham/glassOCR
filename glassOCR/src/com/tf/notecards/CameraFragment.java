package com.tf.notecards;

import java.io.IOException;

import android.app.Activity;
import android.app.Fragment;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;

public class CameraFragment extends Fragment implements SurfaceHolder.Callback,
		Camera.PictureCallback, OnTouchListener{

	public interface OnTakePictureListener {

		public void onTakePicture(byte[] picture);
	}

	private Camera camera;
	private Button button;

	private static final String TAG = "CameraFragment";

	CameraFragment.OnTakePictureListener listener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (CameraFragment.OnTakePictureListener) activity;
		} catch (ClassCastException cce) {
			throw new ClassCastException(activity.toString()
					+ " must implement DatePickerDialog.OnDateSetListener");
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SurfaceView surface = (SurfaceView) getActivity().findViewById(
				R.id.surfaceView);
		SurfaceHolder holder = surface.getHolder();
		holder.addCallback(this);
		
	}

	@Override
	public void onPictureTaken(byte[] picture, Camera arg1) {
		listener.onTakePicture(picture);

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int arg1, int arg2,
			int arg3) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();
			// here you can draw over preview

		} catch (IOException ioe) {
			Log.d(TAG, "IOException", ioe);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();

	}

	
	@Override
	public void onPause() {
		super.onPause();
		camera.release();
	}

	@Override
	public void onResume() {
		super.onResume();
		camera = Camera.open();

		Camera.Parameters parameters = camera.getParameters();
		if (parameters.getSupportedFocusModes().contains(
				Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
			parameters
					.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		}
		camera.setParameters(parameters);
	}




	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		camera.takePicture(null, null, this);
		return true;
	}

}
