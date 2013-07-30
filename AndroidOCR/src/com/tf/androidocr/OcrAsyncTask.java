package com.tf.androidocr;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.googlecode.leptonica.android.AdaptiveMap;
import com.googlecode.leptonica.android.Binarize;
import com.googlecode.leptonica.android.Convert;
import com.googlecode.leptonica.android.Enhance;
import com.googlecode.leptonica.android.Pix;
import com.googlecode.leptonica.android.ReadFile;
import com.googlecode.leptonica.android.Rotate;
import com.googlecode.leptonica.android.Skew;
import com.googlecode.leptonica.android.WriteFile;
import com.googlecode.tesseract.android.TessBaseAPI;

public class OcrAsyncTask extends AsyncTask<byte[], Integer, String> {

	// https://code.google.com/p/tesseract-android-tools/source/browse/tesseract-android-tools/src/com/googlecode/leptonica/android/Convert.java?r=877156d935b0f14f2102d88327f24ef0e529840e
	//great API
	String TAG = "OcrAsyncTask";
	Activity activity;
	
	File file = new File(AndroidOCR.DATA_PATH + "image.jpeg");

	public OcrAsyncTask(Activity activity) {
		this.activity = activity;
	}

	@Override
	protected String doInBackground(byte[]... datum) {
		Log.d(TAG, "doInBackground");
		String text = null;
		for (byte[] data : datum) {

			BitmapFactory.Options options = new BitmapFactory.Options();
			int sampleSize = 4;
			options.inSampleSize = sampleSize;
			Log.d(TAG, "bitmap size reduced by " + sampleSize);
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
					options);

			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
			Log.d(TAG, "bitmap converted to ARGB_8888");

			Pix pix = ReadFile.readBitmap(bitmap);
			// Pix pix = ReadFile.readMem(data);

			// do I really need to be getting the return since objects are
			// mutable?
			
			//converts to grayscale
			Convert.convertTo8(pix);

			//converts to 
			AdaptiveMap.backgroundNormMorph(pix);

			Binarize.otsuAdaptiveThreshold(pix);
			
			Enhance.unsharpMasking(pix, 2, 0.5f);
			
			float skew = Skew.findSkew(pix);
			Rotate.rotate(pix, skew, true);
			
			WriteFile.writeImpliedFormat(pix, file);
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_EMAIL, "todd.frisch@gmail.com");
			intent.putExtra(Intent.EXTRA_SUBJECT, "OCR image");
			Uri uri = Uri.fromFile(file);
			intent.putExtra(Intent.EXTRA_STREAM, uri);


			//should probably test here what the image looks like by creating an image
//			TessBaseAPI baseApi = new TessBaseAPI();
//
//			Log.d(TAG, "TessBaseAPI created");
//			baseApi.init(AndroidOCR.DATA_PATH, AndroidOCR.ENGLISH_LANGUAGE);
//			Log.d(TAG, "TessBaseAPI initialized");
//			baseApi.setImage(pix);
//
//			Log.d(TAG, "TessBaseApi image set");
//			text = baseApi.getUTF8Text();
//			Log.d(TAG, "text returned from TessBaseAPI");
//			baseApi.end();
//			text = text.replaceAll("[^a-zA-Z0-9]+", " ");
//			text = text.trim();

		}
		return text;
	}

	@Override
	protected void onPostExecute(String result) {

//		Intent intent = new Intent(activity, ResultActivity.class);
//		intent.putExtra(Intent.EXTRA_TEXT, result);
//		activity.startActivity(intent);
		

	}
	
	
	
//	@Override
//	protected String doInBackground(byte[]... datum) {
//		Log.d(TAG, "doInBackground");
//		String text = null;
//		for (byte[] data : datum) {
//
//			BitmapFactory.Options options = new BitmapFactory.Options();
//			int sampleSize = 4;
//			options.inSampleSize = sampleSize;
//			Log.d(TAG, "bitmap size reduced by " + sampleSize);
//			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
//					options);
//
//			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//			Log.d(TAG, "bitmap converted to ARGB_8888");
//
//			Pix pix = ReadFile.readBitmap(bitmap);
//			// Pix pix = ReadFile.readMem(data);
//
//			// do I really need to be getting the return since objects are
//			// mutable?
//			
//			//converts to grayscale
//			Convert.convertTo8(pix);
//
//			//converts to 
//			AdaptiveMap.backgroundNormMorph(pix);
//
//			Binarize.otsuAdaptiveThreshold(pix);
//			
//			Enhance.unsharpMasking(pix, 2, 0.5f);
//			
//			float skew = Skew.findSkew(pix);
//			Rotate.rotate(pix, skew, true);
//			
//			
//
//
//			//should probably test here what the image looks like by creating an image
//			TessBaseAPI baseApi = new TessBaseAPI();
//
//			Log.d(TAG, "TessBaseAPI created");
//			baseApi.init(AndroidOCR.DATA_PATH, AndroidOCR.ENGLISH_LANGUAGE);
//			Log.d(TAG, "TessBaseAPI initialized");
//			baseApi.setImage(pix);
//
//			Log.d(TAG, "TessBaseApi image set");
//			text = baseApi.getUTF8Text();
//			Log.d(TAG, "text returned from TessBaseAPI");
//			baseApi.end();
//			text = text.replaceAll("[^a-zA-Z0-9]+", " ");
//			text = text.trim();
//
//		}
//		return text;
//	}
//
//	@Override
//	protected void onPostExecute(String result) {
//
//		Intent intent = new Intent(activity, ResultActivity.class);
//		intent.putExtra(Intent.EXTRA_TEXT, result);
//		activity.startActivity(intent);
//		// new intent for ResultActivity
//
//	}


}
