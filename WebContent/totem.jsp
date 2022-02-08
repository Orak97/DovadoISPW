<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   <%@ page import = "java.io.*,java.util.*, logic.controller.LogPartnerController, logic.model.ScheduledActivity, logic.model.Partner, logic.controller.AddActivityToScheduleController, logic.model.SuperActivity, logic.model.DAOPreferences, logic.model.DAOActivity, logic.model.DAOSchedules, logic.model.SuperUser, logic.model.User, logic.model.Schedule, logic.model.ScheduledActivity, logic.controller.AddActivityToScheduleController" %>
    
<!DOCTYPE html>
<html>
<head>
	  	<title>Dovado - totem </title>

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

	<div class="row">
	    <nav class="navbar navbar-expand-lg navbar-dark fixed-top">
	      <div class="container-fluid">
	        <a class="navbar-brand" href="Home.jsp">
	        	 <img src="logo/DovadoLogo(3).png" alt="" width="auto" height="50vh">
	        </a>
	       </div>
	    </nav>



	    <!-- Optional JavaScript; choose one of the two! -->

	    <!-- Option 1: Bootstrap Bundle with Popper -->
	    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
	    
	    <!-- including icons of bootstrap: -->
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.0/font/bootstrap-icons.css">
		
		<!-- fine navbar -->
		
		
	</div>	
	<div class="container-fluid vh-100 pt-6" >
		<div class="row h-50 align-items-center " >
		
			<div class="col ">
				<h1 class="h3 mb-3 fw-normal text-center ">Diventa esploratore</h1> 
			</div>
			<div class="col">
				<div class="d-flex justify-content-center ">	
					<a class="btn btn-danger btn-lg" href="login.jsp" role="button">Accedi come Esploratore</a>
				</div>
			</div>
		</div>
		<div class="row h-50 align-items-center" style="background-color:#FF5C5C">
			<div class="col">
				<div class="d-flex justify-content-center">
 					<a class="btn btn-primary btn-lg" href="loginPartner.jsp" role="button">Accedi come Partner</a>
				</div>
			</div>
			<div class="col">
				<h1 class="h3 mb-3 fw-normal text-center">Diventa partner</h1>
			</div>
			
		</div>
	</div>

</body>
</html>