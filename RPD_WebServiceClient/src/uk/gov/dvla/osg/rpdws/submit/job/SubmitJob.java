package uk.gov.dvla.osg.rpdws.submit.job;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;


public class SubmitJob {
	private static Properties props;
	private String token;
	
	public SubmitJob(Properties props) {
		this.token="<credential token='" + props.getProperty("token") + "'/>";
		this.props=props;
	}
	
	public void submitJob(String filename){
		
		String url = "http://" + props.getProperty("host") + ":" + props.getProperty("port") + props.getProperty("submitJobUri");
		Logger logger = Logger.getLogger(getClass().getName());
		
		logger.info("Logging..");
		Feature feature = new LoggingFeature(logger);
		
		Client client = ClientBuilder.newClient()
				.register(feature)
				.register(MultiPartFeature.class);
		WebTarget target = client.target(url)
				.register(feature);
		
		MultiPart multiPart = new MultiPart();
		multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
		
		
		
		FileDataBodyPart fdbp = new FileDataBodyPart("file",new File(filename));
		
		multiPart.bodyPart(fdbp);
		
		
		System.out.println(target.getUri());
		
		try{
			Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON)
					.header("ippdcredential", token);
			System.out.println(invocationBuilder);
			
			
			
			Response response = invocationBuilder.post(Entity.entity( multiPart, multiPart.getMediaType()));
			String rsp = response.toString();
			String data = response.readEntity(String.class); 
			
			System.out.println("data:"+ data);
			System.out.println("rsp:" + rsp);
			
			if(response.getStatus() == 202){
				System.out.println("It worked!");
			}
		} catch (Exception e){
			System.out.println("It didn't work :(");
			System.out.println(e.getMessage());
		}
	}
	
}
