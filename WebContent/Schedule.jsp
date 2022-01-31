<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "java.io.*,java.util.*, logic.model.Schedule, logic.model.ScheduledActivity, logic.model.User, logic.model.DateBean, logic.controller.AddActivityToScheduleController, logic.model.SuperActivity, logic.model.DAOPreferences, logic.model.DAOActivity, logic.model.DAOSuperUser, logic.model.DAOSchedules, logic.model.SuperUser, logic.model.User, logic.model.Schedule, logic.model.ScheduledActivity, logic.controller.AddActivityToScheduleController" %>

	<% application.setAttribute( "titolo" , "Schedule"); %>

	<%@ include file="Navbar.jsp" %>
	
 	<jsp:useBean id="scheduleBean" scope="request" class="logic.model.ScheduleBean" />
	<jsp:setProperty name="scheduleBean" property="*" />
	
	<%
		User u = (User) session.getAttribute("user");
		if(request.getParameter("idSchedule")!= null){ //controllo la richiesta ricevuta, se all'interno è presente un parametro date vuol dire che arrivo a questa pagina tramite la pressione del bottone save changes, quindi ne consegue che i dati sono pieni e quindi posso andare avanti
		 	AddActivityToScheduleController controller = new AddActivityToScheduleController(u,scheduleBean);
		
			try{ 
				controller.modifySchedule();
			}catch(Exception e){
				%> 
				<script> alert('Qualcosa è andato storto!')</script>
				<%
				e.printStackTrace();
			}
		}
		
		if(request.getParameter("scheduleToRemove")!=null){
			AddActivityToScheduleController controller = new AddActivityToScheduleController(u,scheduleBean);
			try{ 
				controller.removeSchedule();
				 response.sendRedirect("Schedule.jsp");
			}catch(Exception e){
				%> 
				<script> alert('Qualcosa è andato storto!')</script>
				<%
				e.printStackTrace();
			}
		
		}

		//Questo controllo è necessario poiché non esegue subito il codice nella navbar, caricando prima questo, e quindi nonostante logicamente dovrebbe prima redirectare verso il login passando nella navbar nella realtà non è così. Infatti prima carica tutta la pagina Jsp con relativa parte Java e poi esegue include e redirect
		if(u != null){
			Schedule s = u.getSchedule();
			ArrayList<ScheduledActivity> activities = (ArrayList<ScheduledActivity>) s.getScheduledActivities();
		
			%>



			<!-- frame per gli eventi -->
			<div class="container schedule pt-6">
				<h1 class="text-center impegni">I tuoi impegni:</h1>
				<div class="row row-cols-1 row-cols-md-3 g-4">
					<% for(ScheduledActivity curr:activities){ %>
				  		<div class="col">
				    		<div class="card h-100 scheduledActivityCards shadow" data-bs-toggle="modal" data-bs-target="#exampleModal" data-bs-id="<%= curr.getId() %>" data-bs-titolo="<%= curr.getReferencedActivity().getName() %>" data-bs-orario="<%= curr.getScheduledTime().toLocalTime() %>" data-bs-luogo="<%= curr.getReferencedActivity().getPlace().getCity() %>" data-bs-data="<%= curr.getScheduledTime().toLocalDate() %>" data-bs-description="<%=curr.getReferencedActivity().getDescription()%>" data-bs-orario-reminder="<%= curr.getReminderTime().toLocalTime() %>" data-bs-data-reminder="<%= curr.getReminderTime().toLocalDate() %>">
				      			<img src="https://source.unsplash.com/random" class="card-img-top" alt="...">
				     			<div class="card-body">
				    				<h5 class="card-title"><%= curr.getReferencedActivity().getName() %></h5>
				     				<p class="card-text">Luogo : <%=curr.getReferencedActivity().getPlace().getName()  %> </p>
				   					<% if(curr.getCoupon()!= null) { %><p> <%= curr.getCoupon().getCouponCode() %></p> <% }%>
				   				</div>
				   	  			<div class="card-footer">
				 	 				<small class="text-muted">Schedulato per: <%=curr.getScheduledFormattedTime() %></small>
					  			</div>
				  			</div>
						</div>
					<% }
		}%>
				
				</div>
			</div>
		
	<!-- fine frame -->

	<!-- inizio modal -->

	<!-- Modal modifica schedulo-->
	<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  		<div class="modal-dialog">
    		<div class="modal-content">
    	  		<div class="modal-header">
        			<h5 class="modal-title" id="exampleModalLabel"></h5>
        			<button type="button" class="btn btn-outline-danger btn-sm btn-remove-activity" id="remove-schedule" data-bs-toggle="modal" data-bs-target="#deleteScheduleModal"> Remove activity <i class="bi bi-trash"></i></button>
        			<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      			</div>
      			<div class="modal-body">
      				<p id="activityDescription"></p>
      				<form action="Schedule.jsp" name="myform" method="GET">
      	  			<input type="text" class="visually-hidden" id="idSchedule" name="idSchedule">
	      			<div class="scheduled-time mb-5">
		        		<div class="mb-3">
		        			<label for="scheduledDate" class="col-form-label">Data:</label>
		            		<input type="date" class="form-control" id="scheduledDate" name="scheduledDate">
		        		</div>

				        <div class="mb-3">
				        	<label for="scheduledTime" class="col-form-label">Orario:</label>
		        		    <input type="time" class="form-control" id="scheduledTime" name="scheduledTime">
		        		</div>
		  			</div>
			
					<hr>
			
		  			<div class="reminder-time">
		        		<div class="mb-3" id="promemoria">
		        			<p>Vuoi modificare quando riceverai il reminder?</p>
		        			<button type="button" class="btn btn-primary btn-sm" onclick="addPromemoria()">Modifica reminder</button>
		        		</div>

				        <div class="reminder-form visually-hidden" id="reminder-form">
				        	<p class="text-center h5">Dati del reminder:</p>
			    		    <div class="mb-3">
			        			<label for="scheduledDate" class="col-form-label">Data:</label>
			       			    <input type="date" class="form-control" id="scheduledReminderDate" name="reminderDate">
			        		</div>

					        <div class="mb-3">
					        	<label for="scheduledTime" class="col-form-label">Orario:</label>
			    		        <input type="time" class="form-control" id="scheduledReminderTime" name="reminderTime">
			    		    </div>
		        		</div>
	      			</div>
				</div>
      			<div class="modal-footer">      	
			        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
    			    <button type="submit" class="btn btn-success"  name="date">Save changes</button>
      			</div>
      				</form>
    		</div>
  		</div>
	</div>

	<!-- fine modal -->

	<!-- Modal elimina -->
	<div class="modal fade" id="deleteScheduleModal" tabindex="-1" aria-labelledby="deleteScheduleModalLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
  		<div class="modal-dialog">
    		<div class="modal-content">
      			<div class="modal-header">
        			<h5 class="modal-title" id="exampleModalLabel">Sei sicuro di voler rimuovere questa attività dallo schedulo?</h5>
        			<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      			</div>
      			<div class="modal-body">
        			<p class="delete-icon-schedule text-center"><i class="bi bi-x-circle-fill"></i></p>
        			<h5 class="text-center irreversible-process">Questo processo è irreversibile</h5>
      			</div>
      			<div class="modal-footer">
        			<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Torna indietro</button>
        			<button type="button" class="btn btn-danger" id="confirm-remove-schedule">Rimuovi</button>
      			</div>
    		</div>
  		</div>
	</div>

 	<script>
 		var exampleModal = document.getElementById('exampleModal')
 	
 		document.querySelector('#confirm-remove-schedule').addEventListener('click', deleteSchedule)
 	
 		exampleModal.addEventListener('show.bs.modal', function (event) {
		    // Button that triggered the modal
	   		var button = event.relatedTarget
	   		// Extract info from data-bs-* attributes
	    	var titolo = button.getAttribute('data-bs-titolo')
	    	var orario = button.getAttribute('data-bs-orario')
	    	var luogo = button.getAttribute('data-bs-luogo')
	    	var data = button.getAttribute('data-bs-data')
 	 	    var idScheduled = button.getAttribute('data-bs-id')
	    	var description = button.getAttribute('data-bs-description')

		    var orarioReminder = button.getAttribute('data-bs-orario-reminder')
		    var dataReminder = button.getAttribute('data-bs-data-reminder')
	   
	   		removePromemoria()
	   
		    // If necessary, you could initiate an AJAX request here
		    // and then do the updating in a callback.
	    	//
	    	// Update the modal's content.
	    	var modalTitle = exampleModal.querySelector('.modal-title')
	    	var modalBodyDate = exampleModal.querySelector('.modal-body #scheduledDate')
	    	var modalBodyTime = exampleModal.querySelector('.modal-body #scheduledTime')
	    	var modalIdSchedule = exampleModal.querySelector('#idSchedule');
	    	var modalDescription = exampleModal.querySelector('#activityDescription');
	    	var modalReminderTime = exampleModal.querySelector('#scheduledReminderTime')
	    	var modalReminderDate = exampleModal.querySelector('#scheduledReminderDate')

	 	    modalIdSchedule.value = idScheduled
		    modalTitle.textContent = titolo
	    	modalBodyDate.value = data
	    	modalBodyTime.value = orario
	    	modalReminderDate.value = dataReminder
	    	modalReminderTime.value = orarioReminder
	    	modalDescription.textContent = description
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
 	
 		function deleteSchedule(){
 			let scheduleToRemove = document.querySelector('#idSchedule').value;
 		
 			window.location.href = "Schedule.jsp?scheduleToRemove="+scheduleToRemove;
 		}

 	</script>



</body>
</html>
