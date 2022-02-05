
<%@ page import = "java.io.*,java.util.*,logic.model.SuperUser, logic.controller.SpotPlaceController, logic.model.User, logic.controller.CreateActivityController, logic.model.Place, logic.model.DAOPlace, logic.model.Partner, logic.model.DAOActivity, logic.model.Activity, logic.model.CertifiedActivity, logic.model.FrequencyOfRepeat, logic.model.ContinuosActivity, logic.model.PeriodicActivity, logic.model.ExpiringActivity, logic.model.Preferences " %>

  <div class="container pt-6">
	
	<jsp:useBean id="createActivityBean" scope="request" class="logic.model.CreateActivityBean" />
	<jsp:setProperty name="createActivityBean" property="*" />
	
	<jsp:useBean id="spotPlaceBean" scope="request" class="logic.model.SpotPlaceBean" />
	<jsp:setProperty name="spotPlaceBean" property="*" />
	
	
<%
	SuperUser u; 
	if(session.getAttribute("user") != null || session.getAttribute("partner") != null){
		
		boolean isUser = true;
		u = (User) session.getAttribute("user");
		if(u == null){
			u = (Partner) session.getAttribute("partner");
			isUser = false;
		}
		//controllo se place name è null, se non lo è ho fatto una request per creare un posto, istanzio il createplacecontroller etc etc
		
		if(request.getParameter("placeName")!=null){
			SpotPlaceController spController = new SpotPlaceController(spotPlaceBean);
			
			if(spController.spotPlace()) response.sendRedirect("CreateActivity.jsp?src="+request.getParameter("placeName")); //TODO: renderlo più appetibile
			else {
				%>
				<script>alert('Errore nella creazione del posto, riprova!')</script>
				<%
			}
		}
	
		
		//controllo se 'openingDate' è null, se non lo è ho fatto una request per creare un' attività, istanzio il createActivityController etc etc
		if(request.getParameter("activityName")!= null){
			CreateActivityController c = new CreateActivityController(createActivityBean,u);
			try{c.saveActivity();}
			catch(Exception e){
				e.printStackTrace();
				%>
				<script>alert('Errore nella creazione dell\'attivita\', riprova!')</script>
				<%
			}
			%>
			<script>alert('Attività creata correttamente!')</script>
			<%
		}
		
		Activity editActivity = null;
		//qui controllo se la richiesta è stata fatta per modificare l'attività
		if(request.getParameter("editActivity")!= null && !isUser){
			DAOActivity dao = DAOActivity.getInstance();
			
			try{
			Long actToEdit = Long.parseLong(request.getParameter("editActivity"));
			
			Partner p = (Partner) u;
			
			for(CertifiedActivity curr: dao.getPartnerActivities(p.getUserID()))
				if(curr.getId() == actToEdit) editActivity = curr;
			}catch(Exception e){
				%>
				<script>alert('Errore nell\'inizializzazione della modifica dell\' attività'')</script>
				<%
			}
		}
	
	%>
	
	<% if(request.getParameter("src")== null && editActivity == null){ %>
		<%-- QUESTO CODICE VIENE VISUALIZZATO SOLO SE È LA PRIMA VOLTA CHE LA PAGINA SI CARICA --%>
		<form action="<%= isUser ? "CreateActivity.jsp" : "CreateActivityPartner.jsp" %>" method="GET">
		
		<div class="search-place justify-content-center">
			<h2 class="text-center text-white"><i class="bi bi-pin-map"></i> Dove vuoi spottare l'attività? <i class="bi bi-pin-map"></i></h2>
			<div class="d-flex justify-content-center pt-1">
				<input type="text" class="form-control" id="placeField" name="src" placeholder="Nome,Luogo,Regione o via">
				<button type="submit" class="btn btn-success search-btn" id="search-btn"><i class="bi bi-search"></i></button>
			</div>
		</div>
		</form>	
	 <% } else {
		String ricerca = request.getParameter("src");
		ArrayList<Place> places = (ArrayList<Place>)DAOPlace.getInstance().searchPlaces(ricerca);
		%>
		
		<div class= "row select-place p-3">
		<h2 class="text-center"><i class="bi bi-pin-map"></i> Dove vuoi spottare l'attività? <i class="bi bi-pin-map"></i></h2>
		<hr class="separator-places">
		<div class ="row"> <h3>Posti trovati per "<%= request.getParameter("src") %>":</h2> </div>
		<p class="lead text-center">Seleziona il posto in cui vuoi creare l'attività</p>
		<div class="col">
				<div class="row row-cols-1 row-cols-md-3 g-4">
		
		
				<% for(Place p: places){ %>
		    
				  <div class="col">
				    <div class="card card-dark text-white h-100 places-cards shadow" onclick="setPlace(this,<%= p.getId() %>)">
				      <img src="https://source.unsplash.com/random" class="card-img-top rounded-img-for-places" alt="...">
				      <div class="card-body">
				        <h5 class="card-title"><%= p.getName() %></h5>
				        <p class="card-text">
				        	
				        	Città: <%=p.getCity() %> <br />
				        	Regione: <%=p.getRegion() %> <br />
				        	Indirizzo: <%=p.getAddress()+( p.getCivico()==null ? "" :" "+p.getCivico())%> <br />
				        	
				        	
				        </p>
				      </div>
				    </div>
				  </div>
				  <% } %>
				
				</div>
			</div>
			
			<div class="row p-2 text-center mt-3"> <p class="lead">Non hai trovato il luogo che cerchi? <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#createPlaceModal">Creane uno tu!</button> oppure <a href="CreateActivity.jsp" class="link-primary"> effettua un'altra ricerca</a></p> </div>
		</div>
	    	
    	<div class="form-create-activity p-3 visually-hidden">
		    <%-- sezione per nome e descrizione etc etc --%>
		    <h2 class="text-center"><i class="bi bi-compass"></i> Di che attività si tratta? <i class="bi bi-compass"></i></h2>
		    <hr class="separator-places">
			<!-- da qui inizia il vecchi form -->
			<form class="row g-3" name="createActivityForm" action="<%= isUser ? "CreateActivity.jsp" : "CreateActivityPartner.jsp" %>" method="GET">
			  
			  <div class="mb-3 visually-hidden">
				<input type="number" class="form-control" id="place" name="place">
				<input type="number" class="form-control" id="idActivity" name="idActivity">
			  </div>
			  
			  <div class="col-md-12">
			    <label for="name" class="form-label">Nome attività:</label>
			    <input type="text" class="form-control" id="name" name="activityName" placeholder="Inserisci il nome dell'attività" required>
			  </div>
			  
			  <div class="col-md-12">
				<label for="description" class="form-label">Descrizione attività:</label>
				<textarea class="form-control" aria-label="With textarea" name="activityDescription" placeholder="inserisci una breve descrizione dell'attività" required></textarea>
			  </div>
			  
			  <h4 class="intermezzo text-center">Di che tipo è l'attività?</h4>
			  
			  <div class="row row-cols-1 row-cols-md-3 g-4 justify-content-center">
			  	<div class="col">
				    <div class="card border-primary mb-3 places-cards shadow h-100 kind-cards pressed" onclick="changedKind('CONTINUA')" id="cardsContinua">
				      <div class="card-body">
				        <h5 class="card-title text-center">Attività Continua</h5>
				        <p class="text-center icon-kind"><i class="bi bi-calendar-check"></i></p>
				        <p class="card-text text-center">Attività aperte tutti i giorni, eg: escursione in villa Borghese </p>
				      </div>
				    </div>
				</div>
				
				<div class="col">
				    <div class=" card border-primary mb-3 places-cards shadow h-100 kind-cards" onclick="changedKind('PERIODICA')" id="cardsPeriodica">
				      <div class="card-body">
				        <h5 class="card-title text-center">Attività Periodica</h5>
				        <p class="text-center icon-kind"><i class="bi bi-calendar-range"></i></p>
				        <p class="card-text text-center">Attività che si ripetono con determinata frequenza, per un periodo di tempo --da aggiustare </p>
				      </div>
				    </div>
				</div>
				
				<div class="col">
				    <div class="card border-primary mb-3 places-cards shadow h-100 kind-cards" onclick="changedKind('SCADENZA')" id="cardsScadenza">
				      <div class="card-body">
				        <h5 class="card-title text-center">Attività a scadenza</h5>
				        <p class="text-center icon-kind"><i class="bi bi-calendar-event"></i></p>
				        <p class="card-text text-center">Attività che si ripetono una sola volta </p>
				      </div>
				    </div>
				</div>
					
			  </div>
			  
			  <h4 class="intermezzo2 text-center">Quando è aperta l'attività?</h4>
			  
			  <div class="col-md-6 visually-hidden" id="kindDiv">
			  
			    <label for="kind" class="form-label">Tipo:</label>
			    <select name="type" id="kind" class="form-select">
			    	 <option value="CONTINUA" selected>Continua</option>
			    	 <option value="PERIODICA">Periodica</option>
			    	 <option value="SCADENZA">Scadenza</option>
			    </select>
			  </div>
			  
			  <div class="col-md-12 visually-hidden" id="cadenceDiv">
			    <label for="cadence" class="form-label">Cadenza:</label>
			    <select name="cadence" id="cadence" class="form-select" disabled>
			    	 <option value="WEEKLY">Settimanale</option>
			    	 <option value="MONTHLY">Mensile</option>
			    	 <option value="ANNUALLY">Annuale</option>
			    </select>
			  </div>
			  
			  <div class="col-md-6 visually-hidden" id="openingDateDiv">
			  	<label for="startDate" class="col-form-label" >Start Date:</label>
				<input type="date" class="form-control" id="startDate" name="openingDate" required disabled>
			  </div>
			  <div class="col-md-6 visually-hidden" id="closingDateDiv">
			  	<label for="closingDate" class="col-form-label">Closing Date:</label>
			  	<input type="date" class="form-control" id="closingDate" name="endDate" required disabled>
			  </div>
			  
			  <div class="col-md-6" id="openingTimeDiv">
			  	<label for="openingTime" class="col-form-label">Opening time:</label>
			  	<input type="time" class="form-control" id="openingTime" name="openingTime" required>
			  </div>
			  <div class="col-md-6" id="closingTimeDiv">
			  	<label for="closingTime" class="col-form-label">Closing time:</label>
			  	<input type="time" class="form-control" id="closingTime" name="closingTime" required>
			  </div>
			  
			  
			  <h4 class="intermezzo2 text-center">A chi potrebbe interessare l'attività?</h4>
			  <div class="col-6">
				  <div class="form-check form-switch">
				  	 <input class="form-check-input" type="checkbox" id="Arte" name="arte">
			  		 <label class="form-check-label" for="Arte">Arte</label>
				  </div>
				  <div class="form-check form-switch">
				  	 <input class="form-check-input" type="checkbox" id="Cibo" name="cibo">
			  		 <label class="form-check-label" for="Cibo">Cibo</label>
				  </div>
				  <div class="form-check form-switch">		
				  	 <input class="form-check-input" type="checkbox" id="Musica" name="musica" >
			  		 <label class="form-check-label" for="Musica">Musica</label>
				  </div>
				  <div class="form-check form-switch">	 
				  	 <input class="form-check-input" type="checkbox" id="Sport" name="sport">
			  		 <label class="form-check-label" for="Sport">Sport</label>
				  </div>
				  
				   <div class="form-check form-switch">	 
				  	 <input class="form-check-input" type="checkbox" id="Social" name="social">
			  		 <label class="form-check-label" for="Social">Social</label>
				  </div>
				  
				  <div class="form-check form-switch">	 
				  	 <input class="form-check-input" type="checkbox" id="Natura" name="natura">
			  		 <label class="form-check-label" for="Natura">Natura</label>
				  </div>
				  
				  <div class="form-check form-switch">	 
				  	 <input class="form-check-input" type="checkbox" id="Esplorazione" name="esplorazione">
			  		 <label class="form-check-label" for="Esplorazione">Esplorazione</label>
				  </div>
			  </div>
			  
			  <div class="col-6">
				  <div class="form-check form-switch">
				  	 <input class="form-check-input" type="checkbox" id="Ricorrenze" name="ricorrenze">
			  		 <label class="form-check-label" for="Ricorrenze">Ricorrenze Locali</label>
				  </div>
				  <div class="form-check form-switch">
				  	 <input class="form-check-input" type="checkbox" id="Moda" name="moda">
			  		 <label class="form-check-label" for="Mode">Moda</label>
				  </div>
				  <div class="form-check form-switch">		
				  	 <input class="form-check-input" type="checkbox" id="Shopping" name="shopping">
			  		 <label class="form-check-label" for="Shopping">Shopping</label>
				  </div>
				  <div class="form-check form-switch">	 
				  	 <input class="form-check-input" type="checkbox" id="Adrenalina" name="adrenalina">
			  		 <label class="form-check-label" for="Adrenalina">Adrenalina</label>
				  </div>
				  
				   <div class="form-check form-switch">	 
				  	 <input class="form-check-input" type="checkbox" id="Relax" name="relax">
			  		 <label class="form-check-label" for="Relax">Relax</label>
				  </div>
				  
				  <div class="form-check form-switch">	 
				  	 <input class="form-check-input" type="checkbox" id="Istruzione" name="istruzione">
			  		 <label class="form-check-label" for="Istruzione">Istruzione</label>
				  </div>
				  
				  <div class="form-check form-switch">	 
				  	 <input class="form-check-input" type="checkbox" id="Monumenti" name="monumenti">
			  		 <label class="form-check-label" for="Monumenti">Monumenti</label>
				  </div>
			  </div>
			  
			  
			  <div class="d-flex">
			    <button type="submit" class="btn btn-success flex-grow-1 create-activity" disabled><%= editActivity == null ? "Crea l'attività" : "Aggiorna attività" %></button>
			  </div>
			</form> 
			<% } %>
		  </div>
	</div>
  
  <!-- Modal -->
	<div class="modal fade" id="createPlaceModal" tabindex="-1" aria-labelledby="createPlaceModalLabel" aria-hidden="true">
	  <div class="modal-dialog modal-fullscreen">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title" id="exampleModalLabel">Premi sulla mappa o inserisci i dati per creare un posto</h5>
	        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	      </div>
	      <div class="modal-body">
	        <form class="row g-3" action="<%= isUser ? "CreateActivity.jsp" : "CreateActivityPartner.jsp" %>" method="GET" id="formPlace">
	          <div  class="visually-hidden" id="mapContainer">
				<div id="Map" class="SpotPlaceMap"></div>
				<input type="text" class="visually-hidden" name="latitude" id="latField">
				<input type="text" class="visually-hidden" name="longitude" id="longField">
		  	  </div>
	         
	          <div class="col-12">
			    <label for="placeName" class="form-label">Nome posto:</label>
			    <input type="text" class="form-control" id="placeName" placeholder="Colosseo ..." name="placeName">
			  </div>
			  <div class="col-9">
			    <label for="inputAddress" class="form-label">Indirizzo:</label>
			    <input type="text" class="form-control" id="inputAddress" placeholder="Main St" name="address">
			  </div>
			  <div class="col-3">
			    <label for="numeroCivico" class="form-label">Civico:</label>
			    <input type="text" class="form-control" id="numeroCivico" placeholder="1" name="streetNumber">
			  </div>
			  <div class="col-md-12">
			    <label for="inputCity" class="form-label">Città:</label>
			    <input type="text" class="form-control" id="inputCity" name="city" placeholder="Roma">
			  </div>
			  <div class="col-md-12">
			    <label for="inputState" class="form-label">Regione:</label>
			    <select id="inputState" class="form-select" name="region">
			      <option selected>Choose...</option>
			      <option>Abruzzo</option>
			      <option>Basilicata</option>
			      <option>Calabria</option>
			      <option>Campania</option>
			      <option>Emilia-Romagna</option>
			      <option>Friuli Venezia Giulia</option>
			      <option>Lazio</option>
			      <option>Liguria</option>
			      <option>Lombardia</option>
			      <option>Marche</option>
			      <option>Molise</option>
			      <option>Piemonte</option>
			      <option>Puglia</option>
			      <option>Sardegna</option>
			      <option>Sicilia</option>
			      <option>Toscana</option>
			      <option>Trentino-Alto Adige</option>
			      <option>Umbria</option>
			      <option>Valle d'Aosta</option>
			      <option>Veneto</option>
			    </select>
			  </div>
			  <div>
			  	<label for="cap" class="form-label">Cap:</label>
			    <input type="text" class="form-control" id="cap" name="cap" placeholder="00100">
			  </div>
	      
	      <div class="visually-hidden" id="mapContainer">
				<div id="Map" class="SpotPlaceMap"></div>
				<p class="h5 text-center pt-3" id="resultMap"><p>
				<input type="text" class="visually-hidden" name="latitude" id="latField">
				<input type="text" class="visually-hidden" name="longitude" id="longField">
		  </div>
	      
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" id="close-btn">Close</button>
	        <button type="button" class="btn btn-primary" id="spotPlace">Trova posto sulla mappa</button>
	        <button type="submit" class="btn btn-success visually-hidden" id="confirmButton">Conferma</button>
	      	</form>
	      </div>
	    </div>
	  </div>
	</div>
	  
  <script src="js/map.js"></script>
  <script>
  	const spotPlaceButton = document.querySelector('#spotPlace');
  	spotPlaceButton.addEventListener('click', spotPlace);
  	
  	var modalSpotPlace = document.querySelector('#createPlaceModal');
  	modalSpotPlace.addEventListener('shown.bs.modal', inizializeMap);
  	modalSpotPlace.addEventListener('hide.bs.modal', emptyModal)
  	
  	const indirizzo = document.querySelector('#inputAddress');
  	const civico = document.querySelector('#numeroCivico');
  	const citta = document.querySelector('#inputCity');
  	const regione = document.querySelector('#inputState');
  	const cap = document.querySelector('#cap');
  	
  	indirizzo.addEventListener('keyup',disableSubmit)
  	civico.addEventListener('keyup',disableSubmit)
  	citta.addEventListener('keyup',disableSubmit)
  	regione.addEventListener('keyup',disableSubmit)
  	cap.addEventListener('keyup',disableSubmit)
  	
  	var lastMarker;
  	
  	function emptyModal(){
  		document.querySelector('#formPlace').reset();
  		disableSubmit();
  		if(lastMarker != null) lastMarker.remove();
  		
  	}
  	
  	function disableSubmit(){
  		document.querySelector('#confirmButton').classList.add('visually-hidden')
  		document.querySelector('#confirmButton').disable=true;
  		
  		document.querySelector('#spotPlace').classList.remove('visually-hidden')
  	}
  	
  	function enableSubmit(){
  		document.querySelector('#confirmButton').classList.remove('visually-hidden')
  		document.querySelector('#confirmButton').disable=false;
  		
  		document.querySelector('#spotPlace').classList.add('visually-hidden')
  	}
  	
  	function inizializeMap(){
  		document.querySelector('#mapContainer').classList.remove('visually-hidden');
  		try{
  			startup(41.9109,12.4818);
  			mymap.on('click', repositionPlace);
  		}catch(e){
  			console.log("mappa già creata")
  		}
  	}
  	
  	function repositionPlace(e){
		//mymap.off('click',repositionMark);
		
		console.log(e);
		
		if(lastMarker != null) lastMarker.remove();
		
		let latitude = e.latlng.lat;
		let longitude = e.latlng.lng;
		
		lastMarker = setCoords(latitude,longitude);
		
		retrieveAddress(latitude,longitude);
		
	}
  	
  	function retrieveAddress(latitude,longitude){
		L.esri.Geocoding.reverseGeocode()
		.latlng([latitude,longitude])
		.run(function (error,result,response){
			if (error) {
				console.log(error);
				return;
			}
			
			console.log({result, response});
			
			//imposto come testo i valori di latitutine e longitudine nascosti nel form
			document.querySelector('#latField').value = latitude;
			document.querySelector('#longField').value = longitude;
			
			indirizzo.value = result.address.Address.replace(result.address.AddNum,'');
			civico.value = result.address.AddNum;
			citta.value = result.address.City;
			regione.value = result.address.Region;
			cap.value = result.address.Postal;
			
			enableSubmit();
		});
	}
  	
  	function spotPlace(){
  		
  		let indirizzoTxt = indirizzo.value;
  		let civicoTxt = civico.value;
  		let cittaTxt = citta.value;
  		let regioneTxt =regione.value;
  		let capTxt =cap.value;
  		
		
  		L.esri.Geocoding.geocode().address(civicoTxt+' '+indirizzoTxt).city(cittaTxt).region(regioneTxt).postal(capTxt).run(function (err, results, response) {
  		  if (err) {
  		    console.log(err);
  		    return;
  		  }
  		  
  		  if(results.results.length < 1){
  			alert("Nessun posto trovato, inserisci più informazioni nei campi!") 
  		  }else{
  		  
	  		  console.log(results.results)
	  		  
	  		  let latitude = results.results[0].latlng.lat;
	  		  let longitude = results.results[0].latlng.lng
	  		  
	  		  if(lastMarker != null) lastMarker.remove();
	  		  
	  		  lastMarker = setCoords(latitude,longitude);
	  		  
	  		  
		 	  //imposto come testo i valori di latitutine e longitudine nascosti nel form
		      document.querySelector('#latField').value = latitude;
			  document.querySelector('#longField').value = longitude;
			  
			  enableSubmit();
  		  }
  		});
  		
  	}
  	
  	
  	
  	function setPlace(event,p){
  		document.getElementById('place').value=p;
  		
  		let oldPressed = document.getElementsByClassName('card-dark-pressed');
  		
  		if((oldPressed[0]) != undefined){
  			oldPressed[0].classList.add('card-dark');
  			oldPressed[0].classList.remove('card-dark-pressed');	
  		}
  		
  		if(event != null){
  			event.classList.remove('card-dark');
  			event.classList.add('card-dark-pressed');
  		}
  		
  		document.querySelector('.select-place').classList.add('visually-hidden')
  		document.querySelector('.form-create-activity').classList.remove('visually-hidden')
  		document.querySelector('button.create-activity').disabled=false;
  		
  		window.scrollTo(0, 0)
  	}
  	
  	function changedKind(value){
  		switch(value){
  		case 'CONTINUA':
  			console.log('continua');
  			
  			//section for pressing the button
  			document.getElementById('cardsContinua').classList.add('pressed');
  			document.getElementById('cardsPeriodica').classList.remove('pressed');
  			document.getElementById('cardsScadenza').classList.remove('pressed');
  			
  			
  			document.getElementById('openingDateDiv').classList.add('visually-hidden');
  			document.getElementById('closingDateDiv').classList.add('visually-hidden');
  			
  			document.getElementById('startDate').disabled = true;
  			document.getElementById('closingDate').disabled = true;
  			
  				
  			document.getElementById('cadenceDiv').classList.add('visually-hidden');
  			document.getElementById('cadence').disabled = true;
  			
  			document.getElementById('kindDiv').classList.add('col-md-6');
  			document.getElementById('kindDiv').classList.remove('col-md-3');
  			
  			document.getElementById('kind').value="CONTINUA";
  			break;
  		case 'PERIODICA':
  			console.log('periodica');
  			
  			//section for pressing the button
  			document.getElementById('cardsContinua').classList.remove('pressed');
  			document.getElementById('cardsPeriodica').classList.add('pressed');
  			document.getElementById('cardsScadenza').classList.remove('pressed');
  			
  			
  			// section for opening and closing date
  			document.getElementById('openingDateDiv').classList.remove('visually-hidden');
  			document.getElementById('closingDateDiv').classList.remove('visually-hidden');
  			
  			document.getElementById('startDate').disabled = false;
  			document.getElementById('closingDate').disabled = false;
  			
  			
  			//section for cadence
  			document.getElementById('cadenceDiv').classList.remove('visually-hidden');
  			document.getElementById('cadence').disabled = false;
  			
  			document.getElementById('kindDiv').classList.remove('col-md-6');
  			document.getElementById('kindDiv').classList.add('col-md-3');
  			
  			document.getElementById('kind').value="PERIODICA";
  			
  			break;
  		case 'SCADENZA':
  			console.log('scadenza');
  			//section for pressing the button
  			document.getElementById('cardsContinua').classList.remove('pressed');
  			document.getElementById('cardsPeriodica').classList.remove('pressed');
  			document.getElementById('cardsScadenza').classList.add('pressed');
  			
  			
  			// section for opening and closing date
  			document.getElementById('openingDateDiv').classList.remove('visually-hidden');
  			document.getElementById('closingDateDiv').classList.remove('visually-hidden');
  			
  			document.getElementById('startDate').disabled = false;
  			document.getElementById('closingDate').disabled = false;
  			
  			// section for cadence
  			
  			document.getElementById('cadenceDiv').classList.add('visually-hidden');
  			document.getElementById('cadence').disabled = true;
  			
  			document.getElementById('kindDiv').classList.add('col-md-6');
  			document.getElementById('kindDiv').classList.remove('col-md-3');
  			
  			document.getElementById('kind').value="SCADENZA";
  			break;
  		default:
  			console.log('hai proprio fuccato up il codice bro');
  			break;
  		}
  	}
  	
  	<%if(editActivity != null){%>
  		setPlace(null,<%=editActivity.getPlace().getId()%>)
  		document.querySelector('[name=idActivity]').value = <%= editActivity.getId()%>
  		document.querySelector('[name=activityName]').value = '<%=editActivity.getName()%>';
  		document.querySelector('[name=activityDescription]').value = "<%=editActivity.getDescription()%>";
  		<%
  			FrequencyOfRepeat tipo = editActivity.getFrequency();
  			
  			if(tipo instanceof ContinuosActivity){
  				ContinuosActivity curr = (ContinuosActivity) tipo;
  				%>
  					document.querySelector('#cardsContinua').click();
  					document.querySelector('[name=openingTime]').value = '<%=curr.getOpeningTime()%>'
  					document.querySelector('[name=closingTime]').value = '<%=curr.getClosingTime()%>'
  				<%
  			}
  			
  			if(tipo instanceof PeriodicActivity){
  				PeriodicActivity curr = (PeriodicActivity) tipo;
  				%>
  					document.querySelector('#cardsPeriodica').click();
  					document.querySelector('[name=openingTime]').value = '<%=curr.getOpeningTime()%>'
  	  				document.querySelector('[name=closingTime]').value = '<%=curr.getClosingTime()%>'
					
  	  				document.querySelector('[name=cadence]').value='<%= curr.getCadence()%>';
  	  				document.querySelector('[name=openingDate]').value='<%=curr.getStartDate()%>'
  	  				document.querySelector('[name=endDate]').value='<%=curr.getEndDate()%>'
				
				<%  			
  			}
  			
  			if(tipo instanceof ExpiringActivity){
  				ExpiringActivity curr = (ExpiringActivity) tipo;
  				%>
  					document.querySelector('#cardsScadenza').click();
  					document.querySelector('[name=openingTime]').value = '<%=curr.getOpeningTime()%>'
  	  				document.querySelector('[name=closingTime]').value = '<%=curr.getClosingTime()%>'
					
  	  				document.querySelector('[name=openingDate]').value='<%=curr.getStartDate()%>'
  	  				document.querySelector('[name=endDate]').value='<%=curr.getEndDate()%>'
  				<%
  			}
  			
  			Preferences p = editActivity.getIntrestedCategories();
  			%>
  			document.querySelector('[name=arte]').checked = <%= p.isArte()%>
  			document.querySelector('[name=cibo]').checked = <%= p.isCibo()%>
  			document.querySelector('[name=musica]').checked = <%= p.isMusica()%>
  			document.querySelector('[name=sport]').checked = <%= p.isSport()%>
  			document.querySelector('[name=social]').checked = <%= p.isSocial()%>
  			document.querySelector('[name=natura]').checked = <%= p.isNatura()%>
  			document.querySelector('[name=esplorazione]').checked = <%= p.isEsplorazione()%>
  			
  			document.querySelector('[name=ricorrenze]').checked = <%= p.isRicorrenzeLocali()%>
  			document.querySelector('[name=moda]').checked = <%= p.isModa()%>
  			document.querySelector('[name=shopping]').checked = <%= p.isShopping()%>
  			document.querySelector('[name=adrenalina]').checked = <%= p.isAdrenalina()%>
  			document.querySelector('[name=relax]').checked = <%= p.isRelax()%>
  			document.querySelector('[name=istruzione]').checked = <%= p.isIstruzione()%>
  			document.querySelector('[name=monumenti]').checked = <%= p.isMonumenti()%>
  			<%
  			
  		%>
  		
  	<%}%>
  </script>
  
  <%} %>