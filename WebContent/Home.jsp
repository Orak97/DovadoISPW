<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 

    <%@ page import = "java.io.*,java.util.*, logic.model.DAOPreferences, logic.model.DAOActivity, logic.model.DAOSuperUser, logic.model.SuperActivity" %>

    <% application.setAttribute( "titolo" , "Home"); %>

	<%@ include file="Navbar.jsp" %>
	
	

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
		<div class="row pt-6">
		
			<div class="col-4 events-list">
			<div class="row row-cols-1 row-cols-md-1 g-1">
			  <% for(SuperActivity curr:activities){ %>
			  
			  <div class="col" >
			    <div class="card" data-bs-toggle="collapse" href="#collapse<%= curr.getId() %>" aria-expanded="false" aria-controls="collapse<%= curr.getId() %>">
			      <img src="https://source.unsplash.com/random" class="card-img-top" alt="...">
			      <div class="card-body">
			        <h5 class="card-title"><% out.println(curr.getName()); %></h5>
			        <p class="card-text">Orario Apertura: <%= curr.getFrequency().getOpeningTime()%> <br />Orario Chiusura: <%= curr.getFrequency().getClosingTime()%></p>
			        <p class="card-text">Luogo : <%= curr.getPlace().getName() %> </p>
			      </div>
			    </div>
			    <div class="collapse" id="collapse<%= curr.getId() %>">
				    <div class="d-grid gap-2 activityButtonGroup">
				        	<button type="button" class="btn btn-dark btnHome">Join Channel</button>
				        
				        	<button type="button" class="btn btn-dark btnHome" onclick="document.getElementById('map').contentWindow.spotPlace('<%=curr.getPlace().getCivico()%>','<%=curr.getPlace().getAddress()%>','<%=curr.getPlace().getCity()%>','<%=curr.getPlace().getRegion()%>');">View on map</button>
				        
				        	<button type="button" class="btn btn-success btnHome">Play Activity</button>
				    </div>
			    </div>
			  </div>
			  
			  <% }%>
			  
			</div>
			</div>
			
			<div class="col-8" style="overflow-y:hidden">
				<iframe src="map.html" title="maps" id="map" style="width:100%; height:100%"></iframe> 
			</div>
		</div>
	
	
	</div>

	
</body>
</html>
