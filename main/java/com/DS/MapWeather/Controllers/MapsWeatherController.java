package com.DS.MapWeather.Controllers;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.DS.MapWeather.Models.MapWeatherBean;
import com.DS.MapWeather.Models.MapWeatherRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;

import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;

@Controller
public class MapsWeatherController 
{
	@Autowired
	MapWeatherRepository Repository;
	
	String mapsKey="AIzaSyAA4aNFh4DZX9qjJiUbN5bS6dEWK8gnHeE";
	String weatherKey="52efdc013b3771a6966a984275b14df2";
	@RequestMapping("/")
	public String anypage() 
	{
		return "view/homepage.jsp";
	}
	
	@RequestMapping("/homepage")
	public String homepage() 
	{
		return "view/homepage.jsp";
	}
	@RequestMapping("/apicall")
	public ModelAndView mapWeatherAPICall(HttpServletRequest hr) 
	{	
		
		
		
		
		String source=hr.getParameter("source");
		source=source.replaceAll(" ","+");
		String destination=hr.getParameter("destination");
		destination=destination.replaceAll(" ","+");
		ArrayList<Float> steplat=new ArrayList<Float>();
		ArrayList<Float> steplong=new ArrayList<Float>();
		ArrayList<String> allweather=new ArrayList<String>();
		ArrayList<String> dbweather=new ArrayList<String>();
		//if set to 0 no database caching
		int checkdatabase=1;
		//calling database before calling api to check the cache
		if(checkdatabase==1) 
		{
			MapWeatherBean db = null; 
			long dbstartTime = System.currentTimeMillis();
			System.out.println("start time db"+dbstartTime);
			db = Repository.findBySourceAndDestination(source, destination);
		
			long dbendTime = System.currentTimeMillis();
			System.out.println("\n end time db"+dbendTime);
			System.out.println("time taken"+(dbendTime-dbstartTime));
			
			//if record found in database
			if(db!=null) 
			{
		//		System.out.println("im from db cache"+db.getWeather());	
			//	System.out.println("\nlat"+db.getLatitude());
				//System.out.println("\n long"+db.getLongitude());
			ModelAndView mv=new ModelAndView();
			mv.setViewName("view/success.jsp");
			mv.addObject("source", source);
			mv.addObject("destination", destination);
			String[] lat=db.getLatitude().split(",");
			String[] lng=db.getLongitude().split(",");
			String[] wth=db.getWeather().split(",");
			for(String i:lat) {
				steplat.add(Float.parseFloat(i));
			}
			for(String i:lng) {
				steplong.add(Float.parseFloat(i));
			}
			
			for(String i:wth) {
			allweather.add("\""+i+"\"");	
			}
			
			System.err.println("\n in Database  fetching");
			int length=steplat.size();
			
		//	System.out.println(allweather);
			mv.addObject("steplat",steplat);
			mv.addObject("steplong",steplong);
			mv.addObject("length",length);
			mv.addObject("allweather",allweather);
			
			return mv;			
		}
			}
		
		
		GeoApiContext context=new GeoApiContext.Builder().apiKey(mapsKey).build();
		OpenWeatherMap owm=new OpenWeatherMap(weatherKey);
		DirectionsResult dirResult=null;
		Gson gson=null;

		try {
			long startTime = System.currentTimeMillis();
			System.out.println("start time maps"+startTime);
			dirResult=DirectionsApi.getDirections(context, source, destination).await();
			long endTime = System.currentTimeMillis();
			System.out.println("\n end time maps"+endTime);
			System.out.println("time taken"+(endTime-startTime));
		} catch (ApiException e1) {
			
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ModelAndView m=new ModelAndView();
			m.setViewName("view/error");
			return m;
			
			
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ModelAndView m=new ModelAndView();
			m.setViewName("view/error");
			return m;

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ModelAndView m=new ModelAndView();
			m.setViewName("view/error");
			return m;

		}
		gson = new GsonBuilder().setPrettyPrinting().create();
	
//		ArrayList<String> allsteps=new ArrayList<String>();
		
		
		CurrentWeather cw;
		String forecast;
		String forecast1;
		String maxtemp;
		String mintemp;
		String weather;
		
	//	forecast="[";
		for(Integer i=0;i < Integer.parseInt(gson.toJson(dirResult.routes[0].legs[0].steps.length));i++)
		{	
			
			maxtemp="";
			mintemp="";
			weather="";
			forecast="";
			forecast1="";
			Float latitude=Float.parseFloat(dirResult.routes[0].legs[0].steps[i].startLocation.lat+"");
			Float longitude=Float.parseFloat(dirResult.routes[0].legs[0].steps[i].startLocation.lng+"");
			
	//		coordinates="{lat:"+latitude+",lng:"+longitude+"}";
	//		allsteps.add(coordinates);
			
			steplat.add(latitude);
			steplong.add(longitude);
			//calling weather for each coordinate
			 long wstartTime = System.currentTimeMillis();
			System.out.println("start time weather"+wstartTime);
			cw=owm.currentWeatherByCoordinates(latitude, longitude);
			long wendTime = System.currentTimeMillis();
			System.out.println("\n end time weather"+wendTime);
			System.out.println("time taken"+(wendTime-wstartTime));
			
			maxtemp=" Max temperature "+cw.getMainInstance().getMaxTemperature();
			mintemp="  Min Temperature "+cw.getMainInstance().getMinTemperature();
			weather="  Weather "+cw.getWeatherInstance(0).getWeatherName();
			forecast="\""+maxtemp+mintemp+weather+"\"";
			allweather.add(forecast);
			
			forecast1=maxtemp+mintemp+weather;
			dbweather.add(forecast1);
			
			
		}
						
//			System.out.println(allweather);
	//	System.out.println(forecast);
		int length=steplat.size();	
		//Saving bean in database
		MapWeatherBean bean=new MapWeatherBean();
		bean.setSource(source);
		bean.setDestination(destination);
		String dblat="";
		String dblng="";
		String dbwth="";
		for(float i:steplat) {
			dblat=dblat+i+",";
		}
		for(float i:steplong) {
			dblng=dblng+i+",";
		}
		for(String i:dbweather) {
			dbwth=dbwth+i+",";
		}
		dblat=dblat.substring(0,dblat.length()-2);
		dblng=dblng.substring(0,dblng.length()-2);
		dbwth=dbwth.substring(0,dbwth.length()-2);
		bean.setLatitude(dblat);
		bean.setLongitude(dblng);
		bean.setWeather(dbwth);
		Repository.save(bean);
	//	MapWeatherBean b1 =Repository.findBySourceAndDestination(source, destination);
	//	System.out.println(b1.getLatitude().get(0));
				
		//setting model and view 
		ModelAndView mv=new ModelAndView();
		mv.setViewName("view/success.jsp");
		mv.addObject("source", source);
		mv.addObject("destination", destination);
		mv.addObject("steplat",steplat);
		mv.addObject("steplong",steplong);
		mv.addObject("length",length);
		mv.addObject("allweather",allweather);
		return mv;
		
		
	}
	
	}
