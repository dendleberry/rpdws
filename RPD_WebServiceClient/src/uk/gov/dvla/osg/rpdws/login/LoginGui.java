package uk.gov.dvla.osg.rpdws.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdesktop.xswingx.PromptSupport;
import org.jdesktop.xswingx.PromptSupport.FocusBehavior;

import uk.gov.dvla.osg.rpdws.logout.LogOut;
import uk.gov.dvla.osg.rpdws.submit.job.SubmitJobGui;


public class LoginGui{

	JFrame frame = new JFrame("Login");  
	private JTextField textField;
	private JButton button;
	private JPasswordField passwordField;
	private static Properties props;

	public LoginGui(Properties props){
		this.props = props;
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
		}
		SwingUtilities.updateComponentTreeUI(frame);
		button=new JButton("Ok");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if( !("".equals(textField.getText())) && !("".equals(passwordField.getPassword())) ){
					String usr = textField.getText();
					String pas = new String(passwordField.getPassword());
					System.out.println("U:"+ usr);
					System.out.println("P:"+ pas);
					props.put("password", pas);
					props.put("user", usr);
					textField.setText("");
					passwordField.setText("");
					LogIn login = new LogIn(props);
					login.login();
					
					String token = login.getToken();
					
					if( "".equals(token) ){
						JOptionPane.showMessageDialog(frame,
								login.getErrorMessage() + "\n\n" + login.getErrorAction(),
							    "Error:"+login.getErrorCode(),
							    JOptionPane.ERROR_MESSAGE);
						textField.requestFocusInWindow();
					} else {
						props.put("token", token);
						frame.setVisible(false);
						launchSubmitGui();
						
					}
				}
			}

		});
		button.setEnabled(false);
		
		frame.setSize(255,193);
		
		
		
		textField = new JTextField();
		PromptSupport.setPrompt("user", textField);
		PromptSupport.setFocusBehavior(FocusBehavior.SHOW_PROMPT,textField);
		textField.setColumns(10);
		textField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
			    changed();
			  }
			  public void removeUpdate(DocumentEvent e) {
			    changed();
			  }
			  public void insertUpdate(DocumentEvent e) {
			    changed();
			  }
			  public void changed() {
			     if (textField.getText().equals("") || new String(passwordField.getPassword()).equals("")){
			    	 button.setEnabled(false);
			     }
			     else {
			    	 button.setEnabled(true);
			    }

			  }
			});
		
		
		
		
		passwordField = new JPasswordField();
		PromptSupport.setPrompt("password", passwordField);
		PromptSupport.setFocusBehavior(FocusBehavior.SHOW_PROMPT,passwordField);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(40)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(40)
							.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(90)
							.addComponent(button, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(40, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(38)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(button, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
					.addGap(37))
		);
		frame.getContentPane().setLayout(groupLayout);
		passwordField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
			    changed();
			  }
			  public void removeUpdate(DocumentEvent e) {
			    changed();
			  }
			  public void insertUpdate(DocumentEvent e) {
			    changed();
			  }
			  public void changed() {
			     if (textField.getText().equals("") || new String(passwordField.getPassword()).equals("")){
			    	 button.setEnabled(false);
			     }
			     else {
			    	 button.setEnabled(true);
			    }

			  }
			});
		frame.setLocationRelativeTo(null);
		frame.getRootPane().setDefaultButton(button);
		frame.setVisible(true);  
	} 
	public void launchSubmitGui() {
		SubmitJobGui submitGui = new SubmitJobGui(props);
	}

}
