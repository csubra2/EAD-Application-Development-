package edu.uic.ids517.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.jstl.sql.Result;

import org.apache.commons.io.FilenameUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;

public class FileActionBean{
	
	DatabaseAccessBean databaseAccessBean = new DatabaseAccessBean();
	DescriptiveAnalysisBean descriptiveAnalysisBean = new DescriptiveAnalysisBean();
	private UploadedFile uploadedFile;
	private String fileLabel;
	private String status;
	private String fileName;
	private String selectedFile;
	private File[] files;
	private boolean isScriptExecuted;
	private boolean isExistFiles;
	private boolean fileRendered;
	private String query;
	private List<String> sqlFileNames;
	
	public String processFileUpload() 
	{
		FacesContext context = FacesContext.getCurrentInstance();
		String path = context.getExternalContext().getRealPath("/tmp"); 
		File tempFile = null;
		FileOutputStream fos = null;
		try 
		{
			String fileName = FilenameUtils.getName(uploadedFile.getName());
			tempFile = new File(path + "/" + fileName);
			fos = new FileOutputStream(tempFile);
			// or uploaded to disk
			fos.write(uploadedFile.getBytes());
			fos.close();
			status = "File Uploaded Successfully";
			fileRendered =true;
			return "FileSuccess";
		} // end try
		catch(NullPointerException npe)
		{
			npe.printStackTrace();
			fileRendered = false;
			status = "Error in File Uploading due to null values";
			return "FileError";
		}
		catch(FileNotFoundException fnf) 
		{
			fnf.printStackTrace();
			fileRendered = false;
			status = "File Not Found! Please check the file path and upload a valid file";
			return "FileError";
		} // end catch
		catch (IOException ie) 
		{
			// TODO Auto-generated catch block
			ie.printStackTrace();
			fileRendered = false;
			status = "Error in File Uploading";
			return "FileError";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			fileRendered = false;
			status = "Error in File Uploading";
			return "FileError";
		}
	} // end processFileUpload()
	
	public String processDisplayFiles() 
	{
		fileRendered = false;
		fileLabel = "";
		FacesContext context = FacesContext.getCurrentInstance();
		String path = context.getExternalContext().getRealPath("/tmp"); 
		File tempFile = null;
		isExistFiles = false;
		try 
		{
			tempFile = new File(path + "/");
			files = tempFile.listFiles(new FilenameFilter() {
			    public boolean accept(File tempFile, String name) {
			        return name.toLowerCase().endsWith(".sql");
			    }});
			sqlFileNames = new ArrayList<String>();
			for(int i=0; i<files.length; i++)
			{
				sqlFileNames.add(files[i].getName());
			}
			isExistFiles = true;
			return "FileExist";
		} // end try
		catch(NullPointerException npe)
		{
			npe.printStackTrace();
			status = "Error in displaying files due to null pointer";
			return "FileError";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			status = "Error in displaying files";
			return "FileError";
		}
	} // end processDisplayFiles()
	
	public String executeDBScripts() 
	{
		fileRendered = false;
		fileLabel = "";
		FacesContext context = FacesContext.getCurrentInstance();
		String path = context.getExternalContext().getRealPath("/tmp"); 
		path = path + "/" + selectedFile;
		isScriptExecuted = false;
		try 
		{
			BufferedReader in = new BufferedReader(new FileReader(path));
			String str;
			StringBuffer sb = new StringBuffer();
			while ((str = in.readLine()) != null) 
			{
				sb.append(str + "\n ");
			}
			in.close();
			query = sb.toString();
			databaseAccessBean.setSqlQuery(query);
			databaseAccessBean.execute();
			List<String> columns = new ArrayList<String>();
			for(int i=0; i<databaseAccessBean.getResult().getColumnNames().length; i++)
			{
				columns.add(databaseAccessBean.getResult().getColumnNames()[i]);
			}
			databaseAccessBean.setColumnNames(columns);
			isScriptExecuted = true;
		} 
		catch(NullPointerException npe)
		{
			npe.printStackTrace();
			status = "Error in File Uploading due to null values";
			return "FileError";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			status = "Error in File Uploading";
			return "FileError";
		} 
		return "File Executed";
	}
	
	public String processFileDownload() 
	{  
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		FileOutputStream fos = null;
		String path = fc.getExternalContext().getRealPath("/tmp");
	    fileName = "f17g307_" + databaseAccessBean.getTableName() + ".csv";
		String pathName = path + "/"+ fileName;
		File f = new File(pathName);
		// convert ResultSet object to Result object
		Result result = (Result) databaseAccessBean.getResult();
		// convert Result object to 2D array
		Object [][] sData = result.getRowsByIndex();
		StringBuffer sb = new StringBuffer();
		// generate temporary file for export
		try 
		{
			fos = new FileOutputStream(pathName);
			for(int i=0; i<result.getColumnNames().length; i++) 
			{
				if(result.getColumnNames()[0].equalsIgnoreCase("ID"))
				{
					result.getColumnNames()[0] = "'ID'";
				}
				sb.append(result.getColumnNames()[i].toString() + ",");
			}
			sb.append("\n");
			fos.write(sb.toString().getBytes());
			for(int i = 0; i < sData.length; i++) //rows
			{
				sb = new StringBuffer();
				for(int j=0; j<sData[0].length; j++) //columns
				{
					if(sData[i][j] == null)
						sData[i][j] = " ";
					else if(sData[i][j].toString().contains(","))
					  {
						   int index = sData[i][j].toString().indexOf(",");
						   String newValue = sData[i][j].toString().substring(0, index - 1);
						   sData[i][j] = newValue + sData[i][j].toString().substring(index + 1, sData[i][j].toString().length());	
					  }
					sb.append(sData[i][j].toString() + ",");
				}	
				sb.append("\n");
				fos.write(sb.toString().getBytes());
			}
			fos.flush();
			fos.close();
		}	
		catch(NullPointerException npe)
		{
			npe.printStackTrace();
			status = "Error in File Downloading due to null values";
			return "FileDownloadError";
		}
		catch(FileNotFoundException fnf) 
		{
			fnf.printStackTrace();
			status = "File Not Found! Please check the file path";
			return "FileDownloadError";
		} // end catch
		catch (IOException ie) 
		{
			// TODO Auto-generated catch block
			ie.printStackTrace();
			status = "Error in File Downloading";
			return "FileDownloadError";
		}	
		catch(Exception e)
		{
			e.printStackTrace();
			status = "Error in File Downloading";
			return "FileDownloadError";
		}
		
		String mimeType = ec.getMimeType(fileName); //application/vnd.ms-excel
		FileInputStream in = null;
		ec.responseReset();
		ec.setResponseContentType(mimeType);
		ec.setResponseContentLength((int) f.length());
		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			
		try 
		{
			in = new FileInputStream(f);
			OutputStream output = ec.getResponseOutputStream();
			Files.copy(f.toPath(), output);
		} 
		catch(NullPointerException npe)
		{
			npe.printStackTrace();
			status = "Error in File Downloading due to null values";
			return "FileDownloadError";
		}
		catch(FileNotFoundException fnf) 
		{
			fnf.printStackTrace();
			status = "File Not Found! Please check the file path";
			return "FileDownloadError";
		} // end catch
		catch (IOException ie) 
		{
			// TODO Auto-generated catch block
			ie.printStackTrace();
			status = "Error in File Downloading";
			return "FileDownloadError";
		}	
		catch(Exception e)
		{
			e.printStackTrace();
			status = "Error in File Downloading";
			return "FileDownloadError";
		}
		finally 
		{
			try 
			{
				in.close();
			} 
			catch (IOException ie) 
			{
				// TODO Auto-generated catch block
				ie.printStackTrace();
				status = "Error in File Downloading";
				return "FileDownloadError";
			}
		}
		fc.responseComplete();
		return "DOWNLOADED";
	}

	public String processExportDescStats() 
	{  
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		FileOutputStream fos = null;
		String path = fc.getExternalContext().getRealPath("/tmp");
	    fileName = "f17g307_" + databaseAccessBean.getTableName() + "_Desc" + ".csv";
		String pathName = path + "/"+ fileName;
		File f = new File(pathName);
		int columnSelectedCount = databaseAccessBean.getColumnNames().size();
		List<StatsBean> stats = descriptiveAnalysisBean.getStatsList();
		StatsBean data;
		StringBuffer sb = new StringBuffer();
		// generate temporary file for export
		try 
		{
			fos = new FileOutputStream(pathName);
			sb.append("Variables" + ",");
			sb.append("Count" + ",");
			sb.append("Min Value" + ",");
			sb.append("Max Value" + ",");
			sb.append("Mean" + ",");
			sb.append("Variance" + ",");
			sb.append("Standard Deviation" + ",");
			sb.append("Median" + ",");
			sb.append("Quartile 1" + ",");
			sb.append("Quartile 2" + ",");
			sb.append("InterQuartile Range" + ",");
			sb.append("Range" + ",");
			sb.append("\n");
			fos.write(sb.toString().getBytes());
			for(int i=0; i<columnSelectedCount; i++) 
			{
				data = stats.get(i);
				sb = new StringBuffer();
				sb.append(data.getColumnSelected() + ",");
				sb.append(data.getCount() + ",");
				sb.append(data.getMinValue() + ",");
				sb.append(data.getMaxValue() + ",");
				sb.append(data.getMean() + ",");
				sb.append(data.getVariance() + ",");
				sb.append(data.getStd() + ",");
				sb.append(data.getMedian() + ",");
				sb.append(data.getQ1() + ",");
				sb.append(data.getQ3() + ",");
				sb.append(data.getIqr() + ",");
				sb.append(data.getRange() + ",");
				sb.append("\n");
				fos.write(sb.toString().getBytes());
			}
			fos.flush();
			fos.close();
			
			String mimeType = ec.getMimeType(fileName); //application/vnd.ms-excel
			FileInputStream in = null;
			ec.responseReset();
			ec.setResponseContentType(mimeType);
			ec.setResponseContentLength((int) f.length());
			ec.setResponseHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");
			
			in = new FileInputStream(f);
			OutputStream output = ec.getResponseOutputStream();
			Files.copy(f.toPath(), output);
			in.close();
		}
		catch(NullPointerException npe)
		{
			npe.printStackTrace();
			status = "Error in File Downloading of Descriptive Statistics due to null values";
			return "FileDownloadError";
		}
		catch(FileNotFoundException fnf) 
		{
			fnf.printStackTrace();
			status = "File Not Found! Please check the file path";
			return "FileDownloadError";
		} // end catch
		catch (IOException ie) 
		{
			// TODO Auto-generated catch block
			ie.printStackTrace();
			status = "Error in File Downloading of Descriptive Statistics";
			return "FileDownloadError";
		}	
		catch(Exception e)
		{
			e.printStackTrace();
			status = "Error in File Downloading of Descriptive Statistics";
			return "FileDownloadError";
		}
		fc.responseComplete();
		return "Desc Stats DOWNLOADED";
	}
	
	public String reset()
	{
		databaseAccessBean.setColumnListRendered(false);
		databaseAccessBean.setRenderSet(false);
		databaseAccessBean.setTableName("");
		isExistFiles = false;
		isScriptExecuted = false;
		fileRendered = false;
		fileLabel = "";
		status = "";
		return "FileReset";
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	
	public String getFileLabel() {
		return fileLabel;
	}

	public void setFileLabel(String fileLabel) {
		this.fileLabel = fileLabel;
	}
	public DatabaseAccessBean getDatabaseAccessBean() {
		return databaseAccessBean;
	}
	public void setDatabaseAccessBean(DatabaseAccessBean databaseAccessBean) {
		this.databaseAccessBean = databaseAccessBean;
	}
	public DescriptiveAnalysisBean getDescriptiveAnalysisBean() {
		return descriptiveAnalysisBean;
	}

	public void setDescriptiveAnalysisBean(DescriptiveAnalysisBean descriptive) {
		this.descriptiveAnalysisBean = descriptive;
	}
	public String getSelectedFile() {
		return selectedFile;
	}

	public void setSelectedFile(String selectedFile) {
		this.selectedFile = selectedFile;
	}
	public File[] getFiles() {
		return files;
	}

	public void setFiles(File[] files) {
		this.files = files;
	}
	public boolean getIsScriptExecuted() {
		return isScriptExecuted;
	}

	public void setIsScriptExecuted(boolean isScriptExecuted) {
		this.isScriptExecuted = isScriptExecuted;
	}
	public boolean getIsExistFiles() {
		return isExistFiles;
	}

	public void setIsExistFiles(boolean isExistFiles) {
		this.isExistFiles = isExistFiles;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	public List<String> getSqlFileNames() {
		return sqlFileNames;
	}

	public void setSqlFileNames(List<String> sqlFileNames) {
		this.sqlFileNames = sqlFileNames;
	}
	
	public boolean isFileRendered() {
		return fileRendered;
	}

	public void setFileRendered(boolean fileRendered) {
		this.fileRendered = fileRendered;
	}
}
