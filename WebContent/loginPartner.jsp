<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "java.io.*,java.util.*, logic.model.Partner, logic.model.DAOActivity, logic.controller.LogPartnerController, logic.model.SuperActivity,logic.model.Log" %>
   
<jsp:useBean id ="logBean" scope="request" class="logic.model.LogBean" />
<jsp:setProperty name="logBean" property="*" />
<!DOCTYPE html>

		
	
    <% application.setAttribute( "titolo" , "loginPartner"); 
    //Potrei gedtire questa eccezione qui del null
    
	%>
	
	<%@ include file="NavbarPartner.jsp" %>
	<div class="container pt-6">
<%
	    
   
	if(request.getParameter("logPartnerForm")!= null){ //controllo la richiesta ricevuta, se all'interno e presente un parametro login vuol dire che arrivo a questa pagina tramite la pressione del bottone login, quindi ne consegue che i dati username e password sono pieni e quindi posso andare avanti
		
		LogPartnerController controller = new LogPartnerController();
		Partner p = controller.loginPartner(logBean);
		if(p != null){ 
			//TODO Una volta fatta la nuova Navbar qui cambiamo
			session.setAttribute("partner", p);
			session.setMaxInactiveInterval(10);
			
			//TODO INSERIRE HOME DEL PARTNER
			response.sendRedirect("HomePartner.jsp");
		} else{
%>

		<p style="color:red;"> Mail o password errate</p>  
		
<%		
		}
	}
%>
   

 <body class="text-center">

<main class="form-signin">
   <form action="loginPartner.jsp" method="POST"> 
   		<img class="mb-4" src="logo/DovadoLogo(3).png" alt="" width="72" height="57">  
   		<h1 class="h3 mb-3 fw-normal">Partner please sign in</h1> 
      		<div class="form-floating">
      			<input type="email" class="form-control" id="emailID" name="email" placeholder="name@example.com" required>
      			<label for="emailID">Email address</label>
    		</div>
    		<div class="form-floating">
      			<input type="password" class="form-control" id="passwordID" name="password"  placeholder="Password"  maxlength="20" required>
      			<label for="passwordID">Password</label>
    		</div>    		   
 			<button class="w-100 btn btn-lg btn-dark" type="submit" name="logPartnerForm"  value="Login">Sign in</button>       
       
      <%--   Implementa Dimenticata  <a href="#">Password</a>   --%>    
    </form>   
    <p class="mt-5 mb-3 text-muted">Se ancora non sei registrato clicca <a href="registerPartner.jsp"> qui </a></p>
    <p class="mt-5 mb-3 text-muted">Non hai un'attività? clicca <a href="login.jsp"> qui </a></p>
  </div>
</main>
  
</body>
</html>