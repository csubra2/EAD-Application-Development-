package edu.uic.ids517.model;

public class StatsBean {
	private String columnSelected;
	private int count;
	private double minValue;
	private double maxValue ;
	private double mean ;
	private double variance;
	private double std ;
	private double median;
	private double q1 ;
	private double q3 ;
	private double iqr;
	private double range;
	
	public StatsBean(String columnSelected,int count, double minValue, double maxValue, double mean, 
							double variance, double std,double median, double q1, double q3, double iqr, double range) 
	{
		super();
		this.columnSelected = columnSelected;
		this.count = count;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.mean = mean;
		this.variance = variance;
		this.std = std;
		this.median = median;
		this.q1 = q1;
		this.q3 = q3;
		this.iqr = iqr;
		this.range = range;
	}
	public String getColumnSelected() {
		return columnSelected;
	}
	public void setColumnSelected(String columnSelected) {
		this.columnSelected = columnSelected;
	}
	public double getMinValue() {
		return minValue;
	}
	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}
	public double getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}
	public double getMean() {
		return mean;
	}
	public void setMean(double mean) {
		this.mean = mean;
	}
	public double getVariance() {
		return variance;
	}
	public void setVariance(double variance) {
		this.variance = variance;
	}
	public double getStd() {
		return std;
	}
	public void setStd(double std) {
		this.std = std;
	}
	public double getMedian() {
		return median;
	}
	public void setMedian(double median) {
		this.median = median;
	}
	public double getQ1() {
		return q1;
	}
	public void setQ1(double q1) {
		this.q1 = q1;
	}
	public double getQ3() {
		return q3;
	}
	public void setQ3(double q3) {
		this.q3 = q3;
	}
	public double getIqr() {
		return iqr;
	}
	public void setIqr(double iqr) {
		this.iqr = iqr;
	}
	public double getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getRange() {
		return range;
	}
	public void setRange(double range) {
		this.range = range;
	}
}
