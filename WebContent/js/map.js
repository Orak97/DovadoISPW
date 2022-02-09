var zoom= 5;
var lat=0;
var long=0;
var coords=[0,0];

var mymap

var markersOnMap = [];

var userIcon = L.icon({
	iconUrl: 'logo/User.png',
	
	iconSize: [90,95],
	iconAnchor: [22, 94],
});


function startup(userLat, userLong){
	lat = userLat;
	long = userLong
	mymap= L.map('Map').setView([lat, long],zoom);   // Do vita alla mappa partendo dalla latitudine e longitudine [x,y] con uno zoom di 5.
	
	const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
	    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
	}); // Utilizzo le tiles di openstreetmap ponendo un link al sito (questione di copyright).
	tiles.addTo(mymap);
	
	//Infine aggiungo le tiles alla mappa rendendola visibile e interagibile.
	
	//var cerchio = L.circle([lat,long-0.01], {
	//  color: 'red',
	//  fillcolor: '#f03',
	//  fillOpacity: 0.5,
	//  radius: 200
	//}).addTo(mymap);
	//cerchio.bindPopup("<b>Evento pero' in cerchio</b>").openPopup();
	
	//var polygon = L.polygon([
	//  [lat+0.09,long-0.18],
	//  [lat+0.09,long-0.09],
	//  [lat-0.09,long-0.18],
	//  [lat-0.09,long-0.09]
	//]).addTo(mymap);
	//polygon.bindPopup("<b>Evento in un poligono</b><br>br<br>br<br>br").openPopup();
	
	var searchControl = L.esri.Geocoding.geosearch().addTo(mymap);
	
	var results = new L.LayerGroup().addTo(mymap);
	
	searchControl.on('results',function(data){
		results.clearLayers();
		for(var i = data.results.length - 1; i >= 0; i--){
			results.addLayer(L.marker(data.results[i].latlng));
	  }
	});
	
	

}

function setCoords(latitude,longitude){
  lat=latitude;
  long=longitude;
  mymap.setView([lat,long],zoom+15);
  var marker = L.marker([lat,long]).addTo(mymap);
  //La linea sottostante aggiunge un popup con un messaggio, ma si può anche evitare.
  //marker.bindPopup("<b>Ti trovi qui</b>").openPopup();
  return marker
}

function addMarker(latitude,longitude,placeName){
  var marker = L.marker([latitude,longitude]).addTo(mymap);    //aggiungo un marker per indicare un posto speciale
  marker.bindPopup(placeName).openPopup();
  //e gli aggiungo un pop-up per indicare magari
  //un evento speciale o quel cazzo che ti pare.
}

function spotPlace(latitude,longitude,placeName,id){
  var marker = L.marker([latitude,longitude]).addTo(mymap);    //aggiungo un marker per indicare un posto speciale
  marker.bindPopup(placeName);
  markersOnMap.push({'id': id, 'marker': marker});
  
  //e gli aggiungo un pop-up per indicare magari
  //un evento speciale o quel cazzo che ti pare.
}


function setUser(latitude,longitude){
	mymap.setView([lat,long],zoom+15);
	return L.marker([latitude,longitude], {icon: userIcon}).addTo(mymap).bindPopup("Ti trovi qui!");
}

function moveView(latitude,longitude,id){
	var activity = markersOnMap.find(element => id);
	mymap.setView([latitude,longitude],zoom+15);
	activity.marker.openPopup();
}

function removeAllMarkers(){
	markersOnMap.forEach( elem => elem.marker.remove());  
}


//------------------------------------------------------------------------------------------_//
//								 inizio codice per andrea
//-------------------------------------------------------------------------------------------//
	
	var latDesk;
	var longDesk;
	
	//chiama prima questa
  	function retrieveLatLng(civicoTxt,indirizzoTxt,cittaTxt,regioneTxt,capTxt){
		
  		L.esri.Geocoding.geocode().address(civicoTxt+' '+indirizzoTxt).city(cittaTxt).region(regioneTxt).postal(capTxt).run( function (err, results, response) {
  		  if (err) {
//  		    console.log(err);
  		    return;
  		  }
  		  
  		  if(results.results.length < 1){
  			 desktopLatLng = null
  		  }else{
  		  
	  		  //console.log(results.results)
	  		  
	  		  //let latitude = results.results[0].latlng.lat;
	  		  //let longitude = results.results[0].latlng.lng
	  		  //console.log(results.results[0].latlng);
	  		  latDesk = results.results[0].latlng.lat;
	  		  longDesk = results.results[0].latlng.lnt;
  		  }
  		});
  	}
  	
  	//e poi queste
  	function getDesktopLatitude(){
		return latDesk;
	}
	
	//e poi questa
  	function getDesktopLongitude(){
		return longDesk;
	}