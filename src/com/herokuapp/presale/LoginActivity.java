package com.herokuapp.presale;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	public void btnLogin(View view) {
		RequestLogin requestLogin = new RequestLogin();
		requestLogin.execute(MainActivity.api_host + "/users/sign_in");
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	class RequestLogin extends AsyncTask<String, String, JSONObject> {
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
	            response = httpclient.execute(post);
	            StatusLine statusLine = response.getStatusLine();
	            ByteArrayOutputStream out = new ByteArrayOutputStream();
	            switch(statusLine.getStatusCode()) {
	            case HttpStatus.SC_OK:
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	            	break;
	            case HttpStatus.SC_UNAUTHORIZED:
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	            	break;
            	default:
            		response.getEntity().getContent().close();
                	throw new IOException(statusLine.getReasonPhrase());	
	            }
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
	    			String error = result.getString("error");
	    			if(error.length() > 0) {
	    				Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
	    			} else {
	    				int id = result.getInt("id");
						String email = result.getString("email");
			    		String auth_token = result.getString("auth_token");
			    		Toast.makeText(LoginActivity.this, auth_token, Toast.LENGTH_LONG).show();
	    			}
				} catch (JSONException e) {
					e.printStackTrace();
				}
	    	}
	    }
	}
}
