package com.herokuapp.presale;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AddProductActivity extends Activity {
	private Product[] products = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_product);
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(AddProductActivity.this);
		MainActivity.api_host = pref.getString("host", "http://10.0.2.2:3000");
		
		Product.context = AddProductActivity.this;
		//Product.testData();
		try {
			products = Product.all();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		ProductAdapter adapter = new ProductAdapter(this);
		ListView lstProducts = (ListView) findViewById(R.id.lstSelectProduct);
		lstProducts.setAdapter(adapter);
		lstProducts.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				Product product = (Product) a.getAdapter().getItem(position);
				Intent intent = new Intent();
				intent.putExtra("SELECTED_PRODUCT_ID", product.id);
				intent.putExtra("SELECTED_PRODUCT_NAME", product.name);
				intent.putExtra("SELECTED_PRODUCT_PRICE", product.price);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_product, menu);
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
	    	txtName.setText(products[position].name);

	    	TextView txtPrice = (TextView)item.findViewById(R.id.txtPrice);
	    	txtPrice.setText("" + products[position].price);

	    	return(item);
	    }
	}
}
