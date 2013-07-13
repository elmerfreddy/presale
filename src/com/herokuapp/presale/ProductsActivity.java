package com.herokuapp.presale;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ProductsActivity extends Activity {
	private Product[] products = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_products);
		
		products = Product.all();
		ProductAdapter adapter = new ProductAdapter(this);
		ListView lstProducts = (ListView) findViewById(R.id.lstProducts);
		lstProducts.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.products, menu);
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
