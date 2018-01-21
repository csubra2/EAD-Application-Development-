package edu.uic.ids517.model;

import java.awt.Color;
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.LineFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.statistics.Regression;  
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;  

public class ChartBean {
	
	private static double beta0;
	private static double beta1;
	private static JFreeChart chart;

	public static JFreeChart createScatterPlotChart(String predictor, String response, XYSeriesCollection dataset,
													double predictMinValue, double predictMaxValue)
	{
		chart = ChartFactory.createScatterPlot("Scatter Plot for " + predictor + " vs " + response, predictor, 
									response, dataset, PlotOrientation.VERTICAL, true, true, false);
		XYPlot plot = chart.getXYPlot();
		plot.getRenderer().setSeriesPaint(0, Color.blue);
		drawRegressionLine(dataset,predictMinValue,predictMaxValue);
		return chart;  			
	}
	 
	 private static void drawRegressionLine(XYSeriesCollection collection,double predictMinValue, double predictMaxValue)
	 {
		try
		{
			double regressionParameters[] = Regression.getOLSRegression(collection,0);
			regressionParameters[0] = beta0;
			regressionParameters[1] = beta1;
			
			// Prepare a line function using the found parameters
			LineFunction2D linefunction2d = new LineFunction2D(regressionParameters[0], regressionParameters[1]);
			
			// Creates a dataset by taking sample values from the line function
			XYDataset dataset = DatasetUtilities.sampleFunction2D(linefunction2d, predictMinValue,
												predictMaxValue, 3, "Fitted Regression Line");
			
			// Draw the line dataset
			XYPlot xyplot = chart.getXYPlot();
			xyplot.setDataset(1, dataset);
			XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer(true, false);
			xylineandshaperenderer.setSeriesPaint(0, Color.RED);
			xyplot.setRenderer(1, xylineandshaperenderer);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	 }
	 
	 public static void computeRegressionEquation(double predictorMean, double responseMean, double[] predictorArray,
			 																	double[] responseArray)
	 {
		double[] x = predictorArray;
	    double[] y = responseArray;
	    int n = x.length;
	    double xbar = predictorMean / n;
	    double ybar = responseMean / n;
	    
	    //compute summary statistics
	    double xxbar = 0.0, xybar = 0.0;
	    for (int i = 0; i < n; i++) 
	    {
	      xxbar += (x[i] - xbar) * (x[i] - xbar);
	      xybar += (x[i] - xbar) * (y[i] - ybar);
	    }
	    beta1 = xybar / xxbar;
	    beta0 = ybar - beta1 * xbar;
	 }
}
