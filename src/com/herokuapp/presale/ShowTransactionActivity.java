package com.herokuapp.presale;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

public class ShowTransactionActivity extends Activity {
	private int transaction_id;
	private Detail[] details = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_transaction);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		transaction_id = bundle.getInt("ID");
		Transaction transaction = Transaction.find(transaction_id);
		details = transaction.details;
		
		((TextView) findViewById(R.id.txtShowStoreName)).setText(transaction.store_name);
		((TextView) findViewById(R.id.txtTransactionTotal)).setText("" + transaction.total);
		ListView lstShowProducts = (ListView) findViewById(R.id.lstShowProducts);
		DetailAdapter detailAdapter = new DetailAdapter(this);
		lstShowProducts.setAdapter(detailAdapter);
		
		Button btnDeleteTransaction = (Button) findViewById(R.id.btnDeleteTransaction);
		btnDeleteTransaction.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DeleteTransaction delete = new DeleteTransaction();
				delete.execute(MainActivity.api_host + "/transactions/" + transaction_id + "?auth_token=" + MainActivity.authToken);
				finish();
			}
		});
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
	
	class DeleteTransaction extends AsyncTask<String, String, JSONObject> {
		@Override
		protected JSONObject doInBackground(String... params) {
			HttpClient httpclient = new DefaultHttpClient();
	        String url = params[0];
	        JSONObject jsonObject = null;
	        try {
	        	HttpDelete delete = new HttpDelete(url);
	        	delete.setHeader("accept", "application/json");
	        	delete.setHeader("content-type", "application/json");
	        	
	            httpclient.execute(delete);
	        } catch (ClientProtocolException e) {
	        	e.getStackTrace();
	        } catch (IOException e) {
	        	e.getStackTrace();
			}
	        
	        return jsonObject;
	    }
	
	    @Override
	    protected void onPostExecute(JSONObject result) {
	    	if(result == null) {
	    		Toast.makeText(ShowTransactionActivity.this, "Transacción eliminada... ", Toast.LENGTH_LONG).show();
	    	}
	    }
	}
}
