package edu.uic.ids517.model;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class RegressionAnalysisBean {

	private DatabaseAccessBean databaseAccessBean = new DatabaseAccessBean();
	private double interceptPValue;
	private double pValuePredictor;
	private double standardErrorModel;
	private double totalSumSquares;
	private String regressionEquation;
	private double pValue;
	private boolean renderRegressionResult;
	private boolean renderNumberOfColumns;
	private boolean renderNumberOfObservations;
	private int totalDF;
	private double intercept;
	private double interceptStandardError;
	private double tStatistic;
	private int predictorDF;
	private int residualErrorDF;
	private double rSquare;
	private double rSquareAdjusted;
	private double slope;
	private double slopeStandardError;
	private double tStatisticpredict;
	private double regressionSumSquares;
	private double sumSquaredErrors;
	private double meanSquare;
	private double meanSquareError;
	private double fValue;
	private double tStatisticPredictor;
	private String predictorValue;
	private XYSeriesCollection dataset;
	private XYSeries series;
	private String response;
	private String predictor;
	private double [] predictorArray;
	private double [] responseArray;
	private String errorMessage;

	
	public String regression()
	{
		errorMessage = "";
		dataset = null;
		series = null;
		databaseAccessBean.setColumnListRendered(false);
		String tableName = databaseAccessBean.getTableName();
		databaseAccessBean.setColumnListRendered(true);
		databaseAccessBean.setSqlQuery("SELECT " + predictor + "," + response + " FROM " + tableName);
		dataset = new XYSeriesCollection();
		series = new XYSeries(tableName);
		databaseAccessBean.execute();
		SimpleRegression sr = new SimpleRegression();
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
					sr.addData(predictorArray[r], responseArray[r]);
				}
			}
			dataset.addSeries(series);
			
			totalDF = responseArray.length - 1;
			TDistribution tDistribution = new TDistribution(totalDF);
			intercept = sr.getIntercept();
			interceptStandardError = sr.getInterceptStdErr();
			tStatistic = 0;
			predictorDF = 1;
			residualErrorDF = totalDF - predictorDF;
			rSquare = sr.getRSquare();
			rSquareAdjusted = rSquare - (1 - rSquare) / (totalDF - predictorDF - 1);
			if (interceptStandardError != 0) {
				tStatistic = (double) intercept / interceptStandardError;
			}
			else
				tStatistic = Double.NaN;
			interceptPValue = (double) 2 * tDistribution.cumulativeProbability(-Math.abs(tStatistic));
			slope = sr.getSlope();
			slopeStandardError = sr.getSlopeStdErr();
			tStatisticpredict = 0;
			if (slopeStandardError != 0) {
				tStatisticpredict = (double) slope / slopeStandardError;
			}
			pValuePredictor = (double) 2 * tDistribution.cumulativeProbability(-Math.abs(tStatisticpredict));
			standardErrorModel = Math.sqrt(StatUtils.variance(responseArray))
					* (Math.sqrt(1 - rSquareAdjusted));
			regressionSumSquares = sr.getRegressionSumSquares();
			sumSquaredErrors = sr.getSumSquaredErrors();
			totalSumSquares = sr.getTotalSumSquares();
			meanSquare = 0;
			if (predictorDF != 0) {
				meanSquare = regressionSumSquares / predictorDF;
			}
			meanSquareError = 0;
			if (residualErrorDF != 0) {
				meanSquareError = (double) (sumSquaredErrors / residualErrorDF);
			}
			fValue = 0;
			if (meanSquareError != 0) {
				fValue = meanSquare / meanSquareError;
			}
			regressionEquation = response + " = " + intercept + " + (" + slope + ") " + predictor;
			FDistribution fDistribution = new FDistribution(predictorDF, residualErrorDF);
			pValue = (double) (1 - fDistribution.cumulativeProbability(fValue));
			renderRegressionResult = true;
			renderNumberOfColumns = true;
			renderNumberOfObservations = true;
		}
		catch(NumberFormatException nfe)
		{
			nfe.printStackTrace();
			renderRegressionResult = false;
			errorMessage = "You have chosen a string variable, regression analysis cannot be done with categorical"
					+ "variables." + "\n" + "Please select a numerical variable";
			return "Fail";
		}
		catch(NullPointerException npe)
		{
			npe.printStackTrace();
			renderRegressionResult = false;
			errorMessage = "Error in performing regression analysis due to null values";
			return "Fail";
		}
		
		return "success";
	}
	
	public String reset()
	{
		databaseAccessBean.setColumnListRendered(false);
		databaseAccessBean.setRenderSet(false);
		databaseAccessBean.setTableName("");
		renderRegressionResult = false;
		errorMessage = "";
		return "RegressionReset";
	}
	
	public DatabaseAccessBean getDatabaseAccessBean() {
		return databaseAccessBean;
	}
	public void setDatabaseAccessBean(DatabaseAccessBean databaseAccessBean) {
		this.databaseAccessBean = databaseAccessBean;
	}
	public double getInterceptPValue() {
		return interceptPValue;
	}
	public void setInterceptPValue(double interceptPValue) {
		this.interceptPValue = interceptPValue;
	}
	public double getpValuePredictor() {
		return pValuePredictor;
	}
	public void setpValuePredictor(double pValuePredictor) {
		this.pValuePredictor = pValuePredictor;
	}
	public double getStandardErrorModel() {
		return standardErrorModel;
	}
	public void setStandardErrorModel(double standardErrorModel) {
		this.standardErrorModel = standardErrorModel;
	}
	public double getTotalSumSquares() {
		return totalSumSquares;
	}
	public void setTotalSumSquares(double totalSumSquares) {
		this.totalSumSquares = totalSumSquares;
	}
	public String getRegressionEquation() {
		return regressionEquation;
	}
	public void setRegressionEquation(String regressionEquation) {
		this.regressionEquation = regressionEquation;
	}
	public double getpValue() {
		return pValue;
	}
	public void setpValue(double pValue) {
		this.pValue = pValue;
	}
	public boolean isRenderRegressionResult() {
		return renderRegressionResult;
	}
	public void setRenderRegressionResult(boolean renderRegressionResult) {
		this.renderRegressionResult = renderRegressionResult;
	}
	public boolean isRenderNumberOfColumns() {
		return renderNumberOfColumns;
	}
	public void setRenderNumberOfColumns(boolean renderNumberOfColumns) {
		this.renderNumberOfColumns = renderNumberOfColumns;
	}
	public boolean isRenderNumberOfObservations() {
		return renderNumberOfObservations;
	}
	public void setRenderNumberOfObservations(boolean renderNumberOfObservations) {
		this.renderNumberOfObservations = renderNumberOfObservations;
	}
	public double gettStatisticPredictor() {
		return tStatisticPredictor;
	}
	public void settStatisticPredictor(double tStatisticPredictor) {
		this.tStatisticPredictor = tStatisticPredictor;
	}
	public String getPredictorValue() {
		return predictorValue;
	}
	public void setPredictorValue(String predictorValue) {
		this.predictorValue = predictorValue;
	}
	public int getTotalDF() {
		return totalDF;
	}
	public void setTotalDF(int totalDF) {
		this.totalDF = totalDF;
	}
	public double getIntercept() {
		return intercept;
	}
	public void setIntercept(double intercept) {
		this.intercept = intercept;
	}
	public double getInterceptStandardError() {
		return interceptStandardError;
	}
	public void setInterceptStandardError(double interceptStandardError) {
		this.interceptStandardError = interceptStandardError;
	}
	public double gettStatistic() {
		return tStatistic;
	}
	public void settStatistic(double tStatistic) {
		this.tStatistic = tStatistic;
	}
	public int getPredictorDF() {
		return predictorDF;
	}
	public void setPredictorDF(int predictorDF) {
		this.predictorDF = predictorDF;
	}
	public int getResidualErrorDF() {
		return residualErrorDF;
	}
	public void setResidualErrorDF(int residualErrorDF) {
		this.residualErrorDF = residualErrorDF;
	}
	public double getrSquare() {
		return rSquare;
	}
	public void setrSquare(double rSquare) {
		this.rSquare = rSquare;
	}
	public double getrSquareAdjusted() {
		return rSquareAdjusted;
	}
	public void setrSquareAdjusted(double rSquareAdjusted) {
		this.rSquareAdjusted = rSquareAdjusted;
	}
	public double getSlope() {
		return slope;
	}
	public void setSlope(double slope) {
		this.slope = slope;
	}
	public double getSlopeStandardError() {
		return slopeStandardError;
	}
	public void setSlopeStandardError(double slopeStandardError) {
		this.slopeStandardError = slopeStandardError;
	}
	public double gettStatisticpredict() {
		return tStatisticpredict;
	}
	public void settStatisticpredict(double tStatisticpredict) {
		this.tStatisticpredict = tStatisticpredict;
	}
	public double getRegressionSumSquares() {
		return regressionSumSquares;
	}
	public void setRegressionSumSquares(double regressionSumSquares) {
		this.regressionSumSquares = regressionSumSquares;
	}
	public double getSumSquaredErrors() {
		return sumSquaredErrors;
	}
	public void setSumSquaredErrors(double sumSquaredErrors) {
		this.sumSquaredErrors = sumSquaredErrors;
	}
	public double getMeanSquare() {
		return meanSquare;
	}
	public void setMeanSquare(double meanSquare) {
		this.meanSquare = meanSquare;
	}
	public double getMeanSquareError() {
		return meanSquareError;
	}
	public void setMeanSquareError(double meanSquareError) {
		this.meanSquareError = meanSquareError;
	}
	public double getfValue() {
		return fValue;
	}
	public void setfValue(double fValue) {
		this.fValue = fValue;
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

	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}