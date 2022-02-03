<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import = "java.io.*,java.util.*, logic.model.Partner, logic.model.CertifiedActivity, logic.model.DAOActivity" %>

<% application.setAttribute( "titolo" , "Home"); %>

<%@ include file="NavbarPartner.jsp" %>
<div class="container pt-6">

<%
	Partner partner = (Partner) session.getAttribute("partner");


	if(partner != null){
	
	ArrayList<CertifiedActivity> foundActivities = new ArrayList<CertifiedActivity>();
	try{	  		
		foundActivities = DAOActivity.getInstance().getPartnerActivities(partner.getUserID());
		  	
	}catch(Exception e){
		%> <script>alert('errore nel recupero attività')</script> <%
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
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Understood</button>
      </div>
    </div>
  </div>
</div>

<%}%>

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
				modalDetails.querySelector('.coupon-error').classList.add('visually-hidden')
				modalDetails.querySelector('.coupon-ok').classList.remove('visually-hidden')
				
				modalDetails.querySelector('#couponcode').textContent= res.code;
				modalDetails.querySelector('#usr').textContent = res.username;
				modalDetails.querySelector('#act').textContent = res.nomeAttivita;
				modalDetails.querySelector('#dataAct').textContent = res.dataSchedulo;
				
				if(res.discount > 0){
					modalDetails.querySelector('.card-discount').classList.remove('visuaylly-hidden');
					modalDetails.querySelector('.percentage').textContent = res.discount+'%'
				}else{
					modalDetails.querySelector('.card-discount').classList.add('visuaylly-hidden');
				}
			}
			if(res.response == 'input error'){
				modalDetails.querySelector('.coupon-error').classList.remove('visually-hidden')
				modalDetails.querySelector('.coupon-ok').classList.add('visually-hidden')
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