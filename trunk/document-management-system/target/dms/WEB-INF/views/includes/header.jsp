<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<html>
<style>
p2.normal {font-style:normal;}
p2.italic {font-style:italic;}
p2.oblique {font-style:oblique;}
p2 {font-size:150%;}
p1 {font-size:120%;}
dms-string{font-size:200%;}
</style>
</head>
<body>

<security:authentication property="principal.username" var="uName"/>

<table border="0" style="width: 100%; color: #0066FF; height:90px;">
	<tr>
		<td>
		<s:url value="/assets/images/dms_image.png" 
				var="dmslogo" />
			
			<s:url value="/docs" var="docsLink"/>	
			<a href="${docsLink}" style="border: none;">
			<img src="${dmslogo}" 
				alt="DOCUMENT MANAGEMENT SYSTEM" 
				width="170"
				height="80" />
			</a><dms-string>&nbsp;&nbsp;DOCUMENT MANAGEMENT SYSTEM</dms-string>
		</td>
		<td>&nbsp;</td>

		<td width="150">
		
		<%--
		Show Welcome message and logout link only when some user has logged in.
		 --%>
		 		
		<security:authorize access="isAuthenticated()">
			<table style="width: 100%; color: #0066FF;" border="0">
				<tr>
					<td><s:url value="/j_spring_security_logout" var="logoutUrl" />
					<a href="${logoutUrl}" style="color: #0066FF; margin-left:70%;">Logout</a></td>
				</tr>
				<tr>
					<td><br><br><br><p1>Welcome </p1>,&nbsp;<p2 class="italic">${uName}</p2></td>
				
					<td>&nbsp;</td>
				</tr>
				
			</table>
		</security:authorize>
		</td>
	</tr>
</table>
</body>
</html>