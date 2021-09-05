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
				        
				        	<button type="button" class="btn btn-success btnHome"data-bs-toggle="modal" data-bs-target="#exampleModal" data-bs-titolo="<%=curr.getName() %>" data-bs-luogo="<%=curr.getPlace().getName()%>">Play Activity</button>
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
	
	<!-- Modal -->
		<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="exampleModalLabel"></h5>
		        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		      </div>
		      <div class="modal-body">
		      <form action="Schedule.jsp" name="myform" method="GET">
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
		 	var exampleModal = document.getElementById('exampleModal')
		 	exampleModal.addEventListener('show.bs.modal', function (event) {
			   // Button that triggered the modal
			   var button = event.relatedTarget
			   // Extract info from data-bs-* attributes
			   var titolo = button.getAttribute('data-bs-titolo')
			   
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
		
		 </script>

	
</body>
</html>
