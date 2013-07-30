package com.tf.androidocr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class ResultActivity extends Activity {
	EditText editTextResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		Intent intent = getIntent();
		
		String text = intent.getStringExtra(Intent.EXTRA_TEXT);
		
		editTextResult = (EditText)findViewById(R.id.editText_result);
		editTextResult.setText(text);
		
		
	}


}
