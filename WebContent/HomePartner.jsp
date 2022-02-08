<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import = "java.io.*,java.util.*, logic.model.Partner, logic.model.CertifiedActivity, logic.model.DAOActivity, logic.controller.AddActivityToScheduleController" %>

<% application.setAttribute( "titolo" , "Home"); %>

<%@ include file="NavbarPartner.jsp" %>
<div class="container pt-6">

<%
Partner partner = (Partner) session.getAttribute("partner");


if(partner != null){
		
	if(request.getParameter("couponToRedeem")!=null){
		AddActivityToScheduleController controller = new AddActivityToScheduleController(partner,request.getParameter("couponToRedeem"));
		boolean success = true;
		try{
			controller.redeemCoupon();
		}catch(Exception e){
			e.printStackTrace();
			success = false;
		}
	
	
		%>
		<%--inizio prova modal per fare conferma o errore nello schedulo di un'attività --%>
		<!-- Modal elimina -->
			<div class="modal fade" id="responseModal" tabindex="-1" aria-labelledby="responseModalLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
		  		<div class="modal-dialog">
		    		<div class="modal-content">
		      			<div class="modal-header">
		        			<h5 class="modal-title" id="responseModalLabel"><%= success ? "Coupon correttamente riscattato!" : "Errore nella riscossione del coupon"  %></h5>
		        			<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		      			</div>
		      			<div class="modal-body">
		        			<p class="delete-icon-schedule text-center" <%if(success){ %>style="color:#198754"><i class="bi bi-check-circle-fill"></i> <%}else{%> ><i class="bi bi-x-circle-fill"></i> <%} %></p>
		        			<h5 class="text-center irreversible-process"><%= success ? "Un'altro cliente che vi raggiunge tramite Dovado!" : "C'è stato un errore nella riscossione dello schedulo"%></h5>
		      			</div>
		      			<div class="modal-footer">
		        			<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Torna indietro</button>
		      			</div>
		    		</div>
		  		</div>
			</div>
			
			<script>
				let responseModal = new bootstrap.Modal(document.getElementById('responseModal'))
				responseModal.show();
			</script>
		
		<%-- fine modal per fare conferma o errore nello schedulo di un'attività --%>
		
		<%
	
	
	
	}		
	
	ArrayList<CertifiedActivity> foundActivities = new ArrayList<CertifiedActivity>();
	try{	  		
		foundActivities = DAOActivity.getInstance().getPartnerActivities(partner.getUserID());
		  	
	}catch(Exception e){
		%> <script>alert('errore nel recupero attività')</script> <%
		e.printStackTrace();
	} 
%>
	
	<h1>Bentornato <%= partner.getUsername()%>:</h1>
	
	<h2>Riscatta coupon:</h2>
	<div class="d-flex justify-content-center pt-1" aria-describedby="infoInputCoupon">
		<input type="number" class="form-control search-input" id="coupon" placeholder="000000">
		<button type="button" class="btn btn-success btn-find-activities" id="redeemCoupon" data-bs-toggle="modal" data-bs-target="#detailsCoupon" disabled>Riscatta</button>
	</div>
	<div id="infoInputCoupon" class="form-text">
		  il coupon deve essere di 6 cifre
	</div>
	
	<h2 class="pt-3">Le mie attività:</h2>
	<div class="my-activities">
		<div class="row row-cols-1 row-cols-md-3 g-4">
		    	
		  	<%for(CertifiedActivity curr:foundActivities){ %>
			  <div class="col">
			    <div class="card h-100 scheduledActivityCards shadow"  data-bs-toggle="modal" data-bs-target="#activityModal" data-bs-titolo="<%=curr.getName() %>" data-bs-luogo="<%=curr.getPlace().getName()%>" data-bs-id="<%=curr.getId() %>" data-bs-description="<%= curr.getDescription() %>" data-bs-playabilityInfo="<%= curr.getPlayabilityInfo()%>" data-bs-address="<%= curr.getPlace().getFormattedAddr()%>" >
			      <span class="badge bg-certified text-white position-absolute top-0 end-0 mt-4">Certificata <i class="bi bi-patch-check-fill"></i></span>
			      <img src="https://source.unsplash.com/random" class="card-img-top" alt="...">
			      <div class="card-body">
			        <h5 class="card-title"><%= curr.getName() %></h5>
			        <p class="card-text">Luogo : <%=curr.getPlace().getName()  %> </p>
			      </div>
			    </div>
			  </div>
		  <% } %>
		  
		  <div class="col">
		  	<a href="CreateActivityPartner.jsp">
			  <div class="card h-100 border-dark scheduledActivityCards shadow">
				  <div class="card-body text-dark">
				    <h5 class="card-title text-center">Aggiungi Attività</h5>
				    <p class="card-text text-center addActivity position-absolute top-50 start-50 translate-middle"><i class="bi bi-plus-circle"></i></p>
				  </div>
			  </div>
			</a>
		  </div>
		
		</div>
	</div>

</div>

<!-- Modal -->
<div class="modal fade" id="detailsCoupon" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="detailsCouponLabel" aria-hidden="true">
  <div class="modal-dialog modal-xl">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="detailsCouponLabel">Dettagli prenotazione</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
      <div class="coupon-ok">
		     <div class="row">
		     	<label for="couponcode" class="col-form-label label-activity">Codice coupon:</label>
		     	<h2 class="text-center" id="couponcode">148247</h2>
		     </div>
		     
		     <div class="row">
		     	<div class="col-4 col-sm-4">
		     		<label for="usr" class="col-form-label label-activity">Utilizzatore:</label>
		     		<p class="lead" id="usr">Giovanni</p>
		     	</div>
		     	<div class="col-8 col-sm-4">
		     		<label for="act" class="col-form-label label-activity">Attività:</label>
		     		<p class="lead" id="act">Roba varia</p>
		     	</div>
		     	<div class="col-12 col-sm-4">
		     		<label for="dataAct" class="col-form-label label-activity">Data appuntamento:</label>
		     		<p class="lead" id="dataAct">12/12/2019 10:20</p>
		     	</div>
		     </div>
		     
		     <div class="card text-white bg-success mb-3 mt-3 card-discount">
			  <div class="card-body">
			    <h5 class="card-title">Sconto:</h5>
			    <p class="card-text">L'utente ha diritto di uno sconto del <span class="percentage">30%</span></p>
			  </div>
			</div>
	     </div>
	     <div class="coupon-error visually-hidden">
	     	<p class="delete-icon-schedule text-center"><i class="bi bi-x-circle-fill"></i></p>
        	<h5 class="text-center irreversible-process">Il coupon non esiste o il valore inserito non è corretto</h5>
	     </div>
      </div>
      <div class="modal-footer">
      	<form action="HomePartner.jsp" method="GET">
      	<input type="number" class="visually-hidden" id="coupon-to-redeem" name="couponToRedeem">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Indietro</button>
        <button type="submit" class="btn btn-primary">Riscatta Coupon</button>
      	</form>
      </div>
    </div>
  </div>
</div>

<%}%>


<%-- modal per gestire le attività --%>
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
		      



		      </div>
		      <div class="modal-footer d-flex justify-content-center">
  			    
	      	  		<input type="number" id="idActivity" name="idActivity" class="visually-hidden">
		        	<button type="button" class="btn btn-secondary" data-bs-dismiss="modal" data-bs-toggle="modal" data-bs-target="#couponModal">Modifica Sconti disponibili <i class="bi bi-tag"></i></button>
		        	<button type="button" class="btn btn-primary"  id="open-chat" data-bs-dismiss="modal" data-bs-toggle="modal" data-bs-target="#chat-modal" >Apri la chat della attività <i class="bi bi-chat-dots"></i></button>
		        	<a role="button" class="btn btn-success"  id="edit-activity">Modifica i dettagli dell'attività <i class="bi bi-gear-wide-connected"></i></a>
		      	
		      </div>
		    </div>
		  </div>
		</div>
		
<!-- fine modal -->

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
	let editActivity =  document.querySelector('#edit-activity')
	
	console.log(id);
	modalID.value=id
	modalTitle.textContent = titolo
	modalDescription.textContent = descr
	modalPlayabilityInfo.textContent = playability 
	modalPlace.textContent = luogo
	modalAddress.textContent = addr
	editActivity.href = 'CreateActivityPartner.jsp?editActivity='+id;
	
	//---------------------sezione per controllare se l'attività è certificata---------------
	
})

</script>

<%-- fine modal per gestire le attività --%>

<%-- modal per la chat --%>
<!-- Modal -->
<div class="modal fade" id="chat-modal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="chatModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-scrollable modal-xl">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="chatModalLabel">Modal title</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" id="closeChat"></button>
      </div>
      <div class="modal-body modal-chat" style="background-color:#9FB6CD">
      </div>
      <div class="modal-footer chat-bar bg-white">
		<input type="text" class="form-control flex-grow-1 col" id="chatField" placeholder="scrivi qualcosa"> 
		<button type="button" class="btn btn-outline-dark send-btn disabled" id="send-btn" onclick="sendMsg()"> <i class="bi bi-send"></i> </button>
      </div>
    </div>
  </div>
</div>

<script>
	let modalChat = document.querySelector('#chat-modal')
	modalChat.addEventListener('show.bs.modal',loadChat)
	modalChat.addEventListener('shown.bs.modal',scrollToBottomChat)
	
	
	var refreshInterval;
	var chat;
	
	//disabilito il bottone se non ho testo da inviare
	var chatField = document.getElementById('chatField');
	chatField.addEventListener('keyup', ()=>{
		if(chatField.value.length > 0) document.getElementById('send-btn').classList.remove("disabled");
		else document.getElementById('send-btn').classList.add("disabled");
	});
	
	document.getElementById('closeChat').addEventListener('click', ()=>{
			
		//stop refreshing
		if(refreshInterval != undefined) clearInterval(refreshInterval);
			
		clearChat();
			
	});
	
 	function loadChat(){
 		let activity = document.querySelector('.modal-footer #idActivity').value;
 		console.log('invio messaggi a '+activity)
 		let request = "chat.jsp?activity="+activity;
 		
 		//cancello il timer di refresh nel caso sia attivo
 		if(refreshInterval != undefined)
 		clearInterval(refreshInterval);
 		
 		//imposto un intervallo per refreshare la chat
 		startRefreshing(request);
 		
 		//step 1: genero oggetto per richiesta al server
 		var req = new XMLHttpRequest();
 		
 		
 		//step 2: creo la funzione che viene eseguita quando ricevo risposta dal server
 		req.onload = function(){
			console.log(this);	
			chat = this.response;
			
			console.log(chat);
	 		
	 		//imposto il titolo del canale
	 		document.getElementById('chatModalLabel').innerText = chat.activityName+"'s channel";
	 		
	 		clearChat();
	 		
	 		populateChat(chat.messages);
	 		
	 		//scrollToBottomChat()
 		}
 		
 		
 		//step 3: dico quale richiesta fare al server
		req.open("GET", request);
		//step 3.1 : imposto document come response type, perché sto per ricevere html html
		req.responseType = "json";
		
		//step 4: invio la richiesta 
		req.send();
 	}
 	
 	function sendMsg(){
 		var message = document.getElementById("chatField").value;
 		let activity = chat.activityId;
 		
 		//TODO: primo controllo che il campo non sia vuoto
 		if(message=='') alert('Chiudi console sviluppatore, è molto poco carino da parte tua voler rompere il sito 🥺')
 		
 		else{
 			let request = "chat.jsp?activity="+activity+"&textMsg="+message;
 			//chat = sendRequest(request);
 			
 			//----------------------------------------AJAX----------------------------------------------
 			//step 1: genero oggetto per richiesta al server
	 		var req = new XMLHttpRequest();
	 		
	 		
	 		//step 2: creo la funzione che viene eseguita quando ricevo risposta dal server
	 		req.onload = function(){
				console.log(this);	
				chat = this.response;
				
				clearChat();
				populateChat(chat.messages);
	 			
	 			
	 			//pulisco l'input di invio DOPO che ho inviato e riblocco il pulsante
	 			document.getElementById('chatField').value = '';
	 			document.getElementById('send-btn').classList.add("disabled");
	 		
	 			scrollToBottomChat()
	 		}
	 		
	 		//step 3: dico quale richiesta fare al server
			req.open("GET", request);
			//step 3.1 : imposto document come response type, perché sto per ricevere html html
			req.responseType = "json";
			
			//step 4: invio la richiesta 
			req.send();
 			
	 		//-----------------------------------Fine AJAX----------------------------------------------
 			
 		}
 	}
 	
 	function populateChat(messages){
 		let length = messages.length
 		//console.log(messages);		 		
 		messages.forEach( function(curr, index){
 					
 			let htmlMsg = '<div class="row d-flex '+(curr.sender == chat.user ? 'justify-content-end' : 'justify-content-start')+'">';
 			htmlMsg+= ' <div class="toast show message '+(curr.sender == chat.user ? 'msg-sent' : 'msg-recieved')+'" role="alert" aria-live="assertive" aria-atomic="true">';
 			htmlMsg+= ' <div class="toast-header">';
 			htmlMsg+= ' <strong class="me-auto">'+curr.sender+'</strong>';
 			htmlMsg+= ' <small class="text-muted">'+curr.sendDate+'</small>';
 			htmlMsg+= ' </div>';
 			htmlMsg+= ' <div class="toast-body">'+curr.text+'</div>';
 			htmlMsg+= ' </div> </div>';
 			
 			//generaro un nodo html da stringhe per creare il box del messaggio 
 			let placeholder = document.createElement('div');
	 		placeholder.insertAdjacentHTML('afterbegin', htmlMsg);
	 		
	 		let message = placeholder.firstElementChild;
	 		
	 		//console.log(message);
	 		//appendo il messaggio dentro il suo container di messaggi
	 		document.querySelector('.modal-body.modal-chat').append(message);
 		
 			//if(index+1 == length) scrollToBottomChat()
 		});
 		
 	}
 	
 	function startRefreshing(request){
			refreshInterval= setInterval(() => {
				//step 1: genero oggetto per richiesta al server
	 		var req = new XMLHttpRequest();
	 		
	 		
	 		//step 2: creo la funzione che viene eseguita quando ricevo risposta dal server
	 		req.onload = function(){
				console.log(this);	
				let newChat = this.response;
				if( JSON.stringify(chat) !=  JSON.stringify(newChat)){
 					clearChat();
 					chat = newChat;
 					populateChat(chat.messages);
 					scrollToBottomChat();
 				}
	 		}
	 		
	 		//step 3: dico quale richiesta fare al server
			req.open("GET", request);
			//step 3.1 : imposto document come response type, perché sto per ricevere html html
			req.responseType = "json";
			
			//step 4: invio la richiesta 
			req.send();
				//aggiorno la chat solo se ci sono nuovi messaggi
				console.log('finito timer');
				}
			,30000);
 	}
 	
 	function scrollToBottomChat(){
 		//comando per scrollare in fondo alla chat
		let chat = document.querySelector('.modal-body.modal-chat');
 		//chat.lastChild.scrollIntoViewIfNeeded()
 		chat.scrollTop = chat.scrollHeight;
 	}
 	
 	function clearChat(){
 		//pulisco la chat
		document.querySelector('.modal-body.modal-chat').innerHTML='';
 	}

</script>
<%-- fine modal per la chat --%>

<%-- inizio modal per gestione coupon --%>

<!-- inizio modal per Coupon -->
		<!-- Modal -->
		<form method="GET" action="discount.jsp">
		<div class="modal fade" id="couponModal" tabindex="-1" aria-labelledby="couponModalLabel" aria-hidden="true">
		  <div class="modal-dialog modal-xl modal-dialog-scrollable">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="couponModalLabel">Quali percentuali di sconto possono generare gli utenti per questa attività?</h5>
		        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		      </div>
		      <div class="modal-body modal-coupon">		      
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
		       <table class="table table-discount">
				  <thead>
				    <tr>
				      <th scope="col">Sconto</th>
				      <th scope="col">Prezzo</th>
				      <th scope="col">Stato</th>
				    </tr>
				  </thead>
				  <tbody>
				    <tr>
				      <td>5%</td>
				      <td>150</td>
				      <td>
				      	<div class="form-check form-switch">
						  <input class="form-check-input" type="checkbox" id="flexSwitchCheckDefault">
						</div>
				      </td>
				    </tr>
				    <tr>
				      <td>10%</td>
				      <td>300</td>
				      <td>
				      	<div class="form-check form-switch">
						  <input class="form-check-input" type="checkbox" id="flexSwitchCheckDefault" checked>
						</div>
				      </td>
				    </tr>
				  </tbody>
				</table>
		       
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Indietro</button>
		        <button type="submit" class="btn btn-success">Salva modifiche</button>
		      </div>
		      </form>
		    </div>
		  </div>
		</div>
				
		<!-- fine modal per coupon -->
		
		<script>
			let couponModal =document.getElementById('couponModal')
			
			let discounts;
			couponModal.addEventListener('show.bs.modal', function(){
		 		let activity = document.querySelector('#activityModal input#idActivity').value;
		 		console.log("l'attività selezionata è:"+activity);
		 		
		 		let url = "discount.jsp?activity="+activity;
		 		
		 		//step 1: genero oggetto per richiesta al server
		 		var req = new XMLHttpRequest();
		 		
		 		//step 2: creo la funzione che viene eseguita quando ricevo risposta dal server
		 		discounts = null;
		 		req.onload= function(){
		 			console.log(this.response);
		 			if(this.response == null) location.reload();
		 			if(this.response == 'invalid-activity') {
		 				alert('Attività inserita non corretta');
		 				couponModal.querySelector('.btn-close').click;
		 				
		 			}else{	
		 				discounts = this.response;
		 				populateDiscounts(discounts);
		 			}
		 		}
		 		
		 		//step 3: dico quale richiesta fare al server
				req.open("GET", url);
		 		
				//step 3.1 : imposto document come response type, perché sto per ricevere html html
				req.responseType = "json";
				
				//step 4: invio la richiesta 
				req.send();
		 	});
			
			function populateDiscounts(discounts){
		 		
		 		clearDiscounts();
		 		
		 		discounts.forEach(curr => {
			 		let htmlDiscount='';
			 		
			 		htmlDiscount+='<tr>';
			 			htmlDiscount+='<td>'+curr.percentage+'%</td>';
			 			htmlDiscount+='<td>'+curr.price+'</td>';
			 			htmlDiscount+='<td>';
			 				htmlDiscount+='<div class="form-check form-switch">';
			 					htmlDiscount+='<input class="form-check-input" type="checkbox" name="discount'+curr.percentage+'" '+(curr.state ? 'checked' : '')+'>'
			 				htmlDiscount+='</div>';
			 			htmlDiscount+='</td>';
			 		htmlDiscount+='</tr>';
					
			 		couponModal.querySelector('.table-discount tbody').innerHTML+=htmlDiscount;
		 		
		 		});
		 	}
			
		 	function clearDiscounts(){
		 		couponModal.querySelector('tbody').innerHTML = '';
		 	}
		 	
		
		</script>

<%-- fine modal per gestione coupon --%>

<style>
	/* Chrome, Safari, Edge, Opera */
	input::-webkit-outer-spin-button,
	input::-webkit-inner-spin-button {
	  -webkit-appearance: none;
	  margin: 0;
	}
	
	/* Firefox */
	input[type=number] {
	  -moz-appearance: textfield;
	}
</style>

<script>

	const couponField = document.querySelector('#coupon');
	let currDigit;
	
	couponField.addEventListener('keyup', (event)=>{
		let coupon = event.target.value
		let couponLen = coupon.toString().length;
		if(couponLen != 6) disableRedeemCoupon(couponLen)
		else enableRedeemCoupon()
	})
	
	function disableRedeemCoupon(len){
		console.log('input disabilitato')
		document.querySelector('#redeemCoupon').disabled =true;
		if(len > 6){
			couponField.classList.add('is-invalid')
		}else{
			couponField.classList.remove('is-invalid')	
		}
	}
	
	function enableRedeemCoupon(){
		console.log('input abilitato')
		document.querySelector('#redeemCoupon').disabled =false;
		couponField.classList.remove('is-invalid')
	}
	
	//------------------------------------------------------------------------
	//									modal
	//------------------------------------------------------------------------
	
	
	let modalDetails = document.querySelector('#detailsCoupon')
	modalDetails.addEventListener('show.bs.modal', retrieveDetails)
	
	function retrieveDetails(){
		let coupon = couponField.value;
		
		let url = "coupon.jsp?idCoupon="+coupon
		
		//step 1: genero oggetto per richiesta al server
		let req = new XMLHttpRequest()
		
		//step 2: creo la funzione che viene eseguita quando ricevo risposta dal server
		req.onload = function(){
			let res = this.response
			if(res.response == 'ok'){
				//faccio scomparire il modal d'errore
				modalDetails.querySelector('.coupon-error').classList.add('visually-hidden')
				
				//faccio comparire il modal con i dati
				modalDetails.querySelector('.coupon-ok').classList.remove('visually-hidden')
				
				//riempio il content del modal
				modalDetails.querySelector('#couponcode').textContent= res.code;
				modalDetails.querySelector('#usr').textContent = res.username;
				modalDetails.querySelector('#act').textContent = res.nomeAttivita;
				modalDetails.querySelector('#dataAct').textContent = res.dataSchedulo;
				modalDetails.querySelector('#coupon-to-redeem').value = res.code;
				
				//faccio comparire il pulsante per fare sumbit
				modalDetails.querySelector('[type=submit]').disabled = false;
				modalDetails.querySelector('[type=submit]').classList.remove('visually-hidden');
				
				
				if(res.discount > 0){
					modalDetails.querySelector('.card-discount').classList.remove('visually-hidden');
					modalDetails.querySelector('.percentage').textContent = res.discount+'%'
				}else{
					modalDetails.querySelector('.card-discount').classList.add('visually-hidden');
				}
			}
			if(res.response == 'input error'){
				modalDetails.querySelector('.coupon-error').classList.remove('visually-hidden')
				modalDetails.querySelector('.coupon-ok').classList.add('visually-hidden')
				
				//faccio comparire il pulsante per fare sumbit
				modalDetails.querySelector('[type=submit]').disabled = true;
				modalDetails.querySelector('[type=submit]').classList.add('visually-hidden');
				
			}
		}
		
		//step 3: dico quale richiesta fare al server
		req.open('GET',url)
		
		//step 3.1 : imposto json come response type, perché sto per ricevere json
		req.responseType = "json"
		
		
		//step 4: invio la richiesta 
		req.send();
	}
</script>



</body>
</html>