package com.herokuapp.presale;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Transaction {
	public int id = 0;
	public int remote_id = 0;
	public String store_name = null;
	public int products_count = 0;
	public String user_name = null;
    public float total = (float) 0.0;
    public Detail[] details = null;
    public String date = null;

	public Transaction() {
	}
	
	public static Transaction find(int _id) {
		Transaction transaction =  null;
		RequestProduct requestTransaction = new RequestProduct();
		requestTransaction.execute(MainActivity.api_host + "/transactions/" + _id + "?auth_token=" + MainActivity.authToken);
		try {
			transaction = parseTransaction(requestTransaction.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return transaction;
	}

	public static Transaction[] all() {
		ArrayList<Transaction> transactions =  new ArrayList<Transaction>();
		RequestProducts requestTransactions = new RequestProducts();
		requestTransactions.execute(MainActivity.api_host + "/transactions?auth_token=" + MainActivity.authToken);
		try {
			transactions = parseTransactions(requestTransactions.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return transactions.toArray(new Transaction[transactions.size()]);
	}
	
	private static Transaction parseTransaction(JSONObject result) {
		JSONObject o;
		Transaction s = new Transaction();
		JSONArray jsonArray;
		try {
			o = result;
			s.id = o.getInt("id");
			s.store_name = o.getString("store_name");
			s.products_count = o.getInt("products_count");
			s.user_name = o.getString("user_name");
			s.total = (float) o.getDouble("total");
			s.date = o.getString("date");
			s.details = new Detail[s.products_count];
			jsonArray = o.getJSONArray("details");
			for (int j = 0; j < jsonArray.length(); j++) {
				Detail d = new Detail();
				o = jsonArray.getJSONObject(j);
				d.id = o.getInt("id");
				d.quantity = o.getInt("quantity");
				d.product_name = o.getString("product_name");
				d.price = (float) o.getDouble("price");
				d.total = (float) o.getDouble("total");
				s.details[j] = d;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return s;
	}

	private static ArrayList<Transaction> parseTransactions(JSONArray result) {
		ArrayList<Transaction> results = new ArrayList<Transaction>();
		for (int i = 0; i < result.length(); i++) {
			JSONObject o;
			Transaction s = new Transaction();
			JSONArray jsonArray;
			try {
				o = result.getJSONObject(i);
				s.id = o.getInt("id");
				s.store_name = o.getString("store_name");
				s.products_count = o.getInt("products_count");
				s.user_name = o.getString("user_name");
				s.total = (float) o.getDouble("total");
				s.date = o.getString("date");
				s.details = new Detail[s.products_count];
				jsonArray = o.getJSONArray("details");
				for (int j = 0; j < jsonArray.length(); j++) {
					Detail d = new Detail();
					o = jsonArray.getJSONObject(j);
					d.id = o.getInt("id");
					d.quantity = o.getInt("quantity");
					d.product_name = o.getString("product_name");
					d.price = (float) o.getDouble("price");
					d.total = (float) o.getDouble("total");
					s.details[j] = d;
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			results.add(s);
		}
		return results;
	}
}
