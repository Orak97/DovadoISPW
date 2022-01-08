<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import = "java.io.*,java.util.*" %>

<% application.setAttribute( "titolo" , "Home"); %>

<%@ include file="Navbar.jsp" %>

<div class="container-fluid home">
	
	<div class="search-place justify-content-center">
			<p class="text-center icon-kind text-white"><i class="bi bi-pin-map"></i></p> 
			<h2 class="text-center text-white" id="headerLoc">Attiva la geolocalizzazione</h2>
			<h4 class="text-center text-white" id="explanationLoc"> per funzionare, questa applicazione ha bisogno della tua posizione, ti preghiamo di attivarla accettando il box in alto a destra!</h4>
			
			<h5 class="text-center text-white" id="statusLocalization"></h5>
			<div class="d-flex justify-content-center" id="spinnerLocalization">
				<div class="spinner-grow text-light" role="status">
				  <span class="visually-hidden">Loading...</span>
				</div>
			</div>
			
			<div class="d-flex justify-content-center visually-hidden" id="mapContainer">
				<div id="Map" class="localizationMap"></div>
			</div>
			
			<div class="d-flex justify-content-center pt-1 visually-hidden">
				<input type="text" class="form-control" id="placeField" name="src" placeholder="Nome,Luogo,Regione o via">
				<button type="submit" class="btn btn-success search-btn" id="search-btn"><i class="bi bi-search"></i></button>
			</div>
	</div>

</div>

<script type="text/javascript">
	const status = document.querySelector('#statusLocalization');
	var mymap;
	
	if(!navigator.geolocation) {
	    status.textContent = 'Geolocation is not supported by your browser';
	  } else {
	    status.textContent = 'Locating…';
	    navigator.geolocation.getCurrentPosition(success, error, {enableHighAccuracy:true});
	  }
	
	function success(position){
		console.log(position);
		
		const latitude = position.coords.latitude;
		const longitude = position.coords.longitude;
		const accuracy = position.coords.accuracy;
		
		//rimuovo il caricatore e il testo:
		document.querySelector('#spinnerLocalization').classList.add('visually-hidden');
		document.querySelector('#explanationLoc').classList.add('visually-hidden');
		document.querySelector('#headerLoc').classList.add('visually-hidden');
		
		//inizio copypaste di roba che sto cercando di capire ancora
		mymap = L.map('Map').setView([latitude, longitude],5);
		
		const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
		    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
		}); // Utilizzo le tiles di openstreetmap ponendo un link al sito (questione di copyright).
		tiles.addTo(mymap);
		
		var searchControl = L.esri.Geocoding.geosearch().addTo(mymap);

		var results = new L.LayerGroup().addTo(mymap);

		searchControl.on('results',function(data){
			results.clearLayers();
			for(var i = data.results.length - 1; i >= 0; i--){
				results.addLayer(L.marker(data.results[i].latlng));
		  }
		});
		//fine copypaste dal robo di andre
		
		document.querySelector('#mapContainer').classList.remove('visually-hidden');
		
		setCoords(latitude,longitude);
	}
	
	function error() {
        status.textContent = 'Unable to retrieve your location';
        document.querySelector('#spinnerLocalization').classList.add('visually-hidden');
	}
	
	//funzione di andre:
	
	function setCoords(latitude,longitude){
	  mymap.setView([latitude,longitude],5+10);
	  var marker = L.marker([latitude,longitude]).addTo(mymap);
	  //La linea sottostante aggiunge un popup con un messaggio, ma si può anche evitare.
	  marker.bindPopup("<b>Ti trovi qui</b>").openPopup();
	}
	
	function addMarker(latitude,longitude,placeName){
	
	  var marker = L.marker([latitude,longitude]).addTo(mymap);    //aggiungo un marker per indicare un posto speciale
	  marker.bindPopup(placeName).openPopup();
	  //e gli aggiungo un pop-up per indicare magari
	  //un evento speciale o quel cazzo che ti pare.
	}
	
	//fine funzioni di andre

</script>

</body>
</html>