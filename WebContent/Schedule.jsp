	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
	<%@ page import = "java.io.*,java.util.*, logic.model.Schedule, logic.model.ScheduledActivity, logic.model.User, logic.model.DateBean, logic.controller.AddActivityToScheduleController, logic.model.SuperActivity, logic.model.DAOPreferences, logic.model.DAOActivity, logic.model.DAOSuperUser, logic.model.DAOSchedules" %>

	<% application.setAttribute( "titolo" , "Schedule"); %>

	<%@ include file="Navbar.jsp" %>

  <jsp:useBean id="scheduleBean" scope="request" class="logic.model.ScheduleBean" />

  <jsp:setProperty name="scheduleBean" property="*" />
	<%

		if(request.getParameter("date")!= null){ //controllo la richiesta ricevuta, se all'interno è presente un parametro date vuol dire che arrivo a questa pagina tramite la pressione del bottone save changes, quindi ne consegue che i dati sono pieni e quindi posso andare avanti
		 out.println("printo prop:"+scheduleBean.getScheduledDate());
		}

		/*
		Enumeration paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements()) {
			String paramName = (String)paramNames.nextElement();
			String paramValue = request.getParameter(paramName);
			if(paramValue == "") out.println("zi, is null");
			out.println(paramValue);
		}
		*/

		DAOSchedules schedule = DAOSchedules.getInstance();

		if(schedule.findSchedule(0) == null) out.println("ok");
	%>



	<!-- frame per gli eventi -->
	<div class="container gy-5">



	<div class="row  p-3">
		<div class="col-2">
			<h2> Oggi</h2>
		</div>
	</div>

	<div class= "row p-3">

		<div class="col">
			<div class="row flex-row flex-nowrap row-cols-1 row-cols-md-3 g-4" style="overflow-x: scroll">
			  <div class="col">
			    <div class="card h-100" data-bs-toggle="modal" data-bs-target="#exampleModal" data-bs-titolo="Concerto di Tupac" data-bs-orario="15:30" data-bs-luogo="Roma" data-bs-data="2021-06-27">
			      <img src="https://source.unsplash.com/random" class="card-img-top" alt="...">
			      <div class="card-body">
			        <h5 class="card-title">Concerto tupac</h5>
			        <p class="card-text">Orario: 15:30</p>
			        <p class="card-text">Luogo : Roma </p>
			      </div>
			    </div>
			  </div>
			  <div class="col">
			    <div class="card h-100" data-bs-toggle="modal" data-bs-target="#exampleModal" data-bs-titolo="Giardinaggio con i boys" data-bs-orario="20:30" data-bs-luogo="Lecce" data-bs-data="2021-06-27">
			      <img src="https://source.unsplash.com/random" class="card-img-top" alt="...">
			      <div class="card-body">
			        <h5 class="card-title">Giardinaggio con i boys</h5>
			        <p class="card-text">Orario: 20:30</p>
			        <p class="card-text">Luogo : Lecce </p>
			      </div>
			    </div>
			  </div>
			  <div class="col">
			    <div class="card h-100">
			      <img src="https://source.unsplash.com/random" class="card-img-top" alt="...">
			      <div class="card-body">
			        <h5 class="card-title">Concerto tupac</h5>
			        <p class="card-text">Lorem ipsum</p>
			      </div>
			    </div>
			  </div>
			  <div class="col">
			    <div class="card h-100">
			      <img src="https://source.unsplash.com/random" class="card-img-top" alt="...">
			      <div class="card-body">
			        <h5 class="card-title">Concerto tupac</h5>
			        <p class="card-text">Lorem ipsum</p>
			      </div>
			    </div>
			  </div>

			  <div class="col">
			    <div class="card h-100">
			      <img src="https://source.unsplash.com/random" class="card-img-top" alt="...">
			      <div class="card-body">
			        <h5 class="card-title">Concerto tupac</h5>
			        <p class="card-text">Lorem ipsum</p>
			      </div>
			    </div>
			  </div>

			  <div class="col">
			    <div class="card h-100">
			      <img src="https://source.unsplash.com/random" class="card-img-top" alt="...">
			      <div class="card-body">
			        <h5 class="card-title">Concerto tupac</h5>
			        <p class="card-text">Lorem ipsum</p>
			      </div>
			    </div>
			  </div>
			</div>
		</div>
	</div>

	</div>

	<!-- fine frame -->

<!-- inizio modal -->

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
		        	<p>Non hai impostato nessun promemoria per questo evento:</p>
		        	<button type="button" class="btn btn-primary btn-sm" onclick="addPromemoria()">Impostane uno</button>
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
        <button type="submit" class="btn btn-success"  name="date">Save changes</button>
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
	   var orario = button.getAttribute('data-bs-orario')
	   var luogo = button.getAttribute('data-bs-luogo')
	   var data = button.getAttribute('data-bs-data')

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
	   var modalBodyDate = exampleModal.querySelector('.modal-body #scheduledDate')
	   var modalBodyTime = exampleModal.querySelector('.modal-body #scheduledTime')


	   modalTitle.textContent = titolo
	   modalBodyDate.value = data
	   modalBodyTime.value = orario
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
