<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "java.io.*,java.util.*, logic.model.DAOPreferences, logic.model.DAOActivity, logic.model.DAOSuperUser, logic.model.SuperActivity,logic.model.Log" %>

    <!doctype html>
	<html lang="en">
	<head>
		<%
				
		boolean logged = true;
		
		String titolo = (String) application.getAttribute("titolo");
		System.out.println(System.getProperty("user.dir"));
		Log.getInstance().getLogger().info(titolo);
		
		if (titolo.equals("login")) {
			if( session.getAttribute("user") != null) {				
				response.sendRedirect("Home.jsp");
			}
			logged = false;
			
		} else if(titolo.equals("register")){
			if( session.getAttribute("user") != null) {				
				response.sendRedirect("Home.jsp");
			}
			logged = false;
			
		} else if(session.getAttribute("user") == null) {
				response.sendRedirect("login.jsp");
				
		} else {
			session.setMaxInactiveInterval(25*60);
		}

	  		int active = 0;

	  		switch(titolo){
	  			case "Home": active = 0;
	  			break;

	  			case "Create Activity": active = 1;
	  			break;

	  			case "Schedule": active = 2;
	  			break;

	  			case "HomeLogin": active = 3;
	  			break;
	  		};
	  	%>

	  	<title>Dovado - <%= titolo %> </title>

	<!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">

    <!-- nostro css -->
    <link rel="stylesheet" href="css/dovado.css">
    
    <!-- css di leaflet -->
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.7.1/dist/leaflet.css"
	  integrity="sha512-xodZBNTC5n17Xt2atTPuE1HxjVMSvLVW9ocqUKLsCC5CXdbqCmblAshOMAS6/keqq/sMZMZ19scR4PsZChSR7A=="
	  crossorigin=""/>
	   
	  <!-- Make sure you put this AFTER Leaflet's CSS -->
	 <script src="https://unpkg.com/leaflet@1.7.1/dist/leaflet.js"
	   integrity="sha512-XQoYMqMTK8LvdxXYG3nZ448hOEQiglfqkJs1NOQV44cWnUrBc8PkAOcXy20w0vlaXaVUearIOBhiXZ5V3ynxwA=="
	   crossorigin=""></script>
	   
	   <!-- robe che ha messo andre non ho capito bene a che servono -->
	   
	   <!-- Load Esri Leaflet from CDN -->
		<script src="https://unpkg.com/esri-leaflet@2.5.3/dist/esri-leaflet.js"></script>
		
		 <!-- Load Esri Leaflet Geocoder from CDN -->
		<link rel="stylesheet" href="https://unpkg.com/esri-leaflet-geocoder@2.3.3/dist/esri-leaflet-geocoder.css">
		<script src="https://unpkg.com/esri-leaflet-geocoder@2.3.3/dist/esri-leaflet-geocoder.js"></script>
		
		<!-- fine delle robe di andre -->

  	</head>
	  <body>
	    <!-- inizio navbar -->

	    <nav class="navbar navbar-expand-lg navbar-dark fixed-top">
	      <div class="container-fluid">
	        <a class="navbar-brand" href="Home.jsp">
	        	 <img src="logo/DovadoLogo(3).png" alt="" width="auto" height="50vh">
	        </a>
	        <%
	        String path = ((HttpServletRequest) request).getRequestURI();
	        System.err.println(path+ "\n");


	        System.err.println("\n"+logged);
	        if (logged) { //qua se non e loggato si mette a false cosi non compaiono i pulsanti %>

	        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
	          <span class="navbar-toggler-icon"></span>
	        </button>
	        <div class="collapse navbar-collapse" id="navbarNav">
	          <ul class="navbar-nav">
	            <li class="nav-item">
	              <a class="nav-link <% if(active == 0) out.print("active"); %>" aria-current="page" href="Home.jsp">Home</a>
	            </li>
	            <li class="nav-item">
	              <a class="nav-link <% if(active == 1) out.print("active"); %>" href="CreateActivity.jsp">Create Activity</a>
	            </li>
	            <li class="nav-item">
	              <a class="nav-link <% if(active == 2) out.print("active"); %>" href="Schedule.jsp">Schedule</a>
	            </li>
	          </ul>
	        </div>
	        <% } %>
	      </div>
	    </nav>


	  <!-- fine navbar -->

	    <!-- Optional JavaScript; choose one of the two! -->

	    <!-- Option 1: Bootstrap Bundle with Popper -->
	    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>

	    <!-- Option 2: Separate Popper and Bootstrap JS -->
	    <!--
	    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js" integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p" crossorigin="anonymous"></script>
	    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.min.js" integrity="sha384-Atwg2Pkwv9vp0ygtn1JAojH0nYbwNJLPhwyoVbhoPwBhjQPR5VtM2+xf0Uwh9KtT" crossorigin="anonymous"></script>
	    -->
	    
	    <!-- including icons of bootstrap: -->
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.0/font/bootstrap-icons.css">