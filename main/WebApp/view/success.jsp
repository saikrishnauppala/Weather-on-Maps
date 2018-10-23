<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Maps Weather Output</title>
<style>
      /* Always set the map height explicitly to define the size of the div
       * element that contains the map. */
      #map {
        height: 100%;
      }
      /* Optional: Makes the sample page fill the window. */
      html, body {
        height: 100%;
        margin: 0;
        padding: 0;
      }
      #floating-panel {
        position: absolute;
        top: 10px;
        left: 25%;
        z-index: 5;
        background-color: #fff;
        padding: 5px;
        border: 1px solid #999;
        text-align: center;
        font-family: 'Roboto','sans-serif';
        line-height: 30px;
        padding-left: 10px;
      }
    </style>
</head>
<body bgcolor="blue">
<center>
	<h2><p><b>Weather On Way<b></b></p></h2>
	<p>source   is : ${source} </p>
	<p>destination  is : ${destination}</p>
	
	</center>
	<div id="map"></div>
	<script>
      function initMap() {
        var directionsService = new google.maps.DirectionsService;
        var directionsDisplay = new google.maps.DirectionsRenderer;
        var map = new google.maps.Map(document.getElementById('map'), 
		{
          zoom: 7,
          center: {lat: ${steplat[0]}, lng: ${steplong[0]}}
        });
 
        
   //     for(int i=0;i<allsteps;i++){}
 //  var test=parseFloat(document.getElementById('test'));
   //     marker = new google.maps.Marker({position: ${allsteps[0]},label:'weather', map: map});
     
    //    marker = new google.maps.Marker({position: {lng: -78.88, lat: 42.88},title:'${allsteps[0]}', map: map});
   // var markers = [];
    var length=parseInt('${length}');
   var steplat=${steplat};
   var steplong=${steplong};
   var allweather=${allweather};
   var polyline=[];
for (var i = 0; i < length ; i++) 
{
//	window.alert(steplat[i]);
	var lat=parseFloat(steplat[i]);
	
	var lng=parseFloat(steplong[i]);
    var pos = new google.maps.LatLng(lat, lng);
 //   window.alert(${steplat.get(i)}+"value of i"+i);
    polyline.push(pos);
    var marker = new google.maps.Marker({
        position: pos,
        map: map,       
        title: allweather[i]
    });
}

  var pathpolyline = new google.maps.Polyline({
    path: polyline,
    geodesic: true,
    strokeColor: 'blue',
    strokeOpacity: 2.0,
    strokeWeight: 3
  });

  pathpolyline.setMap(map);
      
directionsDisplay.setMap(map);  
     }
    </script>
    <script async defer
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAA4aNFh4DZX9qjJiUbN5bS6dEWK8gnHeE&callback=initMap">
    </script>
	
</body>
</html>