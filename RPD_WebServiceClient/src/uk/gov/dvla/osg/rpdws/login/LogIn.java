package uk.gov.dvla.osg.rpdws.login;

import java.net.ConnectException;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class LogIn {

	private String token, errorMessage, errorAction, errorCode;
	private static Properties props;
	
	public LogIn(Properties props){
		this.props=props;
		token="";
	}
	
	public void login(){
		String url = "http://" + props.getProperty("host") + ":" + props.getProperty("port") + props.getProperty("loginUri");
		ClientConfig config = new ClientConfig();
		config.property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY);
		Client client = ClientBuilder.newClient(config);
		WebTarget target = client.target(url).queryParam("name",props.getProperty("user")).queryParam("pwd",props.getProperty("password"));
		System.out.println(target.getUri());
		
		try{
			Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.get();
			String rsp = response.toString();
			String data = response.readEntity(String.class); 
			
			Gson gson = new GsonBuilder().create();
			System.out.println("rsp:" + rsp);
			
			if(response.getStatus() == 200){
				LoginGoodResponseModel gr = gson.fromJson(data, LoginGoodResponseModel.class);
				token=gr.getToken();
			} else{
				LoginBadResponseModel br = gson.fromJson(data, LoginBadResponseModel.class);
				errorMessage = br.getGeneralErrors().get(0).getMessage();
				errorAction = br.getGeneralErrors().get(0).getAction();
				errorCode = br.getGeneralErrors().get(0).getCode();
			}
			
			System.out.println("token:" + token);
		} catch (Exception e){
			errorMessage = e.getMessage();
			errorAction = "If the problem persits, please contact Dev team.";
			errorCode = "Login:error";
		}
		
		
	}
	
	public String getToken(){
		return token;
	}
	public String getErrorMessage(){
		return errorMessage;
	}
	public String getErrorAction(){
		return errorAction;
	}
	public String getErrorCode(){
		return errorCode;
	}
}
