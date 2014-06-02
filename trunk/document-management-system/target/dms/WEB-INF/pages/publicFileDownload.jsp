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


<%-- 
<security:authentication property="principal.username" var="uName"/>
--%>
Click to document for download: <br><br>
<table style="width: 100%; font-size: 12px;" border="0" id="searchTbl">


	<c:choose>
			<c:when
				test="${plst.documentType eq 'application/msword' 
			or plst.documentType eq 'application/vnd.ms-word.document.12'}">
				<s:url value="/assets/images/word-icon.png" var="icon" />
			</c:when>
			<c:when test="${plst.documentType eq 'application/pdf'}">
				<s:url value="/assets/images/pdf-icon.png" var="icon" />
			</c:when>
			<c:when test="${plst.documentType eq 'text/plain'}">
				<s:url value="/assets/images/text-icon.png" var="icon" />
			</c:when>
			<c:when
				test="${plst.documentType eq 'application/vnd.ms-excel'
			or plst.documentType eq 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}">
				<s:url value="/assets/images/excel-icon.png" var="icon" />
			</c:when>
			<c:when test="${plst.documentType eq 'text/html'}">
				<s:url value="/assets/images/ie-icon.png" var="icon" />
			</c:when>
			<c:when
				test="${plst.documentType eq 'application/zip' 
			or plst.documentType eq 'application/java-archive'}">
				<s:url value="/assets/images/zip-icon.png" var="icon" />
			</c:when>
			<c:when test="${plst.documentType eq 'application/octet-stream'}">
				<s:url value="/assets/images/rar-icon.png" var="icon" />
			</c:when>
			<c:when
				test="${plst.documentType eq 'image/jpeg'
			or plst.documentType eq 'image/gif'
			or plst.documentType eq 'image/png'}">
				<s:url value="/assets/images/img-icon.png" var="icon" />
			</c:when>
			<c:otherwise> 
			 <s:url value="/assets/images/file_30x30.png" var="icon" />
			 </c:otherwise>
		</c:choose>
<tr>
<td>

</td>
<tr class="${className}">
			<td width="25">${status.count}</td>
			<td width="25">
			<div
				style="width: 25px; height: 25px; float: left; margin-right: 10px;"><img
				src="${icon}" /></div>
			</td>
			<td title="Click link to download Document"><a
				id="${plst.publicId}"
				href="<s:url value="/pbdownload/${plst.publicId}"/>">${plst.documentFileName}</a></td>
			
			
</tr>



</table>



</body>
</html>