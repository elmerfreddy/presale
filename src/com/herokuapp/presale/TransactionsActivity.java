package com.herokuapp.presale;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TransactionsActivity extends Activity {
	
	private Transaction[] transactions = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transactions);
		
		transactions = Transaction.all();
		TransactionAdapter adapter = new TransactionAdapter(this);
		ListView lstProducts = (ListView) findViewById(R.id.lstTransactions);
		lstProducts.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.transactions, menu);
		return true;
	}
	public class TransactionAdapter extends ArrayAdapter<Transaction> {
		private Activity context;
		 
	    public TransactionAdapter(Activity context) {
	        super(context, R.layout.listitem_transaction, transactions);
	        this.context = context;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	    	LayoutInflater inflater = context.getLayoutInflater();
	    	View item = inflater.inflate(R.layout.listitem_transaction, null);

	    	TextView txtName = (TextView) item.findViewById(R.id.txtStoreName);
	    	txtName.setText(transactions[position].store_name);

	    	TextView txtProductsCount = (TextView) item.findViewById(R.id.txtProductsCount);
	    	txtProductsCount.setText("" + transactions[position].products_count);
	    	
	    	TextView txtUserName = (TextView) item.findViewById(R.id.txtUserName);
	    	txtUserName.setText(transactions[position].user_name);

	    	return(item);
	    }
	}
}
