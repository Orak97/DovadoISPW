<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    
<%@ page import = "java.io.*,java.util.*, logic.model.User, logic.model.Preferences, logic.controller.FindActivityController, logic.model.Activity" %>
 
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
				  <div class="col">
				    <div class="card h-100 scheduledActivityCards shadow" data-bs-toggle="modal" data-bs-target="#exampleModal" data-bs-id="<%= curr.getId() %>" data-bs-titolo="<%= curr.getName() %>">
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
	<% 	}
	} %>
</div>

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
</body>
</html>