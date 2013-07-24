package com.herokuapp.presale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NewTransactionActivity extends Activity {
	
	private int store_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_transaction);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		store_id = bundle.getInt("STORE_ID");
		String storeName = bundle.getString("STORE_NAME");
		
		TextView txtNewTStore = (TextView) findViewById(R.id.txtNewTStore);
		txtNewTStore.setText(storeName);
		
		Button btnSaveTransaction = (Button) findViewById(R.id.btnSaveTransaction);
		btnSaveTransaction.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_transaction, menu);
		return true;
	}

}
