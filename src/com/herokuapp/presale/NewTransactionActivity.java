package com.herokuapp.presale;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewTransactionActivity extends Activity {
	private ArrayList<Detail> details = new ArrayList<Detail>();
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
		
		lstProducts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				Detail detail = (Detail) a.getAdapter().getItem(position);
				Intent intent = new Intent(NewTransactionActivity.this, EditDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("PRODUCT_NAME", detail.product_name);
				bundle.putString("PRODUCT_PRICE", "" + detail.price);
				bundle.putString("PRODUCT_QUANTITY", "" + detail.quantity);
				bundle.putInt("POSITION", position);
				intent.putExtras(bundle);
				startActivityForResult(intent, 2);
			}
		});
		
		Button btnSaveTransaction = (Button) findViewById(R.id.btnSaveTransaction);
		btnSaveTransaction.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SaveDetail requestDetail = new SaveDetail();
				requestDetail.execute(MainActivity.api_host + "/transactions?auth_token=" + MainActivity.authToken);
				Toast.makeText(NewTransactionActivity.this, "Guardando transacción...", Toast.LENGTH_SHORT).show();
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
	    	  Detail d = new Detail();
	    	  d.product_id = data.getIntExtra("SELECTED_PRODUCT_ID", 0);
	    	  d.product_name = data.getStringExtra("SELECTED_PRODUCT_NAME");
	    	  d.price = data.getFloatExtra("SELECTED_PRODUCT_PRICE", 0);
	    	  d.total = d.getTotal();
	    	  adapter.add(d);
	      } 
	      break; 
	    }
	    case (2) : {
	    	if(resultCode == Activity.RESULT_OK) {
	    		TextView txtProductTotal = (TextView) findViewById(R.id.txtProductTotal);
		    	Detail d = details.get(data.getIntExtra("POSITION", 0));
		    	if(data.hasExtra("DELETE_PRODUCT")) {
		    		details.remove(d);
		    	} else {
			    	d.quantity = Integer.parseInt(data.getStringExtra("PRODUCT_QUANTITY"));
			    	d.total = d.getTotal();
		    	}
		    	txtProductTotal.setText("" + getDetailsTotal());
		    	adapter.notifyDataSetChanged();
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
	
	public class ProductAdapter extends ArrayAdapter<Detail> {
		private Activity context;
		 
	    public ProductAdapter(Activity context) {
	        super(context, R.layout.listitem_detail, details);
	        this.context = context;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	    	LayoutInflater inflater = context.getLayoutInflater();
	    	View item = inflater.inflate(R.layout.listitem_detail, null);

	    	TextView txtDetailProductName = (TextView) item.findViewById(R.id.txtDetailProductName);
	    	txtDetailProductName.setText(details.get(position).product_name);

	    	TextView txtDetailQuantity= (TextView) item.findViewById(R.id.txtDetailQuantity);
	    	txtDetailQuantity.setText("" + details.get(position).quantity);
	    	
	    	TextView txtDetailPrice = (TextView) item.findViewById(R.id.txtDetailPrice);
	    	txtDetailPrice.setText("" + details.get(position).price);
	    	
	    	TextView txtDetailSubtotal = (TextView) item.findViewById(R.id.txtDetailSubtotal);
	    	txtDetailSubtotal.setText("" + details.get(position).total);


	    	return(item);
	    }
	}
	
	private float getDetailsTotal() {
		float t = 0;
		for (Detail d: details) {
			t = t + d.getTotal();
		}
		return t;
	}
	
	
	class SaveDetail extends AsyncTask<String, String, JSONObject> {
		@Override
		protected JSONObject doInBackground(String... params) {
			HttpClient httpclient = new DefaultHttpClient();
	        HttpResponse response;
	        JSONObject jsonObject = null;
	        String responseString = "";
	        String url = params[0];
	        try {
	        	HttpPost post = new HttpPost(url);
	        	post.setHeader("accept", "application/json");
	        	post.setHeader("content-type", "application/json");
	        	
				JSONObject transaction = parseToJSON();
				Log.i("TRANSACTION", transaction.toString());
				
				StringEntity entity = new StringEntity(transaction.toString());
				post.setEntity(entity);
	        		
	            response = httpclient.execute(post);
	            ByteArrayOutputStream out = new ByteArrayOutputStream();
	            response.getEntity().writeTo(out);
	            out.close();
	            responseString = out.toString();
	            
	        } catch (ClientProtocolException e) {
	        	e.getStackTrace();
	        } catch (IOException e) {
	        	e.getStackTrace();
			}
			try {
				jsonObject = new JSONObject(responseString);
			} catch (JSONException e) {
				e.printStackTrace();
			}
	        
	        return jsonObject;
	    }
	
	    @Override
	    protected void onPostExecute(JSONObject result) {
	    	if(result != null) {
	    		try {
	    			if(result.has("error")) {
	    				String error = result.getString("error");
	    				Toast.makeText(NewTransactionActivity.this, error, Toast.LENGTH_LONG).show();
	    			} else {
	    				Toast.makeText(NewTransactionActivity.this, "Transacción Guardada... " + result.getInt("id"), Toast.LENGTH_LONG).show();
	    				Log.i("TRANSACTION CREATED", result.toString());
	    			}
				} catch (JSONException e) {
					e.printStackTrace();
				}
	    	}
	    }
	    
	    private JSONObject parseToJSON() {
	    	JSONObject trans = null, o = null, store = null;
			try {
				trans = new JSONObject();
				store = new JSONObject();
				store.put("store_id", store_id);
				JSONArray dets = new JSONArray();
				for(Detail d: details) {
					o = new JSONObject();
					o.put("product_id", d.product_id);
					o.put("price", d.price);
					o.put("quantity", d.quantity);
					dets.put(o);
				}
				store.put("details_attributes", dets);
				trans.put("transaction", store);
			} catch (JSONException e) {
				e.printStackTrace();
			}
	    	return trans;
	    }
	}
}
