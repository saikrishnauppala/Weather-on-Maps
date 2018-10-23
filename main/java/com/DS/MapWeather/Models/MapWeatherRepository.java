package com.DS.MapWeather.Models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapWeatherRepository extends JpaRepository<MapWeatherBean,Integer>
{
	MapWeatherBean findBySourceAndDestination(String source,String destination);
	
}
