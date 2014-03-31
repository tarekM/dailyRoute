package com.example.dailyroute;

import android.os.Bundle; 
import android.app.Activity;
import android.view.Menu;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class MainActivity extends Activity {
	private EditText eventname = null;
	private TimePicker tp1 = null;
	private TimePicker tp2 = null;
	private Integer start_time = 0;
	private Integer end_time = 0;
	
	class ServerRequestTask extends AsyncTask<String, Void, String> {
	    protected String doInBackground(String... strings) {
	    	String responseString = "";
	    	try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost("http://leolei.herokuapp.com/users/" + strings[2]);
				
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		        nvps.add(new BasicNameValuePair("content-type", "application/json"));
		
		        StringEntity input = new StringEntity("{\"event\": \"" + strings[0] + "\",\"start time\": \"" + strings[1] + "\",\"end time\": \"" + strings[2] + "\"}");
		        input.setContentType("application/json");
		        httpPost.setEntity(input);
		
		        for (NameValuePair h : nvps)
		        {
		            httpPost.addHeader(h.getName(), h.getValue());
		        }
				
			    HttpResponse response = httpclient.execute(httpPost);
			    StatusLine statusLine = response.getStatusLine();
			    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
			        ByteArrayOutputStream out = new ByteArrayOutputStream();
			        response.getEntity().writeTo(out);
			        out.close();
			        responseString = out.toString();
			    } else{
			        //Closes the connection.
			    	responseString = "Response NOT OK";
			        response.getEntity().getContent().close();
			        throw new IOException(statusLine.getReasonPhrase());
			    }
			} catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
				responseString = e.getMessage();
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    	responseString = e.getMessage();
		    } finally {
		    	return responseString;
		    }
	    }

	    protected void onPostExecute(String responseString) {
	    	((android.widget.TextView) findViewById(R.id.textView3)).setText(responseString);
	    }
	}
    	
    	private class GenericOnClickListener implements OnClickListener {
    		@Override
            public void onClick(View v) {
    			String eventname__ = eventname.getText().toString();
    			String timeS = start_time.toString();
    			String timeE = end_time.toString();
    			switch(v.getId()){
    				case R.id.button2:
    					new ServerRequestTask().execute(eventname__, timeS,timeE, "add");
    					break;
    				case R.id.button1:
    					//cancel everything
    					break;
    			}
    		}
    	}
    	@Override
    	protected void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);
    		setContentView(R.layout.activity_main);
    		
    		eventname = (EditText) findViewById(R.id.editText1);
    		tp1 = (TimePicker) findViewById(R.id.timePicker1);
    		tp2 = (TimePicker) findViewById(R.id.timePicker2);
    		start_time = tp1.getCurrentHour()*100 + tp1.getCurrentMinute();
    		end_time = tp2.getCurrentHour()*100 + tp2.getCurrentMinute();
    		
    		findViewById(R.id.button1).setOnClickListener(new GenericOnClickListener());
    		findViewById(R.id.button2).setOnClickListener(new GenericOnClickListener());
    	}

    	@Override
    	public boolean onCreateOptionsMenu(Menu menu) {
    		// Inflate the menu; this adds items to the action bar if it is present.
    		getMenuInflater().inflate(R.menu.main, menu);
    		return true;
    	}

 }

    

