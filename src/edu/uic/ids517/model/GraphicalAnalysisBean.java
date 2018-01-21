package edu.uic.ids517.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.faces.context.FacesContext;
import org.apache.commons.math3.stat.StatUtils;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class GraphicalAnalysisBean {
	
	DatabaseAccessBean databaseAccessBean = new DatabaseAccessBean();
	private String scatterPlotChartFile;  
	private boolean renderChart; 
	private boolean renderSet;
	private String response;
	private double responseMean;
	private double predictMinValue;
	private double [] predictorArray;
	private double [] responseArray;
	private double predictMaxValue;
	private double responseMinValue;
	private double responseMaxValue;
	private double predictorMean;
	private String predictor;
	private XYSeriesCollection dataset;
	private XYSeries series;
	private String statusMessage;
	
	public String processGraphicQuery() 
	{   
		statusMessage = "";
		dataset = null;
		series = null;
		renderChart = false;
		databaseAccessBean.setColumnListRendered(false);
		String tableName = databaseAccessBean.getTableName();
		databaseAccessBean.setColumnListRendered(true);
		databaseAccessBean.setSqlQuery("SELECT " + predictor + "," + response + " FROM " + tableName);
		dataset = new XYSeriesCollection();
		series = new XYSeries(tableName);
		databaseAccessBean.execute();
		Object[][] sData = databaseAccessBean.getResult().getRowsByIndex();
		predictorArray = new double[sData.length];
		responseArray = new double[sData.length];
		try
		{
			for(int r=0; r<sData.length; r++) 
			{  
				for(int c=0; c<sData[r].length; c+=2)
				{
					if(sData[r][c] == null || sData[r][c] == "")
						sData[r][c] = 0.0;
					if(sData[r][c+1] == null || sData[r][c+1] == "")
						sData[r][c+1] = 0.0;
					predictorArray[r] = Double.valueOf(sData[r][c].toString());
					responseArray[r] = Double.valueOf(sData[r][c+1].toString());
					series.add(predictorArray[r], responseArray[r]);
				}
			}
		dataset.addSeries(series);
		}
		catch(NumberFormatException nfe)
		{
			nfe.printStackTrace();
			statusMessage = "You have chosen a string variable, scatter plot cannot be created with categorical"
					+ " variables." + "\n" + "Please select a numerical variable";
		}
		catch(NullPointerException npe)
		{
			npe.printStackTrace();
			statusMessage = "Error in creating scatter plot due to null values";
		}
		predictorMean = StatUtils.sum(predictorArray);
		responseMean = StatUtils.sum(responseArray);
		predictMinValue = StatUtils.min(predictorArray);
		predictMaxValue = StatUtils.max(predictorArray);
		responseMinValue = StatUtils.min(responseArray);
		responseMaxValue = StatUtils.max(responseArray);
		
		ChartBean.computeRegressionEquation(predictorMean,responseMean,predictorArray, responseArray);
		JFreeChart chart = ChartBean.createScatterPlotChart(predictor, response, dataset,predictMinValue, predictMaxValue);
		FacesContext context = FacesContext.getCurrentInstance();      
		String path = context.getExternalContext().getRealPath("/tmp");         
		File out = null;   
		try 
		{   
			// setup temporary naming system  
			out = new File(path+ "/" + databaseAccessBean.getDatabaseAccessInformationBean().getUserName()+"_"
					+tableName+".png"); 
			ChartUtilities.saveChartAsPNG(out, chart, 600, 450);
			scatterPlotChartFile = "/tmp/" + databaseAccessBean.getDatabaseAccessInformationBean().getUserName()+"_"
					+tableName+".png";
			if(statusMessage.isEmpty())
				renderChart = true;
		}
		catch(NullPointerException npe)
		{
			npe.printStackTrace();
			renderChart =  false;
			statusMessage = "Error in saving chart due to null values";
		}
		catch(FileNotFoundException fnf) 
		{
			fnf.printStackTrace();
			renderChart = false;
			statusMessage = "File Not Found! Please check the file path";
		} // end catch
		catch (IOException ie) 
		{
			// TODO Auto-generated catch block
			ie.printStackTrace();
			renderChart = false;
			statusMessage = "Error in saving chart";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			renderChart = false;
			statusMessage = "Error in saving chart";
		}   
		renderSet = false;  		 
		return "SUCCESS";     
	}
	
	public String reset()
	{
		databaseAccessBean.setColumnListRendered(false);
		databaseAccessBean.setRenderSet(false);
		databaseAccessBean.setTableName("");
		renderChart = false;
		statusMessage = "";
		return "PlotReset";
	}
	
	public double getPredictMinValue() {
		return predictMinValue;
	}

	public void setPredictMinValue(double predictMinValue) {
		this.predictMinValue = predictMinValue;
	}

	public double getPredictMaxValue() {
		return predictMaxValue;
	}

	public void setPredictMaxValue(double predictMaxValue) {
		this.predictMaxValue = predictMaxValue;
	}

	public double getResponseMinValue() {
		return responseMinValue;
	}

	public void setResponseMinValue(double responseMinValue) {
		this.responseMinValue = responseMinValue;
	}

	public double getResponseMaxValue() {
		return responseMaxValue;
	}

	public void setResponseMaxValue(double responseMaxValue) {
		this.responseMaxValue = responseMaxValue;
	}
	
	public double[] getPredictorArray() {
		return predictorArray;
	}

	public void setPredictorArray(double[] predictorArray) {
		this.predictorArray = predictorArray;
	}

	public double[] getResponseArray() {
		return responseArray;
	}

	public void setResponseArray(double[] responseArray) {
		this.responseArray = responseArray;
	}

	public double getResponseMean() {
		return responseMean;
	}

	public void setResponseMean(double responseMean) {
		this.responseMean = responseMean;
	}

	public double getPredictorMean() {
		return predictorMean;
	}

	public void setPredictorMean(double predictorMean) {
		this.predictorMean = predictorMean;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getPredictor() {
		return predictor;
	}

	public void setPredictor(String predictor) {
		this.predictor = predictor;
	}

	public boolean isRenderChart() {
		return renderChart;
	}

	public void setRenderChart(boolean renderChart) {
		this.renderChart = renderChart;
	}

	public boolean isRenderSet() {
		return renderSet;
	}

	public void setRenderSet(boolean renderSet) {
		this.renderSet = renderSet;
	}
	
	public String getscatterPlotChartFile() {
		return scatterPlotChartFile;
	}

	public void setscatterPlotChartFile(String scatterPlotChartFile) {
		this.scatterPlotChartFile = scatterPlotChartFile;
	}

	public DatabaseAccessBean getDatabaseAccessBean() {
		return databaseAccessBean;
	}

	public void setDatabaseAccessBean(DatabaseAccessBean databaseAccessBean) {
		this.databaseAccessBean = databaseAccessBean;
	}

	public XYSeriesCollection getDataset() {
		return dataset;
	}

	public void setDataset(XYSeriesCollection dataset) {
		this.dataset = dataset;
	}

	public XYSeries getSeries() {
		return series;
	}

	public void setSeries(XYSeries series) {
		this.series = series;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
}
