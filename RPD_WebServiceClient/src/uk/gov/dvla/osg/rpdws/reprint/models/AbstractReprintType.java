package uk.gov.dvla.osg.rpdws.reprint.models;

public abstract class AbstractReprintType {
	
	protected String jobId;
	protected String pieceId;
	protected Integer noOfRecords;
	
	public abstract String output();
	
	public Integer getNoOfRecords(){
		return noOfRecords;
	}
	
	
	@Override
	public String toString(){
		return jobId + " " + pieceId;
	}
	
}
