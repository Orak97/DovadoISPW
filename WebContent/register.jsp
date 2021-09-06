<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<jsp:useBean id ="regBean" scope="request" class="logic.model.RegBean" />
<jsp:setProperty name="regBean" property="*" />

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
	if(request.getParameter("regForm")!= null){ //controllo la richiesta ricevuta, se all'interno e presente un parametro login vuol dire che arrivo a questa pagina tramite la pressione del bottone login, quindi ne consegue che i dati username e password sono pieni e quindi posso andare avanti
		if(regBean.validate()){ 
%>
			<jsp:forward page="login.jsp"/>
<%
		} else{
%>
	
		<p style="color:red;"> ${regBean.getError()}</p>
<%		
		};
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
 			<button class="w-100 btn btn-lg btn-dark" type="submit" name="regForm"  value="Register Here">Sign up</button>       
    </form>   
    <p class="mt-5 mb-3 text-muted">Se già sei registrato clicca <a href="login.jsp"> qui </a></p>

  </div>
</main>      
</div>

</body>
</html>