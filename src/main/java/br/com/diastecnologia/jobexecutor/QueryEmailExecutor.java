package br.com.diastecnologia.jobexecutor;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.diastecnologia.jobexecutor.beans.Job;
import br.com.diastecnologia.jobexecutor.dao.JobDao;
import br.com.diastecnologia.jobexecutor.emails.EmailSender;

public class QueryEmailExecutor {

	private static Logger logger = Logger.getLogger(QueryEmailExecutor.class);
	private JobDao jobDao;
	
	public QueryEmailExecutor(JobDao jobDao){
		this.jobDao = jobDao;
	}
	
	public void execute( Job job ) throws IOException{
		logger.info("Início da execução com " + QueryEmailExecutor.class.toString() );
		
		String body = EmailSender.getHtmlBody(job.getEmail());
		
		List<List<String>> values = jobDao.execute(job.getQuery());
		for( List<String> colValues : values ){
			int userID = new Integer(colValues.get( 0 ));
			
			int executionID = jobDao.insertJobItem(job, userID);
			
			String emailBody = body.toString();
			
			String token = jobDao.createAccessToken(userID);
			emailBody = emailBody.replace("[VALUE_ACTION_TOKEN]", token);
			
			String toName = colValues.get( 1 );
			String to = colValues.get( 2 );
			
			for( int i = 2 ; i<colValues.size() ; i++){
				emailBody = emailBody.replace("[VALUE_"+i+"]", colValues.get(i));
			}
			
			EmailSender.sendEmail(job.getJobName(), emailBody, to, toName);
			
			jobDao.updateJobItemAsExecuted(executionID);
		}
		
		logger.info("Término da execução com " + QueryEmailExecutor.class.toString() );
	}
	
}
