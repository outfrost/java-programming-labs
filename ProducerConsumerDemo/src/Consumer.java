/*
 *  Consumer - a thread-based class to simulate a consumer
 *
 *  Author: Paweł Rogaliński, Iwo Bujkiewicz
 *  Date:   08 Dec 2016
 */

class Consumer extends Worker {

	public Consumer(Buffer buffer, int number, ProducerConsumerDemo manager) {
		super(buffer, number, manager);
	}
	
	public void run() {
		while(isRunning()) {
			getBuffer().get(getNumber());
			try {
				sleep((int)(Math.random() * getSleepTimeMultiplier()));
			} catch (InterruptedException e) { }
			if (isSuspended()) {
				((ProducerConsumerDemo)getManager()).hold();
				setSuspended(false);
			}
		}
	}
}
