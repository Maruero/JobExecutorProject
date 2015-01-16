package br.com.diastecnologia.jobexecutor.beans;

import java.util.Date;

public class JobExecution {

	private int executionID;
	private int jobID;
	private Date executionLoaded;
	private Date executionExecuted;
	public int getExecutionID() {
		return executionID;
	}
	public void setExecutionID(int executionID) {
		this.executionID = executionID;
	}
	public int getJobID() {
		return jobID;
	}
	public void setJobID(int jobID) {
		this.jobID = jobID;
	}
	public Date getExecutionLoaded() {
		return executionLoaded;
	}
	public void setExecutionLoaded(Date executionLoaded) {
		this.executionLoaded = executionLoaded;
	}
	public Date getExecutionExecuted() {
		return executionExecuted;
	}
	public void setExecutionExecuted(Date executionExecuted) {
		this.executionExecuted = executionExecuted;
	}
	
	
	
	
}
