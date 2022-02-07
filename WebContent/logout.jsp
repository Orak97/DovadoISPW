<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<%session.invalidate();
	response.sendRedirect("totem.jsp");
%>

<head>
<meta charset="UTF-8">
<title>Logout</title>
</head>
<body>

</body>
</html>