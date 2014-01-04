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
<sf:form method="POST" modelAttribute="docForm"
	enctype="multipart/form-data">
	<fieldset title="Document Upload">
	<table cellspacing="0">
		<tr>
			<th><label for="subject">Subject:</label></th>
			<td><sf:input path="subject" size="15" id="subject" /></td>
		</tr>
		<tr title="Category of the Document">
			<th><label for="category">Category:</label></th>
			<td><sf:input path="category" size="15" id="category" /></td>
		</tr>
		<tr
			title="Documents are searched based on the keywords. You can enter a comma separated list of keyword here.">
			<th><label for="keywords">Keywords:</label></th>
			<td><sf:input path="keywords" size="15" id="keywords" /></td>
		</tr>
		<tr
			title="Please enter any comments or notes about the document here.">
			<th><label for="comments">Comments:</label></th>
			<td><sf:textarea path="comments" id="comments" cols="50"
				rows="10" /></td>
		</tr>
		<tr title="Please choose Document to upload.">
			<th><label for="document">Document:</label></th>
			<td><input name="document" type="file" />
		</tr>
		<tr title="If Selected Document will be stored in a public area and will be accessable to all.">
			<th><label for="isPublic">Public:</label></th>
			<td><sf:checkbox  path="isPublic" cssStyle="width:12px;"/><span style="font-size:9px; padding-left:20px;color:red;">Caution: Public documents are visible to all</span>
			</td>
		</tr>
		<tr>
			<th>&nbsp;</th>
			<td><input name="commit" type="submit"
				value="Upload Document" style="width:125px;"/></td>
	</table>
	</fieldset>
</sf:form>

</body>
</html>