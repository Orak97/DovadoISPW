<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import = "java.io.*,java.util.*, logic.controller.RegPartnerController, logic.model.DAOActivity, logic.model.SuperActivity,logic.model.Log" %>

<jsp:useBean id ="regBean" scope="request" class="logic.model.RegPartnerBean" />
<jsp:setProperty name="regBean" property="*" />

<!DOCTYPE html>

 	<% application.setAttribute( "titolo" , "registerPartner"); %>

	<%@ include file="NavbarPartner.jsp" %>
	

<html>
<head>
<meta charset="ISO-8859-1">
<title>Partner Register Page</title>
</head>
<body>


<div class="container pt-6">
<body class="text-center">

<main class="form-signup">
<%
	if(request.getParameter("regForm")!= null){ //controllo la richiesta ricevuta, se all'interno e presente un parametro login vuol dire che arrivo a questa pagina tramite la pressione del bottone login, quindi ne consegue che i dati username e password sono pieni e quindi posso andare avanti
		String error;
		RegPartnerController regController = new RegPartnerController();
		regBean = regController.validateForm(regBean);
		
		if(regBean.getError() == null){ 
			regBean = regController.addPartner(regBean);
					if(regBean.getError() == null){
				
			
%>			
			<jsp:forward page="login.jsp"/>
<%
			}else{
				%>
				
				<p style="color:red;"> ${regBean.getError()}</p>
				<%
			}
		} else{
			Log.getInstance().getLogger().warning(regBean.getError());
%>
	
		<p style="color:red;"> ${regBean.getError()}</p>
<%		
		};
	}
%>
   <form action="registerPartner.jsp" method="POST">   
   		<img class="mb-4" src="logo/DovadoLogo(3).png" alt="" width="72" height="57">  
   		<h1 class="h3 mb-3 fw-normal">Partner please sign up</h1> 
      		<div class="form-floating">
      			<input type="email" class="form-control" id="emailID" name="email" placeholder="name@example.com" required>
      			<label for="emailID">Email address</label>
    		</div>
    		<div class="form-floating">
      			<input type="text" class="form-control" id="usernameID" name="username" placeholder="Username" pattern=".{4,}" maxlength="15" title="massimo 15 caratteri, minimo 4" required>
      			<label for="usernameID">Username</label>
    		</div>
    		<div class="form-floating">
      			<input type="text" class="form-control" id="pIVAID" name="pIVA" placeholder="00000000000" pattern="[0-9]{11}" title="Inserire le 11 cifre della partita IVA" required>
      			<label for="pIVAID">Partita Iva</label>
    		</div>
    		<div class="form-floating">
      			<input type="text" class="form-control" id="compNameID" name="compName" placeholder="Nome Azienda" pattern=".{4,}" maxlength="80" title="massimo 80 caratteri, minimo 4" required>
      			<label for="compNameID">Nome Azienda</label>
    		</div>
    		<div class="form-floating">
      			<input type="Password" class="form-control" id="password" name="password" placeholder="Password" pattern=".{8,}" maxlength="20" title="La password deve contenere almeno 8 cratteri e deve contenere numeri, lettere e un carattere tra:{',','.','&','!','?'}" required>
      			<label for="passwordID">Password</label>
    		</div>
    		<div class="form-floating">
      			<input type="Password" class="form-control" id="password2" name="password2"  placeholder="Password" pattern=".{8,}" maxlength="20" onkeyup="compare()" required >
      			<label for="password2">Conferma Password</label>
    		</div>    		    		   
 			<button class="w-100 btn btn-lg btn-dark" type="submit" name="regForm"  value="Register Here">Sign up</button>       
    </form>   
    <p class="mt-5 mb-3 text-muted">Se già sei registrato clicca <a href="loginPartner.jsp"> qui </a></p>

  </div>
</main>      
</div>

</body>
</html>