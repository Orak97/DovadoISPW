<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 

    <%@ page import = "java.io.*,java.util.*, logic.model.User, logic.model.Preferences, logic.controller.SetPreferencesController" %>

    <% application.setAttribute( "titolo" , "User Profile"); %>

	<%@ include file="Navbar.jsp" %>
	
	<jsp:useBean id="preferenceBean" scope="request" class="logic.model.PreferenceBean" />

	<jsp:setProperty name="preferenceBean" property="*" />
	
	<%
		User utente = (User) session.getAttribute("user");
	
		if(request.getParameter("isEdited") != null){
			try{
				SetPreferencesController controller = new SetPreferencesController(utente,preferenceBean);
				controller.updatePreferences();
				%>
					<script>alert('Aggiornamento delle preferenze avvenuto correttamente!')</script>
				<%
			}catch(Exception e){
				%>
					<script>alert('qualcosa è andato storto durante l\'aggiornamento delle preferenze!')</script>
				<%
				e.printStackTrace();
			}
		}
		
	
	%>
	
	<div class="container pt-6">
	<div class="infoBox">
		<h2 class="titleUser">Informazioni di base:</h2>
		
		<div class ="row g-4 px-2">
			<div class="col shadow-sm info">
				<label for="username" class="form-label">Username:</label>
				<p class="h4" id="username"><%= utente.getUsername() %></p>
			</div>
			<div class="col shadow-sm info">
				<label for="email" class="form-label">Email:</label>
				<p class="h4" id="email"><%= utente.getEmail() %></p>
			</div>
			<div class="col shadow-sm info">
				<label for="wallet" class="form-label">Wallet:</label>
				<p class="h4" id="wallet"><%= utente.getBalance() %></p>
			</div>
		</div>
		
		<h2 class="titleUser">Preferenze:</h2>
		<% 
			Preferences pref = utente.getPreferences();
		%>
		<form name="updatePreferencesForm" action="UserProfile.jsp" method="GET" id="formPref">
		<input class="visually-hidden" type="checkbox" name="isEdited" id="isEdited">
		<div class="row px-2 info shadow-sm">
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
			  	
			</div>
		</div>
		<div class="row px-2 py-4">
	    	<button type="submit" class="btn btn-success savePref-btn" id="savePref" disabled>Salva preferenze</button>
	  	</div>
	  	
	  	</form>
	</div>
	</div>
	
	<script>
	 const form = document.querySelector('#formPref');
	 const savePref = document.querySelector('#savePref')
	 const isEdited = document.querySelector('#isEdited')
	 
	 form.addEventListener('change', ()=> {
		 savePref.disabled = false;
		 isEdited.checked= true;
	 });
	</script>
	
	</body>
</html>