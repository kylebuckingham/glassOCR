package com.tf.ocrglass;

import java.io.IOException;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;


public class CameraFragment extends Fragment implements SurfaceHolder.Callback,
		Camera.PictureCallback, View.OnClickListener{

	public interface OnTakePictureListener {

		public void onTakePicture(byte[] picture);
	}

	private Camera camera;
	private GestureDetector mGestureDetector;
	private static final String TAG = "CameraFragment";

	OnTakePictureListener listener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		mGestureDetector = new GestureDetector(new GlassDPadController(this));
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		return inflater.inflate(R.layout.fragment_camera, container, false);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(TAG, "onAttach");
		// sets orientation
		//activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		try {
			listener = (OnTakePictureListener) activity;
		} catch (ClassCastException cce) {
			throw new ClassCastException(activity.toString()
					+ " must implement DatePickerDialog.OnDateSetListener");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated");
		SurfaceView surface = (SurfaceView) getActivity().findViewById(
				R.id.surfaceView);
		surface.setOnClickListener(this);
		SurfaceHolder holder = surface.getHolder();
		holder.addCallback(this);

	}

	@Override
	public void onPictureTaken(byte[] picture, Camera arg1) {
		Log.d(TAG, "onPictureTaken");
		listener.onTakePicture(picture);

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int arg1, int arg2,
			int arg3) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated");
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
		Log.d(TAG, "surfaceDestroyed");
		if (camera != null) {
			camera.stopPreview();
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		camera.release();
		camera = null;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResuming");
		camera = Camera.open();

		Camera.Parameters parameters = camera.getParameters();
		if (parameters.getSupportedFocusModes().contains(
				Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
			parameters
					.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		}
		camera.setParameters(parameters);
	}

	
	public void onTap() {
		Log.d(TAG, "onClick");
		camera.takePicture(null, null, this);

	}
	
	@Override
	public void onClick(View v) {
		Log.d(TAG, "onClick");
		camera.takePicture(null, null, this);

	}

}
