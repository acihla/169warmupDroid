package com.example.frontendwarmup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	
	private EditText uName;
	private EditText uPassword;
	private TextView loginMessage;
	
	private String result;
	
	private final int SUCCESS               =   1;
    private final int ERR_BAD_CREDENTIALS   =  -1;
    private final int ERR_USER_EXISTS       =  -2;
    private final int ERR_BAD_USERNAME      =  -3;
    private final int ERR_BAD_PASSWORD      =  -4;
	
    
    private static String uNameStr = "";
    private static String uPasswordStr = "";
    private static String count;
    private static int errCode;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.example.frontendwarmup.R.layout.activity_main);
		
		findViewById(R.id.login).setVisibility(0);
		findViewById(R.id.signup).setVisibility(0);
		findViewById(R.id.logout).setVisibility(4);
		
		EditText newUsername = (EditText)findViewById(R.id.usernameIn);
		EditText newPassword = (EditText)findViewById(R.id.passwordIn);
		
		findViewById(R.id.login).setOnClickListener(
				new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						uName = (EditText) findViewById(R.id.usernameIn);
						uPassword = (EditText) findViewById(R.id.passwordIn);
						uNameStr = uName.getText().toString();
						uPasswordStr = uPassword.getText().toString();
						attemptLogin();
						
					}
				});
		
		findViewById(R.id.signup).setOnClickListener(
				new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						uName = (EditText) findViewById(R.id.usernameIn);
						uPassword = (EditText) findViewById(R.id.passwordIn);
						uNameStr = uName.getText().toString();
						uPasswordStr = uPassword.getText().toString();
						attemptSignup();
						
					}
				});
		
		findViewById(R.id.logout).setOnClickListener(
				new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						attemptLogout();
						
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void attemptLogin() {
		loginMessage = (TextView) findViewById(R.id.message);
		HttpClient client = new DefaultHttpClient();
		EditText newUsername = (EditText)findViewById(R.id.usernameIn);
		EditText newPassword = (EditText)findViewById(R.id.passwordIn);
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
		JSONObject json = new JSONObject();
		String url = "http://serene-meadow-7552.herokuapp.com/users/login";
		HttpPost post = new HttpPost(url);
		try {      
	        json.put("user", uNameStr);
	        json.put("password", uPasswordStr);
	        
	        Log.v("LOGGING IN USER", uNameStr + " "+ uPasswordStr);
            StringEntity se = new StringEntity( json.toString());  
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            HttpResponse response = client.execute(post);
            
            HttpEntity resEntity = response.getEntity();
            
            String jsonString = EntityUtils.toString(resEntity);

            JSONObject forParse =new JSONObject(jsonString);
            
            
	        if (resEntity != null) {  
	             
	             errCode = Integer.parseInt(forParse.getString("errCode"));
	             
	             switch (errCode) {
		 			case SUCCESS:
		 				count = forParse.getString("count");
		              	Log.i("GET RESPONSE",jsonString);
		              	loginMessage.setText("Login was successful... Hi " +uNameStr+ " you have logged in " +count+ " times!");
		              	findViewById(R.id.login).setVisibility(4);
		 				findViewById(R.id.signup).setVisibility(4);
		 				findViewById(R.id.logout).setVisibility(0);
		 				findViewById(R.id.usernameIn).setVisibility(4);
		 				findViewById(R.id.passwordIn).setVisibility(4);
		 				break;
		 	
		 			case ERR_BAD_CREDENTIALS:
		 				Log.i("GET RESPONSE",jsonString);
		              	loginMessage.setText("Invalid username and password combination. Please try again. ");
		         		newUsername.setText("");
		         		newPassword.setText("");
		         		break;
		 				
		 			case ERR_USER_EXISTS:
		 				Log.i("GET RESPONSE",jsonString);
		              	loginMessage.setText("This user name already exists. Please try again.");
		         		newUsername.setText("");
		         		newPassword.setText("");
		         		break;
		 				
		 			case ERR_BAD_USERNAME:
		 				Log.i("GET RESPONSE",jsonString);
		              	loginMessage.setText("The user name should not be empty and at most 128 characters long. Please try again.");
		         		newUsername.setText("");
		         		newPassword.setText("");
		         		break;
		 				
		 			case ERR_BAD_PASSWORD:
		 				Log.i("GET RESPONSE",jsonString);
		              	loginMessage.setText("The password should be at most 128 characters long. Please try again");
		         		newUsername.setText("");
		         		newPassword.setText("");
		         		break;
		 				
		 			default:
		 				loginMessage.setText("Something Strange happened. Consult the server admin... " +errCode);
		         		newUsername.setText("");
		         		newPassword.setText("");
		 				break;
	             }
	             
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        loginMessage.setText("Login was NOT TEST successful");
	    } 
	}
	    
	
		

	
	public void attemptSignup() {
		loginMessage = (TextView) findViewById(R.id.message);
		HttpClient client = new DefaultHttpClient();
		EditText newUsername = (EditText)findViewById(R.id.usernameIn);
		EditText newPassword = (EditText)findViewById(R.id.passwordIn);
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
		JSONObject json = new JSONObject();
		String url = "http://serene-meadow-7552.herokuapp.com/users/add";
		HttpPost post = new HttpPost(url);
		try {      
	        json.put("user", uNameStr);
	        json.put("password", uPasswordStr);
	        
	        Log.v("SIGN UP USER", uNameStr +" "+ uPasswordStr);
            StringEntity se = new StringEntity( json.toString());  
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            HttpResponse response = client.execute(post);
            
            HttpEntity resEntity = response.getEntity();
            
            String jsonString = EntityUtils.toString(resEntity);

            JSONObject forParse =new JSONObject(jsonString);
            
            
	        if (resEntity != null) {  
	             
	             errCode = Integer.parseInt(forParse.getString("errCode"));
	             
	             switch (errCode) {
	             case SUCCESS:
		 				count = forParse.getString("count");
		              	Log.i("GET RESPONSE",jsonString);
		              	loginMessage.setText("Login was successful... Hi " +uNameStr+ " you have logged in " +count+ " times!");
		              	findViewById(R.id.login).setVisibility(4);
		 				findViewById(R.id.signup).setVisibility(4);
		 				findViewById(R.id.logout).setVisibility(0);
		 				findViewById(R.id.usernameIn).setVisibility(4);
		 				findViewById(R.id.passwordIn).setVisibility(4);
		 				break;
		 	
		 			case ERR_BAD_CREDENTIALS:
		 				Log.i("GET RESPONSE",jsonString);
		              	loginMessage.setText("Invalid username and password combination. Please try again. ");
		         		newUsername.setText("");
		         		newPassword.setText("");
		         		break;
		 				
		 			case ERR_USER_EXISTS:
		 				Log.i("GET RESPONSE",jsonString);
		              	loginMessage.setText("This user name already exists. Please try again.");
		         		newUsername.setText("");
		         		newPassword.setText("");
		         		break;
		 				
		 			case ERR_BAD_USERNAME:
		 				Log.i("GET RESPONSE",jsonString);
		              	loginMessage.setText("The user name should not be empty and at most 128 characters long. Please try again.");
		         		newUsername.setText("");
		         		newPassword.setText("");
		         		break;
		 				
		 			case ERR_BAD_PASSWORD:
		 				Log.i("GET RESPONSE",jsonString);
		              	loginMessage.setText("The password should be at most 128 characters long. Please try again");
		         		newUsername.setText("");
		         		newPassword.setText("");
		         		break;
		 				
		 			default:
		 				loginMessage.setText("Something Strange happened. Consult the server admin... " +errCode);
		         		newUsername.setText("");
		         		newPassword.setText("");
		 				break;
	             }
	             
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        loginMessage.setText("Login was NOT TEST successful");
	    } 
	}
	
	
	public void attemptLogout() {
		loginMessage = (TextView) findViewById(R.id.message);
		findViewById(R.id.login).setVisibility(0);
		findViewById(R.id.signup).setVisibility(0);
		findViewById(R.id.logout).setVisibility(4);
		EditText newUsername = (EditText)findViewById(R.id.usernameIn);
		newUsername.setText("");
		EditText newPassword = (EditText)findViewById(R.id.passwordIn);
		newPassword.setText("");
		findViewById(R.id.usernameIn).setVisibility(0);
		findViewById(R.id.passwordIn).setVisibility(0);
		loginMessage.setText("Logout was successful!");
			
	}
	

}



