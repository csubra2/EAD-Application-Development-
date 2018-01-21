package edu.uic.ids517.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;


public class DatabaseAccessBean{

	DatabaseAccessInformationBean databaseAccessInformationBean = new DatabaseAccessInformationBean();
	Connection   connection;  
	DatabaseMetaData  databaseMetaData;  
	Statement   statement;  
	ResultSet   resultSet, rs; 
	ResultSetMetaData  resultSetMetaData;
	Result result;
	private boolean renderSet;
	
	private String jdbcDriver;
	private String url;
	private List <String> columnNames;
	private String sqlQuery;
	
	private String sqlMessage;
	private String sqlErrorCode;
	private String sqlState;
	private String message;
	private static final String [] TABLE_TYPES={"TABLE","VIEW"};
	
	private List <String> tableViewList;
	private List <String> columnViewList;
	private String tableName;
	private boolean tableListRendered;
	private boolean columnListRendered;

	public DatabaseAccessBean() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public String connect()
	{
		switch(databaseAccessInformationBean.getDbType().toUpperCase())
		{
			case "MYSQL":
			{
				jdbcDriver = "com.mysql.jdbc.Driver";
				url = "jdbc:mysql://" + databaseAccessInformationBean.getDbmsHost() + ":3306"  
				+ "/" + databaseAccessInformationBean.getDbSchema();
				break;
			}	
			case "DB2":
			{
				jdbcDriver = "com.ibm.db2.jcc.DB2Driver";
				url = "jdbc:db2://" + databaseAccessInformationBean.getDbmsHost() + ":50000" 
				+ "/" + databaseAccessInformationBean.getDbSchema();
				break;
			}	
			case "ORACLE":
			{
				jdbcDriver = "oracle.jdbc.driver.OracleDriver";
				url = "jdbc:oracle:thin:@" + databaseAccessInformationBean.getDbmsHost() + ":1521" 
				+ ":" + databaseAccessInformationBean.getDbSchema();
				break;
			}	
			default:
				System.out.println("Invalid Database Type");
				break;
		}
		
		try
		{
			message = "";
			Class.forName(jdbcDriver);
		    connection = DriverManager.getConnection(url, databaseAccessInformationBean.getUserName(),
		    		databaseAccessInformationBean.getPassword());
		    statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			databaseMetaData = connection.getMetaData();
			tableViewList = tableList();
			createAccessLogs();
			return "Success";
		}
		catch(ClassNotFoundException ex)
		{
			message = "Invalid Credentials";
			return "Fail";
		}
		catch (Exception e) 
		{
		   message = "Invalid Credentials!";
		   return "Fail";
		}
		
	}
	
	public String generateSqlQuery() 
	{
		String selectedColumns = "";
		for(int i=0; i<columnNames.size(); i++)
		{
			selectedColumns = selectedColumns+" "+columnNames.get(i)+","; 
		}
		selectedColumns = selectedColumns.substring(0, selectedColumns.length() - 1);
		sqlQuery = "select "+  selectedColumns + " from "+ tableName ;
		 String getTableResult = execute();
		if(getTableResult.equalsIgnoreCase("success"))
			return "success";
		else
			return "fail";
	}
	public String execute()
	{
		try
		{
			sqlMessage = "";
			sqlState = "";
			sqlErrorCode = "";
			result = null;
			resultSet = null;
			if(sqlQuery.toUpperCase().contains("SELECT"))
				resultSet = statement.executeQuery(sqlQuery);
			else
			{
				statement.executeUpdate(sqlQuery);
				resultSet = null;
				result = null;
			}
			if(resultSet != null) 
			{
				renderSet = true;
				generateResult();
			}
			return "SUCCESS";
		} 
		catch (SQLException e) 
		{
			sqlMessage = e.getMessage();
			sqlState = e.getSQLState();
			sqlErrorCode = Integer.toString(e.getErrorCode());
			return "FAIL";
		}

	}
	
	public void generateResult() 
	{
		if (resultSet != null) 
		{
			result = ResultSupport.toResult(resultSet);
		}
	}
	
	public List<String> tableList() 
	{
		List <String> tableList = null;
		try 
		{
			// assumes database meta data available
			resultSet = databaseMetaData.getTables(null, databaseAccessInformationBean.getUserName(), 
							null, TABLE_TYPES);
			resultSet.last();
			int numberRows = resultSet.getRow();
			tableList = new ArrayList<String>(numberRows);
			resultSet.beforeFirst();
			if(resultSet != null) 
			{
				while(resultSet.next()) 
				{
					tableName = resultSet.getString("TABLE_NAME");
					if(!databaseAccessInformationBean.getDbType().equalsIgnoreCase("oracle") || tableName.length()<4)
						tableList.add(tableName);
					else if(!tableName.substring(0,4).equalsIgnoreCase("BIN$"))
						tableList.add(tableName);
				} // end while
			} // end if
			tableListRendered = true;
			tableName="";
		} // end try
		catch (SQLException e) 
		{
			sqlMessage = e.getMessage();
			sqlState = e.getSQLState();
			sqlErrorCode = Integer.toString(e.getErrorCode());
		} // end catch
		return tableList;
	} // end table List method
	
	public void columnList(ValueChangeEvent event) 
	{
		columnViewList = new ArrayList<String>();
		try 
		{
			tableName = event.getNewValue().toString();
			resultSet = databaseMetaData.getColumns( null, databaseAccessInformationBean.getUserName(), 
																tableName, null);
			String columnName="";
			if(resultSet != null) 
			{
				while(resultSet.next()) 
				{
					columnName = resultSet.getString("COLUMN_NAME");
					columnViewList.add(columnName);
				}
			}
		}
		catch (SQLException e) 
		{
			sqlMessage = e.getMessage();
			sqlState = e.getSQLState();
			sqlErrorCode = Integer.toString(e.getErrorCode());
		}
		columnListRendered = true;
	}

	public String close() 
	{			
	  try 
		{
		   sqlMessage = "";
		   sqlState = "";
		   sqlErrorCode = "";
		   processLogout();
		   if(statement!=null && connection!=null)
		   {
			   statement.close();
			   connection.close();	
		   }
		   FacesContext context = FacesContext.getCurrentInstance();
		   context.getExternalContext().invalidateSession();
		   return "LOGOUT SUCCESS";
		}
		catch (SQLException e) 
	    {
			sqlMessage = e.getMessage();
			sqlState = e.getSQLState();
			sqlErrorCode = Integer.toString(e.getErrorCode());
			return "LOGOUT FAIL";
		}
	}
	
	public String terminate() 
	{	
	   message = "";	
	   sqlMessage = "";
	   FacesContext context = FacesContext.getCurrentInstance();
	   context.getExternalContext().invalidateSession();
	   return "TERMINATE SUCCESS";
	}
	
	public String reset()
	{
		sqlMessage="";
		columnListRendered=false;
		renderSet=false;
		tableName="";
		columnNames = null;
		return "Reset";
	}
	
	public boolean createAccessLogs()
	{
		try
		{
			String createQuery = "Create table if not exists "+databaseAccessInformationBean.getDbSchema()+".f17g307_log (LogID INT(6)" +
					" NOT NULL AUTO_INCREMENT , Username char(50) not null, " +
					"dbms char(50) ,LoginTime char(50) null, LogoutTime char(50) null, " +
					"IPAddress char(50), SessionID char(50), PRIMARY KEY (LogID)) " +
					"ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;";

			FacesContext fc = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
			String sessionId = session.getId(); 
			
			HttpServletRequest request =
					(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) 
				ipAddress = request.getRemoteAddr();
			
			String insertQuery = "Insert into "+databaseAccessInformationBean.getDbSchema()+".f17g307_log (Username,dbms," +
					"LoginTime,IPAddress,SessionID)" +
					"values ('" + databaseAccessInformationBean.getUserName() + "' , '" + 
					databaseAccessInformationBean.getDbSchema() + "' , '" + 
					new java.sql.Timestamp(new Date().getTime()) + "' , '" + ipAddress
					+ "' , '" + sessionId + "' )";
			
			if (tableViewList.contains("f17g307_log")) 
			{
				setSqlQuery(insertQuery);
				execute();
			} 
			else
			{
				setSqlQuery(createQuery);
				String statusNew = execute();
				if(statusNew.equalsIgnoreCase("SUCCESS"))
				{
					setSqlQuery(insertQuery);
					execute();
				}
			}
			return true;
		} 
		catch (Exception e) 
		{
			message = e.getMessage();
			return false;
		}
	}

	public int getMaxRowId() 
	{
		int rowNumber = 0;
		try 
		{
			String getRowIdQuery = "Select LogID from "+databaseAccessInformationBean.getDbSchema()+".f17g307_log where Username = '" + 
					databaseAccessInformationBean.getUserName() + "' order by LogID desc";
			setSqlQuery(getRowIdQuery);
			String status = execute();
				if(status.equalsIgnoreCase("SUCCESS"))
				{
					if (resultSet != null)
					{
						resultSet.last();
						rowNumber = resultSet.getRow();
					}
				}
				return rowNumber;
		} 
		catch (Exception e) 
		{
			message = e.getMessage();
			return rowNumber = -1;
		}
	}

	public boolean processLogout()
	{
		try
		{
			int rows = getMaxRowId();
			if(rows==0)
			{
				message = "Sorry no data found.";
				return false;
			}
			else
			{
				String updateQuery="Update "+databaseAccessInformationBean.getDbSchema()+".f17g307_log set LogoutTime= '" 
										+ new java.sql.Timestamp(new Date().getTime()) + "' where LogID = " + rows;
				setSqlQuery(updateQuery);
				execute();
			}
			return true;
		}
		catch(Exception e)
		{
			message=e.getMessage();
			return false;
		}
	}
	
	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}
	
	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}
	
	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}
	
	public ResultSetMetaData getResultSetMetaData() {
		return resultSetMetaData;
	}

	public void setResultSetMetaData(ResultSetMetaData resultSetMetaData) {
		this.resultSetMetaData = resultSetMetaData;
	}
	
	public boolean isRenderSet() {
		return renderSet;
	}

	public void setRenderSet(boolean renderSet) {
		this.renderSet = renderSet;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public boolean isTableListRendered() {
		return tableListRendered;
	}

	public void setTableListRendered(boolean tableListRendered) {
		this.tableListRendered = tableListRendered;
	}

	public List<String> getTableViewList() {
		return tableViewList;
	}

	public void setTableViewList(List<String> tableList) {
		this.tableViewList = tableList;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public DatabaseAccessInformationBean getDatabaseAccessInformationBean() {
		return databaseAccessInformationBean;
	}

	public void setDatabaseAccessInformationBean(DatabaseAccessInformationBean databaseAccessInformationBean) {
		this.databaseAccessInformationBean = databaseAccessInformationBean;
	}
	
	public String getSqlMessage() {
		return sqlMessage;
	}

	public void setSqlMessage(String sqlMessage) {
		this.sqlMessage = sqlMessage;
	}

	public String getSqlErrorCode() {
		return sqlErrorCode;
	}

	public void setSqlErrorCode(String sqlErrorCode) {
		this.sqlErrorCode = sqlErrorCode;
	}

	public String getSqlState() {
		return sqlState;
	}

	public void setSqlStatus(String sqlState) {
		this.sqlState = sqlState;
	}
	
	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}
	
	public List<String> getColumnViewList() {
		return columnViewList;
	}

	public void setColumnViewList(List<String> columnViewList) {
		this.columnViewList = columnViewList;
	}
	
	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public boolean isColumnListRendered() {
		return columnListRendered;
	}

	public void setColumnListRendered(boolean columnListRendered) {
		this.columnListRendered = columnListRendered;
	}
}
