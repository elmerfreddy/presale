package com.herokuapp.presale;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class NewTransactionActivity extends Activity {
	private ArrayList<Product> products = new ArrayList<Product>();
	private int store_id = 0;
	private ProductAdapter adapter;

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
		
		adapter = new ProductAdapter(NewTransactionActivity.this);
		ListView lstProducts = (ListView) findViewById(R.id.lstSelectedProducts);
		lstProducts.setAdapter(adapter);
		
		Button btnSaveTransaction = (Button) findViewById(R.id.btnSaveTransaction);
		btnSaveTransaction.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		Button btnAddProduct = (Button) findViewById(R.id.btnAddProduct);
		btnAddProduct.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(NewTransactionActivity.this, AddProductActivity.class);
				startActivityForResult(intent, 1);
			}
		});
	}
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
	  super.onActivityResult(requestCode, resultCode, data); 
	  switch(requestCode) { 
	    case (1) : { 
	      if (resultCode == Activity.RESULT_OK) {
	    	  Product p = new Product();
	    	  p.id = data.getIntExtra("SELECTED_PRODUCT_ID", 0);
	    	  p.name = data.getStringExtra("SELECTED_PRODUCT_NAME");
	    	  p.price = data.getFloatExtra("SELECTED_PRODUCT_PRICE", 0);
	    	  adapter.add(p);
	      } 
	      break; 
	    } 
	  } 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_transaction, menu);
		return true;
	}
	
	public class ProductAdapter extends ArrayAdapter<Product> {
		private Activity context;
		 
	    public ProductAdapter(Activity context) {
	        super(context, R.layout.listitem_product, products);
	        this.context = context;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	    	LayoutInflater inflater = context.getLayoutInflater();
	    	View item = inflater.inflate(R.layout.listitem_product, null);

	    	TextView txtName = (TextView)item.findViewById(R.id.txtName);
	    	txtName.setText(products.get(position).name);

	    	TextView txtPrice = (TextView)item.findViewById(R.id.txtPrice);
	    	txtPrice.setText("" + products.get(position).price);

	    	return(item);
	    }
	}
}
