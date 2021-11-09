<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 

    <%@ page import = "java.io.*,java.util.*, logic.model.DAOPreferences, logic.model.DAOActivity, logic.model.DAOSuperUser, logic.model.SuperActivity, logic.model.SuperUser, logic.model.User" %>

    <% application.setAttribute( "titolo" , "Home"); %>

	<%@ include file="Navbar.jsp" %>
	
	<jsp:useBean id="scheduleBean" scope="request" class="logic.model.ScheduleBean" />

	<jsp:setProperty name="scheduleBean" property="*" />
	<%

		if(request.getParameter("date")!= null){ //controllo la richiesta ricevuta, se all'interno è presente un parametro date vuol dire che arrivo a questa pagina tramite la pressione del bottone save changes, quindi ne consegue che i dati sono pieni e quindi posso andare avanti
		 //out.println("printo prop:"+scheduleBean.getScheduledDateTime());
		 
		SuperUser u = (User) session.getAttribute("user");
		 
		 try {
			 out.println(u.getUsername());
			 DAOActivity a = DAOActivity.getInstance();
			 DAOSuperUser su = DAOSuperUser.getInstance();
			 Long idA = scheduleBean.getIdActivity();
			 SuperActivity att = a.findActivityByID(su,idA);
			 ((User) u).getSchedule().addActivityToSchedule(att, scheduleBean.getScheduledDateTime(),scheduleBean.getScheduledDateTime(), u);
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 }

		
		/*
		Enumeration paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements()) {
			String paramName = (String)paramNames.nextElement();
			String paramValue = request.getParameter(paramName);
			if(paramValue == "") out.println("zi, is null");
			out.println(paramName+':'+paramValue);
		} */
		

	%>

	
	
	
	

	<% //tentativo di fare una home decente:
		
		DAOPreferences daoPref = DAOPreferences.getInstance();
		DAOActivity daoAct = DAOActivity.getInstance();
		DAOSuperUser daoSU = DAOSuperUser.getInstance();
	
		ArrayList<SuperActivity> activities = new ArrayList<SuperActivity>();
		
		System.err.println("\n"+"Working Directory = " + System.getProperty("user.dir"));	
		activities.addAll(daoAct.findActivityByPreference(daoSU, "BOXE"));
		activities.addAll(daoAct.findActivityByPreference(daoSU, "TENNIS"));

		System.err.println("\n"+"\nNumero di attivita trovate: "+activities.size());
	
	%>
	<div class="container-fluid home">
		<div class="row pt-6 home-body" id="home-body">
		
			<div class="col-4 events-list">
			<div class="row row-cols-1 row-cols-md-1 g-1">
			  <% for(SuperActivity curr:activities){ %>
			  
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
				        
				        	<button type="button" class="btn btn-dark btnHome" onclick="document.getElementById('map').contentWindow.spotPlace('<%=curr.getPlace().getCivico()%>','<%=curr.getPlace().getAddress()%>','<%=curr.getPlace().getCity()%>','<%=curr.getPlace().getRegion()%>');">View on map</button>
				        
				        	<button type="button" class="btn btn-success btnHome"data-bs-toggle="modal" data-bs-target="#exampleModal" data-bs-titolo="<%=curr.getName() %>" data-bs-luogo="<%=curr.getPlace().getName()%>" data-bs-id="<%=curr.getId() %>">Play Activity</button>
				    </div>
			    </div>
			  </div>
			  
			  <% }%>
			  
			</div>
			</div>
			
			<div class="col-8" id="map" style="overflow-y:hidden">
				<iframe src="map.html" title="maps" id="map" style="width:100%; height:100%"></iframe> 
			</div>
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
		 	
		 	function loadChat(activity){
		 		let request = "chat.jsp?activity="+activity;
		 		
		 		//cancello il timer nel caso esistesse già
		 		if(refreshInterval != undefined)
		 		clearInterval(refreshInterval);
		 		
		 		//imposto un intervallo per refresharela chat
	 			refreshInterval= setInterval(() => {sendRequest(request)
	 				console.log('finito timer')},500);
		 		
		 		
		 		sendRequest(request);
		 	}
		 	
		 	function sendMsg(activity){
		 		let message = document.getElementById("chatField").value;
		 		
		 		
		 		let request = "chat.jsp?activity="+activity+"&textMsg="+message;
		 		sendRequest(request);
		 	}
		 	
		 	function sendRequest(request){
		 		//step 1: genero oggetto per richiesta al server
		 		var req = new XMLHttpRequest();
		 		
		 		
		 		//step 2: creo la funzione che viene eseguita quando ricevo risposta dal server
		 		req.onload = function(){
		 			
		 			//controllo che non ci siano già chat aperte, in caso le chiudo
			 		let existingChat = document.getElementById('chat');
			 		if(existingChat != null) existingChat.remove();
		 			
		 			
					console.log(this.responseXML.body.firstChild);
					document.getElementById('home-body').append(this.responseXML.body.firstChild)
					//nascondo la mappa -DA RIVEDERE-
					document.getElementById('map').classList.add("visually-hidden");
					
					//comando per scrollare in fondo alla chat
					let chat = document.getElementsByClassName('chatroom')[0];
				 	chat.scrollTop = chat.scrollHeight;
				
		 			//disabilito il bottone se non ho testo da inviare
		 			let chatField = document.getElementById('chatField');
		 			chatField.addEventListener('keyup', ()=>{
		 				if(chatField.value.length > 0) document.getElementById('send-btn').classList.remove("disabled");
		 				else document.getElementById('send-btn').classList.add("disabled");
		 			})
		 			
		 		}
		 		
		 		//step 3: dico quale richiesta fare al server
				req.open("GET", request);
				//step 3.1 : imposto document come response type, perché sto per ricevere html html
				req.responseType = "document";
				
				//step 4: invio la richiesta 
				req.send();
		 	}
		
		 </script>

	
</body>
</html>
