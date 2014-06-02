<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  -->
  
<!DOCTYPE html> 
<%@page import="javax.servlet.jsp.jstl.core.LoopTagSupport"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Errors</title>
</head>
<body>
 User is not exist OR  user is already wp leader for current folder. <br>
 If you want to change wp leader for this folder you must insert username <br> when user has workpackage leader's role.<br>
 
 <s:url value="/wpfolders" var="wpfolders" />
  <a href="${wpfolders}">WP Folders</a>

</body>
</html>