<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    
<%@ page import = "java.io.*,java.util.*, logic.model.User, logic.model.Preferences, logic.controller.FindActivityController, logic.model.Activity, logic.model.CertifiedActivity, java.time.LocalDate" %>
 
<% application.setAttribute( "titolo" , "Find Activities"); %>

<%@ include file="Navbar.jsp" %>

<jsp:useBean id="findBean" scope="request" class="logic.model.FindActivitiesBean" />
<jsp:setProperty name="findBean" property="*" />

<jsp:useBean id="preferenceBean" scope="request" class="logic.model.PreferenceBean" />
<jsp:setProperty name="preferenceBean" property="*" />



<div class="container pt-6">

<%	
	boolean displayForm = true;
	if(request.getParameter("zone") != null){
		FindActivityController controller = new FindActivityController(findBean,preferenceBean);
		ArrayList<Activity> foundActivities = new ArrayList<Activity>();
		try{
		 foundActivities = controller.FindActivities();
		}catch(NullPointerException e){
			%> <script> alert('Inserisci correttamente i campi zona e data!') </script> <%
			e.printStackTrace();
		}catch(IllegalArgumentException e){
			%> <script> alert('La data in cui vuoi fare le attività deve essere oggi o i giorni successivi!') </script> <%
			e.printStackTrace();
		}catch(Exception e){
			%> <script> alert('qualcosa è andato storto, riprova più tardi') </script> <%
			e.printStackTrace();
		}
		
		if(foundActivities.size()<=0){
			 %> <script> alert('non è stata trovata nessuna attività con questi parametri, prova con un\'altra ricerca') </script> <%
		}else{
			displayForm = false;
		
	
			%> 
			
			<h2 class="text-center">Ecco le attività trovate:</h2>
			<div class="activities-found">
				<div class="row row-cols-1 row-cols-md-3 g-4">
				  <% for(Activity curr:foundActivities){ %>
				  
				  <%
				  	boolean isCertified = false;
				 	if(curr instanceof CertifiedActivity) isCertified= true;
				  %>
				  <div class="col">
				    <div class="card h-100 scheduledActivityCards shadow" data-bs-date="<%=findBean.getDate()%>" data-bs-toggle="modal" data-bs-target="#activityModal" data-bs-titolo="<%=curr.getName() %>" data-bs-luogo="<%=curr.getPlace().getName()%>" data-bs-id="<%=curr.getId() %>" data-bs-description="<%= curr.getDescription() %>" data-bs-playabilityInfo="<%= curr.getPlayabilityInfo()%>" data-bs-address="<%= curr.getPlace().getFormattedAddr()%>" <%if(isCertified){%> data-bs-certified="true" <%}%>>
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
			
			</div>
			
			<%
	
		}
	}
	
	
	if(displayForm){

%>

	<h2 class="text-center">Trova attività di tuo interesse tramite la ricerca avanzata:</h2>
	<form method="GET" action="FindActivities.jsp">
	<div class="select-zone">
		<h4 class="text-center"><i class="bi bi-pin-map"></i> Dove vuoi cercare l'attività? <i class="bi bi-pin-map"></i></h4>
		<hr class="separator-places">
		<div class="d-flex justify-content-center pt-1">
			<input type="text" class="form-control search-input" id="zone" name="zone" placeholder="Città,regione,cap...">
			<button type="button" class="btn btn-success search-btn" id="go-to-date" disabled>Avanti</button>
		</div>
	</div>
	
	<div class="select-date visually-hidden">
		<h4 class="text-center"><i class="bi bi-calendar-date"></i> Quando vuoi fare l'attività? <i class="bi bi-calendar-date"></i></h4>
		<hr class="separator-places">
		<div class="d-flex justify-content-center pt-1">
			<input type="date" class="form-control search-input" id="datePicker" name="date">
		</div>
		<div class="d-flex justify-content-center pt-3">
			<button type="button" class="btn btn-danger btn-find-activities" id="back-to-zone">Indietro</button>
			<button type="button" class="btn btn-success btn-find-activities" id="go-to-pref">Avanti</button>	
		</div>
		
	
	</div>
	
	<% 
	
	User utente = (User) session.getAttribute("user");	
	if(utente !=null){
		
	Preferences pref = utente.getPreferences();
	%>
	<div class ="select-preferences visually-hidden">
		<h4 class="text-center"><i class="bi bi-tags"></i> A quale categoria sei interessato? <i class="bi bi-tags"></i></h4>
		<hr class="separator-places">
		<div class="row">
			<div class="col">
				<div class="form-check form-switch">
				  	 <input class="form-check-input" type="checkbox" id="Arte" name="arte" <% if(pref.isArte()) out.println("checked"); %>>
			  		 <label class="form-check-label" for="Arte">Arte</label>
				  </div>
				  
				  <div class="form-check form-switch">
				  	 <input class="form-check-input" type="checkbox" id="Cibo" name="cibo" <% if(pref.isCibo()) out.println("checked"); %>>
			  		 <label class="form-check-label" for="Cibo">Cibo</label>
				  </div>
				  
				  <div class="form-check form-switch">		
				  	 <input class="form-check-input" type="checkbox" id="Musica" name="musica" <% if(pref.isMusica()) out.println("checked"); %>>
			  		 <label class="form-check-label" for="Musica">Musica</label>
				  </div>
				  
				  <div class="form-check form-switch">	 
				  	 <input class="form-check-input" type="checkbox" id="Sport" name="sport" <% if(pref.isSport()) out.println("checked"); %>>
			  		 <label class="form-check-label" for="Sport">Sport</label>
				  </div>
				  
				  <div class="form-check form-switch">	 
				  	 <input class="form-check-input" type="checkbox" id="Social" name="social" <% if(pref.isSocial()) out.println("checked"); %>>
			  		 <label class="form-check-label" for="Social">Social</label>
				  </div>
				  
				  <div class="form-check form-switch">	 
				  	 <input class="form-check-input" type="checkbox" id="Natura" name="natura" <% if(pref.isNatura()) out.println("checked"); %>>
			  		 <label class="form-check-label" for="Natura">Natura</label>
				  </div>
				  
				  <div class="form-check form-switch">	 
				  	 <input class="form-check-input" type="checkbox" id="Esplorazione" name="esplorazione" <% if(pref.isEsplorazione()) out.println("checked"); %>>
			  		 <label class="form-check-label" for="Esplorazione">Esplorazione</label>
				  </div>
				
			</div>
			<div class="col">
				<div class="form-check form-switch">
					<input class="form-check-input" type="checkbox" id="Ricorrenze" name="ricorrenze" <% if(pref.isRicorrenzeLocali()) out.println("checked"); %>>
					<label class="form-check-label" for="Ricorrenze">Ricorrenze Locali</label>
			  	</div>
			  	
			  	<div class="form-check form-switch">
			  		<input class="form-check-input" type="checkbox" id="Moda" name="moda" <% if(pref.isModa()) out.println("checked"); %>>
		  			<label class="form-check-label" for="Mode">Moda</label>
			  	</div>
			  	
			  	<div class="form-check form-switch">		
			  		<input class="form-check-input" type="checkbox" id="Shopping" name="shopping" <% if(pref.isShopping()) out.println("checked"); %>>
		  		 	<label class="form-check-label" for="Shopping">Shopping</label>
			  	</div>
			  	
			  	<div class="form-check form-switch">	 
			  	 	<input class="form-check-input" type="checkbox" id="Adrenalina" name="adrenalina" <% if(pref.isAdrenalina()) out.println("checked"); %>>
		  		 	<label class="form-check-label" for="Adrenalina">Adrenalina</label>
			  	</div>
			  	
			   	<div class="form-check form-switch">	 
			  	 	<input class="form-check-input" type="checkbox" id="Relax" name="relax" <% if(pref.isRelax()) out.println("checked"); %>>
		  			<label class="form-check-label" for="Relax">Relax</label>
			  	</div>
			  	
			  	<div class="form-check form-switch">	 
			  	 	<input class="form-check-input" type="checkbox" id="Istruzione" name="istruzione" <% if(pref.isIstruzione()) out.println("checked"); %>>
		  		 	<label class="form-check-label" for="Istruzione">Istruzione</label>
			  	</div>
			  	
			  	<div class="form-check form-switch">	 
			  	 	<input class="form-check-input" type="checkbox" id="Monumenti" name="monumenti" <% if(pref.isMonumenti()) out.println("checked"); %>>
		  		 	<label class="form-check-label" for="Monumenti">Monumenti</label>
			  	</div>
			  	
			  	<div class="d-flex justify-content-end">  
				  <button type="button" class="btn btn-secondary btn-sm btn-find-activities" id="remove-all-pref">Rimuovi tutto</button>				
				  <button type="button" class="btn btn-primary btn-sm btn-find-activities" id="select-all-pref">Seleziona tutto</button>
				</div>
			</div>
		</div>
		
		<div class="d-flex justify-content-center pt-3">
			<button type="button" class="btn btn-danger btn-find-activities" id="back-to-date">Indietro</button>
			<button type="button" class="btn btn-success btn-find-activities" id="go-to-keywords">Avanti</button>	
		</div>
		
	</div>
	<div class="select-keywords visually-hidden">
		<h4 class="text-center"><i class="bi bi-search"></i> Vuoi effettuare la ricerca anche per parole chiave? <i class="bi bi-search"></i></h4>
		<hr class="separator-places">
			
	<div class="d-flex justify-content-center pt-1" id="researchBox">
			<label for="keywordsField" class="form-label">Non scrivere niente se non vuoi cercare per parole chiave:</label>
			<input type="text" class="form-control" name="keywords" id="keywordsField" placeholder="L'attività deve contenere le parole...">
	</div>
	
	<div class="d-flex justify-content-center pt-3">
		<button type="button" class="btn btn-danger btn-find-activities" id="back-to-preferences">Indietro</button>
		<button type="submit" class="btn btn-success btn-find-activities" id="findActivities" disabled>Trovami le attività!</button>	
	</div>
		
	
	</div>
	</form>
	
	<script>
	
	//---------------------sezione zona------------------------------------------------
	const zoneSearchField = document.querySelector('#zone');
	zoneSearchField.addEventListener('keyup',disableIfEmpty);
	zoneSearchField.button = '#go-to-date'
	
	function disableIfEmpty(event){
		let field = event.target
		let btn = document.querySelector(field.button)
		if(field.value == '') btn.disabled = true
		else btn.disabled = false
	}
	
	const goToDate = document.querySelector('#go-to-date')
	goToDate.addEventListener('click', ()=>{
		document.querySelector('.select-zone').classList.add('visually-hidden');
		document.querySelector('.select-date').classList.remove('visually-hidden');
	});
	//------------------------------sezione data----------------------------------
	
	Date.prototype.toDateInputValue = (function() {
	    var local = new Date(this);
	    local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
	    return local.toJSON().slice(0,10);
	});
	document.querySelector('#datePicker').value = new Date().toDateInputValue();
	document.querySelector('#datePicker').min = new Date().toDateInputValue();
	
	const backToZone= document.querySelector('#back-to-zone')
	backToZone.addEventListener('click', ()=>{
		document.querySelector('.select-zone').classList.remove('visually-hidden');
		document.querySelector('.select-date').classList.add('visually-hidden');
	});
	
	const goToPref = document.querySelector('#go-to-pref')
	goToPref.addEventListener('click', ()=>{
		document.querySelector('.select-date').classList.add('visually-hidden');
		document.querySelector('.select-preferences').classList.remove('visually-hidden');
	});
	
	//-----------------------------sezione preferenze-----------------------------
	const selectAllPref = document.querySelector('#select-all-pref');
	selectAllPref.addEventListener('click', () =>{
		let checkBoxes = document.querySelectorAll('.form-check-input');
		checkBoxes.forEach( checkbox => checkbox.checked = true);
	});
	
	const deselectAllPref = document.querySelector('#remove-all-pref');
	deselectAllPref.addEventListener('click', () =>{
		let checkBoxes = document.querySelectorAll('.form-check-input');
		checkBoxes.forEach( checkbox => checkbox.checked = false);
	});
	
	const backToDate = document.querySelector('#back-to-date')
	backToDate.addEventListener('click', ()=>{
		document.querySelector('.select-preferences').classList.add('visually-hidden');
		document.querySelector('.select-date').classList.remove('visually-hidden');
	});
	
	const goToKeywords = document.querySelector('#go-to-keywords')
	goToKeywords.addEventListener('click', ()=>{
		document.querySelector('.select-preferences').classList.add('visually-hidden');
		document.querySelector('.select-keywords').classList.remove('visually-hidden');
		document.querySelector('#findActivities').disabled = false;
	});
	
	
	
	//----------------------------------sezione keywords--------------------------
	
	const backToPreferences = document.querySelector('#back-to-preferences')
	backToPreferences.addEventListener('click', ()=>{
		document.querySelector('.select-preferences').classList.remove('visually-hidden');
		document.querySelector('.select-keywords').classList.add('visually-hidden');
		document.querySelector('#findActivities').disabled = true;
	});
	
	</script>
	
	
	<% 	}
	} %>
</div>

<!-- Modal -->
		<div class="modal fade" id="activityModal" tabindex="-1" aria-labelledby="activityModalLabel" aria-hidden="true">
		  <div class="modal-dialog modal-xl">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="activityModalLabel"></h5>
		        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
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
		      <hr>
		      <form action="Home.jsp" name="myform" method="GET">
		      	  <input type="number" id="idActivity" name="idActivity" class="visually-hidden">
		      	  <input type="number" id="selectedCoupon" name="selectedCoupon" class="visually-hidden">
			      <p>Quando vorresti fare questa attività?</p>
			      <div class="scheduled-time">
			      	<div class="row">
				        <div class="mb-3 col">
				        	<label for="scheduledDate" class="col-form-label label-activity">Data:</label>
				            <input type="date" class="form-control" id="scheduledDate" name="scheduledDate" min="<%= LocalDate.now()%>">
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
				        	<p>Vuoi ricevere un promemoria? <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="collapse" href="#reminder-form" aria-expanded="false" aria-controls="reminder-form" id="mostraPromemoria">Si, imposta un promemoria</button></p>
				        </div>
		
				        <div class="reminder-form collapse" id="reminder-form">
				        	<p>Dati del reminder:</p>
				        	<div class="row">
					        <div class="mb-3 col">
					        	<label for="scheduledDate" class="col-form-label label-activity">Data:</label>
					            <input type="date" class="form-control" id="reminderDate" name="reminderDate" min="<%= LocalDate.now()%>">
					        </div>
		
					        <div class="mb-3 col">
					        	<label for="scheduledTime" class="col-form-label label-activity">Orario:</label>
					            <input type="time" class="form-control" id="reminderTime" name="reminderTime">
					        </div>
					        </div>
				        </div>
			      </div>
		
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
		        <button type="button" class="btn btn-success"  id="generateCouponButton" data-bs-target="#couponModal" data-bs-toggle="modal" data-bs-dismiss="modal">Generate Coupon</button>
		        <button type="submit" class="btn btn-success"  id="playActivityButton">Schedule Activity</button>
		      </div>
		      </form>
		    </div>
		  </div>
		</div>
		
		<!-- fine modal -->
		
		<!-- inizio modal per Coupon -->
		<!-- Modal -->
		<div class="modal fade" id="couponModal" tabindex="-1" aria-labelledby="couponModalLabel" aria-hidden="true">
		  <div class="modal-dialog modal-xl modal-dialog-scrollable">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="couponModalLabel">Vuoi generare un codice sconto per questa attività?</h5>
		        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		      </div>
		      <div class="modal-body modal-coupon">
		      
		      <%-- roba che fa vedere il saldo disponibile --%>
		      <div class="row text-center sticky-top saldo shadow-sm"><p class="lead">Soldi disponibili: <span id="saldo-disponibile"></span></p></div>
		      
		      <%-- come funziona --%>
		      
		      <div class="accordion" id="infoCoupon">
				  <div class="accordion-item">
				    <h2 class="accordion-header" id="come-funziona">
				      <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#spiegazione" aria-expanded="true" aria-controls="spiegazione">
				        Come funzionano i coupon?
				      </button>
				    </h2>
				    <div id="spiegazione" class="accordion-collapse collapse show" aria-labelledby="come-funziona" data-bs-parent="#infoCoupon">
				      <div class="accordion-body">
				        <strong>This is the first item's accordion body.</strong> It is shown by default, until the collapse plugin adds the appropriate classes that we use to style each element. These classes control the overall appearance, as well as the showing and hiding via CSS transitions. You can modify any of this with custom CSS or overriding our default variables. It's also worth noting that just about any HTML can go within the <code>.accordion-body</code>, though the transition does limit overflow.
				      </div>
				    </div>
				  </div>
				</div>
		      
		      
		      <%--inizio righe dei coupon --%>
		       <div class="row coupon shadow">
		       	<div class="col percentage-info text-center">
		       		<div class="position-relative top-50 start-50 translate-middle">
		       			<label for="percentage" class="col-form-label label-sale">Sconto del</label>
		       			<p id="percentage"><span class="percentage">40%</span></p>
		       		</div>
		       	</div>
		       	<div class="col percentage-redeem text-center">
		       		<div class="position-relative top-50 start-50 translate-middle">
		       			<button type="button" class="btn btn-outline-success btn-lg btnHome">Genera Coupon per 10 soldi</button>
		       		</div>
		       	</div>
		       </div>
		       
		       <div class="row coupon shadow">
		       	<div class="col percentage-info text-center disabled">
		       		<div class="position-relative top-50 start-50 translate-middle">
		       			<label for="percentage" class="col-form-label label-sale">Sconto del</label>
		       			<p id="percentage"><strong>60%</strong></p>
		       		</div>
		       	</div>
		       	<div class="col percentage-redeem text-center">
		       		<div class="position-relative top-50 start-50 translate-middle">
		       			<p class="lead insufficient-funds">Soldi insufficienti per generare questo coupon</p>
		       			<button type="button" class="btn btn-outline-success btn-lg btnHome" disabled>Genera Coupon per 100 soldi</button>
		       		</div>
		       	</div>
		       </div>
		       
		       <div class="row coupon shadow">
		       	<div class="col percentage-info text-center disabled">
		       		<div class="position-relative top-50 start-50 translate-middle">
		       			<label for="percentage" class="col-form-label label-sale">Sconto del</label>
		       			<p id="percentage"><strong>80%</strong></p>
		       		</div>
		       	</div>
		       	<div class="col percentage-redeem text-center">
		       		<div class="position-relative top-50 start-50 translate-middle">
		       			<button type="button" class="btn btn-outline-success btn-lg btnHome" disabled>Genera Coupon per 300 soldi</button>
		       		</div>
		       	</div>
		       </div>
		       
		       <div class="row coupon shadow">
		       	<div class="col percentage-info text-center disabled">
		       		<div class="position-relative top-50 start-50 translate-middle">
		       			<label for="percentage" class="col-form-label label-sale">Sconto del</label>
		       			<p id="percentage"><strong>100%</strong></p>
		       		</div>
		       	</div>
		       	<div class="col percentage-redeem text-center">
		       		<div class="position-relative top-50 start-50 translate-middle">
		       			<button type="button" class="btn btn-outline-success btn-lg btnHome" disabled>Genera Coupon per 600 soldi</button>
		       		</div>
		       	</div>
		       </div>
		       
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
		        <button type="button" class="btn btn-success" onclick="generateDiscount(0)">Non desidero generare nessun coupon</button>
		      </div>
		    </div>
		  </div>
		</div>
				
		
		
		<!-- fine modal per coupon -->



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
		
       let isHidden = exampleModal.querySelector('#reminder-form.show')
	   if(!(isHidden == null)) exampleModal.querySelector('#mostraPromemoria').click();
	   
	   // If necessary, you could initiate an AJAX request here
	   // and then do the updating in a callback.
	   //
	   // Update the modal's content.
	   var modalTitle = exampleModal.querySelector('.modal-title')
	   var modalID = exampleModal.querySelector('.modal-body #idActivity')
	   var modalDescription = exampleModal.querySelector('#activityDescription')
	   let modalPlayabilityInfo = exampleModal.querySelector('#playabilityInfo')
	   let modalPlace = exampleModal.querySelector('#placename')
	   let modalAddress = exampleModal.querySelector('#activityaddress')
	   
	   exampleModal.querySelector('#scheduledDate').value=data;
	   exampleModal.querySelector('#scheduledTime').value='';
	   exampleModal.querySelector('#reminderDate').value='';
	   exampleModal.querySelector('#reminderTime').value='';
	   
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
	   let btnPlayActivity = exampleModal.querySelector('#playActivityButton');
	   
	   //bottone per generare coupon
	   let btnGenerateCoupon = exampleModal.querySelector('#generateCouponButton');
	   
	   if(isCertified != null){
		   //l'attività è certificata
		   
		   //disabilito il schedule activity e lo nascondo
		   btnPlayActivity.disabled = true;
		   btnPlayActivity.classList.add('visually-hidden');
		   
		   //attivo e faccio comparire il generate coupon
		   btnGenerateCoupon.disabled = false;
		   btnGenerateCoupon.classList.remove('visually-hidden');
		   
		   //disabilito il campo di testo del coupon
		   exampleModal.querySelector('#selectedCoupon').disabled=false;
	   	   
	   }else{
		   //l'attività non è certificata
		   
		   //attivo il schdule activity e lo faccio comparire
		   btnPlayActivity.disabled = false;
		   btnPlayActivity.classList.remove('visually-hidden');
	   	   
		   //disattivo il generate coupon e lo nascondo
		   btnGenerateCoupon.disabled = true;
		   btnGenerateCoupon.classList.add('visually-hidden');
	   		
		   exampleModal.querySelector('#selectedCoupon').disabled=true;
	   
	   }
	})
	
		 	//------------------------------------------------------------------------
		 	//							form checks
		 	//------------------------------------------------------------------------
		 	
		 	document.querySelector('#scheduledDate').addEventListener('change', (event)=>{
		 		selectedDate = event.target.value;
		 		document.querySelector('#reminderDate').max = selectedDate; 
		 	})
		 	
		 	//------------------------------------------------------------------------
		 	//						visualizzazione coupon
		 	//------------------------------------------------------------------------
		 	
		 	const couponModal = document.getElementById('couponModal')
		 	
		 	couponModal.addEventListener('show.bs.modal', function(){
		 		let activity = document.querySelector('#activityModal input#idActivity').value;
		 		console.log("l'attività selezionata è:"+activity);
		 		
		 		let url = "discount.jsp?activity="+activity;
		 		
		 		//step 1: genero oggetto per richiesta al server
		 		var req = new XMLHttpRequest();
		 		
		 		//step 2: creo la funzione che viene eseguita quando ricevo risposta dal server
		 		req.onload= function(){
		 			console.log(this.response);
		 			let usrSale = this.response.usrWallet;
		 			let discounts = this.response.discounts;
		 			
		 			couponModal.querySelector('#saldo-disponibile').textContent = usrSale;
		 			
		 			populateDiscounts(discounts,usrSale);
		 		}
		 		
		 		//step 3: dico quale richiesta fare al server
				req.open("GET", url);
		 		
				//step 3.1 : imposto document come response type, perché sto per ricevere html html
				req.responseType = "json";
				
				//step 4: invio la richiesta 
				req.send();
		 	});
		 	
		 	function clearDiscounts(){
		 		couponModal.querySelectorAll('div.row.coupon.shadow').forEach(curr => curr.remove());
		 	}
		 	
		 	function populateDiscounts(discounts, myAvaibility){
		 		
		 		clearDiscounts();
		 		
		 		discounts.forEach(curr => {
		 			
		 			
			 		let htmlDiscount;
			 		
			 		let redeemable = (curr.price <= myAvaibility)
			 		
			 		htmlDiscount+='<div class="row coupon shadow">';
			 			htmlDiscount+='<div class="col percentage-info text-center '+(redeemable == false ? 'disabled' : '')+'">';
			 				htmlDiscount+='<div class="position-relative top-50 start-50 translate-middle">';
			 					htmlDiscount+='<label for="percentage" class="col-form-label label-sale">Sconto del</label>';
			 					htmlDiscount+='<p id="percentage"><span class="percentage">'+curr.percentage+'%</span></p>';
			 				htmlDiscount+='</div>';
		 				htmlDiscount+='</div>';
			 			htmlDiscount+='<div class="col percentage-redeem text-center">';
			 				htmlDiscount+='<div class="position-relative top-50 start-50 translate-middle">';
			 					if(!redeemable) htmlDiscount+='<p class="lead insufficient-funds">Soldi insufficienti per generare questo coupon</p>';
			 					htmlDiscount+='<button type="button" class="btn btn-outline-success btn-lg btnHome" '+(redeemable == false ? 'disabled' : '')+'>Genera Coupon per '+curr.price+' soldi</button>';
			 				htmlDiscount+='</div>';
			 			htmlDiscount+='</div>';
					htmlDiscount+='</div>';
					
					//generaro un nodo html da stringhe per creare il box del messaggio 
		 			let placeholder = document.createElement('div');
			 		placeholder.insertAdjacentHTML('afterbegin', htmlDiscount);
			 		
			 		let discount = placeholder.firstElementChild;
			 		if(redeemable) discount.querySelector('button').addEventListener('click', () => generateDiscount(curr.percentage))
			 		couponModal.querySelector('.modal-body.modal-coupon').append(discount);
		 		
		 		});
		 	}
		 	
		 	function generateDiscount(perc){
		 		console.log("Selezionata la pecentuale di sconto del "+perc+"%");
		 		exampleModal.querySelector('#selectedCoupon').value = perc;
		 		exampleModal.querySelector('#playActivityButton').disabled = false;
		 		exampleModal.querySelector('#playActivityButton').click();
		 	}
		 	
	
</script>
</body>
</html>