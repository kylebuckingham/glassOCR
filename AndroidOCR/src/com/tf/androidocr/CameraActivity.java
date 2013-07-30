package com.tf.androidocr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

public class CameraActivity extends Activity implements SurfaceHolder.Callback,
		Camera.PictureCallback, View.OnClickListener {

	private static final String TAG = "CameraActivity";
	private Camera camera;
	SurfaceView surface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		setContentView(R.layout.activity_camera);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		File dir = new File(AndroidOCR.DATA_PATH, AndroidOCR.TESSDATA);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				Log.d(TAG, "Error: Creation of directory failed");
			} else {
				Log.d(TAG, dir.getPath() + " was successfully created");
			}
		} else {
			Log.d(TAG, dir.getPath() + " exists");
		}
		loadLanguage(AndroidOCR.ENGLISH_LANGUAGE);

		Log.d(TAG, "onActivityCreated");
		SurfaceView surface = (SurfaceView) findViewById(R.id.surfaceView);
		surface.setOnClickListener(this);
		SurfaceHolder holder = surface.getHolder();
		holder.addCallback(this);

		// keeps screen from turning off
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

	}

	private void loadLanguage(String language) {
		Log.d(TAG, "loadLanguage");
		if (!(new File(AndroidOCR.DATA_PATH + "tessdata/" + language
				+ ".traineddata")).exists()) {
			try {

				AssetManager assetManager = getAssets();
				InputStream in = assetManager.open(AndroidOCR.TESSDATA + "/"
						+ language + ".traineddata");

				OutputStream out = new FileOutputStream(AndroidOCR.DATA_PATH
						+ AndroidOCR.TESSDATA + "/" + language + ".traineddata");

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;

				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();

				out.close();

			} catch (IOException e) {
				Log.d(TAG, "load language failed due to IOException");
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
		camera.release();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
		camera = Camera.open();

		Camera.Parameters parameters = camera.getParameters();
		if (parameters.getSupportedFocusModes().contains(
				Camera.Parameters.FOCUS_MODE_MACRO)) {
			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
		}

		if(parameters.getSupportedFlashModes().contains(Camera.Parameters.FLASH_MODE_AUTO)){
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
		}
		camera.setParameters(parameters);
	}

	// what do we want to do here on a restore to this screen???
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onClick(View v) {
		Log.d(TAG, "onClick");
		camera.takePicture(null, null, this);

	}

	@Override
	public void onPictureTaken(byte[] picture, Camera arg1) {
		Log.d(TAG, "onPictureTaken");

		new OcrAsyncTask(this).execute(picture);

		// add progress bar here !!!!!!!!

	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

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
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}

	
}