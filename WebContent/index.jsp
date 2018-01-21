<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<title>f17g307 Home</title>
</head>

<body>
<f:view>
	<h3 align="center" style="font-weight:bold">IDS517 - Fall 2017 f17g307 - Home </h3>
	<hr />
	<br />
	<br/>
		<center>
			<a href="login.jsp" >Login</a><br /><br />
			<a href="http://www.uic.edu" target="_blank">UIC</a><br /><br /><br/>
			
			<h:outputLabel value="Individual team members JSF implementation:" style="font-weight:bold"/>
			<br /><br />
				
			<h:panelGrid columns="2" border="1">
			<h:outputLabel value="Group Members" style="font-weight:bold"/>
			<h:outputLabel value="Servers" style="font-weight:bold"/>
						
			<h:outputLabel value="Charanya Subramanian"/>
			<a href = "http://131.193.209.54:8080/f17g307_csubra2/faces/"> Server 54 </a><br />
			<a href = "http://131.193.209.57:8080/f17g307_csubra2/faces/"> Server 57 </a><br />
			<a href = "http://131.193.209.68:8080/f17g307_csubra2/faces/"> Server 68 </a><br />
			<a href = "http://131.193.209.69:8080/f17g307_csubra2/faces/"> Server 69 </a><br />
			<h:outputLabel value="Mittali Sawant"/>
			<a href = "http://131.193.209.54:8080/f17g307_msawan3/faces/"> Server 54 </a><br />
			<a href = "http://131.193.209.57:8080/f17g307_msawan3/faces/"> Server 57 </a><br />
			<a href = "http://131.193.209.68:8080/f17g307_msawan3/faces/"> Server 68 </a><br />
			<a href = "http://131.193.209.69:8080/f17g307_msawan3/faces/"> Server 69 </a><br />
			<h:outputLabel value="Mathew Vadakkencheril"/>
			<a href = "http://131.193.209.54:8080/f17g307_mvadak2/faces/"> Server 54 </a><br />
			<a href = "http://131.193.209.57:8080/f17g307_mvadak2/faces/"> Server 57 </a><br />
			<a href = "http://131.193.209.68:8080/f17g307_mvadak2/faces/"> Server 68 </a><br />
			<a href = "http://131.193.209.69:8080/f17g307_mvadak2/faces/"> Server 69 </a><br />
			
			</h:panelGrid>
			
		</center>
		
</f:view>
</body>
</html>