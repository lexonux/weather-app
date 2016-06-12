package com.example.andriodwebservicesdemo;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;

public class WeatherMainActivity extends Activity {

	
	private Spinner citySpinner, countrySpinner;
	private TextView temperaturetv, relativeHumiditytv, visibilitytv, pressuretv, dewpointtv, windtv, locationtv;
	private ProgressDialog progressdialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_main);
	
	
		citySpinner = (Spinner)findViewById(R.id.citySpinner);
		countrySpinner = (Spinner)findViewById(R.id.countrySpinner);
		
		
		countriesTask contask = new countriesTask();
		contask.execute();
		
		
		temperaturetv = (TextView)findViewById(R.id.temperatureTv);
		relativeHumiditytv = (TextView)findViewById(R.id.relativeHumidityTv);
		visibilitytv = (TextView)findViewById(R.id.visibilityTv);
		pressuretv = (TextView)findViewById(R.id.pressureTv);
		dewpointtv = (TextView)findViewById(R.id.dewpointtv);
		windtv = (TextView)findViewById(R.id.windtv);
		locationtv = (TextView)findViewById(R.id.locationtv);
		
		countrySpinner.setOnItemSelectedListener( new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
			
				CitiesTask task = new CitiesTask();
				task.execute();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			} 
			
			
		});
		
	}

	
	
	class CitiesTask extends AsyncTask<Void, Void, Void>{

		String response;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressdialog = new ProgressDialog(WeatherMainActivity.this);
			progressdialog.setMessage("Loading cities");
			progressdialog.setCancelable(false);
			progressdialog.show();
		
			
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
		
			SOAPRequestLibrary request = new SOAPRequestLibrary();
			response = request.requestCities(countrySpinner.getSelectedItem().toString());
			
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		
			ArrayList<String> citiesList = readXML(response, "city");
			
			if(citiesList.size()==0){
				
				AlertDialog.Builder alertbuilder = new AlertDialog.Builder(WeatherMainActivity.this);
				alertbuilder.setMessage("Cities data not available");
				alertbuilder.setPositiveButton("Ok", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						progressdialog.dismiss();
					}
				});
				
				alertbuilder.show();
				
				
			}
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_cities_layoutfile, R.id.citiesrow, citiesList);
			
			citySpinner.setAdapter(adapter);
						
			progressdialog.dismiss();
		}
		
		
	}
	
	
	
	private ArrayList<String> readXML(String response, final String tagname){
	
		final ArrayList<String> citiesList = new ArrayList<String>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			
			parser.parse(IOUtils.toInputStream(response),new DefaultHandler(){
				
				boolean found = false;
				
				@Override
				public void startElement(String uri, String localName, String qName, Attributes attributes)
					throws SAXException {
					// TODO Auto-generated method stub
					super.startElement(uri, localName, qName, attributes);
				
					if(qName.equalsIgnoreCase(tagname))
						found = true;
				}
			
			
			@Override
				public void characters(char[] ch, int start, int length) throws SAXException {
					// TODO Auto-generated method stub
					super.characters(ch, start, length);
					if(found)
						citiesList.add(new String(ch, start, length));	
			}
		
			@Override
				public void endElement(String uri, String localName, String qName) throws SAXException {
					// TODO Auto-generated method stub
					super.endElement(uri, localName, qName);
					
					if(qName.equalsIgnoreCase(tagname))
					found = false;
				}				
			});
		
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return citiesList;	
	}
	
	
	public void getWeather(View v){
			
		WeatherTask weathertask = new WeatherTask();
		weathertask.execute();
	}
	
	
	class WeatherTask extends AsyncTask<Void, Void, Void>{

		String response;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		
			progressdialog = new ProgressDialog(WeatherMainActivity.this);
			progressdialog.setMessage("Loading weather");
			progressdialog.setCancelable(false);
			progressdialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			SOAPRequestLibrary request = new SOAPRequestLibrary();
			
			Object cityspinnervalue = citySpinner.getSelectedItem();
		    Object countryspinnervalue = countrySpinner.getSelectedItem();
		    		
		    if(cityspinnervalue != null && countryspinnervalue != null)
			response = request.requestWeatherInfo(cityspinnervalue.toString(), countryspinnervalue.toString());			
			    
			return null;
		}
		
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		//temperature
			
			if(response != null){
			
			if(readXML(response, "Temperature").size()==0){
				
				temperaturetv.setText("Temperature: data not available");	
			}
			else{
				temperaturetv.setText("Temperature: "+readXML(response, "Temperature").get(0));
			}
			//pressure
			if(readXML(response, "Pressure").size()==0){
				
				pressuretv.setText("Pressure: data not available");	
			}
			else{
				pressuretv.setText("Pressure: "+readXML(response, "Pressure").get(0));
			}
			//relative humidity
			if(readXML(response, "RelativeHumidity").size()==0){
	
				relativeHumiditytv.setText("Relative Humidity: data not available");	
			}
			else{
				relativeHumiditytv.setText("Relative Humidity: "+readXML(response, "RelativeHumidity").get(0));
			}
			//visibility
			if(readXML(response, "Visibility").size()==0){
	
				visibilitytv.setText("Visibility: data not available");	
			}
			else{
				visibilitytv.setText("Visibility: "+readXML(response, "Visibility").get(0));
			}
			//dew point
			if(readXML(response, "DewPoint").size()==0){
	
				dewpointtv.setText("Dew Point: data not available");	
			}
			else{
				dewpointtv.setText("Dew Point: "+readXML(response, "DewPoint").get(0));
			}
			//wind
			if(readXML(response, "Wind").size()==0){
	
				windtv.setText("Wind: data not available");	
			}
			else{
				windtv.setText("Wind: "+readXML(response, "Wind").get(0));
			}
	
			//location
			if(readXML(response, "Location").size()==0){
				
				locationtv.setText("Location: data not available");	
			}
			else{
				locationtv.setText("Location: "+readXML(response, "Location").get(0));
			}
			}
			progressdialog.dismiss();
			
		}
	}
	
	
	
	
	class countriesTask extends AsyncTask<Void, Void, Void>{

		String response;
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			progressdialog = new ProgressDialog(WeatherMainActivity.this);
			progressdialog.setMessage("Loading countries");
			progressdialog.setCancelable(false);
			progressdialog.show();
			
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SOAPRequestLibrary request = new SOAPRequestLibrary();
			
			response = request.requestCountries();
		
			return null;	
		}
	
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		
			if(response.length()==0){
				
				AlertDialog.Builder builder = new AlertDialog.Builder(WeatherMainActivity.this);
				builder.setMessage("Please connect to the Internet");
				builder.setPositiveButton("Ok", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				
				builder.show();
				
				
			}
			else{
				ArrayList<String> countries = readXML(response, "Name");
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_cities_layoutfile, R.id.citiesrow, countries);
			
				countrySpinner.setAdapter(adapter);
				
				countrySpinner.setSelection(1);
				
			}
			
			progressdialog.dismiss();
			
		}
		
	}
	
	
	
}
