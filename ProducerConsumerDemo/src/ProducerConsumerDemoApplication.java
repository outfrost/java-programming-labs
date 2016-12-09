/*
 *  ProducerConsumerDemoApplication - a GUI application that allows to control and visualise the ProducerConsumerDemo simulation
 *
 *  Author: Iwo Bujkiewicz
 *  Date:   07 Dec 2016
 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProducerConsumerDemoApplication extends JFrame implements ActionListener, StringHandler, ChangeListener {
	
	private static String ABOUT_MESSAGE = "Symulacja problemu producentów i konsumentów\n" +
		                                      "\n" +
		                                      "Autorzy: Paweł Rogaliński, Iwo Bujkiewicz\n" +
		                                      "Data: 09.12.2016";
	
	private ProducerConsumerDemo producerConsumerDemo;
	
	private JSpinner producersCountSpinner = new JSpinner(new SpinnerNumberModel(2, 0, Integer.MAX_VALUE, 1));
	private JSpinner bufferLengthSpinner = new JSpinner(new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1));
	private JSpinner consumersCountSpinner = new JSpinner(new SpinnerNumberModel(2, 0, Integer.MAX_VALUE, 1));
	
	private JButton startSimulationButton = new JButton("Start");
	private JButton suspendSimulationButton = new JButton("Wstrzymaj");
	private JButton haltSimulationButton = new JButton("Zatrzymaj i zresetuj");
	
	private JSlider simulationSpeedSlider = new JSlider(0, 40, 20);
	
	private JTextArea simulationOutputTextArea = new JTextArea(16, 80);
	
	private JButton aboutMessageButton = new JButton("O programie");
	
	private ProducerConsumerDemoApplication() {
		super("ProducerConsumerDemo");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setSize(640, 580);
		
		JPanel controlsWrapper = new JPanel();
		controlsWrapper.setLayout(new BoxLayout(controlsWrapper, BoxLayout.Y_AXIS));
		
		JPanel numberPanel = new JPanel();
		numberPanel.add(producersCountSpinner);
		numberPanel.add(bufferLengthSpinner);
		numberPanel.add(consumersCountSpinner);
		controlsWrapper.add(numberPanel);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(startSimulationButton);
		buttonPanel.add(suspendSimulationButton);
		buttonPanel.add(haltSimulationButton);
		controlsWrapper.add(buttonPanel);
		
		JPanel sliderPanel = new JPanel();
		sliderPanel.add(new JLabel("Prędkość symulacji"));
		sliderPanel.add(simulationSpeedSlider);
		controlsWrapper.add(sliderPanel);
		
		this.getContentPane().add(controlsWrapper, BorderLayout.PAGE_START);
		
		simulationOutputTextArea.setLineWrap(true);
		JScrollPane simulationOutputScroller = new JScrollPane(simulationOutputTextArea);
		simulationOutputScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		((DefaultCaret)simulationOutputTextArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		this.getContentPane().add(simulationOutputScroller, BorderLayout.CENTER);
		
		JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		infoPanel.add(aboutMessageButton);
		
		this.getContentPane().add(infoPanel, BorderLayout.PAGE_END);
		
		startSimulationButton.addActionListener(this);
		suspendSimulationButton.addActionListener(this);
		haltSimulationButton.addActionListener(this);
		simulationSpeedSlider.addChangeListener(this);
		aboutMessageButton.addActionListener(this);
		
		this.setVisible(true);
	}
	
	private void startSimulation() {
		if (producerConsumerDemo == null) {
			producerConsumerDemo = new ProducerConsumerDemo((int)bufferLengthSpinner.getValue(), (int)producersCountSpinner.getValue(), (int)consumersCountSpinner.getValue(), this);
			producerConsumerDemo.updateSimulationSpeed(simulationSpeedSlider.getValue() / 10.0d);
			simulationOutputTextArea.setText("");
		}
		bufferLengthSpinner.setEnabled(false);
		producersCountSpinner.setEnabled(false);
		consumersCountSpinner.setEnabled(false);
		producerConsumerDemo.start();
	}
	
	private void suspendSimulation() {
		if (producerConsumerDemo != null)
			producerConsumerDemo.pause();
	}
	
	private void haltSimulation() {
		if (producerConsumerDemo != null) {
			producerConsumerDemo.halt();
			producerConsumerDemo = null;
		}
		bufferLengthSpinner.setEnabled(true);
		producersCountSpinner.setEnabled(true);
		consumersCountSpinner.setEnabled(true);
	}
	
	public static void main(String[] args) {
		new ProducerConsumerDemoApplication();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startSimulationButton)
			startSimulation();
		else if (e.getSource() == suspendSimulationButton)
			suspendSimulation();
		else if (e.getSource() == haltSimulationButton)
			haltSimulation();
		else if (e.getSource() == aboutMessageButton)
			JOptionPane.showMessageDialog(this, ABOUT_MESSAGE);
	}
	
	@Override
	public void accept(String s) {
		simulationOutputTextArea.append(s);
		simulationOutputTextArea.append("\n");
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == simulationSpeedSlider && producerConsumerDemo != null)
			producerConsumerDemo.updateSimulationSpeed(simulationSpeedSlider.getValue() / 10.0d);
	}
}
