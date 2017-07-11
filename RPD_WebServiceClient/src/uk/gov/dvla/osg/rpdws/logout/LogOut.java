package uk.gov.dvla.osg.rpdws.logout;

import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;

public class LogOut {
	private static Properties props;
	
	public LogOut(Properties props){
		this.props=props;
		logout();
	}
	public void logout(){
		String url = "http://" + props.getProperty("host") + ":" + props.getProperty("port") + props.getProperty("logoutUri");
		ClientConfig config = new ClientConfig();
		config.property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY);
		Client client = ClientBuilder.newClient(config);
		WebTarget target = client.target(url).path(props.getProperty("user"));
		System.out.println(target.getUri());
		
		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON).header("token", props.getProperty("token"));
		Response response = invocationBuilder.post(null);
		String rsp = response.toString();
		String data = response.readEntity(String.class); 
		
		System.out.println("data:"+ data);
		System.out.println("rsp:" + rsp);
		
		if(response.getStatus() == 200){
			System.out.println("Signed out " + props.getProperty("user"));
		}
	}
}
