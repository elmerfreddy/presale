package com.herokuapp.presale;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;


public class Product {
	public static Context context;
	public int id = 0;
	public int remote_id = 0;
	public String name = null;
	public Float price = (float) 0.0;
	public String updated_at = null;
	public String created_at = null;
	static ArrayList<Product> alProducts =  new ArrayList<Product>();
	
	public Product() {
	}
	
	public Product(String name) {
		this.id = (int) (Math.random()*20);
		this.name = name;
		this.price = (float) (Math.random()*200 + 50);
	}
	
	public static Product find(int product_id) {
		Product product = new Product();
		product.id = product_id;
		product.name = "Product " + product_id;
		product.price = (float) (Math.random()*200 + 50);
		return product;
	}
	
	/*
	 * Retorna los objetos de la base de datos como objectos de la clase Product
	 */
	public static Product[] all() throws InterruptedException, ExecutionException {
		RequestProducts requestProducts = new RequestProducts();
		requestProducts.execute(MainActivity.api_host + "/products?auth_token=" + MainActivity.authToken);
		
		alProducts = parseProducts(requestProducts.get());
		
		//Toast.makeText(context, "" + alProducts.size(), Toast.LENGTH_LONG).show();
		return alProducts.toArray(new Product[alProducts.size()]);
	}
	
	public static void testData() {
		Products products = new Products(context);
		try {
			products.open();
			products.insertTestData();
			products.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static ArrayList<Product> parseProducts(JSONArray products) {
		ArrayList<Product> prods = new ArrayList<Product>();
		for (int i = 0; i < products.length(); i++) {
			JSONObject o;
			Product p = new Product();
			try {
				o = products.getJSONObject(i);
				p.id = o.getInt("id");
				p.price = (float) o.getDouble("price");
				p.name = o.getString("name");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			prods.add(p);
		}
		return prods;
	}
}
