package com.herokuapp.presale;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class Store {

	public static Context context;
	public int id = 0;
	public int remote_id = 0;
	public String name = null;
	public String owner = null;
	public String address = null;
	public double latitude = 0.0;
	public double longitude = 0.0;
	public String updated_at = null;
	public String created_at = null;
	
	public Store() {
	}
	
	public static Store find(int store_id) {
		Store store = new Store();
		store.id = store_id;
		return store;
	}
	
	/*
	 * Retorna los objetos de la base de datos como objectos de la clase Product
	 */
	public static Store[] all(Context c) {
		context =  c;
		Stores s = new Stores(context);
		ArrayList<Store> stores =  new ArrayList<Store>();
		RequestProducts requestStores = new RequestProducts();
		requestStores.execute(MainActivity.api_host + "/stores?auth_token=" + MainActivity.authToken);
		try {
			s.open();
			stores = parseStores(requestStores.get());
			s.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return stores.toArray(new Store[stores.size()]);
	}
	
	public static void testData() {
		Stores stores = new Stores(context);
		try {
			stores.open();
			stores.insertTestData();
			stores.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static ArrayList<Store> parseStores(JSONArray stores) {
		ArrayList<Store> stors = new ArrayList<Store>();
		for (int i = 0; i < stores.length(); i++) {
			JSONObject o;
			Store s = new Store();
			try {
				o = stores.getJSONObject(i);
				s.id = o.getInt("id");
				s.name = o.getString("name");
				s.owner = o.getString("owner");
				s.address = o.getString("address");
				s.latitude = o.getDouble("latitude");
				s.longitude = o.getDouble("longitude");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			stors.add(s);
		}
		return stors;
	}
}
