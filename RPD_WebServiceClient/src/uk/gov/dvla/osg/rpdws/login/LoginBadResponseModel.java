package uk.gov.dvla.osg.rpdws.login;

import java.util.List;

public class LoginBadResponseModel {
	
	private Object attributeErrors;
	private List<LoginErrors> generalErrors;

	
	public Object getAttributeErrors() {
		return attributeErrors;
	}
	public void setAttributeErrors(Object attributeErrors) {
		this.attributeErrors = attributeErrors;
	}
	public List<LoginErrors> getGeneralErrors() {
		return generalErrors;
	}
	public void setGeneralErrors(List<LoginErrors> generalErrors) {
		this.generalErrors = generalErrors;
	}
	
}
