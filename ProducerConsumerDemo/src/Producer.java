/*
 *  Producer - a thread-based class to simulate a producer
 *
 *  Author: Paweł Rogaliński
 *  Date:   01 Oct 2009
 */

class Producer extends Thread
{
	static char item = 'A';
	
	Buffer buf;
	int number;
	
	public Producer(Buffer c, int number) {
		buf = c;
		this.number = number;
	}
	
	public void run() {
		char c;
		while(true){
			c = item++;
			buf.put(number, c);
			try {
				sleep((int)(Math.random() * 1000));
			} catch (InterruptedException e) { }
		}
	}
}
