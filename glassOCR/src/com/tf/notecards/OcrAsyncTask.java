package com.tf.notecards;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import com.googlecode.tesseract.android.TessBaseAPI;

public class OcrAsyncTask extends AsyncTask<byte[], Integer, String> {
	
	private static final String ENGLISH_LANGUAGE = "eng";
	private static final String APP_NAME = "Notecards";
	private static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/" + APP_NAME + "/";


	//why is this var args?
	@Override
	protected String doInBackground(byte[]... datum) {
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


	}
	
	// not currently being used / needs refactoring since it is taking a bitmap,
	// but is looking at the path for the jpeg and it would be best to not have
	// to write the file
//	private Bitmap rotate(Bitmap bitmap) throws IOException {
//		ExifInterface exif = new ExifInterface(path);
//		int exifOrientation = exif
//				.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//						ExifInterface.ORIENTATION_NORMAL);
//
//		int rotate = 0;
//		switch (exifOrientation) {
//		case ExifInterface.ORIENTATION_ROTATE_90:
//			rotate = 90;
//			break;
//		case ExifInterface.ORIENTATION_ROTATE_180:
//			rotate = 180;
//			break;
//		case ExifInterface.ORIENTATION_ROTATE_270:
//			rotate = 270;
//			break;
//		}
//
//		if (rotate != 0) {
//			int w = bitmap.getWidth();
//			int h = bitmap.getHeight();
//			Matrix mtx = new Matrix();
//			mtx.preRotate(rotate);
//
//			bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
//
//		}
//		return bitmap;
//	}

}
