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
<s:url value="/assets/css/button.css" 
				var="button_css" />
				
<link rel="stylesheet" type="text/css" href="${button_css}" />

<script>


var count = 1;
function generatenew(){
	var element =  document.getElementById("txtBox");
	var regexp = /^[A-Za-z0-9_-]{3,20}$/;
	if (count > 1) {
        
       showDialog('Error','Error! Please,type folder name. ','error');            
	}
	
	$("lol1").show();
	
	 count++;//problem with count if it in the end of code,pizdets koro4e...
	 //document.abc.role1.style.visibility="visible"; 
	// document.abc.role2.style.visibility="visible"; 
	 document.abc.submit.style.visibility="visible";
	// document.abc.kelas.style.visibility="visible";
	 document.abc.select.style.visibility="visible";
	 
    
   
}

function Empty(element){
	var regexp = /^[A-Za-z0-9_-]{1,20}$/;  // 1,20 is it range value 

    if(element.value.trim()== ""){

      document.getElementById("errorMessage").innerHTML="Folder name is empty. Try again.";
        element.focus();
        
        return false;
    }  
    if(!element.value.match(regexp))
    {

    	document.getElementById("errorMessage").innerHTML="Unavailable characters. Try again.";

    return false;
    }

    return true;   
}

function reloadPage()
{
location.reload();
}

function showStuff(hide) {
    document.getElementById('hide').style.display = 'block';
}

</script>
</head>
<body>


<br>
User:
<table width="500" border="0" >
	<tr style="font-weight: bold; background-color: #321900; color: #FFFFFF;">
		<td>User Id</td>
		<td width="">User Name</td>
		<td width="">First Name</td>
		<td width="">Last Name</td>
		
		<!-- <td width="75">Enabled</td>-->
	</tr>
	<c:forEach items="${usq}" var="user" varStatus="status" >
		<c:choose>
			<c:when test="${status.count mod 2 eq 0}">
				<c:set var="rowcolor" value="even" />
			</c:when>
			<c:otherwise>
			<c:set var="rowcolor" value="odd"/>
			</c:otherwise>
		</c:choose>
		<c:if test="${status.count < 2}">
		<tr class="${rowcolor}">
			<td>${user.userId}</td>
			<td>${user.username}</td>
			<td>${user.firstname}</td>
			<td>${user.lastname}</td>
		<!-- 	<td><c:forEach items="${user.userRoles}" var="role">
				${role.authority}<br/>
			</c:forEach></td> -->
			
		</tr>
		</c:if>
	</c:forEach>
	
</table>

<br>

<!-- USER ROLES  -->

User Roles: <br><br>
<table width="500" border="0" >
	
	<c:forEach items="${usq}" var="user" varStatus="status" >
	<c:if test="${status.count < 2}">
	<c:forEach items="${user.userRoles}" var="role">
		<tr>	<td>${role.authority}</td>
				<td>
			
		<c:choose>		
		<c:when test="${role.authority == 'ROLE_USER'}">
		<img  src="<s:url value="/assets/images/delete-icon-grey.png"/>"
						style="width: 18px; height: 18px; float:left;" title="No Permissions""/>
		</c:when>	
		<c:when test="${role.authority == 'ROLE_ADMIN'}">
		<img  src="<s:url value="/assets/images/delete-icon-grey.png"/>"
						style="width: 18px; height: 18px;" alt="No Permissions" title="No Permissions"/>
		</c:when>	
		<c:otherwise>
		
		<a href="javascript:deleteRole('<s:url value="/deleterole"/>?id=${role.userRoleId }');">
					<img title="Click to delete role" src="<s:url value="/assets/images/delete-icon.png"/>"
						style="width: 18px; height: 18px;" /></a>
		</c:otherwise>
		</c:choose>
		</td></tr>
			</c:forEach> 
	
			<!-- edw kanoume set se metavlites -->
			<c:set var="userId" value="${user.userId}"></c:set>
			<c:set var="username" value="${user.username}"></c:set>
	</c:if>
	</c:forEach>
	
	 </table>
	 <!-- END OF USER ROLES -->
	 
	 
	<!-- find all users role and save that in varibles -->
	
<c:set var="coordinator" value="false" />
<c:set var="tech_manager" value="false" />
<c:set var="wpleader" value="false" />
 <c:forEach items="${usq}" var="user" varStatus="status" >
    <c:forEach items="${user.userRoles}" var="role">
		<c:if test="${role.authority == 'ROLE_COORDINATOR'}">
		<c:set var="coordinator" value="true" />
		</c:if>	
		
		<c:if test="${role.authority == 'ROLE_TECHNICAL_MANAGER'}">
		<c:set var="tech_manager" value="true" />
		</c:if>	
		
		<c:if test="${role.authority == 'ROLE_WORKPACKAGE_LEADER'}">
		<c:set var="wpleader" value="true" />
		</c:if>	
	</c:forEach>
 </c:forEach>

<!-- end of find bla bla bla -->
<!-- userId -->
	
<!-- end of userId -->
<security:authorize access="hasAnyRole('ROLE_COORDINATOR')">

 <c:if test ="${wpleader==true}">
 <!-- else if is alredy wp_leader we set number of folder -->
  <s:url value="/forsearchwpfold" var="forsearchwpfold" />
   <form name='fold_as_link' method="post" action="${forsearchwpfold}" >
				   <input type="hidden" value="${username}" name="username" /> 
				  
	  <button id="button">WP Leader list</button> 
				
	</form>
</c:if>

</security:authorize>

<br>
<security:authorize access="hasAnyRole('ROLE_ADMIN')">
 <c:if test ="${tech_manager==false or coordinator==false}">
<input type="button" id="button" onclick="showStuff(hide);" value="Set New Role">
</c:if>
</security:authorize>

 <security:authorize access="hasAnyRole('ROLE_COORDINATOR')">
 <c:if test ="${wpleader==false}">

<input type="button" id="button" onclick="showStuff(hide);" value="Show ">
</c:if>
</security:authorize>




 
<br>
<div id="hide" style="display:none;float:left;">

<!-- when user has role admin -->
 <br>
<security:authorize access="hasAnyRole('ROLE_ADMIN')">
 <form method="post" action="/dms/setnewrole"  style="position:absolute; left:17%;">
 
 <c:if test ="${coordinator==false}">
 <input type="checkbox" name="role" id="role1" value="ROLE_COORDINATOR">ROLE_COORDINATOR
 </c:if>
  <br>
  <c:if test ="${tech_manager==false}">
<input type="checkbox" name="role" id="role2" value="ROLE_TECHNICAL_MANAGER" >ROLE_TECHNICAL_MANAGER
</c:if>
  <br><br>

<input type="hidden" value="${userId}" name="userId" /> 
 <input type="hidden" value="${username}" name="username" />

 <input type="submit" id="submit" name="submit" value="Set" 
 style=" background-color:#3366FF ;
background-repeat:no-repeat; text-align:center; width:60px; height:27px;outline: 0;
  padding:0; border:none;color:white;" name="submit"
  > 
 </form>
 </security:authorize>
 <!-- end of user admin --> 
 
 
  
 
   <c:out value="${str}"/>
 
 <security:authorize access="hasAnyRole('ROLE_COORDINATOR')">
 <!-- if user is not wp_leader we are set role for him -->
 <s:url value="/setnewrole" var="setnewrole" />
 <c:if test ="${wpleader==false}">
 <form method="post" action="${setnewrole}"  style="position:absolute; left:17%;">
 <input type="checkbox" name="role" id="role3" value="ROLE_WORKPACKAGE_LEADER" >ROLE_WORKPACKAGE_LEADER
 <input type="hidden" value="${userId}" name="userId" />
 <input type="hidden" value="${username}" name="username" />
 
    <input type="submit" id="submit" name="submit" value="Set " 
    style=" background-color:#3366FF ;
background-repeat:no-repeat; text-align:center; width:60px; height:27px;outline: 0;
  padding:0; border:none;color:white;" name="submit"
   > 
 <!-- style="position:absolute; float:left;" -->
 </form>
 </c:if>

<!-- 
 <c:if test ="${wpleader==true}">
  <s:url value="/dms/projects/wpleaderlist" var="wpleaderlist" />
   <form name='fold_as_link' method="post" action="${wpleaderlist}" >
				   <input type="hidden" value="${username}" name="username" /> 
				  
	  <button id="button"> list of folders</button> 
				
	</form>



<br>
  </c:if>
   -->
 </security:authorize>
 
 </div>
 

</body>
</html>