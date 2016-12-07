/*
 *  Consumer - a thread-based class to simulate a consumer
 *
 *  Author: Paweł Rogaliński
 *  Date:   01 Oct 2009
 */

class Consumer extends Thread
{
	Buffer buf;
    int number;

	public Consumer(Buffer c, int number) {
		buf = c;
		this.number = number;
	}
	
	public void run() {
		while(true){
			buf.get(number);
			try {
				sleep((int)(Math.random() * 1000));
			} catch (InterruptedException e) { }
		}
	}
}
