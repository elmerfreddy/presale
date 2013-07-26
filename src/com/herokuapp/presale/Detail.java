package com.herokuapp.presale;

public class Detail {
    public int id = 0;
    public int quantity = 0;
    public String product_name = null;
    public int product_id = 0;
    public float price = (float) 0.0;
    public float total = (float) 0.0;

    public Detail() {
    }
    
    public float getTotal() {
    	return quantity * price;
    }
}
