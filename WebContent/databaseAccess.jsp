<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<title>Database Access Page</title>
</head>

<body>
<f:view>
	<h3 align="center" style="font-weight:bold">IDS517 - Fall 2017 f17g307 - Database Access</h3>
	<br />
	<hr />
	<br />

	<center>
	<a href="index.jsp">Home</a><br /><br />
	<h:form>
		<h:commandLink value="Logout" action="#{databaseAccessBean.close}"></h:commandLink><br /><br /><br/>
		<h:commandLink value="Descriptive Analysis" action="#{databaseAccessBean.reset}"></h:commandLink><br /><br />
		<h:commandLink value="Graphical Analysis" action="#{graphicalAnalysisBean.reset}"></h:commandLink><br /><br />	
		<h:commandLink value="Regression Analysis" action="#{regressionAnalysisBean.reset}"></h:commandLink><br /><br />
		<h:commandLink value="File Upload, Execute Script Files" action="#{fileActionBean.reset}"></h:commandLink><br /><br />
	</h:form>
	</center>
	<br />
	
	<h:form >
	<h:panelGrid columns ="2">
	<h:outputText value = "Select table:" style="font-weight:bold" rendered="#{databaseAccessBean.tableListRendered}"/>
	<h:outputText value = "Select column(s):" style="font-weight:bold" rendered="#{databaseAccessBean.columnListRendered}"/>
	<h:selectOneListbox size="10" styleClass="selectOneListbox_mono" 
	     value="#{databaseAccessBean.tableName}" onchange="submit()"
	     		valueChangeListener="#{databaseAccessBean.columnList}" 
	     			rendered="#{databaseAccessBean.tableListRendered}" >
		<f:selectItems value="#{databaseAccessBean.tableViewList}" />
	</h:selectOneListbox>
	<h:selectManyListbox size="10" styleClass="selectManyListbox"
			value="#{databaseAccessBean.columnNames}" rendered="#{databaseAccessBean.columnListRendered}">
		<f:selectItems value="#{databaseAccessBean.columnViewList}" />
	</h:selectManyListbox>
	</h:panelGrid>
	<br/>
	<h:commandButton type="submit" value="Retrieve Data" action="#{databaseAccessBean.generateSqlQuery}" 
														rendered="#{databaseAccessBean.columnListRendered}" />
	&nbsp;
	<h:commandButton type="submit" value="Export" action="#{fileActionBean.processFileDownload}" 
														rendered="#{databaseAccessBean.columnListRendered}" />
	</h:form>
	<br />
	<h:outputText value = "Data table:" rendered="#{databaseAccessBean.renderSet}"/>
	<div style="background-attachment: scroll; overflow:auto; height:400px; background-repeat: repeat">
	<t:dataTable value="#{databaseAccessBean.result}" var="row" rendered="#{databaseAccessBean.renderSet}" 
			border="1" cellspacing="0" cellpadding="1" columnClasses="columnClass1 border"
			headerClass="headerClass" footerClass="footerClass" rowClasses="rowClass2"
			styleClass="dataTableEx" width="900">
		<t:columns var="col" value="#{databaseAccessBean.columnNames}">
			<f:facet name="header">
				<t:outputText styleClass="outputHeader"	value="#{col}" />
			</f:facet>
			<t:outputText styleClass="outputText" value="#{row[col]}" />
		</t:columns>
	</t:dataTable>
	</div>	
</f:view>
</body>
</html>