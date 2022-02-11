<%@page import="logic.controller.AuthException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import = "java.io.*,java.util.*, logic.model.DAOPreferences, logic.controller.RegExplorerController, logic.model.SuperActivity,logic.model.Log" %>

<jsp:useBean id ="regExpBean" scope="request" class="logic.model.RegExpBean" />
<jsp:setProperty name="regExpBean" property="*" />

<!DOCTYPE html>

 	<% application.setAttribute( "titolo" , "register"); %>

	<%@ include file="Navbar.jsp" %>
	

<html>
<head>
<meta charset="ISO-8859-1">
<title>Register Page</title>
</head>
<body>


<div class="container pt-6">
<body class="text-center">

<main class="form-signup">
<%
if(request.getParameter("regForm")!= null){ 
	String error;
	AuthException exc;
	RegExplorerController regController = new RegExplorerController();
	
	try{
		regExpBean = regController.validateForm(regExpBean);
		
		regExpBean = regController.addExplorer(regExpBean);
%>
				<jsp:forward page="login.jsp"/>
<%
		
	} catch(AuthException e){
			exc = e;
		%>
			<p style="color:red;"> <%= exc.getMessage()%></p>
		<%	
	}
}
%>
   <form action="register.jsp" method="POST">   
   		<img class="mb-4" src="logo/DovadoLogo(3).png" alt="" width="72" height="57">  
   		<h1 class="h3 mb-3 fw-normal">Please sign up</h1> 
      		<div class="form-floating">
      			<input type="email" class="form-control" id="emailID" name="email" placeholder="name@example.com" required>
      			<label for="emailID">Email address</label>
    		</div>
    		<div class="form-floating">
      			<input type="text" class="form-control" id="usernameID" name="username" placeholder="Username" pattern=".{4,}" maxlength="15" title="massimo 15 caratteri, minimo 4" required>
      			<label for="usernameID">Username</label>
    		</div>
    		<div class="form-floating">
      			<input type="Password" class="form-control" id="password" name="password" placeholder="Password" pattern=".{8,}" maxlength="20" title="La password deve contenere almeno 8 cratteri e deve contenere numeri, lettere e un carattere tra:{',','.','&','!','?'}" required>
      			<label for="passwordID">Password</label>
    		</div>
    		<div class="form-floating">
      			<input type="Password" class="form-control" id="password2" name="password2"  placeholder="Password" pattern=".{8,}" maxlength="20" onkeyup="compare()" required >
      			<label for="password2">Conferma Password</label>
    		</div>    		    
    		
    			<div class ="select-preferences">
					<h4 class="text-center"><em class="bi bi-tags"></em>  A quale categoria sei interessato? <em class="bi bi-tags"></em> </h4>
					<hr class="separator-places">
					<div class="row">
						<div class="col">
							<div class="form-check form-switch">
				  	 			<input class="form-check-input" type="checkbox" id="Arte" name="arte" >
			  		 			<label class="form-check-label" for="Arte">Arte</label>
				  			</div>
				  
				  			<div class="form-check form-switch">
				  	 			<input class="form-check-input" type="checkbox" id="Cibo" name="cibo" >
			  		 			<label class="form-check-label" for="Cibo">Cibo</label>
				  			</div>
				  
				  			<div class="form-check form-switch">		
				  	 			<input class="form-check-input" type="checkbox" id="Musica" name="musica" >
			  		 			<label class="form-check-label" for="Musica">Musica</label>
				  			</div>
				  
				  			<div class="form-check form-switch">	 
				  	 			<input class="form-check-input" type="checkbox" id="Sport" name="sport" >
			  		 			<label class="form-check-label" for="Sport">Sport</label>
				  			</div>
				  
				  			<div class="form-check form-switch">	 
				  	 			<input class="form-check-input" type="checkbox" id="Social" name="social" >
			  		 			<label class="form-check-label" for="Social">Social</label>
				  			</div>
				  
				  			<div class="form-check form-switch">	 
				  	 			<input class="form-check-input" type="checkbox" id="Natura" name="natura" >
			  		 			<label class="form-check-label" for="Natura">Natura</label>
				  			</div>
				  
				  			<div class="form-check form-switch">	 
				  	 			<input class="form-check-input" type="checkbox" id="Esplorazione" name="esplorazione" >
			  		 			<label class="form-check-label" for="Esplorazione">Esplorazione</label>
				  			</div>
				
						</div>
						<div class="col">
							<div class="form-check form-switch">
								<input class="form-check-input" type="checkbox" id="Ricorrenze" name="ricorrenze" >
								<label class="form-check-label" for="Ricorrenze">Ricorrenze Locali</label>
			  				</div>
			  	
			  				<div class="form-check form-switch">
			  					<input class="form-check-input" type="checkbox" id="Moda" name="moda" >
		  						<label class="form-check-label" for="Mode">Moda</label>
			  				</div>
			  	
			  				<div class="form-check form-switch">		
			  					<input class="form-check-input" type="checkbox" id="Shopping" name="shopping" >
		  		 				<label class="form-check-label" for="Shopping">Shopping</label>
			  				</div>
			  	
			  				<div class="form-check form-switch">	 
			  	 				<input class="form-check-input" type="checkbox" id="Adrenalina" name="adrenalina" >
		  		 				<label class="form-check-label" for="Adrenalina">Adrenalina</label>
			  				</div>
			  	
			   				<div class="form-check form-switch">	 
			  	 				<input class="form-check-input" type="checkbox" id="Relax" name="relax"  >
		  						<label class="form-check-label" for="Relax">Relax</label>
			  				</div>
			  	
			  				<div class="form-check form-switch">	 
			  	 				<input class="form-check-input" type="checkbox" id="Istruzione" name="istruzione" >
		  		 				<label class="form-check-label" for="Istruzione">Istruzione</label>
			  				</div>
			  	
			  				<div class="form-check form-switch">	 
			  	 				<input class="form-check-input" type="checkbox" id="Monumenti" name="monumenti" >
		  		 				<label class="form-check-label" for="Monumenti">Monumenti</label>
			  				</div>
						</div>
					</div>
				</div>
    		
    		
    		
    				   
 			<button class="w-100 btn btn-lg btn-dark" type="submit" name="regForm"  value="Register Here">Sign up</button>       
    </form>   
    <p class="mt-5 mb-3 text-muted">Se già sei registrato clicca <a href="login.jsp"> qui </a></p>

  </div>
</main>      
</div>

</body>
</html>