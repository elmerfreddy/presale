package com.herokuapp.presale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnProducts = (Button) findViewById(R.id.btnProducts);
		btnProducts.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch(v.getId()) {
		case R.id.btnProducts:
			intent = new Intent(this, ProductsActivity.class);
			startActivity(intent);
			break;
			/*
		case R.id.btnStores:
			intent = new Intent(this, StoresActivity.class);
			startActivity(intent);
			break;
		case R.id.btnTransactions:
			intent = new Intent(this, TransactionsActivity.class);
			startActivity(intent);
			break;
		case R.id.btnSynchronize:
			Toast.makeText(this, "Implementar con webservice", Toast.LENGTH_SHORT).show();
			break;
			*/
		}
	}

}
