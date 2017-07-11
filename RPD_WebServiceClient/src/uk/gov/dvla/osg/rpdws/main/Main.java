package uk.gov.dvla.osg.rpdws.main;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.SwingUtilities;

import uk.gov.dvla.osg.rpdws.login.LoginGui;
import uk.gov.dvla.osg.rpdws.submit.job.SubmitJobGui;


public class Main {
	
	private static String propsFile;
	private static Properties prop;
	
	public static void main(String[] args) {
		
		
		
		if( args.length != 1 ){
			System.out.println("Incorrect number of args, require path to props file.");
			System.exit(1);
		} else {
			propsFile = args[0];
			if( !(new File(propsFile).exists()) ){
				System.out.println("Props file '" + propsFile + " doesn't exist!");
				System.exit(1);
			} else {
				prop = new Properties();
				InputStream input = null;

				try {

					input = new FileInputStream(propsFile);

					// load a properties file
					prop.load(input);

				} catch (IOException ex) {
					ex.printStackTrace();
				} finally {
					if (input != null) {
						try {
							input.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}
		}
		//SubmitJobGui  s= new SubmitJobGui(prop);
		
		System.out.println("Running...");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
		    	  LoginGui gui = new LoginGui(prop);
			}
		});
	}
}
