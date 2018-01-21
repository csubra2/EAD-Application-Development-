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
	<title>Descriptive Analysis</title>
</head>

<body>
<f:view>
	<h3 align="center" style="font-weight:bold">IDS517 - Fall 2017 f17g307 - Descriptive Analysis</h3>
	<br />
	<hr />
	<br />

	<center>
	<a href="index.jsp">Home</a><br /><br /><br />
	<h:form>
	<h:commandLink value = "Back" action="#{descriptiveAnalysisBean.reset}"></h:commandLink>
	</h:form>
	</center>
	
	<h:form>
	<h:panelGrid columns ="2">
	<h:outputText value = "Select table:" style="font-weight:bold" rendered="#{databaseAccessBean.tableListRendered}"/>
	<h:outputText value = "Select column(s):" style="font-weight:bold" rendered="#{databaseAccessBean.columnListRendered}"/>
		<h:selectOneListbox size="10" styleClass="selectOneListbox_mono" 
		     value="#{databaseAccessBean.tableName}" onchange="submit()"
		     		valueChangeListener="#{databaseAccessBean.columnList}" 
		     			rendered="#{databaseAccessBean.tableListRendered}">
			<f:selectItems value="#{databaseAccessBean.tableViewList}" />
		</h:selectOneListbox> 
		 
		<h:selectManyListbox size="10" styleClass="selectManyListbox"
				value="#{databaseAccessBean.columnNames}" rendered="#{databaseAccessBean.columnListRendered}">
			<f:selectItems value="#{databaseAccessBean.columnViewList}" />
		</h:selectManyListbox>
		</h:panelGrid>
		<br/>
		<h:commandButton type="submit" value="Generate Stats" action="#{descriptiveAnalysisBean.processRequest}" 
																rendered="#{databaseAccessBean.columnListRendered}"/>
		&nbsp;&nbsp;
		<h:commandButton type="submit" value="Export Stats" action="#{fileActionBean.processExportDescStats}" 
																rendered="#{databaseAccessBean.columnListRendered}"/>
		<br />
		</h:form>
	<br />
	<center><h:outputText value="#{descriptiveAnalysisBean.error}" escape="false"></h:outputText></center>
	<f:verbatim><br /> <br /> </f:verbatim>
	<h:outputLabel value="Descriptive Statistics:" rendered="#{descriptiveAnalysisBean.render}"/>
	<t:dataTable id="dataList"
		var="item"
		value="#{descriptiveAnalysisBean.statsList}"
		rendered="#{descriptiveAnalysisBean.render}"
		border="1" cellspacing="0" cellpadding="1"
		columnClasses="columnClass1 border"
		headerClass="headerClass"
		footerClass="footerClass"
		rowClasses="rowClass2"
		styleClass="dataTableEx"
		width="200">
		<h:column>
			<f:facet name="header">
			<h:outputText styleClass="outputHeader" value="Variables" />
			</f:facet>
			<t:outputText styleClass="outputText" value="#{item.columnSelected}" />
		</h:column>
		<t:column>
			<f:facet name="header">
			<h:outputText styleClass="outputHeader" value="Count" />
			</f:facet>
			<h:outputText styleClass="outputText" value="#{item.count}" />
		</t:column>
		<t:column>
			<f:facet name="header">
			<h:outputText styleClass="outputHeader" value="Min Value" />
			</f:facet>
			<h:outputText styleClass="outputText" value="#{item.minValue}" />
		</t:column>
		<t:column>
			<f:facet name="header">
			<h:outputText styleClass="outputHeader" value="Max Value" />
			</f:facet>
			<h:outputText styleClass="outputText" value="#{item.maxValue}" />
		</t:column>
		<t:column>
			<f:facet name="header">
			<h:outputText styleClass="outputHeader" value="Mean" />
			</f:facet>
			<h:outputText styleClass="outputText" value="#{item.mean}" />
		</t:column>
		<t:column>
			<f:facet name="header">
			<h:outputText styleClass="outputHeader" value="Variance" />
			</f:facet>
			<h:outputText styleClass="outputText" value="#{item.variance}" />
		</t:column>
		<t:column>
			<f:facet name="header">
			<h:outputText styleClass="outputHeader" value="Standard Deviation" />
			</f:facet>
			<h:outputText styleClass="outputText" value="#{item.std}" />
		</t:column>
		<t:column>
			<f:facet name="header">
			<h:outputText styleClass="outputHeader" value="Median" />
			</f:facet>
			<h:outputText styleClass="outputText" value="#{item.median}" />
		</t:column>
		<t:column>
			<f:facet name="header">
			<h:outputText styleClass="outputHeader" value="Quartile 1" />
			</f:facet>
			<h:outputText styleClass="outputText" value="#{item.q1}" />
		</t:column>
		<t:column>
			<f:facet name="header">
			<h:outputText styleClass="outputHeader" value="Quartile 2" />
			</f:facet>
			<h:outputText styleClass="outputText" value="#{item.q3}" />
		</t:column>
		<t:column>
			<f:facet name="header">
			<h:outputText styleClass="outputHeader" value="Interquartile Range" />
			</f:facet>
			<h:outputText styleClass="outputText" value="#{item.iqr}" />
		</t:column>
		<t:column>
			<f:facet name="header">
			<h:outputText styleClass="outputHeader" value="Range" />
			</f:facet>
			<h:outputText styleClass="outputText" value="#{item.range}" />
		</t:column>
		</t:dataTable>
	<br/>
	
</f:view>
</body>
</html>