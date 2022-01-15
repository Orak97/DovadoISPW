<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 

    <%@ page import = "java.io.*,java.util.*, logic.model.DAOPreferences, logic.model.DAOActivity, logic.model.DAOSuperUser, logic.model.SuperActivity, logic.model.SuperUser, logic.model.User, logic.model.Activity, logic.controller.AddActivityToScheduleController" %>

    <% application.setAttribute( "titolo" , "Home"); %>

	<%@ include file="Navbar.jsp" %>
	
	<jsp:useBean id="scheduleBean" scope="request" class="logic.model.ScheduleBean" />

	<jsp:setProperty name="scheduleBean" property="*" />
	

	<div class="container-fluid home">
	<% //tentativo di fare una home decente:
		User utente = (User) session.getAttribute("user");
		if(utente == null) response.sendRedirect("login.jsp");
		
		//controllo latiduine e longitudine, se sono 0 -> non è stata inizializzata -> porto l'utente alla pagina per attivare la geolocalizzazione
		if(utente.getLatitude() == 0 || utente.getLongitude() == 0) response.sendRedirect("localization.jsp");
		
		if(request.getParameter("idActivity")!= null){ //controllo la richiesta ricevuta, se all'interno è presente un parametro date vuol dire che arrivo a questa pagina tramite la pressione del bottone save changes, quindi ne consegue che i dati sono pieni e quindi posso andare avanti
			AddActivityToScheduleController controller = new AddActivityToScheduleController(utente,scheduleBean);
			try{
				controller.addActivityToSchedule();
			}catch(Exception e){
				%>
					<script> alert('Sembra che ci sia un errore nell\' aggiungere l\'attività nello schedulo!') </script>
				<%
				e.printStackTrace();
			} 
		}
		
		
		DAOActivity daoAct = DAOActivity.getInstance();
		ArrayList<Activity> activities = new ArrayList<Activity>();
		
		try{
			double userLat = 41.8901232;
			double userLong = 12.4960768;
			float maxDistance = 2.0f; //in km
					
			activities = daoAct.getNearbyActivities(userLat, userLong, maxDistance);
		}catch(Exception e) {
			//TODO: Fixare ASAP facendo comparire un messaggio di errore!!!!
			e.printStackTrace();
		}
	%>
	
		<div class="row pt-6 home-body" id="home-body">
		
			<div class="col-4 events-list">
			<div class="row row-cols-1 row-cols-md-1 g-1">
			  <% for(Activity curr:activities){ %>
			  
			  <div class="col" >
			    <div class="card card-dark text-white" data-bs-toggle="collapse" href="#collapse<%= curr.getId() %>" aria-expanded="false" aria-controls="collapse<%= curr.getId() %>">
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
				        
				        	<button type="button" class="btn btn-dark btnHome" onclick="moveView(<%= curr.getPlace().getLatitudine() %>,<%= curr.getPlace().getLongitudine() %>, <%= curr.getId()%>)">View on map</button>
				        
				        	<button type="button" class="btn btn-success btnHome"data-bs-toggle="modal" data-bs-target="#exampleModal" data-bs-titolo="<%=curr.getName() %>" data-bs-luogo="<%=curr.getPlace().getName()%>" data-bs-id="<%=curr.getId() %>">Play Activity</button>
				    </div>
			    </div>
			  </div>
			  
			  <% }%>
			  
			</div>
			</div>
			
			<%-- map --%>
			<div class="col-8" id="mapContainer">
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
				 	spotPlace(<%= curr.getPlace().getLatitudine() %>,<%= curr.getPlace().getLongitudine() %>, '<%= curr.getName() %>', <%=curr.getId()%>);
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
		<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="exampleModalLabel"></h5>
		        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		      </div>
		      <div class="modal-body">
		      <form action="Home.jsp" name="myform" method="GET">
		      	  <input type="number" id="idActivity" name="idActivity" class="visually-hidden">
			      <div class="scheduled-time">
				        <div class="mb-3">
				        	<label for="scheduledDate" class="col-form-label">Data:</label>
				            <input type="date" class="form-control" id="scheduledDate" name="scheduledDate">
				        </div>
		
				        <div class="mb-3">
				        	<label for="scheduledTime" class="col-form-label">Orario:</label>
				            <input type="time" class="form-control" id="scheduledTime" name="scheduledTime">
				        </div>
				  </div>
		
				  <div class="reminder-time">
				        <div class="mb-3" id="promemoria">
				        	<p>Vuoi ricevere un promemoria per questo evento?</p>
				        	<button type="button" class="btn btn-primary btn-sm" onclick="addPromemoria()">Impostaun promemoria</button>
				        </div>
		
				        <div class="reminder-form visually-hidden" id="reminder-form">
				        	<p>Dati del reminder:</p>
					        <div class="mb-3">
					        	<label for="scheduledDate" class="col-form-label">Data:</label>
					            <input type="date" class="form-control" id="scheduledDate" name="reminderDate">
					        </div>
		
					        <div class="mb-3">
					        	<label for="scheduledTime" class="col-form-label">Orario:</label>
					            <input type="time" class="form-control" id="scheduledTime" name="reminderTime">
					        </div>
				        </div>
			      </div>
		
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
		        <button type="submit" class="btn btn-success"  name="date">Schedule Activity</button>
		      </div>
		      </form>
		    </div>
		  </div>
		</div>
		
		<!-- fine modal -->
		 <script>
			//---------------------------------------------------------------
		 	//|						 	modal								|
		 	//---------------------------------------------------------------
		 
		 	var exampleModal = document.getElementById('exampleModal')
		 	exampleModal.addEventListener('show.bs.modal', function (event) {
			   // Button that triggered the modal
			   var button = event.relatedTarget
			   // Extract info from data-bs-* attributes
			   var titolo = button.getAttribute('data-bs-titolo')
			   var id = button.getAttribute('data-bs-id')
			   var luogo = button.getAttribute('data-bs-luogo')
			   
			   var orarioReminder = button.getAttribute('data-bs-orarioReminder')
			   var dataReminder = button.getAttribute('data-bs-dataReminder')
			   if(orarioReminder == null || dataReminder == null){
				   console.log('nessun promemoria')
				   removePromemoria()
			   }
			   // If necessary, you could initiate an AJAX request here
			   // and then do the updating in a callback.
			   //
			   // Update the modal's content.
			   var modalTitle = exampleModal.querySelector('.modal-title')
			   var modalID = exampleModal.querySelector('.modal-body #idActivity')
		
			   console.log(id);
			   modalID.value=id
			   modalTitle.textContent = titolo
			})
		
			function addPromemoria(){
		 		let div = document.getElementById('promemoria')
		 		div.classList.add("visually-hidden");
		
		 		let form = document.getElementById('reminder-form');
		 		form.classList.remove("visually-hidden");
		 	}
		
		 	function removePromemoria(){
		 		let div = document.getElementById('promemoria')
		 		div.classList.remove("visually-hidden");
		
		
		 		let form = document.getElementById('reminder-form');
		 		form.classList.add("visually-hidden");
		 	}
		 	
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
 				
 				//rendo visibile la chat nel caso non lo sia già
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
		 	
	</script>
</body>
</html>
