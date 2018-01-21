<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<title>Login</title>
</head>

<body>
<f:view>
	<h3 align="center" style="font-weight:bold">IDS517 - Fall 2017 f17g307 - Login</h3>
	<br />
	<hr />
	<br />

	<center>
	<h:form>
	<h:commandLink value="Home" action="#{databaseAccessBean.terminate}"></h:commandLink>
	</h:form>
	</center>
	<br/> <br/> <br/>
	<h:form>
	<center>
		<h:panelGrid columns="2" style="background-color: lightGrey; border-bottom-style: solid;
				border-top-style: solid; border-left-style: solid; border-right-style: solid">
			<h:outputLabel value="UserName" style="font-weight:bold"/>
			<h:inputText id="userName" value="#{databaseAccessInformationBean.userName}" />
			<h:outputLabel value="Password" style="font-weight:bold"/>
			<h:inputSecret id="password"  value="#{databaseAccessInformationBean.password}" />
			
			<h:outputLabel value="Database Type" style="font-weight:bold"/>
			<h:selectOneListbox value="#{databaseAccessInformationBean.dbType}" size="1">
			<f:selectItem itemValue="" itemLabel="--Select--" />
			<f:selectItem itemValue="MYSQL" itemLabel="MYSQL" />
			<f:selectItem itemValue="Oracle" itemLabel="Oracle" />
			<f:selectItem itemValue="DB2" itemLabel="DB2" />
			</h:selectOneListbox>
			
			<h:outputLabel value="Database Host" style="font-weight:bold"/>
			<h:selectOneListbox value="#{databaseAccessInformationBean.dbmsHost}" size="1">
			<f:selectItem itemValue="" itemLabel="--Select--" />
			<f:selectItem itemValue="131.193.209.54" itemLabel="131.193.209.54" />
			<f:selectItem itemValue="131.193.209.57" itemLabel="131.193.209.57" />
			<f:selectItem itemValue="131.193.209.68" itemLabel="131.193.209.68" />
			<f:selectItem itemValue="131.193.209.69" itemLabel="131.193.209.69" />
			<f:selectItem itemValue="localhost" itemLabel="localhost" />
			</h:selectOneListbox>
			
			<h:outputLabel value="Database Schema" style="font-weight:bold"/>
			<h:inputText id="dbmsSchema" value="#{databaseAccessInformationBean.dbSchema}" />
			
			<h:outputLabel value=" " />
			<h:commandButton type="submit" value="Login" style="font-weight:bold" action="#{databaseAccessBean.connect}" />		
		</h:panelGrid>
		<br /><br /><br />
		<h:outputLabel for= "submit" value="#{databaseAccessBean.message}" style="color:red"/>
	</center>
	</h:form>
</f:view>
</body>
</html>