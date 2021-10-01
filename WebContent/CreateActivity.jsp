<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

 <%@ page import = "java.io.*,java.util.*, logic.model.Place, logic.model.DAOPlace, java.time.LocalDate, java.time.format.DateTimeFormatter, java.time.LocalTime, logic.controller.CreateActivityController, logic.model.SuperUser, logic.model.User" %>

    <% application.setAttribute( "titolo" , "Create Activity"); %>

	<%@ include file="Navbar.jsp" %>

  <div class="container pt-6">
	
	<jsp:useBean id="createActivityBean" scope="request" class="logic.model.CreateActivityBean" />
	<jsp:setProperty name="createActivityBean" property="*" />
	
	<%
		if(request.getParameter("openingDate")!= null){
			SuperUser u = (User) session.getAttribute("user");
			CreateActivityController c = new CreateActivityController(u,createActivityBean);
			ArrayList<Place> places = (ArrayList<Place>)DAOPlace.getInstance().findPlacesByCity("Roma");
			c.createActivity("ciao", places.get(0));
			out.println("fatto");
		
		}
	
	%>
	
	<% if(request.getParameter("comune")== null){ %>
		<div class="row g-3">
			<form action="CreateActivity.jsp" method="GET">
				<div class="mb-3">
				    <label for="Comune" class="form-label">inserisci il comune in cui vuoi creare l'attività</label>
				    <input type="text" class="form-control" id="Comune" name="comune">
				</div>
				<button type="submit" class="btn btn-primary">Cerca posti</button>
			</form>
		</div>
	 <% } else {
		String comune = request.getParameter("comune");
		ArrayList<Place> places = (ArrayList<Place>)DAOPlace.getInstance().findPlacesByCity(comune);
		%>
		
		<div class= "row p-3">
		<div class ="row"> <h2>Posti trovati per <%= request.getParameter("comune") %>:</h2> </div>
		<div class="col">
				<div class="row flex-row flex-nowrap row-cols-1 row-cols-md-3 g-4" style="overflow-x: scroll">
		
		
				<% for(Place p: places){ %>
		    
				  <div class="col">
				    <div class="card card-dark text-white h-100" onclick="setPlace(this,<%= p.getId() %>)">
				      <img src="https://source.unsplash.com/random" class="card-img-top" alt="...">
				      <div class="card-body">
				        <h5 class="card-title"><%= p.getName() %></h5>
				        <p class="card-text">
				        	
				        	Città: <%=p.getCity() %> <br />
				        	Regione: <%=p.getRegion() %> <br />
				        	Indirizzo: <%=p.getAddress()+" "+p.getCivico() %> <br />
				        	
				        	
				        </p>
				      </div>
				    </div>
				  </div>
				  <% } %>
				
				</div>
			</div>
			
			<div class="row p-2"> <p>Non hai trovato il luogo che cerchi? <button type="button" class="btn btn-primary btn-sm">Creane uno tu!</button> </p> </div>
		</div>
	    
	    
	<!-- da qui inizia il vecchi form -->
	<form class="row g-3" name="createActivityForm" action="CreateActivity.jsp" method="GET">
	  
	  <div class="mb-3 visually-hidden">
		<input type="number" class="form-control" id="place" name="place">
	  </div>
	  
	  <div class="col-md-6">
	    <label for="name" class="form-label">Nome attività:</label>
	    <input type="text" class="form-control" id="name" name="activityName">
	  </div>
	  <div class="col-md-6" id="kindDiv">
	  
	    <label for="kind" class="form-label">Tipo:</label>
	    <select name="type" id="kind" class="form-select" onchange="changedKind(this.value)">
	    	 <option value="CONTINUA">Continua</option>
	    	 <option value="PERIODICA">Periodica</option>
	    	 <option value="SCADENZA">Scadenza</option>
	    </select>
	  </div>
	  
	  <div class="col-md-3 visually-hidden" id="cadenceDiv">
	    <label for="cadence" class="form-label">Cadenza:</label>
	    <select name="cadence" id="cadence" class="form-select" disabled>
	    	 <option value="WEEKLY">Settimanale</option>
	    	 <option value="MONTHLY">Mensile</option>
	    	 <option value="ANNUALLY">Annuale</option>
	    </select>
	  </div>
	  
	  <div class="col-md-6 visually-hidden" id="openingDateDiv">
	  	<label for="startDate" class="col-form-label">Start Date:</label>
		<input type="date" class="form-control" id="startDate" name="openingDate" disabled>
	  </div>
	  <div class="col-md-6 visually-hidden" id="closingDateDiv">
	  	<label for="closingDate" class="col-form-label">Closing Date:</label>
	  	<input type="date" class="form-control" id="closingDate" name="endDate" disabled>
	  </div>
	  
	  <div class="col-md-6" id="openingTimeDiv">
	  	<label for="openingTime" class="col-form-label">Opening time:</label>
	  	<input type="time" class="form-control" id="openingTime" name="openingTime">
	  </div>
	  <div class="col-md-6" id="closingTimeDiv">
	  	<label for="closingTime" class="col-form-label">Closing time:</label>
	  	<input type="time" class="form-control" id="closingTime" name="closingTime">
	  </div>
	  
	  
	  <div class="col-12">
	    <button type="submit" class="btn btn-primary">Sign in</button>
	  </div>
	</form> 
	<% } %>
  </div>
  
  <script>
  	function setPlace(event,p){
  		document.getElementById('place').value=p;
  		
  		let oldPressed = document.getElementsByClassName('card-dark-pressed');
  		
  		if((oldPressed[0]) != undefined){
  			oldPressed[0].classList.add('card-dark');
  			oldPressed[0].classList.remove('card-dark-pressed');	
  		}
  		
  		event.classList.remove('card-dark');
  		event.classList.add('card-dark-pressed');
  	}
  	
  	function changedKind(value){
  		switch(value){
  		case 'CONTINUA':
  			console.log('continua');
  			document.getElementById('openingDateDiv').classList.add('visually-hidden');
  			document.getElementById('closingDateDiv').classList.add('visually-hidden');
  			
  			document.getElementById('startDate').disabled = true;
  			document.getElementById('closingDate').disabled = true;
  			
  				
  			document.getElementById('cadenceDiv').classList.add('visually-hidden');
  			document.getElementById('cadence').disabled = true;
  			
  			document.getElementById('kindDiv').classList.add('col-md-6');
  			document.getElementById('kindDiv').classList.remove('col-md-3');
  			break;
  		case 'PERIODICA':
  			console.log('periodica');
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
  			break;
  		case 'SCADENZA':
  			console.log('scadenza');
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
  			break;
  		default:
  			console.log('hai proprio fuccato up il codice bro');
  			break;
  		}
  	}
  </script>
</body>
</html>