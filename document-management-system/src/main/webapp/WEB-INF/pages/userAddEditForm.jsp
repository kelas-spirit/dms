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

<sf:form method="POST" modelAttribute="formBean">
	<s:bind path="*">
		<c:if test="${status.error}">
			<div id="message" class="error">Form has errors</div>
		</c:if>
	</s:bind>
	<fieldset title="Register new User"><legend>Register
	new User</legend>

	<table cellspacing="5" align="left" style="text-align: left;"
		border="0">
		<tr>
			<th><label for="username">Username:</label></th>
			<td style="width:160px;"><sf:input path="username" size="15" id="username"
				maxlength="15" cssStyle="width:150px;" /></td>
			<td>&nbsp;<sf:errors path="username" cssStyle="color:red;" /></td>
		</tr>
		<tr title="Password">
			<th><label for="password">Password:</label></th>
			<td  style="width:160px;"><sf:password path="password" size="15" id="password"
				maxlength="15" cssStyle="width:150px;" /></td>
			<td>&nbsp;<sf:errors path="password" cssStyle="color:red;" /></td>
		</tr>
		<tr title="Re-enter Password">
			<th><label for="rePassword">Confirm Password:</label></th>
			<td  style="width:160px;"><sf:password path="rePassword" size="15" id="rePassword"
				maxlength="15" cssStyle="width:150px;" /></td>
			<td>&nbsp;<sf:errors path="rePassword" cssStyle="color:red;" /></td>
		</tr>
		<tr title="">
			<th><label for="roles">Roles:</label></th>
			<td  style="width:160px;">
			<table>
				<%--
				<tr>
					<td>General User Role:</td>
					<td><sf:checkbox path="roles" id="roles" value="ROLE_USER"
						cssStyle="width:20px;" disabled="false"/></td>
					<td style="font-size: 9">Has access to personal and public <br />
					documents only.</td>
				</tr>
				 --%>
				<tr>
					<td>Admin Role:</td>
					<td><sf:checkbox path="roles" id="roles" value="ROLE_ADMIN"
						cssStyle="width:20px;" /></td>
				</tr>
			</table>
			</td>
			<td style="font-size: 9; color: red;">Admin Role should be granted with
					Caution<br />
					User with this role will have access to all the<br />
					documents.</td>
		</tr>
		<tr>
			<th>&nbsp;</th>
			<td><input name="commit" type="submit" value="Register Me"
				style="width: 125px;" /></td>
			<td>&nbsp;</td>
	</table>
	</fieldset>
</sf:form>
<table width="500" border="0" >
	<tr style="font-weight: bold; background-color: #321900; color: #FFFFFF;">
		<td width="50">Sr.No.</td>
		<td width="">User Name</td>
		<td width="175">Roles</td>
		<td width="75">Enabled</td>
	</tr>
	<c:forEach items="${users}" var="item" varStatus="status">
		<c:choose>
			<c:when test="${status.count mod 2 eq 0}">
				<c:set var="rowcolor" value="even" />
			</c:when>
			<c:otherwise>
			<c:set var="rowcolor" value="odd"/>
			</c:otherwise>
		</c:choose>
		<tr class="${rowcolor}">
			<td>${status.count}</td>
			<td>${item.username}</td>
			<td><c:forEach items="${item.userRoles}" var="role">
				${role.authority}<br/>
			</c:forEach></td>
			<td><c:choose>
				<c:when test="${item.enabled eq 1}">
					YES
				</c:when>
				<c:otherwise>
					NO
				</c:otherwise>
			</c:choose></td>
		</tr>
	</c:forEach>
</table>

</body>
</html>