<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import = "java.io.*,java.util.*, logic.model.Partner, logic.model.CertifiedActivity, logic.model.DAOActivity, logic.controller.FindActivityController, logic.model.Activity" %>

<% application.setAttribute( "titolo" , "Create Activity"); %>

<%@ include file="NavbarPartner.jsp" %>

<jsp:useBean id="findBean" scope="request" class="logic.model.FindActivitiesBean" />
<jsp:setProperty name="findBean" property="*" />

<%	
	ArrayList<Activity> foundActivities = new ArrayList<Activity>();
	boolean isSearch = false;
	if(request.getParameter("zone")!= null){
		FindActivityController controller = new FindActivityController(findBean,null);	
		try{
			foundActivities = controller.FindActivitiesByZone();
			isSearch = true;
		}catch(NullPointerException e){
			%> <script> alert('Inserisci correttamente i campi zona!') </script> <%
			e.printStackTrace();
		}catch(Exception e){
			%> <script> alert('qualcosa è andato storto, riprova più tardi') </script> <%
			e.printStackTrace();
		}
	}
	
%>

<div class="container pt-6">


</div>

<!-- Modal -->
		<div class="modal fade" id="activityModal" tabindex="-1" aria-labelledby="activityModalLabel" aria-hidden="true">
		  <div class="modal-dialog modal-xl">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="activityModalLabel"></h5>
		        <button type="button" class="btn-close" data-bs-target="#findActivitiesModal" data-bs-toggle="modal" data-bs-dismiss="modal" aria-label="Close"></button>
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
		      



		      </div>
		      <div class="modal-footer">
  			    <form method="GET" action="CreateActivityPartner.jsp">
	      	  		<input type="number" id="idActivity" name="idActivity" class="visually-hidden">
		        	<button type="button" class="btn btn-secondary" data-bs-target="#findActivitiesModal" data-bs-toggle="modal" data-bs-dismiss="modal">Close</button>
		        	<button type="button" class="btn btn-warning"  id="contact-support" onclick="alert('funzione non disponibile, verrà aggiunta nella prima release')">Contatta l'assistenza</button>
		        	<button type="submit" class="btn btn-success"  id="redeem-activity">Riscatta l'attività</button>
		      	</form>
		      </div>
		    </div>
		  </div>
		</div>
		
<!-- fine modal -->



<!-- Modal -->

<form method="GET" action="CreateActivityPartner.jsp">
<div class="modal fade" id="findActivitiesModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="findActivitiesModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-scrollable modal-xl">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="findActivitiesModalLabel">Dove si trova la tua attività?</h5>
        <%-- <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button> --%>
      </div>
      
      <div class="modal-body modal-coupon">
      	<%-- roba che fa vedere il saldo disponibile --%>
      	<div class="row text-center sticky-top saldo shadow-sm">
      		<input type="text" class="form-control search-input" id="SearchActivities" name="zone" placeholder="nome attività" list="places-available" <%= isSearch ? "value="+findBean.getZone() : "" %>>
      	</div>
      	<%if(isSearch){ %> 
      		<h2 class="text-center pt-3">Ecco le attività trovate:</h2>
				<div class="row row-cols-1 row-cols-md-3 g-4">
				  <% for(Activity curr:foundActivities){ %>
				  
				  <%
				  	boolean isCertified = false;
				 	if(curr instanceof CertifiedActivity) isCertified= true;
				  %>
				  <div class="col">
				    <div class="card h-100 scheduledActivityCards shadow" data-bs-toggle="modal" data-bs-target="#activityModal" data-bs-titolo="<%=curr.getName() %>" data-bs-luogo="<%=curr.getPlace().getName()%>" data-bs-id="<%=curr.getId() %>" data-bs-description="<%= curr.getDescription() %>" data-bs-playabilityInfo="<%= curr.getPlayabilityInfo()%>" data-bs-address="<%= curr.getPlace().getFormattedAddr()%>" <%if(isCertified){%> data-bs-certified="true" <%}%> data-bs-dismiss="modal">
				      <% if(isCertified){ %><span class="badge bg-certified text-white position-absolute top-0 end-0 mt-4">Certificata <i class="bi bi-patch-check-fill"></i></span> <%}%>
				      <img src="https://source.unsplash.com/random" class="card-img-top" alt="...">
				      <div class="card-body">
				        <h5 class="card-title"><%= curr.getName() %></h5>
				        <p class="card-text">Luogo : <%=curr.getPlace().getName()  %> </p>
				      </div>
				    </div>
				  </div>
				  <% } %>
			</div>
      	
      	<%} %>
		      
      </div>
      <div class="modal-footer">
        <% if(isSearch){ %><button type="button" class="btn btn-secondary" data-bs-dismiss="modal">La mia attività non è presente nel sistema</button> <%}%>
        <button type="submit" class="btn btn-primary" id="findActivities" <%= isSearch ? "": "disabled" %>><%= isSearch ? "Effettua una nuova ricerca" : "Cerca Attività" %></button>
      </div>
    </div>
  </div>
</div>
</form>

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
   let data = button.getAttribute('data-bs-date')
	
   
   // If necessary, you could initiate an AJAX request here
   // and then do the updating in a callback.
   //
   // Update the modal's content.
   var modalTitle = exampleModal.querySelector('.modal-title')
   var modalID = exampleModal.querySelector('.modal-footer #idActivity')
   var modalDescription = exampleModal.querySelector('#activityDescription')
   let modalPlayabilityInfo = exampleModal.querySelector('#playabilityInfo')
   let modalPlace = exampleModal.querySelector('#placename')
   let modalAddress = exampleModal.querySelector('#activityaddress')
   
   
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
   let btnPlayActivity = exampleModal.querySelector('#redeem-activity');
   
   //bottone per generare coupon
   let btnGenerateCoupon = exampleModal.querySelector('#contact-support');
   
   if(isCertified != null){
	   //l'attività è certificata
	   
	   //disabilito il schedule activity e lo nascondo
	   btnPlayActivity.disabled = true;
	   btnPlayActivity.classList.add('visually-hidden');
	   
	   //attivo e faccio comparire il generate coupon
	   btnGenerateCoupon.disabled = false;
	   btnGenerateCoupon.classList.remove('visually-hidden');
	   
   	   
   }else{
	   //l'attività non è certificata
	   
	   //attivo il schdule activity e lo faccio comparire
	   btnPlayActivity.disabled = false;
	   btnPlayActivity.classList.remove('visually-hidden');
   	   
	   //disattivo il generate coupon e lo nascondo
	   btnGenerateCoupon.disabled = true;
	   btnGenerateCoupon.classList.add('visually-hidden');
 
   
   }
})

	let findActModal = document.getElementById('findActivitiesModal')
	let searchActivityModal = new bootstrap.Modal(findActModal)
	try{
		searchActivityModal.show();
	}catch(e){
		console.log('Reloaded page')
	}
	let searchFieldAct = findActModal.querySelector('#SearchActivities')
	searchFieldAct.addEventListener('keyup', (event)=>{
		if(event.target.value == '') findActModal.querySelector('#findActivities').disabled =true;
		else findActModal.querySelector('#findActivities').disabled =false;
	})
	
</script>

</body>
</html>