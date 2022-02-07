<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import = "java.io.*,java.util.*, logic.model.User" %>

<% application.setAttribute( "titolo" , "Home"); %>

<%@ include file="Navbar.jsp" %>


<%
	User usr = (User)session.getAttribute("user");
	if(usr != null && request.getParameter("latitude") != null && request.getParameter("longitude") != null){
		
		try{
			String lat = request.getParameter("latitude");
			String lng = request.getParameter("longitude");
			
			double latd = Double.parseDouble(lat);
			double lngd = Double.parseDouble(lng);
			
			usr.setLatitude(latd);
			usr.setLongitude(lngd);
			
			response.sendRedirect("Home.jsp");
		}catch(Exception e){
			response.sendRedirect("localization.jsp");
		}	
	}

%>

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
			
			<div class="visually-hidden" id="mapContainer">
				<div id="Map" class="localizationMap"></div>
			</div>
			
			<h5 class="text-center text-white visually-hidden mt-3" id="retrievedAddress"></h5>
			<div class="d-flex justify-content-center pt-1 visually-hidden" id="confirmButton">
				<button type="button" class="btn btn-danger btn-lg reposition-btn" id="reposition-btn"><i class="bi bi-map"></i> No, fammi riposizionare</button>				
				<form action="localization.jsp" method="GET">
					<input type="text" class="visually-hidden" name="latitude" id="latField">
					<input type="text" class="visually-hidden" name="longitude" id="longField">
					<button type="submit" class="btn btn-success btn-lg lesgo-btn" id="lesgo-btn"><i class="bi bi-signpost-2"></i> Si, Dovado?</button>			
				</form>
			</div>
			
	</div>

</div>


<script src="js/map.js"></script>
<script type="text/javascript">
	const status = document.querySelector('#statusLocalization');
	var mymap;
	var lastMarker;
	

	document.querySelector('#reposition-btn').addEventListener('click', removeLastMark);
	
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
		
		document.querySelector('#explanationLoc').classList.add('visually-hidden');
		document.querySelector('#headerLoc').classList.add('visually-hidden');
		
		status.textContent = 'la tua posizione, con un accuratezza di circa '+accuracy+' metri è:';
		
		//faccio comparire la mappa:
		document.querySelector('#mapContainer').classList.remove('visually-hidden');
		startup(latitude,longitude);
		lastMarker = setCoords(latitude,longitude);	
		retrieveAddress(latitude,longitude);
		
	}
	
	function error() {
        status.textContent = 'Non è stato possibile trovare la tua posizione, premi sulla mappa per segnalarti';
        
       //rimuovo il caricatore e il testo:
		document.querySelector('#spinnerLocalization').classList.add('visually-hidden');
		document.querySelector('#explanationLoc').classList.add('visually-hidden');
		document.querySelector('#headerLoc').classList.add('visually-hidden');
		
		//faccio comparire la mappa:
		document.querySelector('#mapContainer').classList.remove('visually-hidden');
		startup(41.9109,12.4818);
		mymap.on('click', repositionMark);
	}
	
	function retrieveAddress(latitude,longitude){
		L.esri.Geocoding.reverseGeocode()
		.latlng([latitude,longitude])
		.run(function (error,result,response){
			if (error) {
				console.log(err);
				return;
			}
			
			console.log({result, response});
			
			//faccio scomparire lo spinner
			document.querySelector('#spinnerLocalization').classList.add('visually-hidden');
			
			//faccio comparire il "ti trovi vicino a..."
			document.querySelector('#retrievedAddress').classList.remove('visually-hidden');
			document.querySelector('#retrievedAddress').textContent = 'Ti trovi vicino a '+result.address.Match_addr+'?';
			
			//faccio comparire i pulsanti per confermare o smentire
			document.querySelector('#confirmButton').classList.remove('visually-hidden');
			
			//imposto come testo i valori di latitutine e longitudine nascosti nel form
			document.querySelector('#latField').value = latitude;
			document.querySelector('#longField').value = longitude;
		});
	}
	
	function removeLastMark(){
		document.querySelector('#retrievedAddress').textContent = 'Premi sulla mappa per indicare dove ti trovi!';
		document.querySelector('#confirmButton').classList.add('visually-hidden');
		
		status.textContent = '';
		
		console.log(lastMarker);
		
		lastMarker.remove();
		mymap.on('click', repositionMark);
	}
	
	function repositionMark(e){
		mymap.off('click',repositionMark);
		
		let latitude = e.latlng.lat;
		let longitude = e.latlng.lng;
		
		status.textContent = 'Anche se la via trovata non corrisponde, se hai posizionato bene il pin puoi andare avanti!';
		
		lastMarker = setCoords(latitude,longitude);
		retrieveAddress(latitude,longitude);
		
	}

</script>

</body>
</html>