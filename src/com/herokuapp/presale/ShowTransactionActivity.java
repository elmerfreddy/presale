package com.herokuapp.presale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ShowTransactionActivity extends Activity {
	
	private Detail[] details = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_transaction);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		int id = bundle.getInt("ID");
		Transaction transaction = Transaction.find(id);
		details = transaction.details;
		
		((TextView) findViewById(R.id.txtShowStoreName)).setText(transaction.store_name);
		((TextView) findViewById(R.id.txtTransactionTotal)).setText("" + transaction.total);
		ListView lstShowProducts = (ListView) findViewById(R.id.lstShowProducts);
		DetailAdapter detailAdapter = new DetailAdapter(this);
		lstShowProducts.setAdapter(detailAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_transaction, menu);
		return true;
	}
	
	class DetailAdapter extends ArrayAdapter<Detail> {
		private Activity context;
		
		public DetailAdapter(Activity context) {
			super(context, R.layout.listitem_detail, details);
	        this.context = context;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
	    	LayoutInflater inflater = context.getLayoutInflater();
	    	View item = inflater.inflate(R.layout.listitem_detail, null);

	    	TextView txtDetailProductName = (TextView) item.findViewById(R.id.txtDetailProductName);
	    	txtDetailProductName.setText(details[position].product_name);

	    	TextView txtDetailQuantity= (TextView) item.findViewById(R.id.txtDetailQuantity);
	    	txtDetailQuantity.setText("" + details[position].quantity);
	    	
	    	TextView txtDetailPrice = (TextView) item.findViewById(R.id.txtDetailPrice);
	    	txtDetailPrice.setText("" + details[position].price);
	    	
	    	TextView txtDetailSubtotal = (TextView) item.findViewById(R.id.txtDetailSubtotal);
	    	txtDetailSubtotal.setText("" + details[position].total);

	    	return(item);
	    }
	}
}
