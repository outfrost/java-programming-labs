/*
 *  Producer - a thread-based class to simulate a producer
 *
 *  Author: Paweł Rogaliński, Iwo Bujkiewicz
 *  Date:   08 Dec 2016
 */

class Producer extends Worker {
	
	private static char item = 'A';
	
	public Producer(Buffer buffer, int number, ProducerConsumerDemo manager) {
		super(buffer, number, manager);
	}
	
	public void run() {
		char c;
		while(isRunning()) {
			c = item++;
			while (item > 'Z') item -= 26;
			getBuffer().put(getNumber(), c);
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
