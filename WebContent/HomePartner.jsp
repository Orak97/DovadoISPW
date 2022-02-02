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
	
	<h2>Le mie attività:</h2>
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

<%}%>





</body>
</html>