/*
 *  Producer-consumer problem
 *
 *  Author: Iwo Bujkiewicz
 *  Date:   07 Dec 2016
 */

import java.util.List;
import java.util.ArrayList;

class ProducerConsumerDemo {
	
	private Buffer buffer;
	private List<Producer> producers;
	private List<Consumer> consumers;
	private boolean threadsStarted;
	
	protected ProducerConsumerDemo(int bufferLength, int producersCount, int consumersCount, StringHandler outputHandler) {
		this.buffer = new Buffer(bufferLength, outputHandler);
		this.producers = new ArrayList<>(producersCount);
		for (int i = 0; i < producersCount; i++)
			this.producers.add(new Producer(buffer, i+1, this));
		this.consumers = new ArrayList<>(consumersCount);
		for (int i = 0; i < consumersCount; i++)
			this.consumers.add(new Consumer(buffer, i+1, this));
		this.threadsStarted = false;
	}
	
	protected synchronized void start() {
		if (!threadsStarted) {
			producers.forEach(producer -> producer.start());
			consumers.forEach(consumer -> consumer.start());
			threadsStarted = true;
		}
		else
			notifyAll();
	}
	
	protected synchronized void pause() {
		if (threadsStarted) {
			producers.forEach(producer -> producer.pause());
			consumers.forEach(consumer -> consumer.pause());
		}
	}
	
	protected synchronized void halt() {
		if (threadsStarted) {
			producers.forEach(producer -> producer.halt());
			consumers.forEach(consumer -> consumer.halt());
		}
		buffer = new Buffer(buffer.getLength(), buffer.getOutputHandler());
		producers.clear();
		consumers.clear();
	}
	
	protected synchronized void hold() {
		try {
			wait();
		} catch (InterruptedException e) { }
	}
	
	protected synchronized void updateSimulationSpeed(double exponent) {
		int sleepTimeMultiplier = 100000 / Math.round((float) Math.pow(10, exponent));
		producers.forEach(producer -> producer.setSleepTimeMultiplier(sleepTimeMultiplier));
		consumers.forEach(consumer -> consumer.setSleepTimeMultiplier(sleepTimeMultiplier));
	}
}


