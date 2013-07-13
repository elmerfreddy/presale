package com.herokuapp.presale;


public class Product {
	public int id = 0;
	public String name = null;
	public Float price = (float) 0.0;
	
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
		Product[] products = new Product[20];
		for (int i = 0; i < 20; i++) {
			products[i] = new Product("Product " + i);
		}
		return products;
	}
}
