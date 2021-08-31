<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
	<%	if(session.isNew()) {
		
		response.sendRedirect("login.jsp");
	} else {
		session.setMaxInactiveInterval(10);	
	}%>

    <%@ page import = "java.io.*,java.util.*" %>

    <% application.setAttribute( "titolo" , "Home"); %>

	<jsp:include page="Navbar.jsp" />
	
	<!-- non serve mettere un body perche viene incluso dentro navbar -->
	
	<div class="container-fluid home">
		<div class="row pt-6">
		
			<div class="col-4 events-list">
			<div class="row row-cols-1 row-cols-md-1 g-1">
			  <div class="col">
			    <div class="card">
			      <img src="https://source.unsplash.com/random" class="card-img-top" alt="...">
			      <div class="card-body">
			        <h5 class="card-title">Concerto tupac</h5>
			        <p class="card-text">Orario: 15:30</p>
			        <p class="card-text">Luogo : Roma </p>
			      </div>
			    </div>
			  </div>
			  <div class="col">
			    <div class="card">
			      <img src="https://source.unsplash.com/random" class="card-img-top" alt="...">
			      <div class="card-body">
			        <h5 class="card-title">Card title</h5>
			        <p class="card-text">This is a longer card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
			      </div>
			    </div>
			  </div>
			  <div class="col">
			    <div class="card">
			      <img src="https://source.unsplash.com/random" class="card-img-top" alt="...">
			      <div class="card-body">
			        <h5 class="card-title">Card title</h5>
			        <p class="card-text">This is a longer card with supporting text below as a natural lead-in to additional content.</p>
			      </div>
			    </div>
			  </div>
			  <div class="col">
			    <div class="card">
			      <img src="https://source.unsplash.com/random" class="card-img-top" alt="...">
			      <div class="card-body">
			        <h5 class="card-title">Card title</h5>
			        <p class="card-text">This is a longer card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
			      </div>
			    </div>
			  </div>
			</div>
			</div>
			
			<div class="col-8" style="overflow-y:hidden">
				<iframe src="map.html" title="maps" style="width:100%; height:100%"></iframe> 
			</div>
		</div>
	
	
	</div>
	
</body>
</html>
