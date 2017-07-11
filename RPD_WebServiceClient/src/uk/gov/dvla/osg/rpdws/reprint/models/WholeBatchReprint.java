package uk.gov.dvla.osg.rpdws.reprint.models;

public class WholeBatchReprint  extends AbstractReprintType{

	public WholeBatchReprint(String input) {
		this.jobId=input.substring(0,10);
		noOfRecords=0;
	}
	
	@Override
	public String toString(){
		return jobId + " full batch";
	}

	public String output(){
		noOfRecords++;
		return this.jobId;
	}
}
