<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
<br><br>
<table width="300" border="0" >
	<tr style="font-weight: bold; background-color: #0066FF;  color: white; height: 20px;">
		<td> Online Users:</td>
		
	</tr>
	<c:forEach items="${usersNamesList}" var="item" varStatus="status">
		
		<tr style="color:#0066FF;  height: 35px;" class="${rowcolor}">
			
			<td>${item}</td>
			
		</tr>
	</c:forEach>
</table>



</body>
</html>