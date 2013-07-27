package com.herokuapp.presale;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;

class RequestProducts extends AsyncTask<String, String, JSONArray> {
	
	@Override
	protected JSONArray doInBackground(String... params) {
		HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        JSONArray jsonArray = null;
        String responseString = "";
        String url = params[0];
        try {
        	HttpGet get = new HttpGet(url);
        	get.setHeader("accept", "application/json");
        	get.setHeader("content-type", "application/json");
        	
            response = httpclient.execute(get);
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
			jsonArray = new JSONArray(responseString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
        
        return jsonArray;
    }
}