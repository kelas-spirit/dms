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
<style type="text/css">
.leftNavTabs {
	width: 100%;
	font-size: 12px;
	color: #FFFFFF;
	/*background-color: #321900;*/
	background-color:white;
}

.leftNavTabs td a {
	width: 100%;
	font-size: 12px;
	display: block;
	color: blue;
	background-color: white;
	height: 20px;
	text-align: left;
	vertical-align: middle;
	text-decoration: none;
}

.leftNavTabs td a:HOVER {
	color: white;
	background-color:#00CCFF;
}
</style>

</head>
<body>
<center>Menu</center>
<table class="leftNavTabs" border="0">

	<security:authorize
		access="hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_SUPER_ADMIN')">
		<tr height="20">
			<td><s:url value="/upload" var="newdoc" /> <a href="${newdoc}">
			New Document</a></td>
		</tr>
		
		<tr height="20">
			<td><s:url value="/docs" var="listdocs" /> <a
				href="${listdocs}">Documents</a></td>
		</tr>
		

	</security:authorize>

	<security:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_COORDINATOR')">
		<tr>
			<td><s:url value="/users" var="userList" /> <a
				href="${userList}">Users</a></td>
		</tr>
	</security:authorize>	

	<security:authorize access="hasAnyRole('ROLE_ADMIN')">

		<tr>
			<td><s:url value="/user/reg" var="regUser" /> <a
				href="${regUser}"> New User</a></td>
		</tr>
		
	<tr height="20">
			<td><s:url value="/usersonline" var="usersonline" /> <a
				href="${usersonline}">Users Online</a></td>
		</tr>
	</security:authorize>
	
<security:authorize access="hasAnyRole('ROLE_WORKPACKAGE_LEADER','TASK_PARTICIPANT')">
	<tr height="20">
			<td><s:url value="/projects" var="Projects" /> <a
				href="${Projects}">My Projects</a></td>
		</tr>
	
	
</security:authorize>
<security:authorize access="hasAnyRole('ROLE_COORDINATOR')">
		<tr>
			<td><s:url value="/wpfolders" var="wpfolders" /> <a
				href="${wpfolders}">WP Folders</a></td>
		</tr>
	</security:authorize>
		<security:authorize
		access="hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_SUPER_ADMIN')">
		<tr height="20">
			<td><s:url value="/chat" var="chat" /> <a href="${chat}">Public Chat</a></td>
		</tr>
		</security:authorize>
		
		<security:authorize
		access="hasAnyRole('ROLE_TECHNICAL_MANAGER','ROLE_ADMIN')">
		<tr height="20">
			<td><s:url value="/tmlist" var="tmlist" /> <a href="${tmlist}">Contact to User</a></td>
		</tr>
		</security:authorize>
		
</table>

</body>
</html>