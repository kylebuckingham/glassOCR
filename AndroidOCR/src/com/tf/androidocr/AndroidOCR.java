package com.tf.androidocr;

import android.os.Environment;

public class AndroidOCR {
	public static final String PICTURE = "picture";
	public static final String TEXT = "text";
	public static final String APP_NAME = "OcrGlass";
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/" + APP_NAME + "/";
	public static final String TESSDATA = "tessdata";
	public static final String ENGLISH_LANGUAGE = "eng";
}
