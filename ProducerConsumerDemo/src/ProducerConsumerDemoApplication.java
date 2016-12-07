/*
 *  ProducerConsumerDemoApplication - a GUI application that allows to control and visualise the ProducerConsumerDemo simulation
 *
 *  Author: Iwo Bujkiewicz
 *  Date:   07 Dec 2016
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProducerConsumerDemoApplication extends JFrame implements ActionListener {
	
	private JTextField producersCountTextField = new JTextField(8);
	private JTextField bufferLengthTextField = new JTextField(8);
	private JTextField consumersCountTextField = new JTextField(8);
	
	private JButton startSimulationButton = new JButton("Start");
	private JButton suspendSimulationButton = new JButton("Suspend");
	private JButton haltSimulationButton = new JButton("Halt and reset");
	
	private JTextArea simulationOutputTextArea = new JTextArea(16, 80);
	
	protected ProducerConsumerDemoApplication() {
		super("ProducerConsumerDemo");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(640, 360);
		
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
