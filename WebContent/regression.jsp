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
	<title>Regression Analysis</title>
</head>

<body>
<f:view>

	<h3 align="center" style="font-weight:bold">IDS517 - Fall 2017 f17g307 - Regression Analysis</h3>
	<br />
	<hr />
	<br />

	<center>
	<a href="index.jsp">Home</a><br /><br /><br />
	<h:form>
	<h:commandLink value = "Back" action="#{regressionAnalysisBean.reset}"></h:commandLink>
	</h:form>
	</center>
	<br />
	
	<h:form>
	<div align = "center">
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
	     value="#{regressionAnalysisBean.predictor}" rendered="#{databaseAccessBean.columnListRendered}">
		<f:selectItems value="#{databaseAccessBean.columnViewList}" />
	</h:selectOneListbox>
	&nbsp;
	<h:outputText value = "Select response variable: " style="font-weight:bold" rendered="#{databaseAccessBean.columnListRendered}"/>
	 <h:selectOneListbox size="10" styleClass="selectOneListbox_mono" 
	     value="#{regressionAnalysisBean.response}" rendered="#{databaseAccessBean.columnListRendered}">
		<f:selectItems value="#{databaseAccessBean.columnViewList}" />
	</h:selectOneListbox><br /><br /><br />
	<h:commandButton type="submit"  value="Regression Analysis"  
						action="#{regressionAnalysisBean.regression}" rendered="#{databaseAccessBean.columnListRendered}"/>
				<br/><br />
				<h:outputText value="Regression Equation: " style="font-weight:bold"
					rendered="#{regressionAnalysisBean.renderRegressionResult}">
				</h:outputText>
				&nbsp;
				<h:outputText value="#{regressionAnalysisBean.regressionEquation}"
					rendered="#{regressionAnalysisBean.renderRegressionResult}">
				</h:outputText>
				<br /> <br />
				<h:outputText value="Regression Model" style="font-weight:bold"
					rendered="#{regressionAnalysisBean.renderRegressionResult}"></h:outputText>
				<h:panelGrid columns="5"
					rendered="#{regressionAnalysisBean.renderRegressionResult}"
					border="3">
					<h:outputText value="Predictor" />
					<h:outputText value="Co-efficient" />
					<h:outputText value="Standard Error Co-efficient" />
					<h:outputText value="T-Statistic" />
					<h:outputText value="P-Value" />
					<h:outputText value="Constant" />
					<h:outputText value="#{regressionAnalysisBean.intercept}" />
					<h:outputText
						value="#{regressionAnalysisBean.interceptStandardError}" />
					<h:outputText value="#{regressionAnalysisBean.tStatistic }" />
					<h:outputText value="#{regressionAnalysisBean.interceptPValue }" />
					<h:outputText value="#{regressionAnalysisBean.predictor}" />
					<h:outputText value="#{regressionAnalysisBean.slope}" />
					<h:outputText value="#{regressionAnalysisBean.slopeStandardError}" />
					<h:outputText
						value="#{regressionAnalysisBean.tStatisticpredict}" />
					<h:outputText value="#{regressionAnalysisBean.pValuePredictor }" />
				</h:panelGrid>
				<br /> <br />
				<h:outputText value="Analysis of Variance" style="font-weight:bold"
					rendered="#{regressionAnalysisBean.renderRegressionResult}" />
				<br />
				<h:panelGrid columns="6"
					rendered="#{regressionAnalysisBean.renderRegressionResult}"
					border="3">
					<h:outputText value="Source" />
					<h:outputText value="Degrees of Freedom(DF)" />
					<h:outputText value="Sum of Squares" />
					<h:outputText value="Mean of Squares" />
					<h:outputText value="F-Statistic" />
					<h:outputText value="P-Value" />
					<h:outputText value="Regression" />
					<h:outputText value="#{regressionAnalysisBean.predictorDF}" />
					<h:outputText
						value="#{regressionAnalysisBean.regressionSumSquares}" />
					<h:outputText value="#{regressionAnalysisBean.meanSquare }" />
					<h:outputText value="#{regressionAnalysisBean.fValue }" />
					<h:outputText value="#{regressionAnalysisBean.pValue}" />
					<h:outputText value="Residual Error" />
					<h:outputText value="#{regressionAnalysisBean.residualErrorDF}" />
					<h:outputText value="#{regressionAnalysisBean.sumSquaredErrors }" />
					<h:outputText value="#{regressionAnalysisBean.meanSquareError }" />
					<h:outputText value="" />
					<h:outputText value="" />
					<h:outputText value="Total" />
					<h:outputText value="#{regressionAnalysisBean.totalDF}" />
					<h:outputText value="#{regressionAnalysisBean.totalSumSquares}" />
				</h:panelGrid>
				<br /> <br />
				<h:outputText value="Model Summary" style="font-weight:bold"
					rendered="#{regressionAnalysisBean.renderRegressionResult}" />
				<br />
				<h:panelGrid columns="2"
					rendered="#{regressionAnalysisBean.renderRegressionResult}"
					border="3">
					<h:outputText value="Model Standard Error:" />
					<h:outputText value="#{regressionAnalysisBean.standardErrorModel}" />
					<h:outputText value="R Square(Co-efficient of Determination)" />
					<h:outputText value="#{regressionAnalysisBean.rSquare}" />
					<h:outputText
						value="R Square Adjusted(Co-efficient of Determination)" />
					<h:outputText value="#{regressionAnalysisBean.rSquareAdjusted}" />
				</h:panelGrid>
				<br /><br />
				<h:outputLabel value ="#{regressionAnalysisBean.errorMessage}" style="color:red"/>
			</div>
			</h:form>
	</f:view>

</body>
</html>