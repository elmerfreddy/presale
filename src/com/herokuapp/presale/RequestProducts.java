package com.herokuapp.presale;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

class RequestProducts extends AsyncTask<String, String, JSONArray> {
	private AsyncTaskListener asyncTaskListener;
	
	public interface AsyncTaskListener { 
		void onFinish(ArrayList<Product> products);
	}
	
	public RequestProducts(AsyncTaskListener asyncTaskListener) {
		this.asyncTaskListener = asyncTaskListener;
	}
	
	@Override
	protected JSONArray doInBackground(String... params) {
		HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        JSONArray jsonArray = null;
        String responseString = "";
        String url = params[0];
        Log.i("URL", url);
        try {
        	HttpGet get = new HttpGet(url);
        	get.setHeader("accept", "application/json");
        	get.setHeader("content-type", "application/json");
        	
            response = httpclient.execute(get);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
            responseString = out.toString();
            Log.i("JSON API", "out.toString()");
        } catch (ClientProtocolException e) {
        	e.getStackTrace();
        } catch (IOException e) {
        	e.getStackTrace();
		}
		try {
			jsonArray = new JSONArray(responseString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
        
        return jsonArray;
    }

    @Override
    protected void onPostExecute(JSONArray result) {
    	if(result != null) {
    		try {
    			Log.i("RESULT", "" + result.length());
    			ArrayList<Product> products = new ArrayList<Product>();
    			
    			for (int i = 0; i < result.length(); i++) {
					JSONObject o = result.getJSONObject(i);
					Product p = new Product();
					p.id = o.getInt("id");
					p.price = (float) o.getDouble("price");
					p.name = o.getString("name");
					products.add(p);
				}
    			Log.i("RESULT", ""+ result.toString() + " products");
    			asyncTaskListener.onFinish(products);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }
}