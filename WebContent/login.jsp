<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 
<%@ page import = "java.io.*,java.util.*, logic.model.User, logic.model.DAOActivity, logic.controller.LogExplorerController, logic.model.SuperActivity,logic.model.Log" %>
   
<jsp:useBean id ="logBean" scope="request" class="logic.model.LogBean" />
<jsp:setProperty name="logBean" property="*" />
<!DOCTYPE html>

		
	
    <% application.setAttribute( "titolo" , "login"); 
    //Potrei gedtire questa eccezione qui del null
    
	%>
	
	<%@ include file="Navbar.jsp" %>
	<div class="container pt-6">
<%
	    
	LogExplorerController controller = new LogExplorerController();
	User u= controller.loginExplorer(logBean);
	if(request.getParameter("logForm")!= null){ //controllo la richiesta ricevuta, se all'interno e presente un parametro login vuol dire che arrivo a questa pagina tramite la pressione del bottone login, quindi ne consegue che i dati username e password sono pieni e quindi posso andare avanti
		if(u != null){ 
			Enumeration em = session.getAttributeNames();
			while (em.hasMoreElements()) {
			    String element = (String)em.nextElement();
			    session.removeAttribute(element);
			}
			session.setAttribute("user", u);
			session.setMaxInactiveInterval(10);
			
			response.sendRedirect("Home.jsp");
		} else{
%>

		<p style="color:red;"> Mail o password errate </p>  
		
<%		
		};
	}
%>
   

 <body class="text-center">

<main class="form-signin">
   <form action="login.jsp" method="POST"> 
   		<img class="mb-4" src="logo/DovadoLogo(3).png" alt="" width="72" height="57">  
   		<h1 class="h3 mb-3 fw-normal">Please sign in</h1> 
      		<div class="form-floating">
      			<input type="email" class="form-control" id="emailID" name="email" placeholder="name@example.com" required>
      			<label for="emailID">Email address</label>
    		</div>
    		<div class="form-floating">
      			<input type="password" class="form-control" id="passwordID" name="password"  placeholder="Password"  maxlength="20" required>
      			<label for="passwordID">Password</label>
    		</div>    		   
 			<button class="w-100 btn btn-lg btn-dark" type="submit" name="logForm"  value="Login">Sign in</button>       
       
      <%--   Implementa Dimenticata  <a href="#">Password</a>   --%>    
    </form>   
    <p class="mt-5 mb-3 text-muted">Se ancora non sei registrato clicca <a href="register.jsp"> qui </a></p>
    <p class="mt-5 mb-3 text-muted">Hai un'attività commerciale e vuoi farla conoscere sulla piattaforma? clicca <a href="loginPartner.jsp"> qui </a></p>
  </div>
</main>
  
</body>
</html>