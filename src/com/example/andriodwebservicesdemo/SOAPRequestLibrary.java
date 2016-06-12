package com.example.andriodwebservicesdemo;

import java.io.IOException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

public class SOAPRequestLibrary {

	
	public String requestCities(String country){
		

		String URL = "http://www.webservicex.net/globalweather.asmx?WSDL";
		String SOAP_ACTION1 = "http://www.webserviceX.NET/GetCitiesByCountry";
		String NAMESPACE = "http://www.webserviceX.NET";
		String METHOD_NAME1 = "GetCitiesByCountry";
		String result_="";
		
		
		SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME1);
		request.addProperty("CountryName",country);
		
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		
		try{
			
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION1, envelope);
			
			SoapObject result = (SoapObject)envelope.bodyIn;
			
			
			if(result != null){
				
				result_ = result.getProperty(0).toString();
				
			}
			else
			{
				result_="no data found";
				
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		
		return result_;
	}
	
	
	public String requestWeatherInfo(String city, String country){
		
		
		String URL = "http://www.webservicex.net/globalweather.asmx?WSDL";
		String SOAP_ACTION1 = "http://www.webserviceX.NET/GetWeather";
	    String NAMESPACE = "http://www.webserviceX.NET";
	    String METHOD_NAME1 = "GetWeather";
	    String result_="";  
		   
	    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME1);
		
	    request.addProperty("CityName", city);
	    request.addProperty("CountryName",country);
	    
	    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    envelope.setOutputSoapObject(request);
	    envelope.dotNet = true;
	    
	    
	    try{
	    	
	    	HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	    	androidHttpTransport.call(SOAP_ACTION1, envelope);
	    	
	    	SoapObject result  = (SoapObject)envelope.bodyIn;
	    	
	    	
	    	if(result != null){
	    		result_ = result.getProperty(0).toString();
	    	}else
	    	{
	    		result_ = "no data found";
	    	}
	    	
	    }catch(Exception e){
	    	
	    	e.printStackTrace();
	    	
	    	
	    }
	    
	   
		return result_;
	}
	
	
	public String requestCountries(){
		
		String URL = "http://www.webservicex.net/country.asmx?WSDL";
		String SOAP_ACTION = "http://www.webserviceX.NET/GetCountries";
		String NAME_SPACE = "http://www.webserviceX.NET";
		String METHOD_NAME = "GetCountries";
		String _result="";
		
		SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		
		
		try {
		
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			
			SoapObject result = (SoapObject) envelope.bodyIn;
			
			if(result != null){
				_result = result.getProperty(0).toString();
			}
			else
			{
				_result = "data not found";
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return _result;
	}
	
	
}
