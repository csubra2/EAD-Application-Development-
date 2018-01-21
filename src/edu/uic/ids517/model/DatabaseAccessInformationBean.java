package edu.uic.ids517.model;

public class DatabaseAccessInformationBean {

	//Login Data Members
	private String userName;
	private String password;
		
	//Database Data Members
	private String dbType;
	private String dbmsHost;
	private String dbSchema;
		
	public DatabaseAccessInformationBean() {
		// TODO Auto-generated constructor stub
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getDbmsHost() {
		return dbmsHost;
	}

	public void setDbmsHost(String dbmsHost) {
		this.dbmsHost = dbmsHost;
	}

	public String getDbSchema() {
		return dbSchema;
	}

	public void setDbSchema(String dbSchema) {
		this.dbSchema = dbSchema;
	}
}
