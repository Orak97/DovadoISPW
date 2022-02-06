<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

 <%@ page import = "java.io.*,java.util.*, logic.model.Place, logic.model.DAOPlace, java.time.LocalDate, java.time.format.DateTimeFormatter, java.time.LocalTime, logic.controller.CreateActivityController, logic.model.SuperUser, logic.model.User, logic.controller.SpotPlaceController" %>

    <% application.setAttribute( "titolo" , "Create Activity"); %>

	<%@ include file="Navbar.jsp" %>
	
	<jsp:useBean id="findBean" scope="request" class="logic.model.FindActivitiesBean" />
	<jsp:setProperty name="findBean" property="*" />
	
	<jsp:useBean id="createBean" scope="request" class="logic.model.CreateActivityBean" />
	<jsp:setProperty name="createBean" property="*" />

 	<%@include file="formCreateActivity.jsp" %>
</body>
</html>