package uk.gov.dvla.osg.rpdws.submit.job;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import uk.gov.dvla.osg.rpdws.logout.LogOut;
import uk.gov.dvla.osg.rpdws.reprint.models.AbstractReprintType;
import uk.gov.dvla.osg.rpdws.reprint.models.RangeReprint;
import uk.gov.dvla.osg.rpdws.reprint.models.SingleReprint;
import uk.gov.dvla.osg.rpdws.reprint.models.WholeBatchReprint;


public class SubmitJobGui {
	private JFrame frame = new JFrame("Submit Job");  
	private JTextField rangeTextBox;
	private JTextField singleTextBox;
	DefaultListModel<AbstractReprintType> model;
	private JList<AbstractReprintType> list;
	private JButton logoutButton;
	private static Properties props;
	private String mode, eotFileName;
	JRadioButton singleRadioButton;
	JRadioButton rangeRadioButton;
	
	public SubmitJobGui(Properties props){
		this.props=props;
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		//frame.setResizable(false);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
		}
		SwingUtilities.updateComponentTreeUI(frame);
		
		
		frame.setSize(518,259);
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	logout();
		    	frame.setVisible(true);
		    }
		    
		});
		
		singleRadioButton = new JRadioButton("Single");
		rangeRadioButton = new JRadioButton("Range");
		
		singleRadioButton.setSelected(true);
	
		singleRadioButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				toggleRangeText();
			}
		});
		rangeRadioButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				toggleRangeText();
			}
		});
		
		ButtonGroup group = new ButtonGroup();
		group.add(singleRadioButton);
		group.add(rangeRadioButton);
		
		rangeTextBox = new JTextField();
		rangeTextBox.setColumns(10);
		rangeTextBox.addActionListener(getRangeAction());
		
		singleTextBox = new JTextField();
		singleTextBox.setColumns(10);
		singleTextBox.addActionListener(getSingleAction());
		
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(model.size()==0){
					JOptionPane.showMessageDialog(frame,
							"Nothing input! Please try again.",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				} else {
					
					String fullPath=getDatFullPath();
					int noOfRecords=0;
					try {

						PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fullPath,false)));
						ArrayList<AbstractReprintType> list = Collections.list(model.elements());
						
						for(AbstractReprintType reprint : list){
							System.out.println("In loop");
							pw.println(reprint.output());
							noOfRecords = noOfRecords + reprint.getNoOfRecords();
						}
						pw.close();
						
						PrintWriter pw2 = new PrintWriter(new BufferedWriter(new FileWriter(eotFileName,false)));
						pw2.println("RUNVOL="+noOfRecords);
						pw2.println("USER="+props.getProperty("user", "UNKNOWN"));
						pw2.close();
						
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					SubmitJob submit = new SubmitJob(props);
					submit.submitJob(fullPath);
					submit.submitJob(eotFileName);
					
					model.clear();
					singleTextBox.setText("");
					rangeTextBox.setText("");
					singleTextBox.requestFocus();
				}
				
			}
		});
		
		model = new DefaultListModel<>();
		list = new JList<AbstractReprintType>(model);
		list.setBorder(new LineBorder(new Color(0, 0, 0)));
		list.addMouseListener( new MouseAdapter(){
	        public void mousePressed(MouseEvent e){
	            if ( SwingUtilities.isRightMouseButton(e) ){
	            	int index = list.getSelectedIndex();
	                if (index != -1) {
	                    model.remove(index);
	                    System.out.println("Model size now:" + model.size());
	                }
	            }
	        }
	     });
		
		
		frame.getRootPane().setDefaultButton(okButton);
		
		logoutButton = new JButton("Logout");
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logout();
			}
		});
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(rangeRadioButton)
						.addComponent(singleRadioButton))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(list, GroupLayout.PREFERRED_SIZE, 306, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(logoutButton, GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
							.addGap(6))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(rangeTextBox, Alignment.LEADING)
								.addComponent(singleTextBox, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(okButton, GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
							.addContainerGap())))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(singleRadioButton)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(1)
							.addComponent(singleTextBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(okButton))
					.addGap(5)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(rangeRadioButton)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(1)
							.addComponent(rangeTextBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(list, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
							.addComponent(logoutButton)))
					.addGap(11))
		);
		
		frame.getContentPane().setLayout(groupLayout);
		frame.repaint();
		frame.setVisible(true);
		toggleRangeText();
	} 
	public void logout() {
		if (reallyLogout()){
			frame.dispose();
			new LogOut(props);
			System.exit(0);
		}
	}
	
	public boolean reallyLogout(){
		if (JOptionPane.showConfirmDialog(frame, "Quit?", "Confirm exit", 
	            JOptionPane.YES_NO_OPTION,
	            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
			return true;
		}else{
			return false;
		}
	}
	
	public void toggleRangeText(){
		if( rangeRadioButton.isSelected() ){
			rangeTextBox.setVisible(true);
			mode="range";
		}else{
			rangeTextBox.setVisible(false);
			mode="single";
		}

		
		rangeTextBox.revalidate();
		rangeTextBox.repaint();
		
		frame.revalidate();
		frame.repaint();
		singleTextBox.requestFocus();
	}
	
	private Action getSingleAction(){
		Action action = new AbstractAction(){
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {
		    	processSingleEnterAction();
		    }
		};
		return action;
	}
	private Action getRangeAction(){
		Action action = new AbstractAction(){
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {
		        processRangeEnterAction();
		    }
		};
		return action;
	}
	
	private void processSingleEnterAction(){
		if(isInputValid(singleTextBox.getText())){
			AbstractReprintType reprint = null;
			if( "single".equalsIgnoreCase(mode) ){
				if( isWholeBatchReprint(singleTextBox) ){
					reprint = new WholeBatchReprint(singleTextBox.getText());
				} else {
					reprint = new SingleReprint(singleTextBox.getText());
				}
				model.addElement(reprint);
				singleTextBox.setText("");
				singleTextBox.requestFocus();
			}else{
				rangeTextBox.requestFocus();
			}
		} else {
			System.out.println("Single text box invalid entry: " + singleTextBox.getText());
		}
		
	}
	
	private boolean isWholeBatchReprint(JTextField textField) {
		if( textField.getText().length() > 10 ){
			return false;
		}else{
			return true;
		}
	}
	
	private void processRangeEnterAction(){
		if(  isInputValid(singleTextBox.getText()) && isInputValid(rangeTextBox.getText()) && isValidRange(singleTextBox.getText(), rangeTextBox.getText())  ){
			AbstractReprintType reprint = new RangeReprint(singleTextBox.getText(),rangeTextBox.getText());
			model.addElement(reprint);
			singleTextBox.setText("");
			rangeTextBox.setText("");
			singleTextBox.requestFocus();
		}else{
			System.out.println("Single text box invalid entry: " + singleTextBox.getText());
		}
	}
	
	private boolean isValidRange(String in1, String in2) {
		int first = Integer.parseInt(in1.substring(10, 15));
		int second = Integer.parseInt(in2.substring(10, 15));
		boolean result = true;
		if( first > second){
			System.out.println("The first entry '" + first + "' is bigger than the second '" + second + "'");
			result = false;
		}
		if( !(in1.substring(0, 10).equals(in2.substring(0, 10))) ){
			System.out.println("The job ids don't match! " + in1.substring(0, 10) + " - " + in2.substring(0, 10));
			result = false;
		}
		return result;
		
	}
	private boolean isInputValid(String input){
		boolean result = true;
		String error="";
		if( "single".equalsIgnoreCase(mode) ){
			if( (input.length() < 10) || ( input.length() > 10 && input.length() < 15) ){
				error="'" + input + "' is incorrect length:" + input.length();
				result = false;
			} else {
				if( input.length() > 10){
					if( !(input.substring(0, 15).matches("[0-9]+")) ){
						error="'" + input.substring(0, 15) + "' contains letters";
						result = false;
					}
				} else {
					if( !(input.substring(0, 10).matches("[0-9]+")) ){
						error="'" + input.substring(0, 10) + "' contains letters";
						result = false;
					}
				}
			}
		} else{
			if( input.length() < 15 ){
				error="'" + input + "' is incorrect length:" + input.length();
				result = false;
			} else {
				if( !(input.substring(0, 15).matches("[0-9]+")) ){
					error="'" + input.substring(0, 15) + "' contains letters";
					result = false;
				}
			}
		}
		
		if(!result){
			JOptionPane.showMessageDialog(frame,
					error,
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		}
		
		return result;
	}
	
	private String getDatFullPath() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

        String timestamp = now.format(formatter);
		
		String result = props.getProperty("reprintWorkingDir") +
				props.getProperty("reprintFileNamePrefix") +
				timestamp +
				props.getProperty("reprintFileNameExtension");

		System.out.println("Filename:" + result);
		
		eotFileName=props.getProperty("reprintWorkingDir") +
				props.getProperty("reprintFileNamePrefix") +
				timestamp + props.getProperty("reprintEotFileNameExtension");
		return result;
	}
	
}
