<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Public Files</title>
</head>
<body>

<security:authentication property="principal.username" var="uName"/>


<table style="width: 100%; font-size: 12px;" border="0" id="searchTbl">

<tr style="font-weight: bold;">
		<td width="25">Sr.No.</td>
		<td width="25">&nbsp;</td>
		<td>Document</td>
		<td width="120">Owner</td>
		<td>Download Page</td>
		


		<td width="20">&nbsp;</td>
	</tr>


<c:forEach items="${plst}" var="obj" varStatus="status">

		<c:choose>
			<c:when
				test="${obj.documentType eq 'application/msword' 
			or obj.documentType eq 'application/vnd.ms-word.document.12'}">
				<s:url value="/assets/images/word-icon.png" var="icon" />
			</c:when>
			<c:when test="${obj.documentType eq 'application/pdf'}">
				<s:url value="/assets/images/pdf-icon.png" var="icon" />
			</c:when>
			<c:when test="${obj.documentType eq 'text/plain'}">
				<s:url value="/assets/images/text-icon.png" var="icon" />
			</c:when>
			<c:when
				test="${obj.documentType eq 'application/vnd.ms-excel'
			or obj.documentType eq 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}">
				<s:url value="/assets/images/excel-icon.png" var="icon" />
			</c:when>
			<c:when test="${obj.documentType eq 'text/html'}">
				<s:url value="/assets/images/ie-icon.png" var="icon" />
			</c:when>
			<c:when
				test="${obj.documentType eq 'application/zip' 
			or obj.documentType eq 'application/java-archive'}">
				<s:url value="/assets/images/zip-icon.png" var="icon" />
			</c:when>
			<c:when test="${obj.documentType eq 'application/octet-stream'}">
				<s:url value="/assets/images/rar-icon.png" var="icon" />
			</c:when>
			<c:when
				test="${obj.documentType eq 'image/jpeg'
			or obj.documentType eq 'image/gif'
			or obj.documentType eq 'image/png'}">
				<s:url value="/assets/images/img-icon.png" var="icon" />
			</c:when>
			<c:otherwise> 
			 <s:url value="/assets/images/file_30x30.png" var="icon" />
			 </c:otherwise>
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
				id="${obj.publicId}"
				href="<s:url value="/pbdownload/${obj.publicId}"/>">${obj.documentFileName}</a></td>
			
			<td width="120">${obj.createUser}</td>
			
			<td><a href="/dms/public/download/${obj.owner}/${obj.publicId} " target="_blank">Go to link</a></td>
		
				
			
			<td width="20">
			
			<%-- 
			User should be able to see the delete link only if he is an admin or owner 
			of the document. This will be helpful in case of public docs where 
			user will not be able to delete public docs that he does not own.
			--%>
			<%-- 
			<security:authorize  access="hasRole('ROLE_ADMIN')" var="userIsAdmin" />
			--%>
			<c:choose>
				<c:when test="${uName eq obj.owner or userIsAdmin}">
					<a href="javascript:deleteDocument('<s:url value="/pbdocs/delete"/>?id=${obj.publicId}');">
					<img title="Click to delete file" src="<s:url value="/assets/images/file-delete-1.png"/>"
						style="width: 24px; height: 24px;" /></a>
				</c:when>
				<c:otherwise>
					<img  src="<s:url value="/assets/images/delete-icon-grey.png"/>"
						style="width: 24px; height: 24px;" alt="No Permissions" title="No Permissions"/>
				</c:otherwise>
			</c:choose>
			</td>
		</tr>
	</c:forEach>

</table>



</body>
</html>