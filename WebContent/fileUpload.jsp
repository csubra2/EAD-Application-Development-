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
	<title>File Upload Page</title>
</head>
<body>
<f:view>

	<h3 align="center" style="font-weight:bold">IDS517 - Fall 2017 f17g307 - File Import</h3>
	<br />
	<hr />
	<br />

	<center>
	<a href="index.jsp">Home</a><br /><br /><br />
	<h:form>
	<h:commandLink value = "Back" action="#{fileActionBean.reset}"></h:commandLink>
	</h:form>
	</center>
	<br />
	
	<center>
	<h:form enctype="multipart/form-data">
	<h:panelGrid columns="2">
	<h:outputLabel value="Select a file to upload:" style="font-weight:bold"/>
	<t:inputFileUpload id="fileUpload" label="File to upload"
				storage="default" value="#{fileActionBean.uploadedFile}" size="60"/>
	<h:outputLabel value="File label:" style="font-weight:bold"/>
	<h:inputText id="fileLabel" value="#{fileActionBean.fileLabel}" size="60" />
	<h:outputLabel value=" "/><br />
	<h:commandButton id="upload" action="#{fileActionBean.processFileUpload}" value="Upload"/>
	</h:panelGrid>
	</h:form><br/><br/>
	<h:outputText value="#{fileActionBean.status}" rendered= "#{fileActionBean.fileRendered}" style="color:green"/><br /><br/>
	
	<h:form enctype="multipart/form-data">
	<h:commandButton id="uploadFiles" action="#{fileActionBean.processDisplayFiles}" value="Display Files"/>
	<br/><br />
	<h:selectOneListbox size="10" styleClass="selectOneListbox_mono" 
	     value="#{fileActionBean.selectedFile}" rendered="#{fileActionBean.isExistFiles}">
		<f:selectItems value="#{fileActionBean.sqlFileNames}" />
	</h:selectOneListbox><br /><br />
	<h:outputLabel value="#{fileActionBean.query}" style="color:darkgrey" rendered="#{fileActionBean.isExistFiles}"/><br /><br />
	<h:commandButton id="execute" action="#{fileActionBean.executeDBScripts}" value="Execute" 
													rendered="#{fileActionBean.isExistFiles}"/><br /><br />
	</h:form>
	
	<h:outputText value="#{databaseAccessBean.sqlMessage}" style="color:red" 
										rendered="#{fileActionBean.isExistFiles}"/><br /><br />
										
	<div style="background-attachment: scroll; overflow:auto; height:400px; background-repeat: repeat">
	<t:dataTable value="#{databaseAccessBean.result}" var="row" rendered="#{fileActionBean.isScriptExecuted}" 
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
	</center>
</f:view>
</body>
</html>