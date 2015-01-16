package br.com.diastecnologia.jobexecutor.beans;

import java.util.Date;

public class JobItem {

	private int itemID;
	private int jobID;
	private Date itemLoaded;
	private Date itemExecuted;
	
	public int getItemID() {
		return itemID;
	}
	public void setItemID(int itemID) {
		this.itemID = itemID;
	}
	public int getJobID() {
		return jobID;
	}
	public void setJobID(int jobID) {
		this.jobID = jobID;
	}
	public Date getItemLoaded() {
		return itemLoaded;
	}
	public void setItemLoaded(Date itemLoaded) {
		this.itemLoaded = itemLoaded;
	}
	public Date getItemExecuted() {
		return itemExecuted;
	}
	public void setItemExecuted(Date itemExecuted) {
		this.itemExecuted = itemExecuted;
	}
	
	
}
