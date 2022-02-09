<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import = "java.io.*,java.util.*, logic.model.Partner, logic.model.CertifiedActivity, logic.model.DAOActivity" %>

<% application.setAttribute( "titolo" , "Partner Profile"); %>

<%@ include file="NavbarPartner.jsp" %>

<%
	Partner partner = (Partner) session.getAttribute("partner");
	if(partner!= null){
		
		ArrayList<CertifiedActivity> foundActivities = new ArrayList<CertifiedActivity>();
		try{	  		
			foundActivities = (ArrayList<CertifiedActivity>)DAOActivity.getInstance().getPartnerActivities(partner.getUserID());	  	
		}catch(Exception e){
			%> <script>alert('errore nel recupero attività')</script> <%
			e.printStackTrace();
		}
		
%>
	<div class="container pt-6">
	<div class="infoBox">
		<h2 class="titleUser">Informazioni di base:</h2>
		
		<div class ="row g-4 px-2 pb-3">
			<div class="col shadow-sm info">
				<label for="username" class="form-label">Username:</label>
				<p class="h4" id="username"><%= partner.getUsername() %></p>
			</div>
			<div class="col shadow-sm info">
				<label for="email" class="form-label">Email:</label>
				<p class="h4" id="email"><%= partner.getEmail() %></p>
			</div>
			<div class="col shadow-sm info">
				<label for="azienda" class="form-label">Azienda:</label>
				<p class="h4" id="azienda"><%= partner.getNomeAzienda() %></p>
			</div>
		</div>
		
		
		<h2 class="titleUser">Dati attività:</h2>
		
		<div class ="row g-4 px-2 pb-3">
			<div class="col shadow-sm info">
				<label for="possedimenti" class="form-label">Attività possedute:</label>
				<p class="h4" id="possedimenti"><%= foundActivities.size() %></p>
			</div>
			<div class="col shadow-sm info">
				<label for="clienti" class="form-label">Clienti raggiunti:</label>
				<p class="h4" id="clienti">105</p>
			</div>
			<div class="col shadow-sm info">
				<label for="couponGen" class="form-label">Coupon generati:</label>
				<p class="h4" id="couponGen">68</p>
			</div>
		</div>
		
	</div>
<% } %>

</body>
</html>