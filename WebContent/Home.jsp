<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 

    <%@ page import = "java.io.*,java.util.*, logic.model.DAOPreferences, logic.model.DAOActivity, logic.model.SuperActivity, logic.model.SuperUser, logic.model.User, logic.model.Activity, logic.controller.AddActivityToScheduleController, logic.model.Preferences, logic.controller.SetPreferencesController, logic.controller.FindActivityController, java.time.LocalDate, logic.model.CertifiedActivity" %>

    <% application.setAttribute( "titolo" , "Home"); %>

	<%@ include file="Navbar.jsp" %>
	
	<jsp:useBean id="scheduleBean" scope="request" class="logic.model.ScheduleBean" />

	<jsp:setProperty name="scheduleBean" property="*" />
	
	
	<jsp:useBean id="preferenceBean" scope="request" class="logic.model.PreferenceBean" />

	<jsp:setProperty name="preferenceBean" property="*" />
	
	

	<div class="container-fluid home">
	<% //tentativo di fare una home decente:
		User utente = (User) session.getAttribute("user");
		
		
		//controllo latiduine e longitudine, se sono 0 -> non è stata inizializzata -> porto l'utente alla pagina per attivare la geolocalizzazione
		if(utente != null) {
		if(utente.getLatitude() == 0 || utente.getLongitude() == 0) {response.sendRedirect("localization.jsp");}
		
		if(request.getParameter("idActivity")!= null){ //controllo la richiesta ricevuta, se all'interno è presente un parametro date vuol dire che arrivo a questa pagina tramite la pressione del bottone save changes, quindi ne consegue che i dati sono pieni e quindi posso andare avanti
			AddActivityToScheduleController controller = new AddActivityToScheduleController(utente,scheduleBean);
			boolean success = true;
			try{
				if(request.getParameter("selectedCoupon") == null)
					controller.addActivityToSchedule();
				else
					controller.addCertifiedActivityToSchedule();
			}catch(Exception e){
				e.printStackTrace();
				success = false;
			}
			
			%>
			<%--inizio prova modal per fare conferma o errore nello schedulo di un'attività --%>
			<!-- Modal elimina -->
				<div class="modal fade" id="responseModal" tabindex="-1" aria-labelledby="responseModalLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
			  		<div class="modal-dialog">
			    		<div class="modal-content">
			      			<div class="modal-header">
			        			<h5 class="modal-title" id="responseModalLabel"><%= success ? "Attività correttamente schedulata!" : "Errore nello schedulo dell'attività"  %></h5>
			        			<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			      			</div>
			      			<div class="modal-body">
			        			<p class="delete-icon-schedule text-center" <%if(success){ %>style="color:#198754"><i class="bi bi-check-circle-fill"></i> <%}else{%> ><i class="bi bi-x-circle-fill"></i> <%} %></p>
			        			<h5 class="text-center irreversible-process"><%= success ? "Visualizzala nello schedulo!" : "C'è stato un errore nell'aggiunta dell'attività nel tuo schedulo"%></h5>
			      			</div>
			      			<div class="modal-footer">
			        			<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Continua a vedere le attività</button>
			        			<% if(success){ %><a role="button" class="btn btn-primary" href="Schedule.jsp">Vediamo lo schedulo</a> <%}%>
			      			</div>
			    		</div>
			  		</div>
				</div>
				
				<script>
					let responseModal = new bootstrap.Modal(document.getElementById('responseModal'))
					responseModal.show();
				</script>
			
			<%-- fine modal per fare conferma o errore nello schedulo di un'attività --%>
			
			<%
		}
		
		
		DAOActivity daoAct = DAOActivity.getInstance();
		ArrayList<Activity> activities = new ArrayList<Activity>();
		
		//only for debugging, then you should pick those from 'utente'
		double userLat = 41.8901232;
		double userLong = 12.4960768;
				
		
		float maxDistance = 20.0f; //in km
		
		boolean areFiltersOn = false;
		
		try{
			maxDistance = Float.parseFloat(request.getParameter("max-distance"));
			if(maxDistance<0) throw new NumberFormatException();
			areFiltersOn = true;
		}catch(NumberFormatException e){
			%>
				<script>alert('Inserisci un valore corretto per la distanza!')</script>
			<%
			maxDistance = 20f;
		}catch(NullPointerException e){
			//non faccio nulla perché non mi interessa
		}
		
		
		try{	
			activities = (ArrayList<Activity>)daoAct.getNearbyActivities(userLat, userLong, maxDistance);
		}catch(Exception e) {
			//TODO: Fixare ASAP facendo comparire un messaggio di errore!!!!
			e.printStackTrace();
		}
		
		Preferences p = utente.getPreferences();
		if(request.getParameter("searchedForPreferences")!= null){
			p = SetPreferencesController.getPreferencesFromBean(preferenceBean);
			areFiltersOn = true;
		}
		activities = (ArrayList<Activity>)FindActivityController.filterActivitiesByPreferences(activities, p);
		
		LocalDate searchedDate = LocalDate.now();
		if(request.getParameter("date")!= null){
			String date = request.getParameter("date");
			try{
				searchedDate = LocalDate.parse(date);
				LocalDate today = LocalDate.now();
				if(searchedDate.isBefore(today)) throw new IllegalArgumentException("La data in cui vuoi fare l'attività deve almeno successiva ad oggi!");
				areFiltersOn = true;
			}catch(Exception e){
				%>
					<script>alert('Inserisci un valore corretto per la data, non può essere prima di oggi, mica viaggi nel tempo!')</script>
				<%
			}
		}
		
		activities = (ArrayList<Activity>) FindActivityController.filterActivitiesByDate(activities, searchedDate);
		
	%>
	
		<div class="row pt-6 home-body" id="home-body">
		
		
			<div class="col-4 events-list">
			<div class="d-flex sticky-top search-activity shadow">
				<input type="text" class="form-control flex-grow-1 search-home" placeholder="Cerca Attività" id="searchBetweenActivities">
			</div>
			<div class="row row-cols-1 row-cols-md-1 g-1">
			  <% for(Activity curr:activities){ %>
			  
			  <%
			  	boolean isCertified = false;
			 	if(curr instanceof CertifiedActivity) isCertified= true;
			  %>
			  <div class="col col-cards" >
			    <div class="card card-dark text-white" data-bs-toggle="collapse" href="#collapse<%= curr.getId() %>" aria-expanded="false" aria-controls="collapse<%= curr.getId() %>">
			      <% if(isCertified){ %><span class="badge bg-certified text-white position-absolute top-0 end-0">Certificata <i class="bi bi-patch-check-fill"></i></span> <%}%>
			      <img src="https://source.unsplash.com/random" class="card-img-top" alt="...">
			      <div class="card-body">
			        <h5 class="card-title"><% out.println(curr.getName()); %></h5>
			        <p class="card-text">Orario Apertura: <%= curr.getFrequency().getOpeningTime()%> <br />Orario Chiusura: <%= curr.getFrequency().getClosingTime()%></p>
			        <p class="card-text">Luogo : <%= curr.getPlace().getName() %> </p>
			      </div>
			    </div>
			    <div class="collapse" id="collapse<%= curr.getId() %>">
				    <div class="d-grid gap-2 activityButtonGroup">
				        	<button type="button" class="btn btn-dark btnHome" onclick="loadChat(<%=curr.getId()%>)">Join Channel</button>
				        
				        	<button type="button" class="btn btn-dark btnHome viewOnMap" onclick="moveView(<%= curr.getPlace().getLatitudine() %>,<%= curr.getPlace().getLongitudine() %>, <%= curr.getId()%>)">View on map</button>
				        
				        	<button type="button" class="btn btn-success btnHome"data-bs-toggle="modal" data-bs-target="#activityModal" data-bs-titolo="<%=curr.getName() %>" data-bs-luogo="<%=curr.getPlace().getName()%>" data-bs-id="<%=curr.getId() %>" data-bs-description="<%= curr.getDescription() %>" data-bs-playabilityInfo="<%= curr.getPlayabilityInfo()%>" data-bs-address="<%= curr.getPlace().getFormattedAddr()%>" <%if(isCertified){%> data-bs-certified="true" <%}%>>Play Activity</button>
				    </div>
			    </div>
			  </div>
			  
			  <% }%>
			  
			</div>
			</div>
			
			<%-- map --%>
			<div class="col-8" id="mapContainer">
				<div class="d-flex flex-row position-absolute filters gx-5"> 
					<button type="button" class="btn btn-filters" data-bs-toggle="modal" data-bs-target="#distanceModal">Distanza</button>
					<button type="button" class="btn btn-filters" data-bs-toggle="modal" data-bs-target="#preferencesModal">Categorie</button>
					<button type="button" class="btn btn-filters" data-bs-toggle="modal" data-bs-target="#dateModal">Data</button>
					<% if(areFiltersOn) { %><a href="Home.jsp" class="btn btn-filters" role="button">Pulisci filtri</a> <% } %>
					<a href="FindActivities.jsp" class="btn btn-filters" role="button">Ricerca avanzata <i class="bi bi-search"></i></a>									
				</div>
				<div id="Map" class="homeMap"></div>
			</div>
			
			<%-- js for the map --%>
			<script src="js/map.js"></script>
			<script type="text/javascript">
				var mymap;
				var latitude = <%= utente.getLatitude() %>
				var longitude = <%= utente.getLongitude() %>
				
				<%-- debugging mode: per assicurarmi che funziona, cancellare questo codice appena ne abbiamo la certezza --%>
				latitude = 41.8901232;
				longitude = 12.4960768;
				<%-- fine del debuggin mode ayooo--%>
				
					
				startup(latitude,longitude);
				
				
				 <% for(Activity curr:activities){ %>
				 	spotPlace(<%= curr.getPlace().getLatitudine() %>,<%= curr.getPlace().getLongitudine() %>, "<%= curr.getName() %>", <%=curr.getId()%>);
				 <% }%>
				 
				 var lastMarker = setUser(latitude,longitude);
				 
			</script>
		
			<%-- chat --%>
			<div class="col-8 chat d-flex flex-column visually-hidden" id="chat">
				<div class="chat-bar d-flex">
					<div class="text-center text-white flex-grow-1 fs-2" id="chatTitle"></div>
					<button type="button" class="btn-close btn-close-white me-2 m-auto" aria-label="Close" id="closeChat"></button>
				</div>
				<div class="container-fluid chatroom flex-grow-1" id="msg-container">
				</div>
				<div class="d-flex msg-bar">
					<input type="text" class="form-control flex-grow-1" id="chatField" placeholder="scrivi qualcosa"> 
					<button type="button" class="btn btn-outline-light send-btn disabled" id="send-btn" onclick="sendMsg()"> <i class="bi bi-send"></i> </button>
				</div>
			</div>
			
			<%-- fine chat --%>
		
		</div>
	
	
	</div>
	
	<!-- Modal -->
		<div class="modal fade" id="activityModal" tabindex="-1" aria-labelledby="activityModalLabel" aria-hidden="true">
		  <div class="modal-dialog modal-xl">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="activityModalLabel"></h5>
		        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		      </div>
		      <div class="modal-body">
		      
		      <%-- carosello --%>
			      <div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel">
				  <div class="carousel-indicators">
				    <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
				    <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="1" aria-label="Slide 2"></button>
				    <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="2" aria-label="Slide 3"></button>
				  </div>
				  <div class="carousel-inner carousel-activity">
				    <div class="carousel-item active">
				      <img src="https://source.unsplash.com/random" class="d-block w-100" alt="...">
				    </div>
				    <div class="carousel-item">
				      <img src="https://source.unsplash.com/random" class="d-block w-100" alt="...">
				    </div>
				    <div class="carousel-item">
				      <img src="https://source.unsplash.com/random" class="d-block w-100" alt="...">
				    </div>
				  </div>
				  <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="prev">
				    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
				    <span class="visually-hidden">Previous</span>
				  </button>
				  <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="next">
				    <span class="carousel-control-next-icon" aria-hidden="true"></span>
				    <span class="visually-hidden">Next</span>
				  </button>
				</div>
		      
		      <%-- fine carosello --%>
		      <label for="activityDescription" class="col-form-label label-activity">Descrizione:</label>
		      <p id="activityDescription" class="lead"><p>
		      
		      <div class="row">
		      	<div class="col">
		      		<label for="placename" class="col-form-label label-activity">Luogo:</label>
		      		<p id="placename" class="lead"></p>
		      	</div>
		      	<div class="col">
		      		<label for="activityaddress" class="col-form-label label-activity">Indirizzo:</label>
		      		<p id="activityaddress" class="lead"></p>
		      	</div>
		      </div>
		      <div class="row">
			      <label for="playabilityInfo" class="col-form-label label-activity">Puoi fare questa attività:</label>
			      <p id="playabilityInfo" class="lead"><p>
		      </div>
		      <hr>
		      <form action="Home.jsp" name="myform" method="GET">
		      	  <input type="number" id="idActivity" name="idActivity" class="visually-hidden">
		      	  <input type="number" id="selectedCoupon" name="selectedCoupon" class="visually-hidden">
			      <p>Quando vorresti fare questa attività?</p>
			      <div class="scheduled-time">
			      	<div class="row">
				        <div class="mb-3 col">
				        	<label for="scheduledDate" class="col-form-label label-activity">Data:</label>
				            <input type="date" class="form-control" id="scheduledDate" name="scheduledDate" min="<%= LocalDate.now()%>">
				        </div>
		
				        <div class="mb-3 col">
				        	<label for="scheduledTime" class="col-form-label label-activity">Orario:</label>
				            <input type="time" class="form-control" id="scheduledTime" name="scheduledTime">
				        </div>
				    </div>
				  </div>
				 <hr>
				  <div class="reminder-time">
				        <div class="mb-3" id="promemoria">
				        	<p>Vuoi ricevere un promemoria? <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="collapse" href="#reminder-form" aria-expanded="false" aria-controls="reminder-form" id="mostraPromemoria">Si, imposta un promemoria</button></p>
				        </div>
		
				        <div class="reminder-form collapse" id="reminder-form">
				        	<p>Dati del reminder:</p>
				        	<div class="row">
					        <div class="mb-3 col">
					        	<label for="scheduledDate" class="col-form-label label-activity">Data:</label>
					            <input type="date" class="form-control" id="reminderDate" name="reminderDate" min="<%= LocalDate.now()%>">
					        </div>
		
					        <div class="mb-3 col">
					        	<label for="scheduledTime" class="col-form-label label-activity">Orario:</label>
					            <input type="time" class="form-control" id="reminderTime" name="reminderTime">
					        </div>
					        </div>
				        </div>
			      </div>
		
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
		        <button type="button" class="btn btn-success"  id="generateCouponButton" data-bs-target="#couponModal" data-bs-toggle="modal" data-bs-dismiss="modal">Generate Coupon</button>
		        <button type="submit" class="btn btn-success"  id="playActivityButton">Schedule Activity</button>
		      </div>
		      </form>
		    </div>
		  </div>
		</div>
		
		<!-- fine modal -->
		
		<!-- inizio modal per Coupon -->
		<!-- Modal -->
		<div class="modal fade" id="couponModal" tabindex="-1" aria-labelledby="couponModalLabel" aria-hidden="true">
		  <div class="modal-dialog modal-xl modal-dialog-scrollable">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="couponModalLabel">Vuoi generare un codice sconto per questa attività?</h5>
		        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		      </div>
		      <div class="modal-body modal-coupon">
		      
		      <%-- roba che fa vedere il saldo disponibile --%>
		      <div class="row text-center sticky-top saldo shadow-sm"><p class="lead">Soldi disponibili: <span id="saldo-disponibile"><%= utente.getBalance() %></span></p></div>
		      
		      <%-- come funziona --%>
		      
		      <div class="accordion" id="infoCoupon">
				  <div class="accordion-item">
				    <h2 class="accordion-header" id="come-funziona">
				      <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#spiegazione" aria-expanded="true" aria-controls="spiegazione">
				        Come funzionano i coupon?
				      </button>
				    </h2>
				    <div id="spiegazione" class="accordion-collapse collapse show" aria-labelledby="come-funziona" data-bs-parent="#infoCoupon">
				      <div class="accordion-body">
				        <strong>This is the first item's accordion body.</strong> It is shown by default, until the collapse plugin adds the appropriate classes that we use to style each element. These classes control the overall appearance, as well as the showing and hiding via CSS transitions. You can modify any of this with custom CSS or overriding our default variables. It's also worth noting that just about any HTML can go within the <code>.accordion-body</code>, though the transition does limit overflow.
				      </div>
				    </div>
				  </div>
				</div>
		      
		      
		      <%--inizio righe dei coupon --%>
		       <div class="row coupon shadow">
		       	<div class="col percentage-info text-center">
		       		<div class="position-relative top-50 start-50 translate-middle">
		       			<label for="percentage" class="col-form-label label-sale">Sconto del</label>
		       			<p id="percentage"><span class="percentage">40%</span></p>
		       		</div>
		       	</div>
		       	<div class="col percentage-redeem text-center">
		       		<div class="position-relative top-50 start-50 translate-middle">
		       			<button type="button" class="btn btn-outline-success btn-lg btnHome">Genera Coupon per 10 soldi</button>
		       		</div>
		       	</div>
		       </div>
		       
		       <div class="row coupon shadow">
		       	<div class="col percentage-info text-center disabled">
		       		<div class="position-relative top-50 start-50 translate-middle">
		       			<label for="percentage" class="col-form-label label-sale">Sconto del</label>
		       			<p id="percentage"><strong>60%</strong></p>
		       		</div>
		       	</div>
		       	<div class="col percentage-redeem text-center">
		       		<div class="position-relative top-50 start-50 translate-middle">
		       			<p class="lead insufficient-funds">Soldi insufficienti per generare questo coupon</p>
		       			<button type="button" class="btn btn-outline-success btn-lg btnHome" disabled>Genera Coupon per 100 soldi</button>
		       		</div>
		       	</div>
		       </div>
		       
		       <div class="row coupon shadow">
		       	<div class="col percentage-info text-center disabled">
		       		<div class="position-relative top-50 start-50 translate-middle">
		       			<label for="percentage" class="col-form-label label-sale">Sconto del</label>
		       			<p id="percentage"><strong>80%</strong></p>
		       		</div>
		       	</div>
		       	<div class="col percentage-redeem text-center">
		       		<div class="position-relative top-50 start-50 translate-middle">
		       			<button type="button" class="btn btn-outline-success btn-lg btnHome" disabled>Genera Coupon per 300 soldi</button>
		       		</div>
		       	</div>
		       </div>
		       
		       <div class="row coupon shadow">
		       	<div class="col percentage-info text-center disabled">
		       		<div class="position-relative top-50 start-50 translate-middle">
		       			<label for="percentage" class="col-form-label label-sale">Sconto del</label>
		       			<p id="percentage"><strong>100%</strong></p>
		       		</div>
		       	</div>
		       	<div class="col percentage-redeem text-center">
		       		<div class="position-relative top-50 start-50 translate-middle">
		       			<button type="button" class="btn btn-outline-success btn-lg btnHome" disabled>Genera Coupon per 600 soldi</button>
		       		</div>
		       	</div>
		       </div>
		       
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
		        <button type="button" class="btn btn-success" onclick="generateDiscount(0)">Non desidero generare nessun coupon</button>
		      </div>
		    </div>
		  </div>
		</div>
				
		
		
		<!-- fine modal per coupon -->
		
		
		<!-- modal per la distanza -->
		
		<form method="GET" action="Home.jsp">
		
		<div class="modal fade" id="distanceModal" tabindex="-1" aria-labelledby="distanceModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="distanceModalLabel">Di quanto ti vuoi spostare?</h5>
		        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		      </div>
		      
		      <div class="modal-body">
		      	<p>Dovado trova le attività che ti potrebbero piacere nel raggio che decidi tu!</p>
		        <label for="max-distance" class="form-label">Voglio spostarmi di massimo:</label>
		        <div class="input-group mb-3">
				  <input type=number step=0.5 min=1 class="form-control" placeholder="Inserisci la distanza" aria-label="kilometriDaPercorrere" aria-describedby="kilometri" value="<%= maxDistance %>" name="max-distance" id="max-distance">
				  <span class="input-group-text" id="kilometri">km</span>
				</div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
		        <button type="submit" class="btn btn-primary">Trova attività</button>
		      </div>
		      
		    </div>
		  </div>
		</div>
		
		
		<!-- modal per le preferenze -->
		<div class="modal fade" id="preferencesModal" tabindex="-1" aria-labelledby="preferencesModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="preferencesModalLabel">A cosa sei interessato?</h5>
		        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		      </div>
		      
		      <div class="modal-body">
		      	<p>Dovado trova le attività che ti potrebbero piacere in base a cosa sei interessato!</p>
		        <p class="form-label">Sono interessato a:</p>
		        <% Preferences pref = p; %>
		      	<div class="row px-2 info shadow-sm">
		      	<input class="visually-hidden" type="checkbox" name="searchedForPreferences" id="searchedForPreferences" checked>
				<div class="col">
					<div class="form-check form-switch">
					  	 <input class="form-check-input" type="checkbox" id="Arte" name="arte" <% if(pref.isArte()) out.println("checked"); %>>
				  		 <label class="form-check-label" for="Arte">Arte</label>
					  </div>
					  
					  <div class="form-check form-switch">
					  	 <input class="form-check-input" type="checkbox" id="Cibo" name="cibo" <% if(pref.isCibo()) out.println("checked"); %>>
				  		 <label class="form-check-label" for="Cibo">Cibo</label>
					  </div>
					  
					  <div class="form-check form-switch">		
					  	 <input class="form-check-input" type="checkbox" id="Musica" name="musica" <% if(pref.isMusica()) out.println("checked"); %>>
				  		 <label class="form-check-label" for="Musica">Musica</label>
					  </div>
					  
					  <div class="form-check form-switch">	 
					  	 <input class="form-check-input" type="checkbox" id="Sport" name="sport" <% if(pref.isSport()) out.println("checked"); %>>
				  		 <label class="form-check-label" for="Sport">Sport</label>
					  </div>
					  
					  <div class="form-check form-switch">	 
					  	 <input class="form-check-input" type="checkbox" id="Social" name="social" <% if(pref.isSocial()) out.println("checked"); %>>
				  		 <label class="form-check-label" for="Social">Social</label>
					  </div>
					  
					  <div class="form-check form-switch">	 
					  	 <input class="form-check-input" type="checkbox" id="Natura" name="natura" <% if(pref.isNatura()) out.println("checked"); %>>
				  		 <label class="form-check-label" for="Natura">Natura</label>
					  </div>
					  
					  <div class="form-check form-switch">	 
					  	 <input class="form-check-input" type="checkbox" id="Esplorazione" name="esplorazione" <% if(pref.isEsplorazione()) out.println("checked"); %>>
				  		 <label class="form-check-label" for="Esplorazione">Esplorazione</label>
					  </div>
				</div>
				<div class="col">
					<div class="form-check form-switch">
						<input class="form-check-input" type="checkbox" id="Ricorrenze" name="ricorrenze" <% if(pref.isRicorrenzeLocali()) out.println("checked"); %>>
						<label class="form-check-label" for="Ricorrenze">Ricorrenze Locali</label>
				  	</div>
				  	
				  	<div class="form-check form-switch">
				  		<input class="form-check-input" type="checkbox" id="Moda" name="moda" <% if(pref.isModa()) out.println("checked"); %>>
			  			<label class="form-check-label" for="Mode">Moda</label>
				  	</div>
				  	
				  	<div class="form-check form-switch">		
				  		<input class="form-check-input" type="checkbox" id="Shopping" name="shopping" <% if(pref.isShopping()) out.println("checked"); %>>
			  		 	<label class="form-check-label" for="Shopping">Shopping</label>
				  	</div>
				  	
				  	<div class="form-check form-switch">	 
				  	 	<input class="form-check-input" type="checkbox" id="Adrenalina" name="adrenalina" <% if(pref.isAdrenalina()) out.println("checked"); %>>
			  		 	<label class="form-check-label" for="Adrenalina">Adrenalina</label>
				  	</div>
				  	
				   	<div class="form-check form-switch">	 
				  	 	<input class="form-check-input" type="checkbox" id="Relax" name="relax" <% if(pref.isRelax()) out.println("checked"); %>>
			  			<label class="form-check-label" for="Relax">Relax</label>
				  	</div>
				  	
				  	<div class="form-check form-switch">	 
				  	 	<input class="form-check-input" type="checkbox" id="Istruzione" name="istruzione" <% if(pref.isIstruzione()) out.println("checked"); %>>
			  		 	<label class="form-check-label" for="Istruzione">Istruzione</label>
				  	</div>
				  	
				  	<div class="form-check form-switch">	 
				  	 	<input class="form-check-input" type="checkbox" id="Monumenti" name="monumenti" <% if(pref.isMonumenti()) out.println("checked"); %>>
			  		 	<label class="form-check-label" for="Monumenti">Monumenti</label>
				  	</div>
			</div>
		</div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
		        <button type="submit" class="btn btn-primary">Trova attività</button>
		      </div>
		      
		    </div>
		  </div>
		</div>
		
		<!-- modal per la data -->
		
		<div class="modal fade" id="dateModal" tabindex="-1" aria-labelledby="dateModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="dateModalLabel">Quando vuoi fare le attività?</h5>
		        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		      </div>
		      
		      <div class="modal-body">
		      	<p>Dovado trova le attività che ti potrebbero piacere nel giorno che decidi tu!</p>
		        <label for="datePicker" class="form-label">Voglio fare le attività il giorno:</label>
		        <div class="input-group mb-3">
				  <input type="date"  class="form-control" id="datePicker" name="date" aria-label="kilometriDaPercorrere"  value="<%= searchedDate %>" min="<%= LocalDate.now() %>">
				</div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
		        <button type="submit" class="btn btn-primary">Trova attività</button>
		      </div>
		      
		    </div>
		  </div>
		</div>
				<%} %>
		</form>
		
		
		<!-- fine modal per la distanza -->
		
		 <script>
			//---------------------------------------------------------------
		 	//|						 	modal								|
		 	//---------------------------------------------------------------
		 
		 	var exampleModal = document.getElementById('activityModal')
		 	exampleModal.addEventListener('show.bs.modal', function (event) {
			   // Button that triggered the modal
			   var button = event.relatedTarget
			   // Extract info from data-bs-* attributes
			   var titolo = button.getAttribute('data-bs-titolo')
			   var id = button.getAttribute('data-bs-id')
			   var luogo = button.getAttribute('data-bs-luogo')
			   let descr = button.getAttribute('data-bs-description')	
			   let playability =  button.getAttribute('data-bs-playabilityInfo')
			   let addr = button.getAttribute('data-bs-address')
				
		       let isHidden = exampleModal.querySelector('#reminder-form.show')
			   if(!(isHidden == null)) exampleModal.querySelector('#mostraPromemoria').click();
			   
			   // If necessary, you could initiate an AJAX request here
			   // and then do the updating in a callback.
			   //
			   // Update the modal's content.
			   var modalTitle = exampleModal.querySelector('.modal-title')
			   var modalID = exampleModal.querySelector('.modal-body #idActivity')
			   var modalDescription = exampleModal.querySelector('#activityDescription')
			   let modalPlayabilityInfo = exampleModal.querySelector('#playabilityInfo')
			   let modalPlace = exampleModal.querySelector('#placename')
			   let modalAddress = exampleModal.querySelector('#activityaddress')
			   
			   exampleModal.querySelector('#scheduledDate').value='';
			   exampleModal.querySelector('#scheduledTime').value='';
			   exampleModal.querySelector('#reminderDate').value='';
			   exampleModal.querySelector('#reminderTime').value='';
			   
			   
			   console.log(id);
			   modalID.value=id
			   modalTitle.textContent = titolo
			   modalDescription.textContent = descr
			   modalPlayabilityInfo.textContent = playability 
			   modalPlace.textContent = luogo
			   modalAddress.textContent = addr
			   
			   //---------------------sezione per controllare se l'attività è certificata---------------
			   
			   let isCertified = button.getAttribute('data-bs-certified');
			   
			   //bottone schedule activity
			   let btnPlayActivity = exampleModal.querySelector('#playActivityButton');
			   
			   //bottone per generare coupon
			   let btnGenerateCoupon = exampleModal.querySelector('#generateCouponButton');
			   
			   if(isCertified != null){
				   //l'attività è certificata
				   
				   //disabilito il schedule activity e lo nascondo
				   btnPlayActivity.disabled = true;
				   btnPlayActivity.classList.add('visually-hidden');
				   
				   //attivo e faccio comparire il generate coupon
				   btnGenerateCoupon.disabled = false;
				   btnGenerateCoupon.classList.remove('visually-hidden');
				   
				   //disabilito il campo di testo del coupon
				   exampleModal.querySelector('#selectedCoupon').disabled=false;
			   	   
			   }else{
				   //l'attività non è certificata
				   
				   //attivo il schdule activity e lo faccio comparire
				   btnPlayActivity.disabled = false;
				   btnPlayActivity.classList.remove('visually-hidden');
			   	   
				   //disattivo il generate coupon e lo nascondo
				   btnGenerateCoupon.disabled = true;
				   btnGenerateCoupon.classList.add('visually-hidden');
			   		
				   exampleModal.querySelector('#selectedCoupon').disabled=true;
			   
			   }
			})
		
			
		
		 	
		 	
		 	//---------------------------------------------------------------
		 	//|							chat								|
		 	//---------------------------------------------------------------
		 	
		 	var refreshInterval;
		 	var chat;
		 	
			//disabilito il bottone se non ho testo da inviare
 			var chatField = document.getElementById('chatField');
 			chatField.addEventListener('keyup', ()=>{
 				if(chatField.value.length > 0) document.getElementById('send-btn').classList.remove("disabled");
 				else document.getElementById('send-btn').classList.add("disabled");
 			});
 			
 			document.getElementById('closeChat').addEventListener('click', ()=>{
 				
 				//stop refreshing
 				if(refreshInterval != undefined) clearInterval(refreshInterval);
 				
 				clearChat();
 				
 				//rendo invisibile la chat nel caso non lo sia già
		 		document.getElementById('chat').classList.add('visually-hidden');
		 		
		 		//Faccio riapparire la mappa -DA RIVEDERE-
				document.getElementById('mapContainer').classList.remove("visually-hidden");
 				
 				
 			});
		 	
		 	function loadChat(activity){
		 		let request = "chat.jsp?activity="+activity;
		 		
		 		//rendo visibile la chat nel caso non lo sia già
		 		document.getElementById('chat').classList.remove('visually-hidden');
		 		
		 		//nascondo la mappa -DA RIVEDERE-
				document.getElementById('mapContainer').classList.add("visually-hidden");
		 		
		 		//cancello il timer di refresh nel caso sia attivo
		 		if(refreshInterval != undefined)
		 		clearInterval(refreshInterval);
		 		
		 		//imposto un intervallo per refreshare la chat
		 		startRefreshing(request);
		 		
		 		//step 1: genero oggetto per richiesta al server
		 		var req = new XMLHttpRequest();
		 		
		 		
		 		//step 2: creo la funzione che viene eseguita quando ricevo risposta dal server
		 		req.onload = function(){
					console.log(this);	
					chat = this.response;
					
					console.log(chat);
			 		
			 		//imposto il titolo del canale
			 		document.getElementById('chatTitle').innerText = chat.activityName+"'s channel";
			 		
			 		clearChat();
			 		
			 		populateChat(chat.messages);
			 		
			 		scrollToBottomChat()
		 		}
		 		
		 		//step 3: dico quale richiesta fare al server
				req.open("GET", request);
				//step 3.1 : imposto document come response type, perché sto per ricevere html html
				req.responseType = "json";
				
				//step 4: invio la richiesta 
				req.send();
		 	}
		 	
		 	
		 	function sendMsg(){
		 		var message = document.getElementById("chatField").value;
		 		let activity = chat.activityId;
		 		
		 		//TODO: primo controllo che il campo non sia vuoto
		 		if(message=='') alert('Chiudi console sviluppatore, è molto poco carino da parte tua voler rompere il sito 🥺')
		 		
		 		else{
		 			let request = "chat.jsp?activity="+activity+"&textMsg="+message;
		 			//chat = sendRequest(request);
		 			
		 			//----------------------------------------AJAX----------------------------------------------
		 			//step 1: genero oggetto per richiesta al server
			 		var req = new XMLHttpRequest();
			 		
			 		
			 		//step 2: creo la funzione che viene eseguita quando ricevo risposta dal server
			 		req.onload = function(){
						console.log(this);	
						chat = this.response;
						
						clearChat();
						populateChat(chat.messages);
			 			scrollToBottomChat()
			 			
			 			//pulisco l'input di invio DOPO che ho inviato e riblocco il pulsante
			 			document.getElementById('chatField').value = '';
			 			document.getElementById('send-btn').classList.add("disabled");
			 		}
			 		
			 		//step 3: dico quale richiesta fare al server
					req.open("GET", request);
					//step 3.1 : imposto document come response type, perché sto per ricevere html html
					req.responseType = "json";
					
					//step 4: invio la richiesta 
					req.send();
		 			
			 		//-----------------------------------Fine AJAX----------------------------------------------
		 			
		 		}
		 	}
		 	
		 	function sendRequest(request){
		 		//step 1: genero oggetto per richiesta al server
		 		var req = new XMLHttpRequest();
		 		
		 		
		 		//step 2: creo la funzione che viene eseguita quando ricevo risposta dal server
		 		req.onload = function(){
					console.log(this);	
					this.response;
		 		}
		 		
		 		//step 3: dico quale richiesta fare al server
				req.open("GET", request);
				//step 3.1 : imposto document come response type, perché sto per ricevere html html
				req.responseType = "json";
				
				//step 4: invio la richiesta 
				req.send();
		 	}
		 	
		 	function populateChat(messages){
		 		
		 		//console.log(messages);		 		
		 		messages.forEach( curr => {
		 					
		 			let htmlMsg = '<div class="row d-flex '+(curr.sender == chat.user ? 'justify-content-end' : 'justify-content-start')+'">';
		 			htmlMsg+= ' <div class="toast show message '+(curr.sender == chat.user ? 'msg-sent' : 'msg-recieved')+'" role="alert" aria-live="assertive" aria-atomic="true">';
		 			htmlMsg+= ' <div class="toast-header">';
		 			htmlMsg+= ' <strong class="me-auto">'+curr.sender+'</strong>';
		 			htmlMsg+= ' <small class="text-muted">'+curr.sendDate+'</small>';
		 			htmlMsg+= ' </div>';
		 			htmlMsg+= ' <div class="toast-body">'+curr.text+'</div>';
		 			htmlMsg+= ' </div> </div>';
		 			
		 			//generaro un nodo html da stringhe per creare il box del messaggio 
		 			let placeholder = document.createElement('div');
			 		placeholder.insertAdjacentHTML('afterbegin', htmlMsg);
			 		
			 		let message = placeholder.firstElementChild;
			 		
			 		//console.log(message);
			 		//appendo il messaggio dentro il suo container di messaggi
			 		document.getElementById('msg-container').append(message);
		 		});	
		 	}
		 	
		 	function startRefreshing(request){
	 			refreshInterval= setInterval(() => {
	 				//step 1: genero oggetto per richiesta al server
			 		var req = new XMLHttpRequest();
			 		
			 		
			 		//step 2: creo la funzione che viene eseguita quando ricevo risposta dal server
			 		req.onload = function(){
						console.log(this);	
						let newChat = this.response;
						if( JSON.stringify(chat) !=  JSON.stringify(newChat)){
		 					clearChat();
		 					chat = newChat;
		 					populateChat(chat.messages);
		 					scrollToBottomChat();
		 				}
			 		}
			 		
			 		//step 3: dico quale richiesta fare al server
					req.open("GET", request);
					//step 3.1 : imposto document come response type, perché sto per ricevere html html
					req.responseType = "json";
					
					//step 4: invio la richiesta 
					req.send();
	 				//aggiorno la chat solo se ci sono nuovi messaggi
	 				console.log('finito timer');
	 				}
	 			,30000);
		 	}
		 	
		 	function scrollToBottomChat(){
		 		//comando per scrollare in fondo alla chat
				let chat = document.getElementsByClassName('chatroom')[0];
		 		chat.scrollTop = chat.scrollHeight;
		 	}
		 	
		 	function clearChat(){
		 		//pulisco la chat
				document.getElementById('msg-container').innerHTML='';
		 	}
		 	
		 	//----------------------------------------------------------------
		 	//							search activitities
		 	//----------------------------------------------------------------
		 	document.querySelector('#searchBetweenActivities').addEventListener('keyup', filterActivities);
		 	
		 	const activitiesCards = document.querySelectorAll('.col-cards')
		 	
		 	function filterActivities(e){
		 		let textfield = e.target.value;
		 		textfield = textfield.toUpperCase();
		 		activitiesCards.forEach(col => containsKeywords(col,textfield));
		 	}
		 	
		 	function containsKeywords(col,src){
		 		let txt = col.querySelector('.card-title').textContent.toUpperCase();
		 		if(txt.includes(src)) col.classList.remove('visually-hidden');
		 		else col.classList.add('visually-hidden');
		 	}
			
		 	//------------------------------------------------------------------
		 	//							buttons on activity
		 	//------------------------------------------------------------------

		 	
		 	document.querySelectorAll('.viewOnMap').forEach(
		 		node => node.addEventListener('click',() => {
		 			if(refreshInterval != undefined) document.querySelector('#closeChat').click();
		 		})
		 	);
		 	
		 	//------------------------------------------------------------------------
		 	//							form checks
		 	//------------------------------------------------------------------------
		 	
		 	document.querySelector('#scheduledDate').addEventListener('change', (event)=>{
		 		selectedDate = event.target.value;
		 		document.querySelector('#reminderDate').max = selectedDate; 
		 	})
		 	
		 	//------------------------------------------------------------------------
		 	//						visualizzazione coupon
		 	//------------------------------------------------------------------------
		 	
		 	const couponModal = document.getElementById('couponModal')
		 	
		 	couponModal.addEventListener('show.bs.modal', function(){
		 		let activity = document.querySelector('#activityModal input#idActivity').value;
		 		console.log("l'attività selezionata è:"+activity);
		 		
		 		let url = "discount.jsp?activity="+activity;
		 		
		 		//step 1: genero oggetto per richiesta al server
		 		var req = new XMLHttpRequest();
		 		
		 		//step 2: creo la funzione che viene eseguita quando ricevo risposta dal server
		 		req.onload= function(){
		 			console.log(this.response);
		 			let usrSale = this.response.usrWallet;
		 			let discounts = this.response.discounts;
		 			
		 			couponModal.querySelector('#saldo-disponibile').textContent = usrSale;
		 			
		 			populateDiscounts(discounts,usrSale);
		 		}
		 		
		 		//step 3: dico quale richiesta fare al server
				req.open("GET", url);
		 		
				//step 3.1 : imposto document come response type, perché sto per ricevere html html
				req.responseType = "json";
				
				//step 4: invio la richiesta 
				req.send();
		 	});
		 	
		 	function clearDiscounts(){
		 		couponModal.querySelectorAll('div.row.coupon.shadow').forEach(curr => curr.remove());
		 	}
		 	
		 	function populateDiscounts(discounts, myAvaibility){
		 		
		 		clearDiscounts();
		 		
		 		discounts.forEach(curr => {
		 			
		 			
			 		let htmlDiscount;
			 		
			 		let redeemable = (curr.price <= myAvaibility)
			 		
			 		htmlDiscount+='<div class="row coupon shadow">';
			 			htmlDiscount+='<div class="col percentage-info text-center '+(redeemable == false ? 'disabled' : '')+'">';
			 				htmlDiscount+='<div class="position-relative top-50 start-50 translate-middle">';
			 					htmlDiscount+='<label for="percentage" class="col-form-label label-sale">Sconto del</label>';
			 					htmlDiscount+='<p id="percentage"><span class="percentage">'+curr.percentage+'%</span></p>';
			 				htmlDiscount+='</div>';
		 				htmlDiscount+='</div>';
			 			htmlDiscount+='<div class="col percentage-redeem text-center">';
			 				htmlDiscount+='<div class="position-relative top-50 start-50 translate-middle">';
			 					if(!redeemable) htmlDiscount+='<p class="lead insufficient-funds">Soldi insufficienti per generare questo coupon</p>';
			 					htmlDiscount+='<button type="button" class="btn btn-outline-success btn-lg btnHome" '+(redeemable == false ? 'disabled' : '')+'>Genera Coupon per '+curr.price+' soldi</button>';
			 				htmlDiscount+='</div>';
			 			htmlDiscount+='</div>';
					htmlDiscount+='</div>';
					
					//generaro un nodo html da stringhe per creare il box del messaggio 
		 			let placeholder = document.createElement('div');
			 		placeholder.insertAdjacentHTML('afterbegin', htmlDiscount);
			 		
			 		let discount = placeholder.firstElementChild;
			 		if(redeemable) discount.querySelector('button').addEventListener('click', () => generateDiscount(curr.percentage))
			 		couponModal.querySelector('.modal-body.modal-coupon').append(discount);
		 		
		 		});
		 	}
		 	
		 	function generateDiscount(perc){
		 		console.log("Selezionata la pecentuale di sconto del "+perc+"%");
		 		exampleModal.querySelector('#selectedCoupon').value = perc;
		 		exampleModal.querySelector('#playActivityButton').disabled = false;
		 		exampleModal.querySelector('#playActivityButton').click();
		 	}
		 	
		 	
		 	
		 	
		 	
		 	
		 	
	</script>
</body>
</html>
