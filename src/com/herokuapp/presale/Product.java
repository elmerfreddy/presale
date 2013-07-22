package com.herokuapp.presale;

import java.util.ArrayList;

import android.content.Context;
import android.widget.Toast;


public class Product {
	public static Context context;
	public int id = 0;
	public int remote_id = 0;
	public String name = null;
	public Float price = (float) 0.0;
	public String updated_at = null;
	public String created_at = null;
	
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
	public static Product[] all() {
		ArrayList<Product> alProducts =  new ArrayList<Product>();
		
		RequestProducts requestProducts = new RequestProducts();
		requestProducts.execute(MainActivity.api_host + "/products?auth_token=" + MainActivity.authToken);
		
		//alProducts = products;
		
		Toast.makeText(context, "" + alProducts.size(), Toast.LENGTH_LONG).show();
		//alProducts = MainActivity.productsList;

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
	
}
