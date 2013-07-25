package com.herokuapp.presale;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
		MainActivity.api_host = pref.getString("host", "http://10.0.2.2:3000");
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
			String email = ((EditText) findViewById(R.id.txtEmail)).getText().toString();
			String password = ((EditText) findViewById(R.id.txtPassword)).getText().toString();
			Log.i("EMAIL", email);
			Log.i("PASSWORD", password);
			HttpClient httpclient = new DefaultHttpClient();
	        HttpResponse response;
	        JSONObject jsonObject = null;
	        String responseString = "";
	        String url = params[0];
	        try {
	        	HttpPost post = new HttpPost(url);
	        	post.setHeader("accept", "application/json");
	        	post.setHeader("content-type", "application/json");
	        	
				JSONStringer user = new JSONStringer()
					.object()
						.key("user")
							.object()
								.key("email").value(email)
								.key("password").value(password)
							.endObject()
						.endObject();
				Log.i("USER", user.toString());
				StringEntity entity = new StringEntity(user.toString());
				post.setEntity(entity);
	        		
	            response = httpclient.execute(post);
	            ByteArrayOutputStream out = new ByteArrayOutputStream();
	            response.getEntity().writeTo(out);
	            out.close();
	            responseString = out.toString();
	        } catch (ClientProtocolException e) {
	        	e.getStackTrace();
	        } catch (IOException e) {
	        	e.getStackTrace();
	        } catch (JSONException e) {
				e.printStackTrace();
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
	    			if(result.has("error")) {
	    				String error = result.getString("error");
	    				Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
	    			} else {
			    		String auth_token = result.getString("auth_token");
			    		MainActivity.authToken = auth_token;
			    		
			    		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			    		startActivity(intent);
	    			}
				} catch (JSONException e) {
					e.printStackTrace();
				}
	    	}
	    }
	}
}
