<%@page import="java.sql.SQLException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "java.io.*,java.util.*, logic.model.Schedule, logic.model.ScheduledActivity, logic.model.User,  logic.controller.AddActivityToScheduleController, logic.model.SuperActivity, logic.model.DAOPreferences, logic.model.DAOActivity, logic.model.DAOSuperUser, logic.model.DAOSchedules, logic.model.SuperUser, logic.model.User, logic.model.Schedule, logic.model.ScheduledActivity, logic.controller.AddActivityToScheduleController, logic.model.CertifiedActivity" %>

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
			} catch(SQLException se){ 
				Log.getInstance().getLogger().warning("Errore: " + se.getErrorCode() +" - Messaggio: " + se.getMessage());
				
				if(se.getSQLState().equals("45000")){
			    	%> 
					<script> alert('<%= se.getMessage().substring(12)%>')</script>
					<%} 
				else{
					%> 
					<script> alert('Qualcosa è andato storto!')</script>
					<%
					se.printStackTrace();
				}
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
					
					<%
					boolean isCertified = false;
				 	if(curr.getReferencedActivity() instanceof CertifiedActivity) isCertified= true; 
				 	%>
				  		<div class="col">
				    		<div class="card h-100 scheduledActivityCards shadow" data-bs-toggle="modal" data-bs-target="#scheduleModal" data-bs-id="<%= curr.getId() %>" data-bs-titolo="<%= curr.getReferencedActivity().getName() %>" data-bs-orario="<%= curr.getScheduledTime().toLocalTime() %>" data-bs-luogo="<%= curr.getReferencedActivity().getPlace().getCity() %>" data-bs-data="<%= curr.getScheduledTime().toLocalDate() %>" data-bs-description="<%=curr.getReferencedActivity().getDescription()%>" data-bs-orario-reminder="<%= curr.getReminderTime().toLocalTime() %>" data-bs-data-reminder="<%= curr.getReminderTime().toLocalDate() %>" <%if(curr.getCoupon() != null) {%> data-bs-coupon="<%=curr.getCoupon().getCouponCode()%>" data-bs-percentage="<%=curr.getCoupon().getDiscount()%>"<%} %>>
				      			 <% if(isCertified){ %><span class="badge bg-certified text-white position-absolute top-0 end-0 mt-4">Certificata <i class="bi bi-patch-check-fill"></i></span> <%}%>
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
	<div class="modal fade" id="scheduleModal" tabindex="-1" aria-labelledby="scheduleModalLabel" aria-hidden="true">
  		<div class="modal-dialog modal-xl">
    		<div class="modal-content">
    	  		<div class="modal-header">
        			<h5 class="modal-title" id="scheduleModalLabel"></h5>
        			<button type="button" class="btn btn-outline-danger btn-sm btn-remove-activity" id="remove-schedule" data-bs-toggle="modal" data-bs-target="#deleteScheduleModal"> Remove activity <i class="bi bi-trash"></i></button>
        			<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      			</div>
      			<div class="modal-body">
      				<div class="couponContainer" id="couponContainer">
      					<label for="qrcode" class="col-form-label label-activity">Il tuo coupon:</label>
      					<div id="qrcode" class="d-flex justify-content-center"></div>
      					<h3 class="text-center mt-2" id="couponCode"></h3>
				    	
				    	<div class="card border-success mb-3">
						  <div class="card-body text-success">
						    <h5 class="card-title">Mostra questo coupon al gestore dell'attività per ottenere `soldi` <span class="sconto"> e uno sconto del <span class="percentage"></span>%</span></h5>
						  </div>
						</div>
				    
				    </div>
				    <label for="activityDescription" class="col-form-label label-activity">Descrizione:</label>
      				<p id="activityDescription" class="lead"></p>
      				
      				<form action="Schedule.jsp" name="myform" method="GET">
      	  			<input type="text" class="visually-hidden" id="idSchedule" name="idSchedule">
	      			<div class="scheduled-time mb-5">
	      				<div class="row">
			        		<div class="mb-3 col">
			        			<label for="scheduledDate" class="col-form-label label-activity">Data:</label>
			            		<input type="date" class="form-control" id="scheduledDate" name="scheduledDate">
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
		        			<p>Vuoi modificare il promemoria? <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="collapse" href="#reminder-form" aria-expanded="false" aria-controls="reminder-form" id="mostraPromemoria">Si, modifica il promemoria</button></p>	
		        		</div>

				        <div class="reminder-form collapse" id="reminder-form">
				        	<p>Dati del reminder:</p>
				        	<div class="row">
			    		    <div class="mb-3 col">
			        			<label for="scheduledDate" class="col-form-label label-activity">Data:</label>
			       			    <input type="date" class="form-control" id="scheduledReminderDate" name="reminderDate">
			        		</div>

					        <div class="mb-3 col">
					        	<label for="scheduledTime" class="col-form-label label-activity">Orario:</label>
			    		        <input type="time" class="form-control" id="scheduledReminderTime" name="reminderTime">
			    		    </div>
			    		    </div>
		        		</div>
	      			</div>
				</div>
      			<div class="modal-footer">      	
			        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
    			    <button type="submit" class="btn btn-success">Save changes</button>
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
	
	<script type="text/javascript" src="js/qrcode.min.js"></script>
 	<script>
 		var exampleModal = document.getElementById('scheduleModal')
 	
 		document.querySelector('#confirm-remove-schedule').addEventListener('click', deleteSchedule)
 	
 		let qrCodeCoupon = null;
 		
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
	    	var percentage = button.getAttribute('data-bs-percentage')
	    	
	    	var coupon = button.getAttribute('data-bs-coupon')
	    	
	    	let qrCodeContainer = exampleModal.querySelector('#couponContainer')
	    	let qr = exampleModal.querySelector('#qrcode');
	    	
		    if(coupon != null){
		    	qrCodeContainer.classList.remove('visually-hidden');
		    	qrCodeContainer.querySelector('#couponCode').textContent = coupon;
		    	
		    	if(percentage == 0){
		    		qrCodeContainer.querySelector('.sconto').classList.add('visually-hidden');
		    	}else{
		    		qrCodeContainer.querySelector('.sconto').classList.remove('visually-hidden');
		    		qrCodeContainer.querySelector('.percentage').textContent= percentage;
		    	}
		    	
	    		if(qrCodeCoupon == null){
	    			qrCodeCoupon = new QRCode(qr,coupon);
	    		}else{
	    			qrCodeCoupon.clear();
	    			qrCodeCoupon.makeCode(coupon);
	    		}
	    	}else{
	    		qrCodeContainer.classList.add('visually-hidden');
	    	}

		    var orarioReminder = button.getAttribute('data-bs-orario-reminder')
		    var dataReminder = button.getAttribute('data-bs-data-reminder')
	   		
		    let isHidden = exampleModal.querySelector('#reminder-form.show')
			if(!(isHidden == null)) exampleModal.querySelector('#mostraPromemoria').click();
	   		
	   
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

 	
 		function deleteSchedule(){
 			let scheduleToRemove = document.querySelector('#idSchedule').value;
 		
 			window.location.href = "Schedule.jsp?scheduleToRemove="+scheduleToRemove;
 		}

 	</script>



</body>
</html>
