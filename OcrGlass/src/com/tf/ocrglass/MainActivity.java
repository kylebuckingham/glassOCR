package com.tf.ocrglass;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.tf.ocrglass.CameraFragment.OnTakePictureListener;

public class MainActivity extends Activity implements OnTakePictureListener {


	private static final String ENGLISH_LANGUAGE = "eng";
	private static final String APP_NAME = "Notecards";
	private static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/" + APP_NAME + "/";
	private static final String TESSDATA = "tessdata";
	private static final String TAG = "MainActivity";
	private FragmentManager fm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
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
		Log.d(TAG, "loadLanguage");
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
		Log.d(TAG, "onTakePicture");
		new OcrAsyncTask().execute(picture);
        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment processingImageFragment = new ProcessingImageFragment();
        ft.replace(android.R.id.content, processingImageFragment);
        ft.commit();

	}
	public class OcrAsyncTask extends AsyncTask<byte[], Integer, String> {
		
		private final String ENGLISH_LANGUAGE = "eng";
		private final String APP_NAME = "Notecards";
		private final String DATA_PATH = Environment
				.getExternalStorageDirectory().toString() + "/" + APP_NAME + "/";


		//why is this var args?
		@Override
		protected String doInBackground(byte[]... datum) {
			Log.d(TAG, "doInBackground");
			String text = null;
			for(byte[] data : datum){
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
					options);
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
			TessBaseAPI baseApi = new TessBaseAPI();
			baseApi.init(DATA_PATH, ENGLISH_LANGUAGE);
			baseApi.setImage(bitmap);
			text = baseApi.getUTF8Text();
			baseApi.end();
			text = text.replaceAll("[^a-zA-Z0-9]+", " ");
			text = text.trim();
			}
			return text;
		}
		
		@Override
		protected void onPostExecute(String result) {
			Log.d(TAG, "onPostExecute");
			FragmentTransaction ft = fm.beginTransaction();
	        Fragment resultFragment = new ResultFragment();
	        Bundle bundle = new Bundle();
	        bundle.putString("text", result);
	        resultFragment.setArguments(bundle);
	        ft.replace(android.R.id.content, resultFragment);
	        ft.commit();

		}
	}

}