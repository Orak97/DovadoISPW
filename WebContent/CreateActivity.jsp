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
				    <div class="card h-100">
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
	  <div class="col-md-6">
	    <label for="name" class="form-label">Nome attività:</label>
	    <input type="text" class="form-control" id="name" name="activityName">
	  </div>
	  <div class="col-md-6">
	    <label for="kind" class="form-label">Tipo:</label>
	    <select name="type" id="kind" class="form-select">
	    	 <option value="CONTINUA">Continua</option>
	    	 <option value="PERIODICA">Periodica</option>
	    	 <option value="SCADENZA">Scadenza</option>
	    </select>
	  </div>
	  
	  <div class="col-md-6">
	  	<label for="startDate" class="col-form-label">Start Date:</label>
		<input type="date" class="form-control" id="startDate" name="openingDate">
	  </div>
	  <div class="col-md-6">
	  	<label for="closingDate" class="col-form-label">Closing Date:</label>
	  	<input type="date" class="form-control" id="closingDate" name="endDate">
	  </div>
	  
	  <div class="col-md-6">
	  	<label for="openingTime" class="col-form-label">Opening time:</label>
	  	<input type="time" class="form-control" id="openingTime" name="openingTime">
	  </div>
	  <div class="col-md-6">
	  	<label for="closingTime" class="col-form-label">Closing time:</label>
	  	<input type="time" class="form-control" id="closingTime" name="closingTime">
	  </div>
	  
	  
	  <div class="col-12">
	    <button type="submit" class="btn btn-primary">Sign in</button>
	  </div>
	</form> 
	<% } %>
  </div>
</body>
</html>