<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
     "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<s:url value="/assets/images/folder.png" 
				var="add_folder" />

<s:url value="/assets/images/dms_image.png" 
				var="folder" />
<s:url value="/assets/images/public_folder.png" 
				var="public_folder" />
<s:url value="/assets/images/add_file_25x25.png" 
				var="add_file" />
				
<s:url value="/assets/images/submit.png" 
				var="submit" />

<s:url value="/assets/js/dialog_box.js" 
				var="dialog_box" />
<s:url value="/assets/js/popup-file.js" 
				var="popup-file" />
<s:url value="/assets/js/jquery-ui-1-9-git.js" 
				var="jquery-ui-1-9-git.js" />
				
<s:url value="/assets/css/dialog_box.css" 
				var="dialog_box_css" />	
<s:url value="/assets/css/button.css" 
				var="button_css" />	
<s:url value="/assets/css/popup_file.css" 
				var="popup_file" />		
				
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.6/jquery.min.js"></script> 				
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
  <script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>


<link rel="stylesheet" type="text/css" href="${dialog_box_css}" />
<link rel="stylesheet" type="text/css" href="${button_css}" />
<link rel="stylesheet" type="text/css" href="${popup_file}" />

<script type="text/javascript" src="${dialog_box}"></script>
<script type="text/javascript" src="${popup-file}"></script>
<script type="text/javascript" src="${jquery-ui-1-9-git.js}"></script>
<title>Email with Spring MVC</title>
</head>
<style>
#username{
color:green;
text-align:left;
margin-left:100px;
}
#mail{
color:red;
text-align:left;
margin-left:100px;
}
</style>
<body>
    <center>
        <h1>Sending e-mail </h1>
        <div id="username"> ${username}</div> <div id="mail">${email}</div>
        <form method="post" action="sendEmail.do">
            <table border="0" width="80%">
                <tr>
                  
                  <td></td>
                  <td></td>
                  
                    <td><input type="hidden" name="recipient" value="${email}" size="65" /></td>
                    <td></td>
                </tr>
                <tr>
                    
                    <td><input type="text" name="subject"  placeholder="Subject" size="65" /></td>
                    <td></td>
                </tr>
                <tr>
                    
                    <td><textarea cols="65" rows="10" name="message" placeholder="Your message..." ></textarea></td>
                    <td></td>
                </tr>               
                <tr>
                    <td colspan="2" align="center">
                        <input type="submit" value="Send E-mail"
style=" background-color:#3366FF ;
background-repeat:no-repeat; width:100px; height:27px;outline: 0;
  padding:0; border:none;color:white;" name="submit"
                        
                         />
                    </td>
                </tr>
            </table>
        </form>
    </center>
</body>
</html>