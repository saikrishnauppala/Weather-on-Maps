package com.DS.MapWeather.Models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "mapweather")
public class MapWeatherBean {


	@Id
	    @GeneratedValue(strategy=GenerationType.AUTO)
	 	
	 	private Integer mapid;
	    private String source;
	    private String destination;
	    private String latitude;
	    private String longitude;
	    
		private String weather;
		
	    public String getLongitude() {
			return longitude;
		}

		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}

	
	    public Integer getMapid() {
			return mapid;
		}
		 public String getLatitude() {
				return latitude;
			}

			public void setLatitude(String latitude) {
				this.latitude = latitude;
			}

			public String getWeather() {
				return weather;
			}

			public void setWeather(String weather) {
				this.weather = weather;
			}
		public void setMapid(Integer mapid) {
			this.mapid = mapid;
		}

		

	

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String getDestination() {
			return destination;
		}

		public void setDestination(String destination) {
			this.destination = destination;
		}

		
}
