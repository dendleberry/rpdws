package uk.gov.dvla.osg.rpdws.reprint.models;

public class SingleReprint extends AbstractReprintType{

	public SingleReprint(String input) {

		this.jobId=input.substring(0,10);
		this.pieceId=input.substring(10,15);
		noOfRecords=0;
	}
	
	public String output(){
		noOfRecords++;
		return this.jobId + this.pieceId;
	}
	
}
