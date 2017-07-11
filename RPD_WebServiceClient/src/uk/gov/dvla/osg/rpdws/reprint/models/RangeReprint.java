package uk.gov.dvla.osg.rpdws.reprint.models;

public class RangeReprint extends AbstractReprintType{
	protected String endPiece;
	public RangeReprint(String input, String input2) {
		this.jobId=input.substring(0,10);
		this.pieceId=input.substring(10,15);
		this.endPiece=input2.substring(10,15);
		noOfRecords=0;
	}

	@Override
	public String toString(){
		return jobId + " " + pieceId + " - " + endPiece;
	}
	
	public String output(){
		int start = Integer.parseInt(pieceId);
		int end = Integer.parseInt(endPiece);
		System.out.println("Creating range: "+ start + " - " + end);
		String result = "";
		for(int count = start; count <= end; count++){
			if(count < end){
				result = result + jobId + String.format("%05d",count) + "\n";
				noOfRecords++;
			}else{
				result = result + jobId + String.format("%05d",count);
				noOfRecords++;
			}
		}
		return result;
	}
}
