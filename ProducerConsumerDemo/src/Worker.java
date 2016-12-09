/*
 *  Worker - an abstract thread-based class that provides a foundation for Producers and Consumers
 *
 *  Author: Iwo Bujkiewicz
 *  Date:   08 Dec 2016
 */

abstract class Worker extends Thread {
	
	private Buffer buf;
	private int number;
	private int sleepTimeMultiplier;
	private boolean running;
	private boolean suspended;
	private Object manager;
	
	public Worker(Buffer buffer, int number, Object manager) {
		this.buf = buffer;
		this.number = number;
		this.sleepTimeMultiplier = 1000;
		this.running = true;
		this.suspended = false;
		this.manager = manager;
	}
	
	public void pause() {
		this.suspended = true;
	}
	
	public void halt() {
		this.running = false;
	}
	
	public Buffer getBuffer() { return buf; }
	public int getNumber() { return number; }
	public int getSleepTimeMultiplier() { return sleepTimeMultiplier; }
	public void setSleepTimeMultiplier(int sleepTimeMultiplier) { this.sleepTimeMultiplier = sleepTimeMultiplier; }
	public boolean isRunning() { return running; }
	public boolean isSuspended() { return suspended; }
	public void setSuspended(boolean state) { this.suspended = state; }
	public Object getManager() { return manager; }
}
