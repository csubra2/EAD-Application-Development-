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
	<title>Scatter Plot</title>
</head>

<body>
<f:view>

	<h3 align="center" style="font-weight:bold">IDS517 - Fall 2017 f17g307 - Graphical Analysis</h3>
	<br />
	<hr />
	<br />

	<center>
	<a href="index.jsp">Home</a><br /><br /><br />
	<h:form>
	<h:commandLink value = "Back" action="#{graphicalAnalysisBean.reset}"></h:commandLink>
	</h:form>
	</center>
	<br />
	
	<h:form>
	<div align="center">
	<h:outputText value = "Select Table:" style="font-weight:bold" rendered="#{databaseAccessBean.tableListRendered}"/>
	&nbsp;
	<h:selectOneListbox size="10" styleClass="selectOneListbox_mono" 
	     value="#{databaseAccessBean.tableName}" onchange="submit()"
	     		valueChangeListener="#{databaseAccessBean.columnList}" 
	     			rendered="#{databaseAccessBean.tableListRendered}" >
		<f:selectItems value="#{databaseAccessBean.tableViewList}" />
	</h:selectOneListbox> 
	 &nbsp;
	 <h:outputText value = "Select predictor variable: " style="font-weight:bold" rendered="#{databaseAccessBean.columnListRendered}"/>
	 <h:selectOneListbox size="10" styleClass="selectOneListbox_mono" 
	     value="#{graphicalAnalysisBean.predictor}" rendered="#{databaseAccessBean.columnListRendered}">
		<f:selectItems value="#{databaseAccessBean.columnViewList}" />
	</h:selectOneListbox>
	&nbsp;
	<h:outputText value = "Select response variable: " style="font-weight:bold" rendered="#{databaseAccessBean.columnListRendered}"/>
	 <h:selectOneListbox size="10" styleClass="selectOneListbox_mono" 
	     value="#{graphicalAnalysisBean.response}" rendered="#{databaseAccessBean.columnListRendered}">
		<f:selectItems value="#{databaseAccessBean.columnViewList}" />
	</h:selectOneListbox>
	<br /><br />
	<h:commandButton type="submit"  value="Graphical Analysis"  
						action="#{graphicalAnalysisBean.processGraphicQuery}" rendered="#{databaseAccessBean.columnListRendered}"/> 
	<br /><br /><br/>
 	<h:graphicImage value="#{graphicalAnalysisBean.scatterPlotChartFile}" height="450" width="600" 
 								rendered="#{graphicalAnalysisBean.renderChart}" alt="scatterPlotChart"/>
 	<br/><br />
 	<h:outputLabel value ="#{graphicalAnalysisBean.statusMessage}" style="color:red"/> 
	<br />  
	</div>      
	</h:form>
	
</f:view>
</body>
</html>