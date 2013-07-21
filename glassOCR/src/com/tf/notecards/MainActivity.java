package com.tf.notecards;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.tf.notecards.CameraFragment.OnTakePictureListener;

public class MainActivity extends Activity implements OnTakePictureListener {


	private static final String ENGLISH_LANGUAGE = "eng";
	private static final String APP_NAME = "Notecards";
	private static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/" + APP_NAME + "/";
	private static final String TESSDATA = "tessdata";
	private static final String TAG = "Notecards";
	private FragmentManager fm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		File dir = new File(DATA_PATH, TESSDATA);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				Log.v(TAG, "Error: Creation of directory failed");
			}
		}
		loadLanguage(ENGLISH_LANGUAGE);
		
        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment cameraFragment = new CameraFragment();
        ft.replace(android.R.id.content, cameraFragment);
        ft.commit();


	}

	private void loadLanguage(String language) {
		if (!(new File(DATA_PATH + "tessdata/" + language + ".traineddata"))
				.exists()) {
			try {

				AssetManager assetManager = getAssets();
				InputStream in = assetManager.open("tessdata/" + language
						+ ".traineddata");

				OutputStream out = new FileOutputStream(DATA_PATH + "tessdata/"
						+ language + ".traineddata");

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;

				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();

				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
	public void onTakePicture(byte[] picture) {
		new OcrAsyncTask().execute(picture);
        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment processingImageFragment = new ProcessingImageFragment();
        ft.replace(android.R.id.content, processingImageFragment);
        ft.commit();

	}

}
