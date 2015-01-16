package br.com.diastecnologia.jobexecutor.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import br.com.diastecnologia.jobexecutor.beans.Job;
import br.com.diastecnologia.jobexecutor.beans.JobType;

public class JobDao {	
	
	private static Logger logger = Logger.getLogger(JobDao.class);
	
	private Connection conn;
	private String url = "jdbc:mysql://mysql-shop-maquinas.jelasticlw.com.br/shopmaquinas";
	private String username = "root";
	private String password = "bhN9RJ9Jay";
	
	private String developmentUrl = "jdbc:mysql://localhost:3306/shopmaquinas";
	private String developmentUsername = "root";
	private String developmentPassword = "dias1986";
	
	public JobDao(boolean production) throws Exception{
		try{
			if( production ){
				conn = DriverManager.getConnection(url, username, password);
				logger.info("Conexão com o banco aberta: " + url );
			}else{
				conn = DriverManager.getConnection(developmentUrl, developmentUsername, developmentPassword);
				logger.info("Conexão com o banco aberta: " + developmentUrl );
			}
			
		}catch(SQLException ex){
			logger.error("Problema ao abrir conexão com o banco: " + url, ex );
			logger.error("SQLException: " + ex.getMessage() );
			logger.error("SQLState: " + ex.getSQLState() );
			logger.error("VendorError: " + ex.getErrorCode() );
			
			throw new Exception(ex);
		}
	}
	
	public String createAccessToken(int userId){
		PreparedStatement stat = null;
		
		String token = UUID.randomUUID().toString();

		try{
			final String SQL = "insert into UserToken (Token, UserID, ExpirationDate) values (?,?,date_add( now(), interval 1 month))";
			stat = conn.prepareStatement(SQL);
			stat.setString(1, token);
			stat.setInt(2, userId);
			
		}catch(SQLException ex){
			logger.error("Problema ao executar atualização de job executado: ", ex);
			logger.error("SQLException: " + ex.getMessage() );
			logger.error("SQLState: " + ex.getSQLState() );
			logger.error("VendorError: " + ex.getErrorCode() );
		}finally{
			try{
				if( stat != null && !stat.isClosed()){
					stat.close();
				}
			}catch(Exception ex){
				logger.error("Problema ao fechar recursos: ", ex);
			}
		}
		return token;
	}
	
	public void updateJobAsExecuted(int executionID){
		PreparedStatement stat = null;
		try{
			final String SQL = "update JobExecution set ExecutionExecuted = now() where ExecutionID = ?";
			
			stat = conn.prepareStatement(SQL);
			stat.setInt(1, executionID);
			stat.executeUpdate();
			
		}catch(SQLException ex){
			logger.error("Problema ao executar atualização de job executado: ", ex);
			logger.error("SQLException: " + ex.getMessage() );
			logger.error("SQLState: " + ex.getSQLState() );
			logger.error("VendorError: " + ex.getErrorCode() );
		}finally{
			try{
				if( stat != null && !stat.isClosed()){
					stat.close();
				}
			}catch(Exception ex){
				logger.error("Problema ao fechar recursos: ", ex);
			}
		}
	}
	
	public int getMaxJobExecutionID(Job job){
		PreparedStatement stat = null;
		ResultSet result = null;
		int executionID = 0;
		try{
			final String SQL = "select max(ExecutionID) as ExecutionID from JobExecution where JobID = ?";
			
			stat = conn.prepareStatement(SQL);
			stat.setInt(1, job.getJobID());
			
			result = stat.executeQuery();
			if( result.next() ){
				executionID = result.getInt("ExecutionID");
			}
			
		}catch(SQLException ex){
			logger.error("Problema ao inserir execução de job: ", ex);
			logger.error("SQLException: " + ex.getMessage() );
			logger.error("SQLState: " + ex.getSQLState() );
			logger.error("VendorError: " + ex.getErrorCode() );
		}finally{
			try{
				if( result != null && !result.isClosed()){
					result.close();
				}
				if( stat != null && !stat.isClosed()){
					stat.close();
				}
			}catch(Exception ex){
				logger.error("Problema ao fechar recursos: ", ex);
			}
		}
		return executionID;
	}
	
	public int insertJobExecution(Job job){
		PreparedStatement stat = null;
		try{
			final String SQL = "insert into JobExecution (JobID, ExecutionLoaded) values (?, now())";
			
			stat = conn.prepareStatement(SQL);
			stat.setInt(1, job.getJobID());
			stat.executeUpdate();
			
			return getMaxJobExecutionID(job);
		}catch(SQLException ex){
			logger.error("Problema ao inserir execução de job: ", ex);
			logger.error("SQLException: " + ex.getMessage() );
			logger.error("SQLState: " + ex.getSQLState() );
			logger.error("VendorError: " + ex.getErrorCode() );
			
			return -1;
		}finally{
			try{
				if( stat != null && !stat.isClosed()){
					stat.close();
				}
			}catch(Exception ex){
				logger.error("Problema ao fechar recursos: ", ex);
			}
		}
	}
	
	public void updateJobItemAsExecuted(int executionID){
		PreparedStatement stat = null;
		try{
			final String SQL = "update JobItem set ItemExecuted = now() where ExecutionID = ?";
			
			stat = conn.prepareStatement(SQL);
			stat.setInt(1, executionID);
			stat.executeUpdate();
			
		}catch(SQLException ex){
			logger.error("Problema ao executar atualização de job executado: ", ex);
			logger.error("SQLException: " + ex.getMessage() );
			logger.error("SQLState: " + ex.getSQLState() );
			logger.error("VendorError: " + ex.getErrorCode() );
		}finally{
			try{
				if( stat != null && !stat.isClosed()){
					stat.close();
				}
			}catch(Exception ex){
				logger.error("Problema ao fechar recursos: ", ex);
			}
		}
	}
	
	public int getMaxJobItemExecutionID(Job job, int itemID){
		PreparedStatement stat = null;
		ResultSet result = null;
		int executionID = 0;
		try{
			final String SQL = "select max(ExecutionID) as ExecutionID from JobItem where JobID = ? and ItemID = ?";
			
			stat = conn.prepareStatement(SQL);
			stat.setInt(1, job.getJobID());
			stat.setInt(2, itemID);
			
			result = stat.executeQuery();
			if( result.next() ){
				executionID = result.getInt("ExecutionID");
			}
			
		}catch(SQLException ex){
			logger.error("Problema ao inserir execução de job: ", ex);
			logger.error("SQLException: " + ex.getMessage() );
			logger.error("SQLState: " + ex.getSQLState() );
			logger.error("VendorError: " + ex.getErrorCode() );
		}finally{
			try{
				if( result != null && !result.isClosed()){
					result.close();
				}
				if( stat != null && !stat.isClosed()){
					stat.close();
				}
			}catch(Exception ex){
				logger.error("Problema ao fechar recursos: ", ex);
			}
		}
		return executionID;
	}
	
	public int insertJobItem(Job job, int itemId){
		PreparedStatement stat = null;
		try{
			final String SQL = "insert into JobItem (JobID, ItemID, ItemLoaded) values (?, ?, now())";
			
			stat = conn.prepareStatement(SQL);
			stat.setInt(1, job.getJobID());
			stat.setInt(2, itemId);
			stat.executeUpdate();
			
			return getMaxJobItemExecutionID(job, itemId);
		}catch(SQLException ex){
			logger.error("Problema ao inserir execução de job: ", ex);
			logger.error("SQLException: " + ex.getMessage() );
			logger.error("SQLState: " + ex.getSQLState() );
			logger.error("VendorError: " + ex.getErrorCode() );
			
			return -1;
		}finally{
			try{
				if( stat != null && !stat.isClosed()){
					stat.close();
				}
			}catch(Exception ex){
				logger.error("Problema ao fechar recursos: ", ex);
			}
		}
	}
	
	public List<Job> getJobsToExecute(){
		PreparedStatement stat = null;
		ResultSet result = null;
		
		List<Job> jobs = new ArrayList<Job>();
		try{
			String sql = "SELECT  j.JobID, j.JobName, j.Query, j.Email, j.JobType FROM "+ 
								"shopmaquinas.job j "+ 
							"LEFT OUTER JOIN "+  
								"shopmaquinas.jobexecution e "+  
								"on "+  
									"e.JobID = j.JobID "+ 
									"and date(e.ExecutionExecuted) = date(now()) "+  
							"WHERE "+  
								"j.BeginHour <= hour(now()) "+ 
								"and j.EndHour >= hour(now()) "+ 
							"GROUP BY j.JobName, j.Query, j.ExecutionCount "+ 
							"HAVING count(e.JobID) < j.ExecutionCount";
			
			stat = conn.prepareStatement(sql);
			result = stat.executeQuery(sql);
			while(result.next()){
				Job job = new Job();
				
				job.setJobID(result.getInt("JobID"));
				job.setJobName(result.getString("JobName"));
				job.setQuery(result.getString("Query"));
				job.setEmail(result.getString("Email"));
				job.setJobType(JobType.valueOf(result.getString("JobType")));
				
				jobs.add(job);
			}
			
		}catch(SQLException ex){
			logger.error("Problema ao executar query para obter os jobs: ", ex);
			logger.error("SQLException: " + ex.getMessage() );
			logger.error("SQLState: " + ex.getSQLState() );
			logger.error("VendorError: " + ex.getErrorCode() );
		}finally{
			try{
				if( result != null && !result.isClosed()){
					result.close();
				}
				if( stat != null && !stat.isClosed()){
					stat.close();
				}
			}catch(Exception ex){
				logger.error("Problema ao fechar recursos: ", ex);
			}
		}
		return jobs;
	}
	
	public List<List<String>> execute(String query){
		PreparedStatement stat = null;
		ResultSet result = null;
		
		List<List<String>> values = new ArrayList<List<String>>();
		try{
			
			stat = conn.prepareStatement(query);
			result = stat.executeQuery(query);
			
			int columns = stat.getMetaData().getColumnCount();
			
			while(result.next()){
				List<String> colValues = new ArrayList<String>();
				values.add(colValues);
				
				for( int i=1 ; i<columns+1 ; i++){
					colValues.add( result.getString(i));
				}
			}
			
		}catch(SQLException ex){
			logger.error("Problema ao executar query para obter os dados do job: ", ex);
			logger.error("SQLException: " + ex.getMessage() );
			logger.error("SQLState: " + ex.getSQLState() );
			logger.error("VendorError: " + ex.getErrorCode() );
		}finally{
			try{
				if( result != null && !result.isClosed()){
					result.close();
				}
				if( stat != null && !stat.isClosed()){
					stat.close();
				}
			}catch(Exception ex){
				logger.error("Problema ao fechar recursos: ", ex);
			}
		}
		return values;
	}
	
	public void close(){
		try{
			if( conn != null && !conn.isClosed()){
				conn.close();
				logger.info("Conexão com o banco de dados fechada.");
			}
		}catch(SQLException ex){
			logger.error("Problema ao fechar conexão: ", ex);
		}
	}
	
}
