package br.com.diastecnologia.jobexecutor;

import java.util.List;

import org.apache.log4j.Logger;

import br.com.diastecnologia.jobexecutor.beans.Job;
import br.com.diastecnologia.jobexecutor.dao.DriveLoader;
import br.com.diastecnologia.jobexecutor.dao.JobDao;

public class Application {

	private static Logger logger = Logger.getLogger(Application.class);
	
	public static void main(String[] args) throws Exception{
		logger.info("Execu��o iniciada.");
		
		boolean production = false;
		if( args != null && args.length > 0 && args[0] != null){
			production = "true".equals(args[0]);
			logger.info("Production: " + args[0]);
		}
		
		DriveLoader.init();
		JobDao jobDao = new JobDao(production);
		
		List<Job> jobs = jobDao.getJobsToExecute();
		logger.info("Jobs carregados para execu��o: " + jobs.size() );
		for( Job job : jobs ){
			logger.info("In�cio da execu��o de: " + job.getJobType() + " - " + job.getJobName() );
			int executionID = jobDao.insertJobExecution(job);
			
			switch( job.getJobType() ){
				case QUERY_EMAIL:
					QueryEmailExecutor executor = new QueryEmailExecutor(jobDao);
					executor.execute(job);
					break;
				default:
					logger.info("Executador n�o encontrado." );
			}
			
			jobDao.updateJobAsExecuted(executionID);
		}
		
		
		jobDao.close();
		
		logger.info("Execu��o terminada.");
	}
	
}
