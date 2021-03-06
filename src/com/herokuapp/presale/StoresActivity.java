package com.herokuapp.presale;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class StoresActivity extends Activity {
	private Store[] stores = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stores);
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(StoresActivity.this);
		MainActivity.api_host = pref.getString("host", "http://10.0.2.2:3000");
		
		Store.context = StoresActivity.this;
		//Store.testData();
		stores = Store.all(this);
		
		StoreAdapter adapter = new StoreAdapter(this);
		ListView lstStores = (ListView) findViewById(R.id.lstTransactions);
		lstStores.setAdapter(adapter);
		

		lstStores.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				Store store = (Store) a.getAdapter().getItem(position);
				
				Bundle bundle = new Bundle();
				bundle.putInt("STORE_ID", store.id);
				bundle.putString("STORE_NAME", store.name);
				
				Intent intent = new Intent(StoresActivity.this, NewTransactionActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stores, menu);
		return true;
	}

	public class StoreAdapter extends ArrayAdapter<Store> {
		private Activity context;
		 
	    public StoreAdapter(Activity context) {
	        super(context, R.layout.listitem_product, stores);
	        this.context = context;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	    	LayoutInflater inflater = context.getLayoutInflater();
	    	View item = inflater.inflate(R.layout.listitem_store, null);

	    	TextView txtName = (TextView)item.findViewById(R.id.txtName);
	    	txtName.setText(stores[position].name);

	    	TextView txtPrice = (TextView)item.findViewById(R.id.txtAddress);
	    	txtPrice.setText("" + stores[position].address);

	    	return(item);
	    }
	}
}
