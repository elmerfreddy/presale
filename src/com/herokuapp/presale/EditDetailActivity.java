package com.herokuapp.presale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditDetailActivity extends Activity {
	private int position;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_detail);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		position = bundle.getInt("POSITION");
		String productName = bundle.getString("PRODUCT_NAME");
		String productPrice = bundle.getString("PRODUCT_PRICE");
		String quantity = bundle.getString("PRODUCT_QUANTITY");
		
		((TextView) findViewById(R.id.txtEditProductName)).setText(productName);
		((TextView) findViewById(R.id.txtEditProductPrice)).setText(productPrice);
		((EditText) findViewById(R.id.txtEditProductQuantity)).setText(quantity);
		
		Button btnSave = (Button) findViewById(R.id.btnEditSave);
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				EditText q = (EditText) findViewById(R.id.txtEditProductQuantity);
				Intent intent = new Intent();
				intent.putExtra("POSITION", position);
				intent.putExtra("PRODUCT_QUANTITY", q.getText().toString());
				Log.i("SEND DATA QUANTITY", q.getText().toString());
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
		
		Button btnCancel = (Button) findViewById(R.id.btnEditCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_detail, menu);
		return true;
	}

}
