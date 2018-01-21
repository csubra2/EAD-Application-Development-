package edu.uic.ids517.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.stat.StatUtils;

public class DescriptiveAnalysisBean {
	
	private DatabaseAccessBean databaseAccessBean;
	private boolean render;
	private List <StatsBean> statsList = null;
	private String error;
	
	public DescriptiveAnalysisBean() {
	// TODO Auto-generated constructor stub
	setRender(false);
	}
	
	public String processRequest() 
	{	
		error="";
		databaseAccessBean.generateSqlQuery();
		Object[][] values = databaseAccessBean.getResult().getRowsByIndex();
		
		try 
		{
			int m = values.length;
		    int n = values[0].length;
		    double [][] doubles = new double[n][m];
			for(int x = 0; x < n; x++)
		    {
		        for(int y = 0; y < m; y++)
		        {
		        	if(values[y][x]==null)
		        	{
		        		values[y][x] = 0;
		        	}
		        		
		            doubles[x][y] = Double.parseDouble(values[y][x].toString());
		        }
		    }
				
		    statsList = new ArrayList <StatsBean> ();
		    DecimalFormat df = new DecimalFormat("#.#####");
		    for(int i=0; i<doubles.length; i++)
			{
				double [] value = doubles[i];		
				double minValue = Double.parseDouble(df.format(StatUtils.min(value)));
				double maxValue = StatUtils.max(value);
				double mean = Double.parseDouble(df.format(StatUtils.mean(value)));
				double variance = Double.parseDouble(df.format(StatUtils.variance(value, mean)));  
				double std =Double.parseDouble(df.format(Math.sqrt(variance))); 
				double median = Double.parseDouble(df.format(StatUtils.percentile(value, 50.0)));
				double q1 = Double.parseDouble(df.format(StatUtils.percentile(value, 25.0)));
				double q3 = Double.parseDouble(df.format(StatUtils.percentile(value, 75.0)));
				double iqr = q3-q1;	
				double range = maxValue - minValue;
				int count= doubles[i].length;
				statsList.add(new StatsBean(databaseAccessBean.getColumnNames().get(i), count, minValue, maxValue, mean, variance, std ,median, q1,q3,iqr,range));
				
			}
		    setRender(true);
		    error="";
			return "SUCCESS";
		}
		catch(NumberFormatException nfe) {
			nfe.printStackTrace();
			error= "<p style='color:red'>Please select numeric columns</p>";
			render = false;
			return "FAIL";
		}
	    catch(NullPointerException npe) {
	    	npe.printStackTrace();
	    	error= "<p style='color:red'>Error in calculating descriptive statistics</p>";
	    	render = false;
			return "FAIL";
	    }
		catch(ArrayIndexOutOfBoundsException abe)
		{
			abe.printStackTrace();
	    	error= "<p style='color:red'> No values - Error in calculating descriptive statistics </p>";
	    	render = false;
			return "FAIL";
		}
		catch(Exception e) {
			e.printStackTrace();
			error= "<p style='color:red'>Error in calculating descriptive statistics</p>";
			render = false;
			return "FAIL";
		}
	}
	
	public String reset()
	{
		databaseAccessBean.setColumnListRendered(false);
		databaseAccessBean.setRenderSet(false);
		databaseAccessBean.setTableName("");
		render = false;
		error = "";
		return "DescReset";
	}
	public DatabaseAccessBean getDatabaseAccessBean() {
		return databaseAccessBean;
	}
	public void setDatabaseAccessBean(DatabaseAccessBean databaseAccessBean) {
		this.databaseAccessBean = databaseAccessBean;
	}
	public boolean isRender() {
		return render;
	}
	public void setRender(boolean render) {
		this.render = render;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public List <StatsBean> getStatsList() {
		return statsList;
	}
	public void setStatsList(List <StatsBean> statsList) {
		this.statsList = statsList;
	}

}
