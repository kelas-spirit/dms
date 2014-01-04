<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="javax.servlet.jsp.jstl.core.LoopTagSupport"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

</head>
<body>
<%--
<div class="message">${msg}</div>
 --%>
 
<security:authentication property="principal.username" var="uName"/>
 
<c:if test="${mgs ne '' and msg ne null}">
	<h3>${msg}</h3>
</c:if>
<br />
<table style="background-color: #FFB8A6; width: 100%;">
	<tr>
		<td>Total ${docCount} documents on this portal.</td>
	</tr>
</table>
<table style="width: 100%; font-size: 12px;" border="0" id="searchTbl">
	<tr style="font-weight: bold;">
		<td width="25">Sr.No.</td>
		<td width="25">&nbsp;</td>
		<td>Document</td>
		<td width="120">Owner</td>
		<td width="120">Uploaded by</td>
		<td width="100">Created Date</td>
		<td width="100">Updated Date</td>
		<td width="100">Size</td>
		<td width="20">&nbsp;</td>
	</tr>

	<c:forEach items="${lst}" var="item" varStatus="status">

		<c:choose>
			<c:when
				test="${item.documentType eq 'application/msword' 
			or item.documentType eq 'application/vnd.ms-word.document.12'}">
				<s:url value="/assets/images/word-icon.png" var="icon" />
			</c:when>
			<c:when test="${item.documentType eq 'application/pdf'}">
				<s:url value="/assets/images/pdf-icon.png" var="icon" />
			</c:when>
			<c:when test="${item.documentType eq 'text/plain'}">
				<s:url value="/assets/images/text-icon.png" var="icon" />
			</c:when>
			<c:when
				test="${item.documentType eq 'application/vnd.ms-excel'
			or item.documentType eq 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}">
				<s:url value="/assets/images/excel-icon.png" var="icon" />
			</c:when>
			<c:when test="${item.documentType eq 'text/html'}">
				<s:url value="/assets/images/ie-icon.png" var="icon" />
			</c:when>
			<c:when
				test="${item.documentType eq 'application/zip' 
			or item.documentType eq 'application/java-archive'}">
				<s:url value="/assets/images/zip-icon.png" var="icon" />
			</c:when>
			<c:when test="${item.documentType eq 'application/octet-stream'}">
				<s:url value="/assets/images/rar-icon.png" var="icon" />
			</c:when>
			<c:when
				test="${item.documentType eq 'image/jpeg'
			or item.documentType eq 'image/gif'
			or item.documentType eq 'image/png'}">
				<s:url value="/assets/images/img-icon.png" var="icon" />
			</c:when>
		</c:choose>

		<c:choose>
			<c:when test="${(status.count mod 2 ) eq 0}">
				<c:set var="className" value="even" scope="page" />
			</c:when>
			<c:when test="${(status.count mod 2 ) ne 0}">
				<c:set var="className" value="odd" scope="page" />
			</c:when>

		</c:choose>
		<tr class="${className}">
			<td width="25">${status.count}</td>
			<td width="25">
			<div
				style="width: 25px; height: 25px; float: left; margin-right: 10px;"><img
				src="${icon}" /></div>
			</td>
			<td title="Click link to download Document"><a
				id="${item.metadataId}"
				href="<s:url value="/download/${item.metadataId}"/>">${item.documentFileName}</a></td>
			<td width="120">${item.owner}</td>
			<td width="120">${item.createUser}</td>
			<td width="100"><fmt:formatDate value="${item.createdDate}" /></td>
			<td width="100"><c:if test="${updatedDate eq null}">
					Not Updated
				</c:if></td>
			<td width="100" >${item.documentSize}&nbsp;Bytes</td>
			<td width="20">
			
			<%-- 
			User should be able to see the delete link only if he is an admin or owner 
			of the document. This will be helpful in case of public docs where 
			user will not be able to delete public docs that he does not own.
			--%>
			<security:authorize  access="hasRole('ROLE_ADMIN')" var="userIsAdmin" />
			
			<c:choose>
				<c:when test="${uName eq item.owner or userIsAdmin}">
					<a href="javascript:deleteDocument('<s:url value="/docs/delete"/>?id=${item.metadataId}');">
					<img src="<s:url value="/assets/images/delete-icon.png"/>"
						style="width: 24px; height: 24px;" /></a>
				</c:when>
				<c:otherwise>
					<img src="<s:url value="/assets/images/delete-icon-grey.png"/>"
						style="width: 24px; height: 24px;" alt="No Permissions" title="No Permissions"/>
				</c:otherwise>
			</c:choose>
			</td>
		</tr>
	</c:forEach>
</table>

</body>
</html>